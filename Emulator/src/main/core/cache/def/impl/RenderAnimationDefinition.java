package core.cache.def.impl;

import core.cache.Cache;
import core.cache.CacheArchive;
import core.cache.CacheIndex;
import core.game.world.GameWorld;
import core.tools.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;

import static core.api.ContentAPIKt.log;

/**
 * The type Render animation definition.
 */
public class RenderAnimationDefinition {
    /**
     * The Turn 180 animation.
     */
    public int turn180Animation;
    /**
     * The An int 951.
     */
    public int anInt951 = -1;
    /**
     * The An int 952.
     */
    public int anInt952;
    /**
     * The Turn cw animation.
     */
    public int turnCWAnimation = -1;
    /**
     * The An int 954.
     */
    public int anInt954;
    /**
     * The An int 955.
     */
    public int anInt955;
    /**
     * The An int 956.
     */
    public int anInt956;
    /**
     * The An int 957.
     */
    public int anInt957;
    /**
     * The An int 958.
     */
    public int anInt958;
    /**
     * The An int array 959.
     */
    public int[] anIntArray959 = null;
    /**
     * The An int 960.
     */
    public int anInt960;
    /**
     * The An int 961.
     */
    public int anInt961 = 0;
    /**
     * The An int 962.
     */
    public int anInt962;
    /**
     * The Walk animation id.
     */
    public int walkAnimationId;
    /**
     * The An int 964.
     */
    public int anInt964;
    /**
     * The An int 965.
     */
    public int anInt965;
    /**
     * The An int 966.
     */
    public int anInt966;
    /**
     * The Stand animation ids.
     */
    public int[] standAnimationIds;
    /**
     * The An int 969.
     */
    public int anInt969;
    /**
     * The An int array 971.
     */
    public int[] anIntArray971;
    /**
     * The Stand animation id.
     */
    public int standAnimationId;
    /**
     * The An int 973.
     */
    public int anInt973;
    /**
     * The An int 974.
     */
    public int anInt974;
    /**
     * The An int 975.
     */
    public int anInt975;
    /**
     * The Run animation id.
     */
    public int runAnimationId;
    /**
     * The An int 977.
     */
    public int anInt977;
    /**
     * The A boolean 978.
     */
    public boolean aBoolean978;
    /**
     * The An int array array 979.
     */
    public int[][] anIntArrayArray979;
    /**
     * The An int 980.
     */
    public int anInt980;
    /**
     * The Turn ccw animation.
     */
    public int turnCCWAnimation;
    /**
     * The An int 983.
     */
    public int anInt983;
    /**
     * The An int 985.
     */
    public int anInt985;
    /**
     * The An int 986.
     */
    public int anInt986;
    /**
     * The An int 987.
     */
    public int anInt987;
    /**
     * The An int 988.
     */
    public int anInt988;
    /**
     * The An int 989.
     */
    public int anInt989;
    /**
     * The An int 990.
     */
    public int anInt990;
    /**
     * The An int 992.
     */
    public int anInt992;
    /**
     * The An int 993.
     */
    public int anInt993;
    /**
     * The An int 994.
     */
    public int anInt994;

    /**
     * For id render animation definition.
     *
     * @param animId the anim id
     * @return the render animation definition
     */
    public static RenderAnimationDefinition forId(int animId) {
		if (animId == -1) {
			return null;
		}
		byte[] data = Cache.getData(CacheIndex.CONFIGURATION, CacheArchive.BAS_TYPE, animId);		RenderAnimationDefinition defs = new RenderAnimationDefinition();
		if (data != null) {
			defs.parse(ByteBuffer.wrap(data));
		} else {
			log(RenderAnimationDefinition.class, Log.ERR, "No definitions found for render animation " + animId + ", size=" + Cache.getArchiveCapacity(CacheIndex.CONFIGURATION, CacheArchive.BAS_TYPE) + "!");
		}
		return defs;
	}

	private void parse(ByteBuffer buffer) {
		for (;;) {
			int opcode = buffer.get() & 0xFF;
			if (opcode == 0) {
				break;
			}
			parseOpcode(buffer, opcode);
		}
	}

