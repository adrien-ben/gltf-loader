package com.adrien.tools.gltf

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import com.beust.klaxon.Klaxon
import java.io.File
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.collections.component1
import kotlin.collections.component2

private const val GLB_HEADER_LENGTH = 12

private const val GLB_MAGIC_FLAG = 0x46546C67

private const val GLB_JSON_CHUNK_TYPE = 0x4E4F534A

private const val GLB_BIN_CHUNK_TYPE = 0x004E4942

/**
 * Custom converter for [MorphTargetRaw]
 *
 * TODO: Remove this when Klaxon can deserialize nested collections
 *
 */
private val morphTargetConverter = object : Converter<MorphTargetRaw> {
    override fun fromJson(jv: JsonValue): MorphTargetRaw {
        return MorphTargetRaw(jv.obj?.mapValues { (_, value) -> value as Int } ?: emptyMap())
    }

    /**
     * To implement when (if?) gltf exports are supported by the library
     */
    override fun toJson(value: MorphTargetRaw): String? {
        throw NotImplementedError("Morph target serialization not implemented")
    }
}

/**
 * Read byte data of a buffer.
 */
private fun BufferRaw.getData(dir: String): ByteArray {
    return uri
            ?.decodeDataUri()
            ?: File(dir, uri).readBytes()
            ?: throw IllegalArgumentException(
                    "Buffer data is not embedded and does not reference a .bin file that could be found")
}

/**
 * Base interface for loaders
 */
internal interface Loader {

    fun load(path: String): GltfRaw?

    companion object {
        fun fromExtension(extension: String) = when (extension) {
            "gltf" -> GltfLoader()
            "glb" -> GlbLoader()
            else -> throw IllegalArgumentException("Unsupported file extension $extension")
        }
    }
}

/**
 * Loader for .gltf files.
 */
private class GltfLoader : Loader {

    /**
     * Load a .gltf file.
     */
    override fun load(path: String): GltfRaw? {
        val file = File(path)
        val assetRaw = Klaxon()
                .converter(morphTargetConverter)
                .parse<GltfAssetRaw>(file)
                ?: return null

        val data = assetRaw.buffers?.map { it.getData(file.parent.toString()) } ?: emptyList()

        return Validator().validate(GltfRaw(assetRaw, data))
    }
}

/**
 * Loader for .glb files.
 */
private class GlbLoader : Loader {

    private val int32Buffer = ByteArray(4)

    /**
     * Load a .glb file.
     */
    override fun load(path: String): GltfRaw? {
        val file = File(path)
        file.inputStream().use {
            val glbHeader = it.readGlbHeader().apply { validate() }

            val firstChunkHeader = it.readChunkHeader().apply { validate(GLB_JSON_CHUNK_TYPE) }
            val assetRaw = Klaxon()
                    .converter(morphTargetConverter)
                    .parse<GltfAssetRaw>(it.readString(firstChunkHeader.length))
                    ?: return null

            val data = ArrayList<ByteArray>()
            if (glbHeader.length - GLB_HEADER_LENGTH > firstChunkHeader.length) {
                val secondChunkHeader = it.readChunkHeader().apply { validate(GLB_BIN_CHUNK_TYPE) }
                val bin = ByteArray(secondChunkHeader.length)
                if (it.read(bin) != secondChunkHeader.length) throw IllegalArgumentException(
                        "Failed to read bytes from input stream.")
                data.add(bin)
            }

            assetRaw.buffers
                    ?.filterIndexed { index, buffer -> index != 0 || buffer.uri != null }
                    ?.mapTo(data) { it.getData(file.parent.toString()) }

            return Validator().validate(GltfRaw(assetRaw, data))
        }
    }

    /**
     * Read the [GlbHeader] from an [InputStream].
     */
    private fun InputStream.readGlbHeader() = GlbHeader(
            this.readInt(), this.readInt(), this.readInt()
    )

    /**
     * Read the [ChunkHeader] from an [InputStream].
     */
    private fun InputStream.readChunkHeader() = ChunkHeader(this.readInt(), this.readInt())

    /**
     * Read an integer from an [InputStream].
     */
    private fun InputStream.readInt(): Int {
        if (this.read(int32Buffer) != 4) throw IllegalArgumentException("Failed to read int from input stream.")
        return int32Buffer.toInt()
    }

    /**
     * Read the first integer in a [ByteArray].
     */
    private fun ByteArray.toInt(): Int {
        return ByteBuffer.wrap(this, 0, 4).order(ByteOrder.LITTLE_ENDIAN).int
    }

    /**
     * Read a string of length [length] from an [InputStream].
     */
    private fun InputStream.readString(length: Int): String {
        val buffer = ByteArray(length)
        if (this.read(buffer) != length) throw IllegalArgumentException("Failed to read int from input stream.")
        return String(buffer)
    }

    /**
     * Glb file header.
     */
    private inner class GlbHeader(val magic: Int, val version: Int, val length: Int) {

        /**
         * Validate the glb header.
         */
        fun validate() {
            if (magic != GLB_MAGIC_FLAG) throw IllegalArgumentException("Failed to read file. Illegal magic value in header.")
            if (length == 12) throw IllegalArgumentException("Failed to read file. No content.")
        }
    }

    /**
     * Glb chunk header.
     */
    private inner class ChunkHeader(val length: Int, val type: Int) {

        /**
         * Validate the chunk header.
         */
        fun validate(expectedType: Int) {
            if (type != expectedType) throw IllegalArgumentException("Failed to read chunk. Unexpected chunk.")
            if (length == 0) throw IllegalArgumentException("Failed to read chunk. No content.")
        }
    }
}
