package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket
import shared.consts.Network

/**
 * Represents the outgoing packet used for closing an interface.
 *
 * @author Emperor
 */
class CloseInterface : OutgoingPacket<OutgoingContext.InterfaceContext> {
    override fun send(context: OutgoingContext.InterfaceContext) {
        val buffer = IoBuffer(Network.CLOSE_INTERFACE)
        buffer.putShort(context.player.interfaceManager.getPacketCount(1))
        buffer.putShort(context.windowId)
        buffer.putShort(context.componentId)
        buffer.cypherOpcode(context.player.session.getIsaacPair()!!.output)
        context.player.session.write(buffer)
    }
}