package content.global.skill.construction.decoration.chapel

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class LampPlugin: OptionHandler() {

    private val burnerIds = intArrayOf(
        Scenery.TORCH_13202,
        Scenery.TORCH_13203,
        Scenery.TORCH_13204,
        Scenery.TORCH_13205,
        Scenery.TORCH_13206,
        Scenery.TORCH_13207,
        Scenery.INCENSE_BURNER_13208,
        Scenery.INCENSE_BURNER_13209,
        Scenery.INCENSE_BURNER_13210,
        Scenery.INCENSE_BURNER_13211,
        Scenery.INCENSE_BURNER_13212,
        Scenery.INCENSE_BURNER_13213,
    )

    override fun newInstance(arg: Any?): OptionHandler {
        burnerIds.forEach {
            SceneryDefinition.forId(it).handlers["option:light"] = this
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        if (player.ironmanManager.checkRestriction() && !player.houseManager.isInHouse(player)) {
            return true
        }

        val missing = checkRequirements(player)
        if (missing != null) {
            sendDialogue(player, missing)
            return true
        }

        if (removeItem(player, Item(Items.CLEAN_MARRENTILL_251))) {
            lock(player, 1)
            animate(player, Animations.USE_TINDERBOX_3687)
            sendMessage(player, "You burn some marrentill in the incense burner.")
            replaceScenery(node.asScenery(), node.id + 1, RandomFunction.random(100, 175), node.location)
        }
        return true
    }

    private fun checkRequirements(player: Player): String? = when {
        !anyInInventory(player, Items.TINDERBOX_590, Items.CLEAN_MARRENTILL_251) ->
            "You'll need a tinderbox and a clean marrentill herb in order to light the burner."

        inInventory(player, Items.TINDERBOX_590) && !inInventory(player, Items.CLEAN_MARRENTILL_251) ->
            "You'll need a clean marrentill herb in order to light the burner."

        inInventory(player, Items.CLEAN_MARRENTILL_251) && !inInventory(player, Items.TINDERBOX_590) ->
            "You'll need a tinderbox in order to light the burner."

        else -> null
    }
}
