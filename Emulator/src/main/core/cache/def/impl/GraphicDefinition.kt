package core.cache.def.impl

import core.cache.Cache
import core.cache.CacheIndex
import java.nio.ByteBuffer

/**
 * Represents a graphical definition.
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
         * Retrieves the [GraphicDefinition] for given id.
         *
         * @param gfxId The id of the graphic to retrieve.
         * @return The [GraphicDefinition] associated with the specified id.
         */
        @JvmStatic
        fun forId(gfxId: Int): GraphicDefinition =
            graphicDefinitions[gfxId] ?: run {
                val data = Cache.getData(CacheIndex.GRAPHICS, gfxId ushr 8, gfxId and 0xFF)
                GraphicDefinition().apply {
                    graphicsId = gfxId
                    data?.let { readValueLoop(ByteBuffer.wrap(it)) }
                    graphicDefinitions[gfxId] = this
                }
            }
    }

    /**
     * Reads the values from the provided [ByteBuffer] and processes each opcode.
     *
     * @param buffer The [ByteBuffer] containing the data to be read.
     */
    private fun readValueLoop(buffer: ByteBuffer) {
        while (true) {
            val opcode = buffer.get().toInt() and 0xFF
            if (opcode == 0) break
            readValues(buffer, opcode)
        }
    }

    /**
     * Processes the value corresponding to the given opcode from the [ByteBuffer].
     *
     * @param buffer The [ByteBuffer] containing the data to be read.
     * @param opcode The opcode that determines how to interpret the following data.
     */
    private fun readValues(
        buffer: ByteBuffer,
        opcode: Int,
    ) {
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
