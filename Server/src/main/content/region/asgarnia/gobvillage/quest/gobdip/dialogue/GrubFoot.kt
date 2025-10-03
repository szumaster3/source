package content.region.asgarnia.gobvillage.quest.gobdip.dialogue

import core.api.getVarp
import core.api.setVarp
import core.game.node.entity.player.Player
import core.game.node.item.Item
import shared.consts.Items

/**
 * Enum representing different GrubFoot types.
 */
enum class GrubFoot(val id: Int, val value: Int, val mail: Item) {
    NORMAL(4495, 1, Item(Items.GOBLIN_MAIL_288)),
    ORANGE(4497, 4, Item(Items.ORANGE_GOBLIN_MAIL_286)),
    BLUE(4498, 5, Item(Items.BLUE_GOBLIN_MAIL_287)),
    BROWN(4496, 6, Item(Items.GOBLIN_MAIL_288));

    /**
     * Sets the config.
     *
     * @param player The player.
     */
    fun setConfig(player: Player) {
        setVarp(player, 62, value)
    }

    companion object {
        /**
         * Gets next GrubFoot from config.
         */
        @JvmStatic
        fun forConfig(player: Player): GrubFoot {
            val config = getVarp(player, 62)
            val index = values().indexOfFirst { it.value == config }
            return if (index == -1 || index == values().lastIndex) NORMAL else values()[index + 1]
        }
    }
}