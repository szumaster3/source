package content.global.skill.fletching.items.crossbow

import core.api.clockReady
import core.api.delayClock
import core.api.playAudio
import core.game.interaction.Clocks
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import shared.consts.Sounds

class LimbPulse(player: Player?, node: Item, private val limb: Limb, private var amount: Int) : SkillPulse<Item?>(player, node) {

    override fun checkRequirements(): Boolean {
        if (player.skills.getLevel(Skills.FLETCHING) < limb.level) {
            player.dialogueInterpreter.sendDialogue(
                "You need a fletching level of " + limb.level + " to attach these limbs.",
            )
            return false
        }
        if (!player.inventory.containsItem(Item(limb.limb))) {
            player.dialogueInterpreter.sendDialogue("That's not the correct limb to attach.")
            return false
        }
        if (!player.inventory.containsItem(Item(limb.stock))) {
            player.dialogueInterpreter.sendDialogue("That's not the correct stock for that limb.")
            return false
        }
        return player.inventory.containsItem(Item(limb.stock))
    }

    override fun animate() {
        player.animate(Animation.create(limb.animation))
        playAudio(player, Sounds.STRING_CROSSBOW_2924)
    }

    override fun reward(): Boolean {
        if (!clockReady(player, Clocks.SKILLING)) return false
        delayClock(player, Clocks.SKILLING, 6)

        if (player.inventory.remove(Item(limb.stock), Item(limb.limb))) {
            player.inventory.add(Item(limb.product))
            player.skills.addExperience(Skills.FLETCHING, limb.experience, true)
            player.packetDispatch.sendMessage("You attach the metal limbs to the stock.")
        }
        if (!player.inventory.containsItem(Item(limb.limb))) {
            return true
        }
        amount--
        return amount == 0
    }

    override fun message(type: Int) {}
}
