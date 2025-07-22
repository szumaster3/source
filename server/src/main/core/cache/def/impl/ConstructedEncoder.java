package core.cache.def.impl;

import core.game.node.scenery.Constructed;

import java.nio.ByteBuffer;

public class ConstructedEncoder {
    public static void encode(ByteBuffer buffer, Constructed constructed) {
        buffer.putInt(constructed.getId());
        buffer.putInt(constructed.getLocation().getX());
        buffer.putInt(constructed.getLocation().getY());
        buffer.put((byte) constructed.getLocation().getZ());

        buffer.put((byte) constructed.getType());
        buffer.put((byte) constructed.getRotation());

        buffer.put((byte) (constructed.isActive() ? 1 : 0));
        buffer.put((byte) (constructed.isRenderable() ? 1 : 0));
    }
}