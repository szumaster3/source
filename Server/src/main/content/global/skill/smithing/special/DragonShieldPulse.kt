package content.global.skill.smithing.special

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Animations
import shared.consts.Items

class DragonShieldPulse(
    player: Player?,
    private val item: Item,
) : SkillPulse<Item>(player, null) {

    private var tick = 0

    override fun checkRequirements(): Boolean {
        if (!inInventory(player, Items.SHIELD_LEFT_HALF_2366)) {
            sendDialogue(player, "You need to have a shield left half to attach the other half to.")
            return false
        }
        if (!inInventory(player, Items.SHIELD_RIGHT_HALF_2368)) {
            sendDialogue(player, "You need to have a shield right half to attach the other half to.")
            return false
        }
        return true
    }

    override fun animate() {

    }

    override fun reward(): Boolean {
        when (tick) {
            0 -> {
                lock(player, 10)
                animate(player, Animations.HUMAN_ANVIL_HAMMER_SMITHING_898)
            }

            5 -> animate(player, Animations.HUMAN_ANVIL_HAMMER_SMITHING_898)

            9 -> {
                sendPlainDialogue(player, false, "Even for an experienced armourer it is not an easy task, but", "eventually it is ready. You have restored the dragon square shield to", "its former glory.")
                addDialogueAction(player) { _, _ ->
                    if (removeItem(player, Items.SHIELD_LEFT_HALF_2366) &&
                        removeItem(player, Items.SHIELD_RIGHT_HALF_2368)
                    ) {
                        addItem(player, Items.DRAGON_SQ_SHIELD_1187, 1)
                        rewardXP(player, Skills.SMITHING, 75.0)
                    } else {
                        closeDialogue(player)
                    }
                    return@addDialogueAction
                }
            }
        }
        tick++
        return false
    }

    override fun message(type: Int) {}
}
