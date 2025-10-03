package content.region.desert.alkharid.plugin

import content.global.plugin.iface.WarningListener
import content.global.plugin.iface.Warnings
import content.global.skill.agility.AgilityHandler
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.tools.DARK_RED
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery

class ShantayPassPlugin : InteractionListener {

    private val shantayPassSceneryId = intArrayOf(Scenery.SHANTAY_PASS_35542, Scenery.SHANTAY_PASS_35543, Scenery.SHANTAY_PASS_35544, Scenery.SHANTAY_PASS_35400)

    override fun defineListeners() {

        /*
         * Handles buying shantay pass.
         */

        on(NPCs.SHANTAY_836, IntType.NPC, "buy-pass") { player, _ ->
            when {
                freeSlots(player) == 0 -> {
                    sendNPCDialogue(player, NPCs.SHANTAY_836, "Sorry friend, you'll need more inventory space to buy a pass.", FaceAnim.NEUTRAL)
                }
                !removeItem(player, Item(Items.COINS_995, 5)) -> {
                    sendNPCDialogue(player, NPCs.SHANTAY_836, "Sorry friend, the Shantay Pass is 5 gold coins. You don't seem to have enough money!", FaceAnim.NEUTRAL)
                }
                else -> {
                    sendItemDialogue(player, Items.SHANTAY_PASS_1854, "You purchase a Shantay Pass.")
                    addItemOrDrop(player, Items.SHANTAY_PASS_1854)
                }
            }
            return@on true
        }

        /*
         * Handles interaction with shantay pass.
         */

        on(shantayPassSceneryId, IntType.SCENERY, "look-at") { player, _ ->
            sendMessage(player, "You look at the huge stone gate.")
            sendDialogueLines(
                player,
                DARK_RED + "The Desert is a VERY Dangerous place. Do not enter if you are",
                DARK_RED + "afraid of dying. Beware of high temperatures, and storms, robbers,",
                DARK_RED + "and slavers. No responsibility is taken by Shantay if anything bad",
                DARK_RED + "should happen to you in any circumstances whatsoever.",
            )
            return@on true
        }

        /*
         * Handles interaction with shantay pass.
         */

        on(shantayPassSceneryId, IntType.SCENERY, "go-through") { player, _ ->
            val goingSouth = player.location.y > 3116
            val destination = player.location.transform(0, if (goingSouth) -2 else 2, 0)

            if (player.location.y < 3117) {
                sendMessage(player, "You go through the gate.")
                AgilityHandler.walk(player, 0, player.location, destination, null, 0.0, null)
            } else {
                if (!WarningListener.isDisabled(player, Warnings.SHANTAY_PASS)) {
                    openInterface(player, Components.CWS_WARNING_10_565)
                } else if (!removeItem(player, Items.SHANTAY_PASS_1854)) {
                    sendNPCDialogue(player, NPCs.SHANTAY_GUARD_838, "You need a Shantay pass to get through this gate. See Shantay, he will sell you one for a very reasonable price.", FaceAnim.NEUTRAL)
                } else {
                    sendMessage(player, "You go through the gate.")
                    AgilityHandler.walk(player, 0, player.location, destination, null, 0.0, null)
                }
            }
            return@on true
        }

        /*
         * Handles quick pass interaction at shantay pass.
         */

        on(shantayPassSceneryId, IntType.SCENERY, "quick-pass") { player, _ ->
            val goingSouth = player.location.y > 3116
            val destination = player.location.transform(0, if (goingSouth) -2 else 2, 0)

            if (goingSouth) {
                if (!inInventory(player, Items.SHANTAY_PASS_1854, 1)) {
                    sendNPCDialogue(player, NPCs.SHANTAY_GUARD_838, "You need a Shantay pass to get through this gate. See Shantay, he will sell you one for a very reasonable price.", FaceAnim.NEUTRAL)
                    return@on true
                }

                if (!removeItem(player, Items.SHANTAY_PASS_1854, Container.INVENTORY)) {
                    sendMessage(player, "An error occurred while trying to remove your Shantay pass. Please try again.")
                    return@on false
                }
                sendMessage(player, "You hand your Shantay pass to the guard and pass through the gate.")
            }
            AgilityHandler.walk(player, 0, player.location, destination, null, 0.0, null)
            return@on true
        }

        /*
         * Handles interaction jail doors.
         */

        on(Scenery.JAIL_DOOR_35401, IntType.SCENERY, "open") { player, node ->
            if (player.getAttribute("shantay-jail", false) && player.location.x > 3299) {
                player.removeAttribute("shantay-jail")
            }

            if (!player.getAttribute("shantay-jail", false)) {
                DoorActionHandler.handleDoor(player, node.asScenery())
                return@on true
            } else {
                player.dialogueInterpreter.open(NPCs.SHANTAY_836, null, true)
                return@on true
            }
        }

        on(Scenery.SHANTAY_CHEST_2693, IntType.SCENERY, "open") { player, _ ->
            player.bank.open()
            return@on true
        }

        on(NPCs.SHANTAY_GUARD_838, IntType.NPC, "bribe") { player, node ->
            player.dialogueInterpreter.open(NPCs.SHANTAY_GUARD_838, node)
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, shantayPassSceneryId, "look-at", "go-through", "quick-pass") { player, node ->
            if (node.id in intArrayOf(35543, 35544)) {
                return@setDest node.location.transform(-1, if (player.location.y > node.location.y) 1 else -1, 0)
            } else {
                return@setDest node.location.transform(1, if (player.location.y > node.location.y) 1 else -1, 0)
            }
        }
    }
}
