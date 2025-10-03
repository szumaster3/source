package content.global.skill.construction;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import content.data.GameAttributes;
import content.global.skill.construction.servants.Servant;
import core.api.regionspec.RegionSpecification;
import core.api.regionspec.contracts.FillChunkContract;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import core.game.world.map.*;
import core.game.world.map.build.DynamicRegion;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneBuilder;
import core.game.world.update.flag.context.Animation;
import core.net.packet.OutgoingContext;
import core.net.packet.PacketRepository;
import core.net.packet.out.MinimapState;
import core.tools.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import core.game.world.GameWorld;
import shared.consts.Components;
import shared.consts.Music;
import shared.consts.NPCs;
import shared.consts.Sounds;

import java.awt.*;

import static core.api.ContentAPIKt.*;
import static core.api.regionspec.RegionSpecificationKt.fillWith;
import static core.api.regionspec.RegionSpecificationKt.using;
import static core.tools.GlobalsKt.DARK_BLUE;

/**
 * Manages the player's house.
 *
 * @author Emperor
 */
public final class HouseManager {

    /**
     * The current region.
     */
    private DynamicRegion houseRegion;

    /**
     * The current region.
     */
    private DynamicRegion dungeonRegion;

    /**
     * The house location.
     */
    private HouseLocation location = HouseLocation.NOWHERE;

    /**
     * The house style.
     */
    private HousingStyle style = HousingStyle.BASIC_WOOD;

    /**
     * The house zone.
     */
    private HouseZone zone = new HouseZone(this);

    /**
     * The player's house rooms.
     */
    private final Room[][][] rooms = new Room[4][8][8];

    /**
     * If building mode is enabled.
     */
    private boolean buildingMode;

    /**
     * If the player has used the portal to lock their house.
     */
    private boolean locked;

    /**
     * The player's servant.
     */
    private Servant servant;

    /**
     * If the house has a dungeon.
     */
    private boolean hasDungeon;

    /**
     * The player's crest.
     */
    private CrestType crest = CrestType.ASGARNIA;

    /**
     * Constructs a new {@code HouseManager} {@code Object}.
     */
    public HouseManager() {
        /*
         * empty.
         */
    }


    public void parse(JsonObject data) {
        location = HouseLocation.values()[data.get("location").getAsInt()];
        style = HousingStyle.values()[data.get("style").getAsInt()];

        if (data.has("servant") && !data.get("servant").isJsonNull()) {
            servant = Servant.parse(data.getAsJsonObject("servant"));
        }

        JsonArray rArray = data.getAsJsonArray("rooms");
        for (JsonElement elem : rArray) {
            JsonObject rm = elem.getAsJsonObject();

            int z = rm.get("z").getAsInt();
            int x = rm.get("x").getAsInt();
            int y = rm.get("y").getAsInt();

            if (z == 3) hasDungeon = true;

            Room room = rooms[z][x][y] = new Room(RoomProperties.values()[rm.get("properties").getAsInt()]);
            room.configure(style);
            room.setRotation(Direction.get(rm.get("rotation").getAsInt()));

            JsonArray hotspots = rm.getAsJsonArray("hotspots");
            for (JsonElement hElem : hotspots) {
                JsonObject spot = hElem.getAsJsonObject();
                int hotspotIndex = spot.get("hotspotIndex").getAsInt();
                int decorationIndex = spot.get("decorationIndex").getAsInt();
                room.getHotspots()[hotspotIndex].setDecorationIndex(decorationIndex);
            }
        }
    }

    /**
     * Handles entering the player house, including construction and teleport logic.
     */
    public void enter(final Player player, boolean buildingMode) {
        preEnter(player, buildingMode);
        player.getProperties().setTeleportLocation(getEnterLocation());
        player.getInterfaceManager().closeSingleTab();
        postEnter(player, buildingMode);
    }