	private void parseOpcode(ByteBuffer buffer, int opcode) {
		if (opcode == 54) {
			@SuppressWarnings("unused")
			int anInt1260 = (buffer.get() & 0xFF) << 6;
			@SuppressWarnings("unused")
			int anInt1227 = (buffer.get() & 0xFF) << 6;
		} else if (opcode == 55) {
			int[] anIntArray1246 = new int[12];
			int i_14_ = buffer.get() & 0xFF;
			anIntArray1246[i_14_] = buffer.getShort() & 0xFFFF;
		} else if (opcode == 56) {
			int[][] anIntArrayArray1217 = new int[12][];
			int i_12_ = buffer.get() & 0xFF;
			anIntArrayArray1217[i_12_] = new int[3];
			for (int i_13_ = 0; i_13_ < 3; i_13_++)
				anIntArrayArray1217[i_12_][i_13_] = buffer.getShort();
		} else if ((opcode ^ 0xffffffff) != -2) {
			if ((opcode ^ 0xffffffff) != -3) {
				if (opcode != 3) {
					if ((opcode ^ 0xffffffff) != -5) {
						if (opcode == 5)
							anInt977 = buffer.getShort() & 0xFFFF;
						else if ((opcode ^ 0xffffffff) != -7) {
							if (opcode == 7)
								anInt960 = buffer.getShort() & 0xFFFF;
							else if ((opcode ^ 0xffffffff) == -9)
								anInt985 = buffer.getShort() & 0xFFFF;
							else if (opcode == 9)
								anInt957 = buffer.getShort() & 0xFFFF;
							else if (opcode == 26) {
								anInt973 = (short) (4 * buffer
										.get() & 0xFF);
								anInt975 = (short) (buffer.get() & 0xFF * 4);
							} else if ((opcode ^ 0xffffffff) == -28) {
								if (anIntArrayArray979 == null)
									anIntArrayArray979 = new int[12][];
								int i = buffer.get() & 0xFF;
								anIntArrayArray979[i] = new int[6];
								for (int i_1_ = 0; (i_1_ ^ 0xffffffff) > -7; i_1_++)
									anIntArrayArray979[i][i_1_] = buffer
											.getShort();
							} else if ((opcode ^ 0xffffffff) == -29) {
								anIntArray971 = new int[12];
								for (int i = 0; i < 12; i++) {
									anIntArray971[i] = buffer
											.get() & 0xFF;
									if (anIntArray971[i] == 255)
										anIntArray971[i] = -1;
								}
							} else if (opcode != 29) {
								if (opcode != 30) {
									if ((opcode ^ 0xffffffff) != -32) {
										if (opcode != 32) {
											if ((opcode ^ 0xffffffff) != -34) {
												if (opcode != 34) {
													if (opcode != 35) {
														if ((opcode ^ 0xffffffff) != -37) {
															if ((opcode ^ 0xffffffff) != -38) {
																if (opcode != 38) {
																	if ((opcode ^ 0xffffffff) != -40) {
																		if ((opcode ^ 0xffffffff) != -41) {
																			if ((opcode ^ 0xffffffff) == -42)
																				turnCWAnimation = buffer
																						.getShort() & 0xFFFF;
																			else if (opcode != 42) {
																				if ((opcode ^ 0xffffffff) == -44)
																					buffer.getShort();
																				else if ((opcode ^ 0xffffffff) != -45) {
																					if ((opcode ^ 0xffffffff) == -46)
																						anInt964 = buffer
																								.getShort() & 0xFFFF;
																					else if ((opcode ^ 0xffffffff) != -47) {
																						if (opcode == 47)
																							anInt966 = buffer
																									.getShort() & 0xFFFF;
																						else if (opcode == 48)
																							anInt989 = buffer
																									.getShort() & 0xFFFF;
																						else if (opcode != 49) {
																							if ((opcode ^ 0xffffffff) != -51) {
																								if (opcode != 51) {
																									if (opcode == 52) {
																										int i = buffer
																												.get() & 0xFF;
																										anIntArray959 = new int[i];
																										standAnimationIds = new int[i];
																										for (int i_2_ = 0; i_2_ < i; i_2_++) {
																											standAnimationIds[i_2_] = buffer
																													.getShort() & 0xFFFF;
																											int i_3_ = buffer
																													.get() & 0xFF;
																											anIntArray959[i_2_] = i_3_;
																											anInt994 += i_3_;
																										}
																									} else if (opcode == 53)
																										aBoolean978 = false;
																								} else
																									anInt962 = buffer
																											.getShort() & 0xFFFF;
																							} else
																								anInt990 = buffer
																										.getShort() & 0xFFFF;
																						} else
																							anInt952 = buffer
																									.getShort() & 0xFFFF;
																					} else
																						anInt983 = buffer
																								.getShort() & 0xFFFF;
																				} else
																					anInt955 = buffer
																							.getShort() & 0xFFFF;
																			} else
																				turnCCWAnimation = buffer
																						.getShort() & 0xFFFF;
																		} else
																			turn180Animation = buffer
																					.getShort() & 0xFFFF;
																	} else
																		anInt954 = buffer
																				.getShort() & 0xFFFF;
																} else
																	anInt958 = (buffer
																			.getShort() & 0xFFFF);
															} else
																anInt951 = (buffer
																		.get() & 0xFF);
														} else
															anInt965 = (buffer
																	.getShort());
													} else
														anInt969 = (buffer
																.getShort() & 0xFFFF);
												} else
													anInt993 = buffer
															.get() & 0xFF;
											} else
												anInt956 = (buffer.getShort());
										} else
											anInt961 = buffer
													.getShort() & 0xFFFF;
									} else
										anInt988 = buffer.get() & 0xFF;
								} else
									anInt980 = buffer.getShort() & 0xFFFF;
							} else
								anInt992 = buffer.get() & 0xFF;
						} else
							runAnimationId = buffer.getShort() & 0xFFFF;
					} else
						anInt986 = buffer.getShort() & 0xFFFF;
				} else
					anInt987 = buffer.getShort() & 0xFFFF;
			} else
				anInt974 = buffer.getShort() & 0xFFFF;
		} else {
			standAnimationId = buffer.getShort() & 0xFFFF;
			walkAnimationId = buffer.getShort() & 0xFFFF;
			if ((standAnimationId ^ 0xffffffff) == -65536)
				standAnimationId = -1;
			if ((walkAnimationId ^ 0xffffffff) == -65536)
				walkAnimationId = -1;
		}
	}

