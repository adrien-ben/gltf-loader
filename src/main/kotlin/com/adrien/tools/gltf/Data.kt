package com.adrien.tools.gltf

import com.beust.klaxon.Klaxon
import java.io.File

typealias Extensions = Map<Any, Any>

// Component types constants
const val BYTE = 5120
const val UNSIGNED_BYTE = 5121
const val SHORT = 5122
const val UNSIGNED_SHORT = 5123
const val UNSIGNED_INT = 5125
const val FLOAT = 5126

// Types constants
const val SCALAR = "SCALAR"
const val VEC2 = "VEC2"
const val VEC3 = "VEC3"
const val VEC4 = "VEC4"
const val MAT2 = "MAT2"
const val MAT3 = "MAT3"
const val MAT4 = "MAT4"

class AccessorRaw(
        val bufferView: Int? = null,
        val byteOffset: Int = 0,
        val componentType: Int,
        val normalized: Boolean = false,
        val count: Int,
        val type: String,
        val max: List<Number>? = null,
        val min: List<Number>? = null,
        val sparse: SparseRaw? = null,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class SparseRaw(
        val count: Int,
        val indices: IndicesRaw,
        val values: ValuesRaw,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class IndicesRaw(
        val bufferView: Int,
        val byteOffset: Int = 0,
        val componentType: Int,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class ValuesRaw(
        val bufferView: Int,
        val byteOffset: Int = 0,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class AnimationRaw(
        val channels: List<ChannelRaw>,
        val samplers: List<AnimationSamplerRaw>,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class ChannelRaw(
        val sampler: Int,
        val target: TargetRaw,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class TargetRaw(
        val node: Int? = null,
        val path: String,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class AnimationSamplerRaw(
        val input: Int,
        val interpolation: String = "LINEAR",
        val output: Int,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class AssetRaw(
        val copyright: String? = null,
        val generator: String? = null,
        val version: String,
        val minVersion: String? = null,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class BufferRaw(
        val uri: String? = null,
        val byteLength: Int,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class BufferViewRaw(
        val buffer: Int,
        val byteOffset: Int = 0,
        val byteLength: Int,
        val byteStride: Int? = null,
        val target: Int? = null,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class CameraRaw(
        val orthographic: OrthographicRaw? = null,
        val perspective: PerspectiveRaw? = null,
        val type: String,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class OrthographicRaw(
        val xmag: Number,
        val ymag: Number,
        val zfar: Number,
        val znear: Number,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class PerspectiveRaw(
        val aspectRatio: Number? = null,
        val yfov: Number,
        val zfar: Number? = null,
        val znear: Number,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class ImageRaw(
        val uri: String? = null,
        val mimeType: String? = null,
        val bufferView: Int? = null,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class MaterialRaw(
        val name: String? = null,
        val pbrMetallicRoughness: PbrMetallicRoughnessRaw = PbrMetallicRoughnessRaw(),
        val normalTexture: NormalTextureInfoRaw? = null,
        val occlusionTexture: OcclusionTextureInfoRaw? = null,
        val emissiveTexture: TextureInfoRaw? = null,
        val emissiveFactor: List<Number> = listOf(0f, 0f, 0f),
        val alphaMode: String = "OPAQUE",
        val alphaCutoff: Number = 0.5f,
        val doubleSided: Boolean = false,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class NormalTextureInfoRaw(
        val index: Int,
        val texCoord: Int = 0,
        val scale: Number = 1f,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class OcclusionTextureInfoRaw(
        val index: Int,
        val texCoord: Int = 0,
        val strength: Number = 1f,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class PbrMetallicRoughnessRaw(
        val baseColorFactor: List<Number> = listOf(1f, 1f, 1f, 1f),
        val baseColorTexture: TextureInfoRaw? = null,
        val metallicFactor: Number = 1f,
        val roughnessFactor: Number = 1f,
        val metallicRoughnessTexture: TextureInfoRaw? = null,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class MeshRaw(
        val primitives: List<PrimitiveRaw>,
        val weights: List<Number>? = null,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class PrimitiveRaw(
        val attributes: Map<String, Int>,
        val indices: Int? = null,
        val material: Int? = null,
        val mode: Int = 4,
        val targets: List<Map<String, Int>>? = null,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class NodeRaw(
        val camera: Int? = null,
        val children: List<Int>? = null,
        val skin: Int? = null,
        val matrix: List<Number> = listOf(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f),
        val mesh: Int? = null,
        val rotation: List<Number> = listOf(0f, 0f, 0f, 1f),
        val scale: List<Number> = listOf(1f, 1f, 1f),
        val translation: List<Number> = listOf(0f, 0f, 0f),
        val weights: List<Number>? = null,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class SamplerRaw(
        val magFilter: Int? = null,
        val minFilter: Int? = null,
        val wrapS: Int = 10497,
        val wrapT: Int = 10497,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class SceneRaw(
        val nodes: List<Int>? = null,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class SkinRaw(
        val inverseBindMatrices: Int? = null,
        val skeleton: Int? = null,
        val joints: List<Int>,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class TextureRaw(
        val sampler: Int? = null,
        val source: Int? = null,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class TextureInfoRaw(
        val index: Int,
        val texCoord: Int = 0,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class GltfAssetRaw(
        val extensionsUsed: List<String>? = null,
        val extensionsRequired: List<String>? = null,
        val accessors: List<AccessorRaw>? = null,
        val animations: List<AnimationRaw>? = null,
        val asset: AssetRaw,
        val buffers: List<BufferRaw>? = null,
        val bufferViews: List<BufferViewRaw>? = null,
        val cameras: List<CameraRaw>? = null,
        val images: List<ImageRaw>? = null,
        val materials: List<MaterialRaw>? = null,
        val meshes: List<MeshRaw>? = null,
        val nodes: List<NodeRaw>? = null,
        val samplers: List<SamplerRaw>? = null,
        val scene: Int? = null,
        val scenes: List<SceneRaw>? = null,
        val skins: List<SkinRaw>? = null,
        val textures: List<TextureRaw>? = null,
        val extensions: Extensions? = null,
        val extras: Any? = null
)

class GltfRaw(val gltfAssetRaw: GltfAssetRaw, val dataByURI: Map<String, ByteArray>?) {
    companion object Factory {

        /**
         * Load a gltf asset from the gltf json file [path] and load external buffers
         */
        fun fromGltfFile(path: String): GltfRaw? {
            val file = File(path)
            val gltfAssetRaw = Klaxon().parse<GltfAssetRaw>(file) ?: return null

            val dataToURI = HashMap<String, ByteArray>()
            gltfAssetRaw.buffers
                    ?.mapNotNull { it.uri }
                    ?.forEach { dataToURI[it] = File(file.parent, it).readBytes() }

            return GltfRaw(gltfAssetRaw, dataToURI)
        }

        /**
         * Load a gltf asset from the glb file [path]
         */
        fun fromGlbFile(path: String): GltfRaw? {
            throw NotImplementedError("Loading .glb files is not yet supported")
        }
    }
}