package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket
import shared.consts.Network

/**
 * The outgoing logout packet.
 *
 * @author Emperor
 */
class Logout : OutgoingPacket<OutgoingContext.PlayerContext> {
    override fun send(context: OutgoingContext.PlayerContext) {
        val buffer = IoBuffer(Network.LOGOUT_FULL)
        buffer.cypherOpcode(context.player.session.getIsaacPair()!!.output)
        context.player.details.session.write(buffer)
    }
}