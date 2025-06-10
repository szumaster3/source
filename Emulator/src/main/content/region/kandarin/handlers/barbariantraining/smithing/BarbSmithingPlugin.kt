package content.region.kandarin.handlers.barbariantraining.smithing

import core.api.amountInInventory
import core.api.asItem
import core.api.skill.sendSkillDialogue
import core.api.submitIndividualPulse
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Scenery

/**
 * Handles using bars on the barbarian anvil to smith barbarian weapons.
 */
@Initializable
class BarbSmithingPlugin : UseWithHandler(*BarbarianWeapon.values().map { it.requiredBar }.toIntArray()) {

    companion object {
        private val ANVIL_ID = Scenery.BARBARIAN_ANVIL_25349
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        BarbarianWeapon.values().forEach { _ ->
            addHandler(ANVIL_ID, OBJECT_TYPE, this)
        }
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player
        val usedItemId = event.usedItem.id
        val usedWith = event.usedWith

        if (usedWith.id != ANVIL_ID) return false

        val weapon = BarbarianWeapon.getWeapon(usedItemId) ?: return false

        sendSkillDialogue(player) {
            withItems(weapon.spearId.asItem(), weapon.hastaId.asItem())
            create { selectedId, amount ->
                submitIndividualPulse(
                    entity = player,
                    pulse = BarbSmithingPulse(player, weapon, amount, selectedId),
                )
            }
            calculateMaxAmount {
                amountInInventory(player, usedItemId)
            }
        }
        return true
    }
}
