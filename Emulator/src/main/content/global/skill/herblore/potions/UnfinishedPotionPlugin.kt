package content.global.skill.herblore.potions

import content.global.skill.herblore.HerblorePulse
import core.api.asItem
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items

@Initializable
class UnfinishedPotionPlugin : UseWithHandler(Items.VIAL_OF_WATER_227, Items.COCONUT_MILK_5935) {

    override fun newInstance(arg: Any?): Plugin<Any> {
        for (potion in UnfinishedPotion.values()) {
            addHandler(potion.ingredient, ITEM_TYPE, this)
        }
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val unf: UnfinishedPotion = UnfinishedPotion.forID(event.usedItem.id, event.baseItem.id) ?: return false
        val potion: GenericPotion = GenericPotion.transform(unf)
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
}
