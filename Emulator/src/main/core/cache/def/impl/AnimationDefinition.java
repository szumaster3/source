package core.cache.def.impl;

import core.cache.Cache;
import core.cache.CacheIndex;
import core.cache.misc.buffer.ByteBufferUtils;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Animation definition.
 */
public final class AnimationDefinition {
    /**
     * The An int 2136.
     */
    public int anInt2136;
    /**
     * The An int 2137.
     */
    public int anInt2137;
    /**
     * The An int array 2139.
     */
    public int[] anIntArray2139;
    /**
     * The An int 2140.
     */
    public int anInt2140;
    /**
     * The Has sound effect.
     */
    public boolean hasSoundEffect = false;
    /**
     * The An int 2142.
     */
    public int anInt2142;
    /**
     * The Emote item.
     */
    public int emoteItem;
    /**
     * The An int 2144.
     */
    public int anInt2144 = -1;
    /**
     * The Handled sounds.
     */
    public int[][] handledSounds;
    /**
     * The A boolean array 2149.
     */
    public boolean[] aBooleanArray2149;
    /**
     * The An int array 2151.
     */
    public int[] anIntArray2151;
    /**
     * The A boolean 2152.
     */
    public boolean aBoolean2152;
    /**
     * The Durations.
     */
    public int[] durations;
    /**
     * The An int 2155.
     */
    public int anInt2155;
    /**
     * The A boolean 2158.
     */
    public boolean aBoolean2158;
    /**
     * The A boolean 2159.
     */
    public boolean aBoolean2159;
    /**
     * The An int 2162.
     */
    public int anInt2162;
    /**
     * The An int 2163.
     */
    public int anInt2163;
    /**
     * The New header.
     */
    boolean newHeader;

    /**
     * The Sound min delay.
     */
    public int[] soundMinDelay;
    /**
     * The Sound max delay.
     */
    public int[] soundMaxDelay;
    /**
     * The An int array 1362.
     */
    public int[] anIntArray1362;
    /**
     * The Effect 2 sound.
     */
    public boolean effect2Sound;

	private static final Map<Integer, AnimationDefinition> animationDefinition = new HashMap<>();

    /**
     * For id animation definition.
     *
     * @param emoteId the emote id
     * @return the animation definition
     */
    public static final AnimationDefinition forId(int emoteId) {
		try {
			AnimationDefinition defs = animationDefinition.get(emoteId);
			if (defs != null) {
				return defs;
			}
			byte[] data = Cache.getData(CacheIndex.SEQUENCE_CONFIGURATION, emoteId >>> 7, emoteId & 0x7f);
			defs = new AnimationDefinition();
			if (data != null) {
				defs.readValueLoop(ByteBuffer.wrap(data));
			}
			defs.method2394();
			animationDefinition.put(emoteId, defs);
			return defs;
		} catch (Throwable t) {
			return null;
		}
	}

	private void readValueLoop(ByteBuffer buffer) {
		for (;;) {
			int opcode = buffer.get() & 0xFF;
			if (opcode == 0) {
				break;
			}
			readValues(buffer, opcode);
		}
	}

    /**
     * Gets duration.
     *
     * @return the duration
     */
    public int getDuration() {
		if (durations == null) {
			return 0;
		}
		int duration = 0;
		for (int i : durations) {
			if (i > 100) {
				continue;
			}
			duration += i * 20;
		}
		return duration;
	}

    /**
     * Gets cycles.
     *
     * @return the cycles
     */
    public int getCycles() {
		if (durations == null) return 0;
		int duration = 0;
		for (int i : durations)
			duration += i;
		return duration;
	}

    /**
     * Gets duration ticks.
     *
     * @return the duration ticks
     */
    public int getDurationTicks() {
		int ticks = getDuration() / 600;
		return Math.max(ticks, 1);
	}

