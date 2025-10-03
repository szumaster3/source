package content.global.skill.crafting.glass

import core.api.*
import core.game.event.ResourceProducedEvent
import core.game.interaction.Clocks
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Sounds

/**
 * Represents Glassblowing pulse.
 *
 * @property player the player.
 * @property product the product.
 * @property amount the amount.
 */
/*
 * TODO:
 *  [ ] - Find easter egg sounds.
 *  [ ] - Find at what point they were used.
 */
class GlassblowingPulse(
    private val player: Player,
    private val product: GlassProduct,
    private var amount: Int,
) : Pulse() {
    override fun pulse(): Boolean {
        if (!clockReady(player, Clocks.SKILLING)) return false
        delayClock(player, Clocks.SKILLING, 3)

        if (amount < 1) {
            return true
        }

        if (!inInventory(player, Items.GLASSBLOWING_PIPE_1785) || !inInventory(player, Items.MOLTEN_GLASS_1775)) {
            return true
        }

        lock(player, 3)
        animate(player, Animations.GLASS_BLOW_884)
        playAudio(player, Sounds.GLASSBLOWING_2724)

        if (product.productId in intArrayOf(Items.UNPOWERED_ORB_567, Items.OIL_LAMP_4525)) {
            sendMessage(player, "You make an ${getItemName(product.productId)}.")
        } else {
            sendMessage(player, "You make a ${getItemName(product.productId)}.")
        }

        if (removeItem(player, Items.MOLTEN_GLASS_1775)) {
            addItem(player, product.productId, product.amount)
            rewardXP(player, Skills.CRAFTING, product.experience)
            player.dispatch(ResourceProducedEvent(product.productId, product.amount, player))
        } else {
            return true
        }
        amount--
        return false
    }
}
