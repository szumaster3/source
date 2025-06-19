package core.cache.def.impl

import core.api.log
import core.cache.Cache.getArchiveCapacity
import core.cache.Cache.getData
import core.cache.CacheArchive
import core.cache.CacheIndex
import core.game.world.GameWorld.prompt
import core.tools.Log
import java.lang.reflect.Modifier
import java.nio.ByteBuffer

/**
 * The type Render animation definition.
 */
class RenderAnimationDefinition {
    @JvmField var turn180Animation: Int
    @JvmField var anInt951: Int = -1
    @JvmField var anInt952: Int
    @JvmField var turnCWAnimation: Int = -1
    @JvmField var anInt954: Int
    @JvmField var anInt955: Int
    @JvmField var anInt956: Int = 0
    @JvmField var anInt957: Int
    @JvmField var anInt958: Int
    @JvmField var anIntArray959: IntArray? = null
    @JvmField var anInt960: Int
    @JvmField var anInt961: Int = 0
    @JvmField var anInt962: Int
    @JvmField var walkAnimationId: Int
    @JvmField var anInt964: Int
    @JvmField var anInt965: Int = 0
    @JvmField var anInt966: Int
    @JvmField var standAnimationIds: IntArray? = null
    @JvmField var anInt969: Int = 0
    @JvmField var anIntArray971: IntArray? = null
    @JvmField var standAnimationId: Int
    @JvmField var anInt973: Int = 0
    @JvmField var anInt974: Int
    @JvmField var anInt975: Int = 0
    @JvmField var runAnimationId: Int
    @JvmField var anInt977: Int
    @JvmField var aBoolean978: Boolean = true
    @JvmField var anIntArrayArray979: Array<IntArray?>? = null
    @JvmField var anInt980: Int = 0
    @JvmField var turnCCWAnimation: Int
    @JvmField var anInt983: Int
    @JvmField var anInt985: Int
    @JvmField var anInt986: Int
    @JvmField var anInt987: Int
    @JvmField var anInt988: Int = 0
    @JvmField var anInt989: Int
    @JvmField var anInt990: Int
    @JvmField var anInt992: Int = 0
    @JvmField var anInt993: Int = 0
    @JvmField var anInt994: Int = 0

    private fun parse(buffer: ByteBuffer) {
        while (true) {
            val opcode = buffer.get().toInt() and 0xFF
            if (opcode == 0) {
                break
            }
            parseDefinition(buffer, opcode)
        }
    }