    /**
     * Prepares the house for entry. Constructs the house if needed.
     */
    public void preEnter(final Player player, boolean buildingMode) {
        if (!isLoaded() || this.buildingMode != buildingMode) {
            this.buildingMode = buildingMode;
            construct();
        }

        player.setAttribute("poh_entry", HouseManager.this);
        player.setAttribute("/save:original-loc", location.getExitLocation());
        player.debug("House location: " + houseRegion.getBaseLocation() + ", entry: " + getEnterLocation());
    }

    /**
     * Called after the player enters the house.
     */
    public void postEnter(final Player player, boolean buildingMode) {
        checkForAndSpawnServant(player);
        updateVarbits(player, buildingMode);
        unlockMusicTrack(player);
        openLoadInterface(player);
    }

    /**
     * Opens the loading screen.
     */
    private void openLoadInterface(Player player) {
        openInterface(player, Components.POH_HOUSE_LOADING_SCREEN_399);
        PacketRepository.send(MinimapState.class, new OutgoingContext.MinimapState(player, 2));
        playAudio(player, Sounds.POH_TP_984);

        GameWorld.getPulser().submit(new Pulse(6, player) {
            @Override
            public boolean pulse() {
                PacketRepository.send(MinimapState.class, new OutgoingContext.MinimapState(player, 0));
                player.getInterfaceManager().close();
                return true;
            }
        });
    }

    private void checkForAndSpawnServant(Player player) {
        if (!hasServant()) return;

        GameWorld.getPulser().submit(new Pulse(1, player) {
            @Override
            public boolean pulse() {
                spawnServant();
                if (servant.isGreet()) {
                    player.getDialogueInterpreter().sendDialogues(servant.getType().getId(), servant.getType().getId() == NPCs.DEMON_BUTLER_4243 ? FaceAnim.OLD_DEFAULT : FaceAnim.HALF_GUILTY, DARK_BLUE + "Welcome.");
                }
                return true;
            }
        });
    }

    private void updateVarbits(Player player, boolean build) {
        setVarp(player, 261, build ? 1 : 0);
        setVarp(player, 262, getRoomAmount());
    }

    private void unlockMusicTrack(Player player) {
        player.getMusicPlayer().unlock(Music.HOME_SWEET_HOME_454, true);
    }

    /**
     * Leaves this house through the portal.
     *
     * @param player The player leaving.
     */
    public static void leave(Player player) {
        HouseManager house = player.getAttribute(GameAttributes.POH_PORTAL, player.getHouseManager());
        if (house.getHouseRegion() == null) {
            return;
        }
        if (house.isInHouse(player)) {
            player.animate(Animation.RESET);
            player.getProperties().setTeleportLocation(house.location.getExitLocation());
        }
    }

    /**
     * Toggles the building mode.
     *
     * @param player The house owner.
     * @param enable If the building mode should be enabled.
     */
    public void toggleBuildingMode(Player player, boolean enable) {
        if (!isInHouse(player)) {
            player.getPacketDispatch().sendMessage("Building mode really only helps if you're in a house.");
            return;
        }
        if (buildingMode != enable) {
            if (enable) {
                expelGuests(player);
            }
            enter(player, enable);
            player.getPacketDispatch().sendMessage("Building mode is now " + (buildingMode ? "on." : "off."));
        }
    }

    /**
     * Reloads the house.
     *
     * @param player       The player.
     * @param buildingMode If building mode should be enabled.
     */
    public void reload(Player player, boolean buildingMode) {
        int diffX = player.getLocation().getLocalX();
        int diffY = player.getLocation().getLocalY();
        int diffZ = player.getLocation().getZ();
        boolean inDungeon = player.getViewport().getRegion() == dungeonRegion;
        this.buildingMode = buildingMode;
        construct();
        Location newLoc = (dungeonRegion == null ? houseRegion : (inDungeon ? dungeonRegion : houseRegion)).getBaseLocation().transform(diffX, diffY, diffZ);
        player.getProperties().setTeleportLocation(newLoc);
    }

