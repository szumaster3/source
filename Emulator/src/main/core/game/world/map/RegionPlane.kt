package core.game.world.map

import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.map.build.DynamicRegion
import core.game.world.map.build.RegionFlags
import core.game.world.update.flag.chunk.ItemUpdateFlag
import core.net.packet.OutgoingContext
import core.net.packet.PacketRepository
import core.net.packet.out.ClearGroundItem
import core.net.packet.out.ConstructGroundItem
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Represents a single plane (z-level) of a region, managing its tiles, objects, entities, and flags.
 *
 * @property region The parent region this plane belongs to.
 * @property plane The plane index (0–3) representing the height level.
 */
class RegionPlane(val region: Region, val plane: Int) {

    companion object {
        /**
         * Represents the size of a region in tiles.
         */
        const val REGION_SIZE = 64

        /**
         * Represents the size of a chunk in tiles (8x8).
         */
        const val CHUNK_SIZE = REGION_SIZE shr 3

        /**
         * Represents the object used when an object is removed or not found.
         */
        val NULL_OBJECT = Scenery(0, Location.create(0, 0, 0))
    }

    /**
     * Represents the clipping and movement flags for the plane.
     */
    val flags = RegionFlags(plane, region.baseLocation.x, region.baseLocation.y, false)

    /**
     * Represents the projectile clipping flags.
     */
    val projectileFlags = RegionFlags(plane, region.baseLocation.x, region.baseLocation.y, true)

    /**
     * Represents the 2D array of chunks (8x8) for this plane.
     */
    val chunks = Array(CHUNK_SIZE) { arrayOfNulls<RegionChunk>(CHUNK_SIZE) }

    /**
     * Represents the list of NPCs currently on this plane.
     */
    val npcs: MutableList<NPC> = CopyOnWriteArrayList()

    /**
     * Represents the list of players currently on this plane.
     */
    val players: MutableList<Player> = CopyOnWriteArrayList()

    /**
     * Represents the 2D array representing placed scenery objects.
     */
    var objects: Array<Array<Scenery?>>? = Array(REGION_SIZE) { arrayOfNulls<Scenery>(REGION_SIZE) }

    /**
     * Resets all chunk flags within this plane.
     */
    fun pulse() {
        chunks.flatten().filterNotNull().forEach { it.resetFlags() }
    }

    /**
     * Adds a scenery object to the world and marks it renderable.
     *
     * @param obj The scenery object to place.
     * @param x The x-coordinate (local to the region).
     * @param y The y-coordinate (local to the region).
     * @param landscape Whether to store the object in the region's landscape object array.
     */
    fun add(obj: Scenery?, x: Int, y: Int, landscape: Boolean) {
        setChunkObject(x, y, obj)
        if (landscape) objects?.get(x)?.set(y, obj)
        obj?.isRenderable = true
    }

    /**
     * Returns or initializes the chunk at the given chunk coordinates.
     */
    fun getRegionChunk(chunkX: Int, chunkY: Int): RegionChunk {
        return chunks[chunkX][chunkY] ?: run {
            val location = region.baseLocation.transform(chunkX shl 3, chunkY shl 3, plane)
            val chunk = if (region.build) BuildRegionChunk(location, 0, this) else RegionChunk(location, 0, this)
            chunks[chunkX][chunkY] = chunk
            chunk
        }
    }

    /**
     * Sets a specific chunk manually in the plane.
     */
    fun setRegionChunk(chunkX: Int, chunkY: Int, chunk: RegionChunk) {
        chunks[chunkX][chunkY] = chunk
    }

    /**
     * Removes a scenery object at the given coordinates.
     *
     * @param x The x-coordinate (local to region).
     * @param y The y-coordinate (local to region).
     * @param objectId Optional object ID to help locate the object in a dynamic chunk.
     */
    fun remove(x: Int, y: Int, objectId: Int = -1) {
        val chunkX = x / CHUNK_SIZE
        val chunkY = y / CHUNK_SIZE
        val offsetX = x % CHUNK_SIZE
        val offsetY = y % CHUNK_SIZE
        val chunk = getRegionChunk(chunkX, chunkY)
        val removed = Scenery(0, region.baseLocation.transform(x, y, plane), 22, 0).apply { isRenderable = false }
        if (chunk is BuildRegionChunk) {
            val index = chunk.getIndex(offsetX, offsetY, objectId)
            chunk.getObjects(index)[offsetX][offsetY] = removed
        } else {
            chunk.objects[offsetX][offsetY] = removed
        }
    }

