package core.cache.def.impl

import core.cache.Cache
import core.cache.CacheIndex
import java.nio.ByteBuffer

/**
 * Represents a graphical definition used for rendering graphics.
 */
class GraphicDefinition {
    var modelID: Int = 0
    var animationID: Int = -1
    var sizeXY: Int = 128
    var sizeZ: Int = 128
    var rotation: Int = 0
    var ambient: Int = 0
    var originalModelColor: ShortArray? = null
    var modifiedModelColor: ShortArray? = null
    var originalTextureColor: ShortArray? = null
    var modifiedTextureColor: ShortArray? = null
    var hasAlpha: Boolean = false
    var castsShadow: Boolean = false
    var modelShadow: Int = 0
    var graphicsId: Int = 0
    var renderPriority: Byte = 0
    var shadowOpacity: Int = -1

    companion object {
        private val graphicDefinitions = mutableMapOf<Int, GraphicDefinition>()

        /**
         * Gets the [GraphicDefinition] for the given id.
         *
         * @param gfxId The graphic id.
         * @return The corresponding [GraphicDefinition].
         */
        @JvmStatic
        fun forId(gfxId: Int): GraphicDefinition = graphicDefinitions[gfxId] ?: run {
            val data = Cache.getData(CacheIndex.GRAPHICS, gfxId ushr 8, gfxId and 0xFF)
            GraphicDefinition().apply {
                graphicsId = gfxId
                data?.let { readValueLoop(ByteBuffer.wrap(it)) }
                graphicDefinitions[gfxId] = this
            }
        }
    }

    /**
     * Reads and processes graphic data from the given [ByteBuffer].
     *
     * @param buffer The buffer containing graphic data.
     */
    private fun readValueLoop(buffer: ByteBuffer) {
        while (true) {
            val opcode = buffer.get().toInt() and 0xFF
            if (opcode == 0) break
            readValues(buffer, opcode)
        }
    }

    /**
     * Processes a single opcode and updates the definition accordingly.
     *
     * @param buffer The data buffer.
     * @param opcode The opcode to process.
     */
    private fun readValues(buffer: ByteBuffer, opcode: Int) {
        when (opcode) {
            1 -> modelID = buffer.short.toInt()
            2 -> animationID = buffer.short.toInt()
            4 -> sizeXY = buffer.short.toInt() and 0xFFFF
            5 -> sizeZ = buffer.short.toInt() and 0xFFFF
            6 -> rotation = buffer.short.toInt() and 0xFFFF
            7 -> ambient = buffer.get().toInt() and 0xFF
            8 -> modelShadow = buffer.get().toInt() and 0xFF
            10 -> castsShadow = true
            11 -> renderPriority = 1
            12 -> renderPriority = 4
            13 -> renderPriority = 5
            14 -> {
                renderPriority = 2
                shadowOpacity = (buffer.get().toInt() and 0xFF) * 256
            }

            15 -> {
                renderPriority = 3
                shadowOpacity = buffer.short.toInt() and 0xFFFF
            }

            16 -> {
                renderPriority = 3
                shadowOpacity = buffer.int
            }

            40 -> {
                val size = buffer.get().toInt() and 0xFF
                originalModelColor = ShortArray(size)
                modifiedModelColor = ShortArray(size)
                for (i in 0 until size) {
                    originalModelColor!![i] = buffer.short
                    modifiedModelColor!![i] = buffer.short
                }
            }

            41 -> {
                val size = buffer.get().toInt() and 0xFF
                originalTextureColor = ShortArray(size)
                modifiedTextureColor = ShortArray(size)
                for (i in 0 until size) {
                    originalTextureColor!![i] = buffer.short
                    modifiedTextureColor!![i] = buffer.short
                }
            }
        }
    }
}
