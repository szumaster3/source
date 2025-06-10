package content.region.kandarin.handlers.barbariantraining.smithing

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items
import kotlin.math.min

/**
 * Handles the smithing of barbarian weapons.
 */
class BarbSmithingPulse(
    player: Player?,
    val weapon: BarbarianWeapon,
    var amount: Int,
    var button: Int,
) : SkillPulse<Item>(player, null) {

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.SMITHING) < weapon.requiredLevel) {
            sendMessage(player, "You need a Smithing level of ${weapon.requiredLevel} to make this.")
            return false
        }

        if (!inInventory(player, Items.HAMMER_2347)) {
            sendDialogue(player, "You need a hammer to work the metal with.")
            return false
        }

        if (!inInventory(player, weapon.requiredBar)) {
            sendDialogue(player, "You don't have the necessary material for the weapon.")
            return false
        }

        if (!inInventory(player, weapon.requiredWood)) {
            sendDialogue(player, "You don't have the necessary logs for the weapon.")
            return false
        }

        return true
    }

    override fun start() {
        super.start()
        val barsAmount = player.inventory.getAmount(Item(weapon.requiredBar))
        val woodAmount = player.inventory.getAmount(Item(weapon.requiredWood))
        amount = min(min(barsAmount, woodAmount), amount)
    }

    override fun animate() {
        animate(player, Animations.HUMAN_ANVIL_HAMMER_SMITHING_898)
    }

    override fun reward(): Boolean {
        if (delay == 1) {
            delay = 4
            return false
        }

        val itemToMake = if (button == 0) weapon.spearId else weapon.hastaId

        if (player.inventory.remove(Item(weapon.requiredBar, 1)) &&
            player.inventory.remove(Item(weapon.requiredWood, 1))) {
            sendMessage(player, "You make a ${getItemName(itemToMake)}.")
            player.inventory.add(Item(itemToMake, 1))
            rewardXP(player, Skills.SMITHING, weapon.experience)
            amount--
            return amount == 0
        }
        return true
    }

    override fun message(type: Int) {}
}