    private fun parseDefinition(
        buffer: ByteBuffer,
        opcode: Int,
    ) {
        if (opcode == 54) {
            @Suppress("unused")
            val anInt1260 = (buffer.get().toInt() and 0xFF) shl 6

            @Suppress("unused")
            val anInt1227 = (buffer.get().toInt() and 0xFF) shl 6
        } else if (opcode == 55) {
            val anIntArray1246 = IntArray(12)
            val i_14_ = buffer.get().toInt() and 0xFF
            anIntArray1246[i_14_] = buffer.getShort().toInt() and 0xFFFF
        } else if (opcode == 56) {
            val anIntArrayArray1217 = arrayOfNulls<IntArray>(12)
            val i_12_ = buffer.get().toInt() and 0xFF
            anIntArrayArray1217[i_12_] = IntArray(3)
            for (i_13_ in 0..2) anIntArrayArray1217[i_12_]!![i_13_] = buffer.getShort().toInt()
        } else if ((opcode xor -0x1) != -2) {
            if ((opcode xor -0x1) != -3) {
                if (opcode != 3) {
                    if ((opcode xor -0x1) != -5) {
                        if (opcode == 5) {
                            anInt977 = buffer.getShort().toInt() and 0xFFFF
                        } else if ((opcode xor -0x1) != -7) {
                            if (opcode == 7) {
                                anInt960 = buffer.getShort().toInt() and 0xFFFF
                            } else if ((opcode xor -0x1) == -9) {
                                anInt985 = buffer.getShort().toInt() and 0xFFFF
                            } else if (opcode == 9) {
                                anInt957 = buffer.getShort().toInt() and 0xFFFF
                            } else if (opcode == 26) {
                                anInt973 = (4 * buffer.get() and 0xFF).toShort().toInt()
                                anInt975 = (buffer.get().toInt() and 0xFF * 4).toShort().toInt()
                            } else if ((opcode xor -0x1) == -28) {
                                if (anIntArrayArray979 == null) anIntArrayArray979 = arrayOfNulls(12)
                                val i = buffer.get().toInt() and 0xFF
                                anIntArrayArray979!![i] = IntArray(6)
                                var i_1_ = 0
                                while ((i_1_ xor -0x1) > -7) {
                                    anIntArrayArray979!![i]!![i_1_] = buffer.getShort().toInt()
                                    i_1_++
                                }
                            } else if ((opcode xor -0x1) == -29) {
                                anIntArray971 = IntArray(12)
                                for (i in 0..11) {
                                    anIntArray971!![i] = buffer.get().toInt() and 0xFF
                                    if (anIntArray971!![i] == 255) anIntArray971!![i] = -1
                                }
                            } else if (opcode != 29) {
                                if (opcode != 30) {
                                    if ((opcode xor -0x1) != -32) {
                                        if (opcode != 32) {
                                            if ((opcode xor -0x1) != -34) {
                                                if (opcode != 34) {
                                                    if (opcode != 35) {
                                                        if ((opcode xor -0x1) != -37) {
                                                            if ((opcode xor -0x1) != -38) {
                                                                if (opcode != 38) {
                                                                    if ((opcode xor -0x1) != -40) {
                                                                        if ((opcode xor -0x1) != -41) {
                                                                            if ((opcode xor -0x1) ==
                                                                                -42
                                                                            ) {
                                                                                turnCWAnimation =
                                                                                    buffer.getShort().toInt() and 0xFFFF
                                                                            } else if (opcode != 42) {
                                                                                if ((opcode xor -0x1) ==
                                                                                    -44
                                                                                ) {
                                                                                    buffer.getShort()
                                                                                } else if ((opcode xor -0x1) != -45) {
                                                                                    if ((opcode xor -0x1) ==
                                                                                        -46
                                                                                    ) {
                                                                                        anInt964 =
                                                                                            buffer
                                                                                                .getShort()
                                                                                                .toInt() and 0xFFFF
                                                                                    } else if ((opcode xor -0x1) !=
                                                                                        -47
                                                                                    ) {
                                                                                        if (opcode == 47) {
                                                                                            anInt966 =
                                                                                                buffer
                                                                                                    .getShort()
                                                                                                    .toInt() and 0xFFFF
                                                                                        } else if (opcode ==
                                                                                            48
                                                                                        ) {
                                                                                            anInt989 =
                                                                                                buffer
                                                                                                    .getShort()
                                                                                                    .toInt() and 0xFFFF
                                                                                        } else if (opcode != 49) {
                                                                                            if ((opcode xor -0x1) !=
                                                                                                -51
                                                                                            ) {
                                                                                                if (opcode != 51) {
                                                                                                    if (opcode == 52) {
                                                                                                        val i =
                                                                                                            buffer
                                                                                                                .get()
                                                                                                                .toInt() and
                                                                                                                0xFF
                                                                                                        anIntArray959 =
                                                                                                            IntArray(i)
                                                                                                        standAnimationIds =
                                                                                                            IntArray(i)
                                                                                                        for (i_2_ in 0 until
                                                                                                            i) {
                                                                                                            standAnimationIds!![i_2_] =
                                                                                                                buffer
                                                                                                                    .getShort()
                                                                                                                    .toInt() and
                                                                                                                0xFFFF
                                                                                                            val i_3_ =
                                                                                                                buffer
                                                                                                                    .get()
                                                                                                                    .toInt() and
                                                                                                                    0xFF
                                                                                                            anIntArray959!![i_2_] =
                                                                                                                i_3_
                                                                                                            anInt994 +=
                                                                                                                i_3_
                                                                                                        }
                                                                                                    } else if (opcode ==
                                                                                                        53
                                                                                                    ) {
                                                                                                        aBoolean978 =
                                                                                                            false
                                                                                                    }
                                                                                                } else {
                                                                                                    anInt962 =
                                                                                                        buffer
                                                                                                            .getShort()
                                                                                                            .toInt() and
                                                                                                        0xFFFF
                                                                                                }
                                                                                            } else {
                                                                                                anInt990 =
                                                                                                    buffer
                                                                                                        .getShort()
                                                                                                        .toInt() and
                                                                                                    0xFFFF
                                                                                            }
                                                                                        } else {
                                                                                            anInt952 =
                                                                                                buffer
                                                                                                    .getShort()
                                                                                                    .toInt() and 0xFFFF
                                                                                        }
                                                                                    } else {
                                                                                        anInt983 = buffer
                                                                                            .getShort()
                                                                                            .toInt() and 0xFFFF
                                                                                    }
                                                                                } else {
                                                                                    anInt955 =
                                                                                        buffer.getShort().toInt() and
                                                                                        0xFFFF
                                                                                }
                                                                            } else {
                                                                                turnCCWAnimation =
                                                                                    buffer.getShort().toInt() and 0xFFFF
                                                                            }
                                                                        } else {
                                                                            turn180Animation =
                                                                                buffer.getShort().toInt() and 0xFFFF
                                                                        }
                                                                    } else {
                                                                        anInt954 =
                                                                            buffer.getShort().toInt() and 0xFFFF
                                                                    }
                                                                } else {
                                                                    anInt958 = (buffer.getShort().toInt() and 0xFFFF)
                                                                }
                                                            } else {
                                                                anInt951 = (buffer.get().toInt() and 0xFF)
                                                            }
                                                        } else {
                                                            anInt965 = (buffer.getShort()).toInt()
                                                        }
                                                    } else {
                                                        anInt969 = (buffer.getShort().toInt() and 0xFFFF)
                                                    }
                                                } else {
                                                    anInt993 = buffer.get().toInt() and 0xFF
                                                }
                                            } else {
                                                anInt956 = (buffer.getShort()).toInt()
                                            }
                                        } else {
                                            anInt961 = buffer.getShort().toInt() and 0xFFFF
                                        }
                                    } else {
                                        anInt988 = buffer.get().toInt() and 0xFF
                                    }
                                } else {
                                    anInt980 = buffer.getShort().toInt() and 0xFFFF
                                }
                            } else {
                                anInt992 = buffer.get().toInt() and 0xFF
                            }
                        } else {
                            runAnimationId = buffer.getShort().toInt() and 0xFFFF
                        }
                    } else {
                        anInt986 = buffer.getShort().toInt() and 0xFFFF
                    }
                } else {
                    anInt987 = buffer.getShort().toInt() and 0xFFFF
                }
            } else {
                anInt974 = buffer.getShort().toInt() and 0xFFFF
            }
        } else {
            standAnimationId = buffer.getShort().toInt() and 0xFFFF
            walkAnimationId = buffer.getShort().toInt() and 0xFFFF
            if ((standAnimationId xor -0x1) == -65536) standAnimationId = -1
            if ((walkAnimationId xor -0x1) == -65536) walkAnimationId = -1
        }
    }

