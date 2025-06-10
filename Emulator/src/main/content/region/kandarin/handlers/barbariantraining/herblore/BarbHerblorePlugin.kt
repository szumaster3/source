package content.region.kandarin.handlers.barbariantraining.herblore

import content.region.kandarin.handlers.barbariantraining.BarbarianTraining
import core.api.*
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items

@Initializable
class BarbHerblorePlugin : UseWithHandler(*BarbarianMix.values().map { it.item }.toIntArray()) {

    companion object {
        private val ROE = Items.ROE_11324
        private val CAVIAR = Items.CAVIAR_11326
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        BarbarianMix.values().forEach { mix ->
            addHandler(mix.item, ITEM_TYPE, this)
            addHandler(ROE, ITEM_TYPE, this)
            addHandler(CAVIAR, ITEM_TYPE, this)
        }
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player
        val first = event.usedItem
        val second = event.usedWith

        // Search for potion and ingredient.
        val potion = BarbarianMix.forId(first.id) ?: BarbarianMix.forId(second.id) ?: return false
        val other = if (potion.item == first.id) second else first

        // Check for second ingredient (roe/caviar)
        if (other.id != ROE && other.id != CAVIAR) return false
        if (potion.both && other.id != ROE) return false

        if (!getAttribute(player, BarbarianTraining.HERBLORE_START, false)) {
            sendMessage(player, "You must begin the relevant section of Otto Godblessed's barbarian training.")
            return true
        }

        if (!hasLevelStat(player, Skills.HERBLORE, potion.level)) {
            sendMessage(player, "You need a Herblore level of ${potion.level} to make this mix.")
            return true
        }

        if (!removeItem(player, potion.item)) return false
        if (!removeItem(player, other.id)) {
            addItem(player, potion.item) // rollback
            return false
        }

        animate(player, org.rs.consts.Animations.PESTLE_MORTAR_364)
        addItem(player, potion.product)
        rewardXP(player, Skills.HERBLORE, potion.exp)
        sendMessage(player, "You combine your potion with the ${getItemName(other.id).lowercase()}.")

        // Barbarian training progress.
        if (getAttribute(player, BarbarianTraining.HERBLORE_BASE, false)) {
            removeAttribute(player, BarbarianTraining.HERBLORE_BASE)
            setAttribute(player, BarbarianTraining.HERBLORE_FULL, true)
            sendDialogueLines(
                player, "You feel you have learned more of barbarian ways. Otto might wish", "to talk to you more."
            )
        }

        return true
    }
}
