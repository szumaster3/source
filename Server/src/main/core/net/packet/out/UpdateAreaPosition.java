package core.net.packet.out;

import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.net.packet.IoBuffer;
import core.net.packet.OutgoingContext;
import core.net.packet.OutgoingPacket;
import core.net.packet.PacketHeader;

/**
 * Handles the update area position packet.
 *
 * @author Emperor
 */
public final class UpdateAreaPosition implements OutgoingPacket<OutgoingContext.AreaPosition> {

    /**
     * Gets the region chunk update buffer.
     *
     * @param player The player.
     * @param base   The base location of the chunk.
     * @return The buffer.
     */
    public static IoBuffer getChunkUpdateBuffer(Player player, Location base) {
        int x = base.getSceneX(player.getPlayerFlags().getLastSceneGraph());
        int y = base.getSceneY(player.getPlayerFlags().getLastSceneGraph());
        return new IoBuffer(230, PacketHeader.SHORT).putA(y).putS(x);
    }

    /**
     * Gets the region chunk update buffer.
     *
     * @param player The player.
     * @param base   The base location of the chunk.
     * @return The buffer.
     */
    public static IoBuffer getBuffer(Player player, Location base) {
        int x = base.getSceneX(player.getPlayerFlags().getLastSceneGraph());
        int y = base.getSceneY(player.getPlayerFlags().getLastSceneGraph());
        return new IoBuffer(26).putC(x).put(y);
    }

    @Override
    public void send(OutgoingContext.AreaPosition context) {
        IoBuffer buffer = getBuffer(context.getPlayer(), context.getLocation());
        buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().output);
        context.getPlayer().getSession().write(buffer);
    }

}