    /**
     * Instantiates a new Render animation definition.
     */
    init {
        anInt957 = -1
        anInt954 = -1
        anInt960 = -1
        anInt958 = -1
        turn180Animation = -1
        standAnimationId = -1
        anInt952 = -1
        anInt983 = -1
        anInt985 = -1
        anInt962 = -1
        anInt966 = -1
        anInt977 = -1
        runAnimationId = -1
        turnCCWAnimation = -1
        anInt987 = -1
        anInt964 = -1
        walkAnimationId = -1
        anInt986 = -1
        anInt955 = -1
        anInt989 = -1
        anInt974 = -1
        anInt990 = -1
    }

    companion object {
        /**
         * For id render animation definition.
         *
         * @param animId the anim id
         * @return the render animation definition
         */
        fun forId(animId: Int): RenderAnimationDefinition? {
            if (animId == -1) {
                return null
            }
            val data = getData(CacheIndex.CONFIGURATION, CacheArchive.BAS_TYPE, animId)
            val defs = RenderAnimationDefinition()
            if (data != null) {
                defs.parse(ByteBuffer.wrap(data))
            } else {
                log(
                    RenderAnimationDefinition::class.java,
                    Log.ERR,
                    "No definitions found for render animation $animId, size=" +
                        getArchiveCapacity(
                            CacheIndex.CONFIGURATION,
                            CacheArchive.BAS_TYPE,
                        ) + "!",
                )
            }
            return defs
        }

        /**
         * Main.
         *
         * @param args the args
         * @throws Throwable the throwable
         */
        @Throws(Throwable::class)
        @JvmStatic
        fun main(args: Array<String>) {
            prompt(false)
            val def = forId(1426)

            for (f in def!!.javaClass.declaredFields) {
                if (!Modifier.isStatic(f.modifiers)) {
                    if (f.type.isArray) {
                        val `object` = f[def]
                        if (`object` != null) {
                            val length =
                                java.lang.reflect.Array
                                    .getLength(`object`)
                            print(f.name + ", [")
                            for (i in 0 until length) {
                                print(
                                    java.lang.reflect.Array
                                        .get(`object`, i)
                                        .toString() + (if (i < (length - 1)) ", " else "]"),
                                )
                            }
                            continue
                        }
                    }
                }
            }
            for (f in def.javaClass.superclass.declaredFields) {
                if (!Modifier.isStatic(f.modifiers)) {
                    if (f.type.isArray) {
                        val `object` = f[def]
                        if (`object` != null) {
                            val length =
                                java.lang.reflect.Array
                                    .getLength(`object`)
                            print(f.name + ", [")
                            for (i in 0 until length) {
                                print(
                                    java.lang.reflect.Array
                                        .get(`object`, i)
                                        .toString() + (if (i < (length - 1)) ", " else "]"),
                                )
                            }
                            continue
                        }
                    }
                }
            }
        }
    }
}
