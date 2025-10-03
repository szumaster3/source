package content.global.plugin.item

import core.api.inInventory
import core.api.removeItem
import core.api.replaceSlot
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.Items
import java.util.*


/**
 * Represents dyes with their item ids.
 */
private enum class Dyes(val id: Int) {
    BLACK(Items.BLACK_MUSHROOM_INK_4622),
    RED(Items.RED_DYE_1763),
    YELLOW(Items.YELLOW_DYE_1765),
    BLUE(Items.BLUE_DYE_1767),
    ORANGE(Items.ORANGE_DYE_1769),
    GREEN(Items.GREEN_DYE_1771),
    PURPLE(Items.PURPLE_DYE_1773),
    PINK(Items.PINK_DYE_6955);

    companion object {
        private val idToDye = values().associateBy { it.id }
        fun forId(id: Int) = idToDye[id]
    }
}

/**
 * Represents capes and their corresponding dye and item ids.
 */
private enum class Capes(val dyeId: Int, val capeId: Int) {
    BLACK(Dyes.BLACK.id, Items.BLACK_CAPE_1019),
    RED(Dyes.RED.id, Items.RED_CAPE_1007),
    BLUE(Dyes.BLUE.id, Items.BLUE_CAPE_1021),
    YELLOW(Dyes.YELLOW.id, Items.YELLOW_CAPE_1023),
    GREEN(Dyes.GREEN.id, Items.GREEN_CAPE_1027),
    PURPLE(Dyes.PURPLE.id, Items.PURPLE_CAPE_1029),
    ORANGE(Dyes.ORANGE.id, Items.ORANGE_CAPE_1031),
    PINK(Dyes.PINK.id, Items.PINK_CAPE_6959);

    companion object {
        private val dyeIdToCape = values().associateBy { it.dyeId }
        fun forDyeId(id: Int) = dyeIdToCape[id]
    }
}

/**
 * Plugin handling item dyeing, including mixing dyes,
 * dyeing capes, and dyeing goblin armor.
 */
class ItemDyePlugin : InteractionListener {

    companion object {
        private val DYES = Dyes.values().map { it.id }.toIntArray()
        private val CAPES = Capes.values().map { it.capeId }.toIntArray()
        private val GOBLIN_MAIL = intArrayOf(Items.GOBLIN_MAIL_288, Items.RED_GOBLIN_MAIL_9054, Items.ORANGE_GOBLIN_MAIL_286, Items.YELLOW_GOBLIN_MAIL_9056, Items.GREEN_GOBLIN_MAIL_9057, Items.BLUE_GOBLIN_MAIL_287, Items.PURPLE_GOBLIN_MAIL_9058, Items.BLACK_GOBLIN_MAIL_9055, Items.PINK_GOBLIN_MAIL_9059)
    }

    override fun defineListeners() {

        /*
         * Handles mix two dyes together.
         */

        onUseWith(IntType.ITEM, DYES, *DYES) { player, used, with ->
            handleDyeCombine(player, used.id, with.id)
            return@onUseWith true
        }

        /*
         * Handles coloring the capes with a dye.
         */

        onUseWith(IntType.ITEM, DYES, *CAPES) { player, used, with ->
            Capes.forDyeId(used.id)?.let { cape ->
                if (!removeItem(player, used.id)) return@onUseWith false
                replaceSlot(player, with.index, Item(cape.capeId))
                player.sendMessage("You dye the cape.")
                return@onUseWith true
            } ?: run {
                player.sendMessage("This dye cannot be used with this cape.")
                return@onUseWith false
            }
        }

        /*
         * Handles dye the goblin mail for (Goblin Diplomacy quest).
         */

        onUseWith(IntType.ITEM, DYES, *GOBLIN_MAIL) { player, used, with ->
            if (with.id == Items.GOBLIN_MAIL_288) {
                dyeGoblinMail(player, used.id, with.id, with.index)
            } else {
                player.sendMessage("That item is already dyed.")
            }
            return@onUseWith true
        }

        /*
         * Handles message when trying to wear goblin armor.
         */

        on(GOBLIN_MAIL, IntType.ITEM, "wear") { player, _ ->
            player.sendMessage("That armour is too small for a human.")
            return@on true
        }
    }

    /**
     * Mixes two different dyes to create a new color.
     */
    private fun handleDyeCombine(player: Player, primaryId: Int, secondaryId: Int): Boolean {
        val firstColor = Dyes.forId(primaryId) ?: return false
        val secondColor = Dyes.forId(secondaryId) ?: return false
        if (firstColor == secondColor) return false

        val mix = when (setOf(firstColor, secondColor)) {
            setOf(Dyes.RED, Dyes.YELLOW) -> Dyes.ORANGE
            setOf(Dyes.YELLOW, Dyes.BLUE) -> Dyes.GREEN
            setOf(Dyes.RED, Dyes.BLUE) -> Dyes.PURPLE
            else -> null
        } ?: return player.sendMessage("Those dyes don't mix together.").let { false }

        if (!inInventory(player, firstColor.id) || !inInventory(player, secondColor.id)) {
            player.sendMessage("You don't have the required dyes to mix.")
            return false
        }

        player.lock(1)
        player.animate(Animation.create(Animations.DYE_COMBINE_4348))
        player.inventory.remove(Item(firstColor.id))
        player.inventory.remove(Item(secondColor.id))
        player.inventory.add(Item(mix.id))

        val article = if (mix.name.first().lowercaseChar() in "aeiou") "an" else "a"
        player.sendMessage("You mix the two dyes and make $article ${mix.name.lowercase()} dye.")
        return true
    }

    /**
     * Dyes goblin armor using a dye item.
     */
    private fun dyeGoblinMail(player: Player, dyeId: Int, mailId: Int, mailSlot: Int): Boolean {
        val dye = Dyes.forId(dyeId) ?: return false
        if (mailId != Items.GOBLIN_MAIL_288) return false

        val productId = GOBLIN_MAIL.getOrNull(dye.ordinal) ?: return false
        if (!removeItem(player, Item(dye.id))) return false

        replaceSlot(player, mailSlot, Item(productId))
        player.sendMessage("You dye the goblin armour ${dye.name.lowercase(Locale.getDefault())}.")
        return true
    }
}