    /**
     * Expels the guests from the house.
     *
     * @param player The house owner.
     */
    public void expelGuests(Player player) {
        if (isLoaded()) {
            for (RegionPlane plane : houseRegion.getPlanes()) {
                for (Player p : plane.getPlayers()) {
                    if (p != player) {
                        leave(p);
                    }
                }
            }
            if (dungeonRegion != null) {
                for (RegionPlane plane : dungeonRegion.getPlanes()) {
                    for (Player p : plane.getPlayers()) {
                        if (p != player) {
                            leave(p);
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets the entering location.
     *
     * @return The entering location.
     */
    public Location getEnterLocation() {
        if (houseRegion == null) {
            log(this.getClass(), Log.ERR, "House wasn't constructed yet!");
            return null;
        }
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Room room = rooms[0][x][y];
                if (room != null && (room.getProperties() == RoomProperties.GARDEN || room.getProperties() == RoomProperties.FORMAL_GARDEN)) {
                    for (Hotspot h : room.getHotspots()) {
                        if (h.getDecorationIndex() > -1) {
                            Decoration d = h.getHotspot().getDecorations()[h.getDecorationIndex()];
                            if (d == Decoration.PORTAL) {
                                return houseRegion.getBaseLocation().transform(x * 8 + h.getChunkX(), y * 8 + h.getChunkY() + 2, 0);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Redecorates the house.
     *
     * @param style The new style.
     */
    public void redecorate(HousingStyle style) {
        this.style = style;
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    Room room = rooms[z][x][y];
                    if (room != null) {
                        room.decorate(style);
                    }
                }
            }
        }
    }

    /**
     * Clears all the rooms (<b>Including portal room!</b>).
     */
    @Deprecated
    public void clearRooms() {
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    rooms[z][x][y] = null;
                }
            }
        }
    }

    /**
     * Creates the default house.
     *
     * @param location The house location.
     */
    public void createNewHouseAt(HouseLocation location) {
        clearRooms();
        Room room = rooms[0][4][3] = new Room(RoomProperties.GARDEN);
        room.configure(style);
        room.getHotspots()[0].setDecorationIndex(0);
        this.location = location;
    }

    /**
     * Constructs the dynamic region for the house.
     *
     * @return The region.
     */
    public DynamicRegion construct() {
        houseRegion = getPreparedRegion();
        configureRoofs();
        prepareHouseChunks(style, houseRegion, buildingMode, rooms);
        houseRegion.flagActive();

        if (hasDungeon()) {
            dungeonRegion = getPreparedRegion();
            prepareDungeonChunks(style, dungeonRegion, houseRegion, buildingMode, rooms[3]);
            dungeonRegion.flagActive();
        }

        ZoneBuilder.configure(zone);
        return houseRegion;
    }

    private DynamicRegion getPreparedRegion() {
        ZoneBorders borders = DynamicRegion.reserveArea(8, 8);
        DynamicRegion region = new DynamicRegion(-1, borders.getSouthWestX() >> 6, borders.getSouthWestY() >> 6);
        region.setBorders(borders);
        region.setUpdateAllPlanes(true);
        region.setBuild(true);
        RegionManager.addRegion(region.getId(), region);
        return region;
    }

    private class RoomLoadContract extends FillChunkContract {

        /**
         * The Rooms.
         */
        Room[][][] rooms;

        /**
         * The Manager.
         */
        HouseManager manager;

        /**
         * The Building mode.
         */
        boolean buildingMode;

        /**
         * Instantiates a new Room load contract.
         *
         * @param manager      the manager
         * @param buildingMode the building mode
         * @param rooms        the rooms
         */
        public RoomLoadContract(HouseManager manager, boolean buildingMode, Room[][][] rooms) {
            this.rooms = rooms;
            this.manager = manager;
            this.buildingMode = buildingMode;
        }

        @Override
        public BuildRegionChunk getChunk(int x, int y, int plane, @NotNull DynamicRegion dyn) {
            return rooms[plane][x][y].getChunk().copy(dyn.getPlanes()[plane]);
        }

        @Override
        public void afterSetting(@Nullable BuildRegionChunk chunk, int x, int y, int plane, @NotNull DynamicRegion dyn) {
            rooms[plane][x][y].loadDecorations(dyn != manager.dungeonRegion ? plane : 3, chunk, manager);
        }
    }

    private void prepareHouseChunks(HousingStyle style, DynamicRegion target, boolean buildingMode, Room[][][] rooms) {
        Region from = RegionManager.forId(style.getRegionId());
        Region.load(from, true);
        RegionChunk defaultChunk = from.getPlanes()[style.getPlane()].getRegionChunk(1, 0);
        RegionChunk defaultSkyChunk = from.getPlanes()[1].getRegionChunk(0, 0);

        RoomLoadContract loadRooms = new RoomLoadContract(this, buildingMode, rooms);
        RegionSpecification spec = new RegionSpecification(
                using(target),
                fillWith(defaultChunk)
                        .from(from)
                        .onPlanes(0),
                fillWith(defaultSkyChunk)
                        .from(from)
                        .onPlanes(1, 2, 3),
                loadRooms
                        .from(from)
                        .onPlanes(0, 1, 2)
                        .onCondition((destX, destY, plane) -> rooms[plane][destX][destY] != null)
        );

        spec.build();
    }

    private void prepareDungeonChunks(HousingStyle style, DynamicRegion target, DynamicRegion house, boolean buildingMode, Room[][] rooms) {
        Region from = RegionManager.forId(style.getRegionId());
        Region.load(from, true);
        RegionChunk defaultChunk = from.getPlanes()[style.getPlane()].getRegionChunk(3, 0);

        RoomLoadContract loadRooms = new RoomLoadContract(this, buildingMode, new Room[][][]{rooms});
        RegionSpecification spec = new RegionSpecification(
                using(target),
                fillWith((x, y, plane, region) -> buildingMode ? null : defaultChunk)
                        .from(from)
                        .onPlanes(0)
                        .onCondition((destX, destY, plane) -> rooms[destX][destY] == null),
                loadRooms
                        .from(from)
                        .onPlanes(0)
                        .onCondition((destX, destY, plane) -> rooms[destX][destY] != null)
        );

        spec.build();
        house.link(target);
    }

    /**
     * Configures the rooftops.
     */
    public void configureRoofs() {
//		boolean[][][] roofs = new boolean[2][8][8];
//		for (int x = 0; x < 8; x++) {
//			for (int y = 0; y < 8; y++) {
//				Room room = rooms[0][x][y];
//				if (room != null && room.getProperties().isChamber()) {
//					room = rooms[1][x][y];
//					int z = 1;
//					if (room != null && room.getProperties().isChamber()) {
//						z = 2;
//					}
//					if (x > 0 )
//				}
//			}
//		}
    }

    /**
     * Gets the current room plane.
     *
     * @param l The location.
     * @return The plane of the room.
     */
    public Room getRoom(Location l) {
        int z = l.getZ();
        if (dungeonRegion != null && l.getRegionId() == dungeonRegion.getId()) {
            z = 3;
        }
        return rooms[z][l.getChunkX()][l.getChunkY()];
    }

    /**
     * Gets the hotspot for the given object.
     *
     * @param object The object.
     * @return The hotspot.
     */
    public Hotspot getHotspot(Scenery object) {
        Room room = getRoom(object.getLocation());
        if (room == null) {
            return null;
        }
        int chunkX = object.getLocation().getChunkOffsetX();
        int chunkY = object.getLocation().getChunkOffsetY();
        switch (room.getRotation()) {
            case WEST: {
                int tempChunk = chunkY;
                chunkY = 7 - chunkX;
                chunkX = tempChunk;
                break;
            }
            case EAST: {
                //x = y, y = x, x = 7 - y
                int tempChunk = chunkX;
                chunkX = 7 - chunkY;
                chunkY = tempChunk;
                break;
            }
            case SOUTH: {
                chunkX = 7 - chunkX;
                chunkY = 7 - chunkY;
                break;
            }
            default: {

            }
        }
        for (Hotspot h : room.getHotspots()) {
            if ((h.getChunkX() == chunkX || h.getChunkX2() == chunkX) && (h.getChunkY() == chunkY || h.getChunkY2() == chunkY) && h.getHotspot().getObjectId(style) == object.getId()) {
                return h;
            }
        }
        return null;
    }

    /**
     * Checks if a room exists on the given location.
     *
     * @param z     The plane.
     * @param roomX The room x-coordinate.
     * @param roomY The room y-coordinate.
     * @return {@code True} if so.
     */
    public boolean hasRoomAt(int z, int roomX, int roomY) {
        Room room = rooms[z][roomX][roomY];
        return room != null && !room.getProperties().isRoof();
    }

    /**
     * Enters the dungeon.
     *
     * @param player The player.
     */
    public void enterDungeon(Player player) {
        if (!hasDungeon()) {
            return;
        }
        int diffX = player.getLocation().getLocalX();
        int diffY = player.getLocation().getLocalY();
        player.getProperties().setTeleportLocation(dungeonRegion.getBaseLocation().transform(diffX, diffY, 0));
    }

    /**
     * Checks if an exit exists on the given room.
     *
     * @param roomX     The x-coordinate of the room.
     * @param roomY     The y-coordinate of the room.
     * @param direction The exit direction.
     * @return {@code True} if so.
     */
    public boolean hasExit(int z, int roomX, int roomY, Direction direction) {
        Room room = rooms[z][roomX][roomY];
        int index = (direction.toInteger() + 3) % 4;
        return room != null && room.getExits()[index];
    }

    /**
     * Gets the amount of rooms.
     *
     * @return The amount of rooms.
     */
    public int getRoomAmount() {
        int count = 0;
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    Room r = rooms[z][x][y];
                    if (r != null && !r.getProperties().isRoof()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Gets the amount of portals available.
     *
     * @return The amount of portals.
     */
    public int getPortalAmount() {
        int count = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Room room = rooms[0][x][y];
                if (room != null && (room.getProperties() == RoomProperties.GARDEN
                        || room.getProperties() == RoomProperties.FORMAL_GARDEN) && room.getHotspots()[0].getDecorationIndex() == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Gets the current house boundaries.
     *
     * @return The boundaries.
     */
    public Rectangle getBoundaries() {
        int startX = 99;
        int startY = 99;
        int endX = 0;
        int endY = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (rooms[0][x][y] != null) {
                    if (x < startX) startX = x;
                    if (y < startY) startY = y;
                    if (x > endX) endX = x;
                    if (y > endY) endY = y;
                }
            }
        }
        return new Rectangle(startX, startY, (endX - startX) + 1, (endY - startY) + 1);
    }

    /**
     * Gets the maximum dimension for the house boundaries.
     *
     * @param player The player.
     * @return The dimension value (value X value = dimension)
     */
    public int getMaximumDimension(Player player) {
        int level = player.getSkills().getStaticLevel(Skills.CONSTRUCTION);
        if (level >= 60) {
            return 7;
        }
        if (level >= 45) {
            return 6;
        }
        if (level >= 30) {
            return 5;
        }
        if (level >= 15) {
            return 4;
        }
        return 3;
    }

    /**
     * Gets the maximum amount of rooms available for the player.
     *
     * @param player The player.
     * @return The maximum amount of rooms.
     */
    public int getMaximumRooms(Player player) {
        int level = player.getSkills().getStaticLevel(Skills.CONSTRUCTION);
        if (level >= 99) return 30;
        if (level >= 96) return 29;
        if (level >= 92) return 28;
        if (level >= 86) return 27;
        if (level >= 80) return 26;
        if (level >= 74) return 25;
        if (level >= 68) return 24;
        if (level >= 62) return 23;
        if (level >= 56) return 22;
        if (level >= 50) return 21;
        return 20;
    }

    /**
     * Spawns the servant inside the player's home.
     */
    private void spawnServant() {
        servant.setLocation(getEnterLocation());
        servant.setWalkRadius(getRoomAmount() * 2);
        servant.setWalks(true);
        servant.init();
    }

    /**
     * Checks if the player has a servant.
     *
     * @return {@code True} if so.
     */
    public boolean hasServant() {
        return servant != null;
    }

    /**
     * Checks if the player is in his own house (or dungeon).
     *
     * @param player The player.
     * @return {@code True} if so.
     */
    public boolean isInHouse(Player player) {
        return isLoaded() && (player.getViewport().getRegion() == houseRegion || player.getViewport().getRegion() == dungeonRegion);
    }

    /**
     * Checks if the player is in his dungeon.
     *
     * @param player The player.
     * @return {@code True} if so.
     */
    public static boolean isInDungeon(Player player) {
        return player.getViewport().getRegion() == player.getHouseManager().dungeonRegion;
    }

    /**
     * Checks if the house region was constructed and active.
     *
     * @return {@code True} if an active region for the house exists.
     */
    //public boolean isLoaded() {
    //	return (houseRegion != null) || (dungeonRegion != null);
    //}
    public boolean isLoaded() {
        return (houseRegion != null && houseRegion.isActive()) || (dungeonRegion != null && dungeonRegion.isActive());
    }

    /**
     * Gets the hasHouse.
     *
     * @return The hasHouse.
     */
    public boolean hasHouse() {
        return location != HouseLocation.NOWHERE;
    }

    /**
     * Checks if the house has a dungeon.
     *
     * @return {@code True} if so.
     */
    public boolean hasDungeon() {
        return hasDungeon;
    }

    /**
     * Sets the has dungeon value.
     *
     * @param hasDungeon If the house has a dungeon.
     */
    public void setHasDungeon(boolean hasDungeon) {
        this.hasDungeon = hasDungeon;
    }

    /**
     * Gets the rooms.
     *
     * @return The rooms.
     */
    public Room[][][] getRooms() {
        return rooms;
    }

    /**
     * Gets the location.
     *
     * @return The location.
     */
    public HouseLocation getLocation() {
        return location;
    }

    /**
     * Sets the location.
     *
     * @param location The location to set.
     */
    public void setLocation(HouseLocation location) {
        this.location = location;
    }

    /**
     * Checks if the building mode is enabled.
     *
     * @return {@code True} if so.
     */
    public boolean isBuildingMode() {
        return buildingMode;
    }

    /**
     * Checks if the player has locked their house.
     *
     * @return {@code True} if so.
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Sets the house to locked.
     *
     * @param locked true or false
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * Gets the region.
     *
     * @return The region.
     */
    public DynamicRegion getHouseRegion() {
        return houseRegion;
    }

    /**
     * Gets the dungeon region.
     *
     * @return The dungeon region.
     */
    public Region getDungeonRegion() {
        return dungeonRegion;
    }

    /**
     * Gets the style.
     *
     * @return the style
     */
    public HousingStyle getStyle() {
        return style;
    }

    /**
     * Sets the style.
     *
     * @param style the style to set.
     */
    public void setStyle(HousingStyle style) {
        this.style = style;
    }

    /**
     * Gets the player's servant
     *
     * @return the servant.
     */
    public Servant getServant() {
        return servant;
    }

    /**
     * Sets the player's servant
     *
     * @param servant The servant to set.
     */
    public void setServant(Servant servant) {
        this.servant = servant;
    }

    /**
     * Gets the crest value.
     *
     * @return The crest.
     */
    public CrestType getCrest() {
        return crest;
    }

    /**
     * Sets the crest value.
     *
     * @param crest The crest to set.
     */
    public void setCrest(CrestType crest) {
        this.crest = crest;
    }

    /**
     * @return the zone
     */
    public HouseZone getZone() {
        return zone;
    }
}