    /**
     * Instantiates a new Render animation definition.
     */
    public RenderAnimationDefinition() {
		anInt957 = -1;
		anInt954 = -1;
		anInt960 = -1;
		anInt958 = -1;
		anInt965 = 0;
		anInt973 = 0;
		turn180Animation = -1;
		anInt956 = 0;
		standAnimationId = -1;
		standAnimationIds = null;
		anInt952 = -1;
		anInt983 = -1;
		anInt985 = -1;
		anInt962 = -1;
		anInt966 = -1;
		anInt977 = -1;
		anInt975 = 0;
		runAnimationId = -1;
		anInt988 = 0;
		turnCCWAnimation = -1;
		anInt987 = -1;
		anInt980 = 0;
		anInt964 = -1;
		walkAnimationId = -1;
		anInt986 = -1;
		aBoolean978 = true;
		anInt992 = 0;
		anInt955 = -1;
		anInt989 = -1;
		anInt974 = -1;
		anInt969 = 0;
		anInt994 = 0;
		anInt990 = -1;
		anInt993 = 0;
	}

    /**
     * Main.
     *
     * @param args the args
     * @throws Throwable the throwable
     */
    public static void main(String...args) throws Throwable {
		GameWorld.prompt(false);
		RenderAnimationDefinition def = RenderAnimationDefinition.forId(1426);

		for (Field f : def.getClass().getDeclaredFields()) {
			if (!Modifier.isStatic(f.getModifiers())) {
				if (f.getType().isArray()) {
					Object object = f.get(def);
					if (object != null) {
						int length = Array.getLength(object);
						System.out.print(f.getName() + ", [");
						for (int i = 0; i < length; i++) {
							System.out.print(Array.get(object, i) + (i < (length - 1) ? ", " : "]"));
						}
						continue;
					}
				}

			}
		}
		for (Field f : def.getClass().getSuperclass().getDeclaredFields()) {
			if (!Modifier.isStatic(f.getModifiers())) {
				if (f.getType().isArray()) {
					Object object = f.get(def);
					if (object != null) {
						int length = Array.getLength(object);
						System.out.print(f.getName() + ", [");
						for (int i = 0; i < length; i++) {
							System.out.print(Array.get(object, i) + (i < (length - 1) ? ", " : "]"));
						}
						continue;
					}
				}

			}
		}
	}
}
