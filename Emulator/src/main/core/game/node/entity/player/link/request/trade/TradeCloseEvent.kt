package core.game.node.entity.player.link.request.trade

import core.api.setVarp
import core.game.component.CloseEvent
import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.request.trade.TradeModule.Companion.getExtension
import core.net.packet.PacketRepository
import core.net.packet.context.ContainerContext
import core.net.packet.out.ContainerPacket

/**
 * Represents the close event invoked at the closing of a trade interface.
 * @author Vexia
 */
class TradeCloseEvent : CloseEvent {
    override fun close(player: Player, c: Component): Boolean {
        val module = getExtension(player)
        player.packetDispatch.sendRunScript(101, "")
        if (module == null) {
            return true
        }
        val otherModule = getExtension(module.target) ?: return true
        if (module.isAccepted && otherModule.isAccepted) {
            return true
        }
        if (module.stage != 2) {
            retainContainer(player)
            retainContainer(module.target!!)
        }
        closeInterfaces(player)
        closeInterfaces(module.target!!)
        module.target!!.interfaceManager.close()
        end(player)
        player.interfaceManager.openDefaultTabs()
        end(module.target!!)
        module.target!!.interfaceManager.openDefaultTabs()
        return true
    }

    /**
     * Method used to close the trade interface.
     * @param player the player.
     */
    private fun closeInterfaces(player: Player) {
        player.removeExtension(TradeModule::class.java)
        player.interfaceManager.closeSingleTab()
        PacketRepository.send(ContainerPacket::class.java, ContainerContext(player, -1, 2, 24, arrayOf(), 27, false))
        PacketRepository.send(ContainerPacket::class.java, ContainerContext(player, -1, 2, 23, arrayOf(), 27, false))
        player.packetDispatch.sendRunScript(101, "")
    }

    /**
     * Method used to end the trade session.
     * @param player the player.
     */
    private fun end(player: Player) {
        setVarp(player, 1043, 0)
        setVarp(player, 1042, 0)
    }

    /**
     * Method used to retain the trade container.
     * @param player the player.
     */
    private fun retainContainer(player: Player) {
        val module = getExtension(player)
        if (module == null || module.isRetained) {
            return
        }
        module.isRetained = true
        player.inventory.addAll(module.container)
    }
}