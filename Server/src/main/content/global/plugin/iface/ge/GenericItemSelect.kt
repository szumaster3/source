package content.global.plugin.iface.ge

import core.api.getAttribute
import core.api.log
import core.api.removeAttribute
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.tools.Log

/**
 * Handles the Generic Item Select interface in the Grand Exchange.
 */
class GenericItemSelect : InterfaceListener {
    val GENERIC_ITEM_SELECT_IFACE = 12

    override fun defineInterfaceListeners() {
        onOpen(GENERIC_ITEM_SELECT_IFACE) { player, _ ->
            player.pulseManager.run(
                object : Pulse() {
                    override fun pulse(): Boolean = false

                    override fun stop() {
                        super.stop()
                        player.interfaceManager.closeSingleTab()
                    }
                },
            )
            return@onOpen true
        }

        on(GENERIC_ITEM_SELECT_IFACE) { player, _, opcode, _, slot, _ ->
            processResponse(player, opcode, slot)
            return@on true
        }

        onClose(GENERIC_ITEM_SELECT_IFACE) { player, _ ->
            removeAttribute(player, "itemselect-callback")
            removeAttribute(player, "itemselect-keepalive")
            return@onClose true
        }
    }

    private fun processResponse(player: Player, opcode: Int, slot: Int) {
        val callback = getAttribute<((Int, Int) -> Unit)?>(player, "itemselect-callback", null)
        if (callback == null) {
            log(this::class.java, Log.WARN, "${player.name} is trying to use an item select prompt with no callback!")
            return
        }

        val optionIndex = when (opcode) {
            155 -> 0
            196 -> 1
            124 -> 2
            199 -> 3
            234 -> 4
            168 -> 5
            166 -> 6
            64 -> 7
            53 -> 8
            9 -> 9
            10 -> 10
            else -> -1
        }
        if (optionIndex == -1) {
            log(
                this::class.java,
                Log.WARN,
                "${player.name} is clicking a right-click index that we don't know the opcode for yet, lol. Here's the opcode: $opcode",
            )
            return
        }

        callback.invoke(slot, optionIndex)

        if (!getAttribute(player, "itemselect-keepalive", false)) {
            removeAttribute(player, "itemselect-callback")
            removeAttribute(player, "itemselect-keepalive")
            player.interfaceManager.closeSingleTab()
        }
    }
}
