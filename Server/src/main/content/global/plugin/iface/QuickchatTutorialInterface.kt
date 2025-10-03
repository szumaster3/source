package content.global.plugin.iface

import core.api.openInterface
import core.api.setVarbit
import core.game.interaction.InterfaceListener
import shared.consts.Components
import shared.consts.Vars

/**
 * Handles quick chat tutorial.
 * @author szu
 */
class QuickchatTutorialInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        onOpen(Components.QUICKCHAT_TUTORIAL_157) { player, _ ->
            setVarbit(player, Vars.VARBIT_IFACE_QUICKCHAT_TUTORIAL_4762_4762, 1)
            return@onOpen true
        }
        onClose(Components.QUICKCHAT_TUTORIAL_157) { player, _ ->
            setVarbit(player, Vars.VARBIT_IFACE_QUICKCHAT_TUTORIAL_4762_4762, 0)
            return@onClose true
        }

        on(Components.CHATDEFAULT_137) { player, _, _, buttonID, _, _ ->
            if (buttonID == 5) {
                openInterface(player, Components.QUICKCHAT_TUTORIAL_157)
            }
            return@on true
        }
    }
}