    /**
     * Stores an object in the correct chunk at the specified tile.
     */
    private fun setChunkObject(x: Int, y: Int, obj: Scenery?) {
        val chunkX = x / CHUNK_SIZE
        val chunkY = y / CHUNK_SIZE
        val offsetX = x % CHUNK_SIZE
        val offsetY = y % CHUNK_SIZE
        val chunk = getRegionChunk(chunkX, chunkY)
        if (chunk is BuildRegionChunk) {
            chunk.store(obj)
        } else {
            chunk.objects[offsetX][offsetY] = obj
        }
    }

    /**
     * Returns a flat list of all scenery objects present in this plane.
     */
    fun getObjectList(): List<Scenery> = buildList {
        objects?.forEach { row -> row.filterNotNull().forEach { add(it) } }
    }

    /**
     * Clears all data from this plane, including chunks and objects.
     */
    fun clear() {
        chunks.flatten().filterNotNull().forEach { it.clear() }
        if (region is DynamicRegion && objects != null) {
            for (x in 0 until REGION_SIZE) {
                for (y in 0 until REGION_SIZE) {
                    objects!![x][y] = null
                }
            }
            objects = null
        }
    }

    /**
     * Adds an NPC to the plane.
     */
    fun add(npc: NPC) { npcs.add(npc) }

    /**
     * Adds a player to the plane.
     */
    fun add(player: Player) { players.add(player) }

    /**
     * Adds a ground item to the appropriate chunk.
     */
    fun add(item: GroundItem) {
        val l = item.location
        val chunk = getRegionChunk(l.localX / RegionChunk.SIZE, l.localY / RegionChunk.SIZE)
        if (!chunk.items.add(item)) return
        if (item.isPrivate) {
            item.dropper?.let { PacketRepository.send(ConstructGroundItem::class.java, OutgoingContext.BuildItem(it, item, 0)) }
        } else {
            chunk.flag(ItemUpdateFlag(item, ItemUpdateFlag.CONSTRUCT_TYPE))
        }
    }

    /** Removes an NPC from the plane. */
    fun remove(npc: NPC) { npcs.remove(npc) }

    /** Removes a player from the plane. */
    fun remove(player: Player) { players.remove(player) }

    /**
     * Removes a ground item from the appropriate chunk.
     */
    fun remove(item: GroundItem) {
        val l = item.location
        val chunk = getRegionChunk(l.localX / RegionChunk.SIZE, l.localY / RegionChunk.SIZE)
        if (!chunk.items.remove(item)) return
        if (item.isPrivate) {
            val dropper = item.dropper
            if (dropper != null && dropper.isPlaying && dropper.location.withinDistance(l)) {
                PacketRepository.send(ClearGroundItem::class.java, OutgoingContext.BuildItem(dropper, item, 0))
            }
        } else {
            chunk.flag(ItemUpdateFlag(item, ItemUpdateFlag.REMOVE_TYPE))
        }
    }

    /**
     * Returns all items within a specific chunk.
     */
    fun getChunkItems(x: Int, y: Int): List<GroundItem> {
        val chunkX = x / CHUNK_SIZE
        val chunkY = y / CHUNK_SIZE
        return getRegionChunk(chunkX, chunkY).items
    }

    /**
     * Retrieves a specific ground item by ID and location, with optional visibility check for a player.
     *
     * @param itemId The ID of the item.
     * @param l The location of the item.
     * @param player Optional player to check private ownership.
     */
    fun getItem(itemId: Int, l: Location, player: Player?): GroundItem? {
        var match: GroundItem? = null
        for (item in getChunkItems(l.localX, l.localY)) {
            val g = item as GroundItem
            if (g.id == itemId && l == g.location && !g.isRemoved) {
                if (match != null && (!g.isPrivate || player == null)) continue
                if (!g.isPrivate || player == null || g.droppedBy(player)) {
                    match = g
                }
            }
        }
        return match
    }

    /**
     * Retrieves an object from the plane at the given tile.
     *
     * @param x The x-coordinate (local to region).
     * @param y The y-coordinate (local to region).
     * @param objectId Optional object ID for dynamic chunk lookup.
     */
    fun getChunkObject(x: Int, y: Int, objectId: Int = -1): Scenery? {
        val chunkX = x / CHUNK_SIZE
        val chunkY = y / CHUNK_SIZE
        val offsetX = x % CHUNK_SIZE
        val offsetY = y % CHUNK_SIZE
        val chunk = getRegionChunk(chunkX, chunkY)
        return if (chunk is BuildRegionChunk) {
            val index = chunk.getIndex(offsetX, offsetY, objectId)
            chunk.get(offsetX, offsetY, index)
        } else {
            chunk.objects[offsetX][offsetY]
        }
    }

    /**
     * Returns a list of all entity nodes (NPCs and scenery) in this plane.
     */
    fun getEntities(): List<Node> = buildList {
        addAll(npcs)
        objects?.forEach { row -> row.filterNotNull().forEach { add(it) } }
    }
}