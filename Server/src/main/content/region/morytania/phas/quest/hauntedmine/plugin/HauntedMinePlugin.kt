package content.region.morytania.phas.quest.hauntedmine.plugin

import core.api.openInterface
import core.api.removeItem
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery

class HauntedMinePlugin : InteractionListener {
    override fun defineListeners() {
        on(NPCs.ZEALOT_1528, IntType.NPC, "pickpocket") { player, _ ->
            /*
            if (isQuestInProgress(player, Quests.HAUNTED_MINE, 1, 2)) {
                lock(player, 1)
                animate(player, Animations.HUMAN_PICKPOCKETING_881)
                if (freeSlots(player) == 0) {
                    sendMessage(player, "You need a space in your inventory in order to attempt this.")
                    return@on true
                }

                if (inBank(player, Items.ZEALOTS_KEY_4078) || inEquipmentOrInventory(player, Items.ZEALOTS_KEY_4078)) {
                    sendMessage(player, "I've already picked his pockets.")
                } else {
                    sendMessage(
                        player,
                        if (getQuestStage(player, Quests.HAUNTED_MINE) <
                            2
                        ) {
                            "I doubt he's got much of value on him."
                        } else {
                            "You pick the zealot's pocket and retrieve a small silvery key."
                        },
                    )
                    addItem(player, Items.ZEALOTS_KEY_4078)
                }
            }
            */
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.GLOWING_FUNGUS_4075, Scenery.MINE_CART_4974) { player, used, _ ->
            if (removeItem(player, used.asItem())) {
                sendMessage(player, "You place the glowing fungus in the mine cart.")
            }
            return@onUseWith true
        }

        on(Scenery.POINTS_SETTINGS_4949, IntType.SCENERY, "check") { player, _ ->
            openInterface(player, Components.HAUNTEDMINE_CONTROLS_144)
            return@on true
        }
    }
}
