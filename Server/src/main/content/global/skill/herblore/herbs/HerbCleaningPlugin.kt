package content.global.skill.herblore.herbs

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Quests

class HerbCleaningPlugin : InteractionListener {

    override fun defineListeners() {

        on(IntType.ITEM, "clean") { player, node ->
            if (!requireQuest(player, Quests.DRUIDIC_RITUAL, "before you can use Herblore.")) return@on true
            val herb: HerbItem = HerbItem.forItem(node as Item) ?: return@on true

            if (getDynLevel(player, Skills.HERBLORE) < herb.level) {
                sendMessage(
                    player,
                    "You cannot clean this herb. You need a Herblore level of " + herb.level + " to attempt this."
                )
                return@on true
            }

            lock(player, 1)
            val exp = herb.experience
            replaceSlot(player, node.asItem().slot, herb.product, node.asItem())
            rewardXP(player, Skills.HERBLORE, exp)
            playAudio(player, 5153)
            sendMessage(
                player,
                "You clean the dirt from the " +
                        herb.product.name
                            .lowercase()
                            .replace("clean", "")
                            .trim { it <= ' ' } +
                        " leaf.",
            )
            return@on true
        }
    }
}