package content.global.skill.herblore.potions

import content.global.skill.herblore.HerblorePulse
import core.api.asItem
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class FinishedPotionPlugin : UseWithHandler(*unfinishedItems) {

    override fun newInstance(arg: Any?): Plugin<Any> {
        for (potion in FinishedPotion.values()) {
            addHandler(potion.ingredient, ITEM_TYPE, this)
        }
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val finished: FinishedPotion =
            FinishedPotion.getPotion(
                if (event.usedItem.name.contains("(unf)")) event.usedItem.id else event.baseItem.id,
                if (event.usedItem.name.contains("(unf)")) event.baseItem.id else event.usedItem.id,
            ) ?: return false
        val potion: GenericPotion = GenericPotion.transform(finished)
        val player = event.player
        val handler: SkillDialogueHandler =
            object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, potion.product!!) {
                override fun create(
                    amount: Int,
                    index: Int,
                ) {
                    player.pulseManager.run(HerblorePulse(player, potion.base.asItem(), amount, potion))
                }

                override fun getAll(index: Int): Int = player.inventory.getAmount(potion.base)
            }
        if (player.inventory.getAmount(potion.base) == 1) {
            handler.create(0, 1)
        } else {
            handler.open()
        }
        return true
    }

    companion object {
        val unfinishedItems: IntArray
            get() {
                val ids = IntArray(UnfinishedPotion.values().size)
                var counter = 0
                for (potion in UnfinishedPotion.values()) {
                    ids[counter] = potion.potion
                    counter++
                }
                return ids
            }
    }
}
