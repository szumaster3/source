package core.cache.def.impl

import core.cache.Cache
import core.cache.CacheIndex
import core.cache.misc.buffer.ByteBufferUtils
import java.nio.ByteBuffer

/**
 * Represents an animation definition.
 */
class AnimationDefinition {
    var maxLoops = 99
    var movementPriority = 0
    var frames: IntArray? = null
    var priorityOverride = -1
    var hasSoundEffect = false
    var priority = 5
    var rightHandItem = -1
    var leftHandItem = -1
    var handledSounds: Array<IntArray?>? = null
    var interleaveOrder: BooleanArray? = null
    var expressionFrames: IntArray? = null
    var isForcedPriority = false
    var duration: IntArray? = null
    var replayMode = 2
    var disableResetOnLoop = false
    var stopsOnMovement = false
    var priorityBackup = -1
    var loopOffset = -1
    var newHeader = false
    var soundMinDelay: IntArray? = null
    var soundMaxDelay: IntArray? = null
    var frameSoundEffects: IntArray? = null
    var effect2Sound = false

    companion object {
        private val animationDefinition = mutableMapOf<Int, AnimationDefinition>()

        /**
         * Retrieves an [AnimationDefinition] by its emote id.
         *
         * @param emoteId The id of the animation.
         * @return The [AnimationDefinition] if found, or `null` otherwise.
         */
        @JvmStatic
        fun forId(emoteId: Int): AnimationDefinition? =
            try {
                animationDefinition[emoteId] ?: run {
                    val data = Cache.getData(CacheIndex.SEQUENCE_CONFIGURATION, emoteId ushr 7, emoteId and 0x7f)
                    val definition = AnimationDefinition()
                    data?.let { definition.readValueLoop(ByteBuffer.wrap(it)) }
                    definition.changeValues()
                    animationDefinition[emoteId] = definition
                    definition
                }
            } catch (_: Throwable) {
                null
            }

        /**
         * Retrieves all loaded animation definitions.
         *
         * @return A map containing animation definitions by their id.
         */
        @JvmStatic
        fun getDefinition(): Map<Int, AnimationDefinition> = animationDefinition
    }

    /**
     * Reads animation values from a buffer.
     *
     * @param buffer The [ByteBuffer] containing animation data.
     */
    private fun readValueLoop(buffer: ByteBuffer) {
        while (true) {
            val opcode = buffer.get().toInt() and 0xFF
            if (opcode == 0) break
            readValues(buffer, opcode)
        }
    }

    /**
     * Gets the total duration of the animation in milliseconds.
     *
     * @return The animation duration in milliseconds.
     */
    fun getDuration(): Int = duration?.filter { it <= 100 }?.sumOf { it * 20 } ?: 0

    /**
     * Gets the total cycle count for the animation.
     *
     * @return The number of cycles.
     */
    fun getCycles(): Int = duration?.sum() ?: 0

    /**
     * Gets the duration of the animation in game ticks.
     *
     * @return The duration in game ticks.
     */
    fun getDurationTicks(): Int = maxOf(getDuration() / 600, 1)

    /**
     * Reads values from a buffer based on opcodes.
     *
     * @param buffer The [ByteBuffer] containing the data.
     * @param opcode The opcode indicating what data to read.
     */
    private fun readValues(
        buffer: ByteBuffer,
        opcode: Int,
    ) {
        when (opcode) {
            1 -> {
                val length = buffer.short.toInt() and 0xFFFF
                duration = IntArray(length) { buffer.short.toInt() and 0xFFFF }
                frames = IntArray(length) { buffer.short.toInt() and 0xFFFF }
                frames?.forEachIndexed { i, v ->
                    frames!![i] = (buffer.short.toInt() and 0xFFFF shl 16) + v
                }
            }

            2 -> loopOffset = buffer.short.toInt() and 0xFFFF
            3 -> {
                interleaveOrder = BooleanArray(256)
                val length = buffer.get().toInt() and 0xFF
                repeat(length) {
                    interleaveOrder!![buffer.get().toInt() and 0xFF] = true
                }
            }

            4 -> isForcedPriority = true
            5 -> priority = buffer.get().toInt() and 0xFF
            6 -> leftHandItem = buffer.short.toInt() and 0xFFFF
            7 -> rightHandItem = buffer.short.toInt() and 0xFFFF
            8 -> maxLoops = buffer.get().toInt() and 0xFF
            9 -> priorityOverride = buffer.get().toInt() and 0xFF
            10 -> priorityBackup = buffer.get().toInt() and 0xFF
            11 -> replayMode = buffer.get().toInt() and 0xFF
            12 -> {
                val i = buffer.get().toInt() and 0xFF
                expressionFrames = IntArray(i) { buffer.short.toInt() and 0xFFFF }
                expressionFrames?.forEachIndexed { index, v ->
                    expressionFrames!![index] = (buffer.short.toInt() and 0xFFFF shl 16) + v
                }
            }

            13 -> {
                val i = buffer.short.toInt() and 0xFFFF
                handledSounds = Array(i) { null }
                repeat(i) { index ->
                    val size = buffer.get().toInt() and 0xFF
                    if (size > 0) {
                        handledSounds!![index] =
                            IntArray(size).apply {
                                this[0] = ByteBufferUtils.getMedium(buffer)
                                for (j in 1 until size) this[j] = buffer.short.toInt() and 0xFFFF
                            }
                    }
                }
            }

            14 -> hasSoundEffect = true
        }
    }

    /**
     * Adjusts default values.
     */
    fun changeValues() {
        if (priorityOverride == -1) {
            priorityOverride = if (interleaveOrder == null) 0 else 2
        }
        if (priorityBackup == -1) {
            priorityBackup = if (interleaveOrder == null) 0 else 2
        }
    }
}