	private void readValues(ByteBuffer buffer, int opcode) {
		if (opcode == 1) {
			int length = buffer.getShort() & 0xFFFF;
			durations = new int[length];
			for (int i = 0; i < length; i++) {
				durations[i] = buffer.getShort() & 0xFFFF;
			}
			anIntArray2139 = new int[length];
			for (int i = 0; i < length; i++) {
				anIntArray2139[i] = buffer.getShort() & 0xFFFF;
			}
			for (int i = 0; i < length; i++) {
				anIntArray2139[i] = ((buffer.getShort() & 0xFFFF << 16) + anIntArray2139[i]);
			}
		} else if (opcode != 2) {
			if (opcode != 3) {
				if (opcode == 4)
					aBoolean2152 = true;
				else if (opcode == 5)
					anInt2142 = buffer.get() & 0xFF;
				else if (opcode != 6) {
					if (opcode == 7)
						emoteItem = buffer.getShort() & 0xFFFF;
					else if ((opcode ^ 0xffffffff) != -9) {
						if (opcode != 9) {
							if (opcode != 10) {
								if (opcode == 11)
									anInt2155 = buffer.get() & 0xFF;
								else if (opcode == 12) {
									int i = buffer.get() & 0xFF;
									anIntArray2151 = new int[i];
									for (int i_19_ = 0; ((i_19_ ^ 0xffffffff) > (i ^ 0xffffffff)); i_19_++) {
										anIntArray2151[i_19_] = buffer.getShort() & 0xFFFF;
									}
									for (int i_20_ = 0; i > i_20_; i_20_++)
										anIntArray2151[i_20_] = ((buffer.getShort() & 0xFFFF << 16) + anIntArray2151[i_20_]);
								} else if (opcode == 13) {
									// opcode 13
									int i = buffer.getShort() & 0xFFFF;
									handledSounds = new int[i][];
									for (int i_21_ = 0; i_21_ < i; i_21_++) {
										int i_22_ = buffer.get() & 0xFF;
										if ((i_22_ ^ 0xffffffff) < -1) {
											handledSounds[i_21_] = new int[i_22_];
											handledSounds[i_21_][0] = ByteBufferUtils.getMedium(buffer);
											for (int i_23_ = 1; ((i_22_ ^ 0xffffffff) < (i_23_ ^ 0xffffffff)); i_23_++) {
												handledSounds[i_21_][i_23_] = buffer.getShort() & 0xFFFF;
											}
										}
									}
								} else if (opcode == 14) {
									hasSoundEffect = true;
								} else {

								}
							} else
								anInt2162 = buffer.get() & 0xFF;
						} else
							anInt2140 = buffer.get() & 0xFF;
					} else
						anInt2136 = buffer.get() & 0xFF;
				} else
					anInt2144 = buffer.getShort() & 0xFFFF;
			} else {
				aBooleanArray2149 = new boolean[256];
				int length = buffer.get() & 0xFF;
				for (int i = 0; i < length; i++) {
					aBooleanArray2149[buffer.get() & 0xFF] = true;
				}
			}
		} else
			anInt2163 = buffer.getShort() & 0xFFFF;
	}

    /**
     * Method 2394.
     */
    public void method2394() {
		if (anInt2140 == -1) {
			if (aBooleanArray2149 == null)
				anInt2140 = 0;
			else
				anInt2140 = 2;
		}
		if (anInt2162 == -1) {
			if (aBooleanArray2149 == null)
				anInt2162 = 0;
			else
				anInt2162 = 2;
		}
	}

    /**
     * Instantiates a new Animation definition.
     */
    public AnimationDefinition() {
		anInt2136 = 99;
		emoteItem = -1;
		anInt2140 = -1;
		aBoolean2152 = false;
		anInt2142 = 5;
		aBoolean2159 = false;
		anInt2163 = -1;
		anInt2155 = 2;
		aBoolean2158 = false;
		anInt2162 = -1;
	}

    /**
     * Gets definition.
     *
     * @return the definition
     */
    public static Map<Integer, AnimationDefinition> getDefinition() {
		return animationDefinition;
	}
}