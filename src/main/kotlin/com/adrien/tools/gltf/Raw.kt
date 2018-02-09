package com.adrien.tools.gltf

import java.util.*

internal typealias Extensions = Map<String, Any>

internal typealias Extras = Any

private val DATA_URI_REGEX = Regex("data:.*(?:;base64)?,(.*)")

internal class BufferRaw(
        val uri: String? = null,
        val byteLength: Int,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class BufferViewRaw(
        val buffer: Int,
        val byteOffset: Int? = null,
        val byteLength: Int,
        val byteStride: Int? = null,
        val target: Int? = null,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class IndicesRaw(
        val bufferView: Int,
        val byteOffset: Int? = null,
        val componentType: Int,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class ValuesRaw(
        val bufferView: Int,
        val byteOffset: Int? = null,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class SparseRaw(
        val count: Int,
        val indices: IndicesRaw,
        val values: ValuesRaw,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class AccessorRaw(
        val bufferView: Int? = null,
        val byteOffset: Int? = null,
        val componentType: Int,
        val normalized: Boolean? = null,
        val count: Int,
        val type: String,
        val max: List<Number>? = null,
        val min: List<Number>? = null,
        val sparse: SparseRaw? = null,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class SamplerRaw(
        val magFilter: Int? = null,
        val minFilter: Int? = null,
        val wrapS: Int? = null,
        val wrapT: Int? = null,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class ImageRaw(
        val uri: String? = null,
        val mimeType: String? = null,
        val bufferView: Int? = null,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class TextureRaw(
        val sampler: Int? = null,
        val source: Int? = null,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class TextureInfoRaw(
        val index: Int,
        val texCoord: Int? = null,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class NormalTextureInfoRaw(
        val index: Int,
        val texCoord: Int? = null,
        val scale: Number? = null,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class OcclusionTextureInfoRaw(
        val index: Int,
        val texCoord: Int? = null,
        val strength: Number? = null,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class PbrMetallicRoughnessRaw(
        val baseColorFactor: List<Number>? = null,
        val baseColorTexture: TextureInfoRaw? = null,
        val metallicFactor: Number? = null,
        val roughnessFactor: Number? = null,
        val metallicRoughnessTexture: TextureInfoRaw? = null,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class MaterialRaw(
        val name: String? = null,
        val pbrMetallicRoughness: PbrMetallicRoughnessRaw? = null,
        val normalTexture: NormalTextureInfoRaw? = null,
        val occlusionTexture: OcclusionTextureInfoRaw? = null,
        val emissiveTexture: TextureInfoRaw? = null,
        val emissiveFactor: List<Number>? = null,
        val alphaMode: String? = null,
        val alphaCutoff: Number? = null,
        val doubleSided: Boolean? = null,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

/**
 * TODO: Remove this when Klaxon can deserialize nested collections
 */
internal class MorphTargetRaw(map: Map<String, Int>) : Map<String, Int> by map

internal class PrimitiveRaw(
        val attributes: Map<String, Int>,
        val indices: Int? = null,
        val material: Int? = null,
        val mode: Int? = null,
        val targets: List<MorphTargetRaw>? = null,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class MeshRaw(
        val primitives: List<PrimitiveRaw>,
        val weights: List<Number>? = null,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class OrthographicRaw(
        val xmag: Number,
        val ymag: Number,
        val zfar: Number,
        val znear: Number,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class PerspectiveRaw(
        val aspectRatio: Number? = null,
        val yfov: Number,
        val zfar: Number? = null,
        val znear: Number,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class CameraRaw(
        val orthographic: OrthographicRaw? = null,
        val perspective: PerspectiveRaw? = null,
        val type: String,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class NodeRaw(
        val camera: Int? = null,
        val children: List<Int>? = null,
        val skin: Int? = null,
        val matrix: List<Number>? = null,
        val mesh: Int? = null,
        val rotation: List<Number>? = null,
        val scale: List<Number>? = null,
        val translation: List<Number>? = null,
        val weights: List<Number>? = null,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class SkinRaw(
        val inverseBindMatrices: Int? = null,
        val skeleton: Int? = null,
        val joints: List<Int>,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class AnimationTargetRaw(
        val node: Int? = null,
        val path: String,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class AnimationSamplerRaw(
        val input: Int,
        val interpolation: String?,
        val output: Int,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class ChannelRaw(
        val sampler: Int,
        val target: AnimationTargetRaw,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class AnimationRaw(
        val channels: List<ChannelRaw>,
        val samplers: List<AnimationSamplerRaw>,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class SceneRaw(
        val nodes: List<Int>? = null,
        val name: String? = null,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class AssetRaw(
        val copyright: String? = null,
        val generator: String? = null,
        val version: String,
        val minVersion: String? = null,
        val extensions: Extensions? = null,
        val extras: Extras? = null
)

internal class GltfAssetRaw(
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
        val extras: Extras? = null
)

internal class GltfRaw(val gltfAssetRaw: GltfAssetRaw, val data: List<ByteArray>)

/**
 * String extensions to decode a data URI if it matches the data uri pattern.
 *
 * It returns a [ByteArray] or null if the string does not match the required pattern.
 */
internal fun String.decodeDataUri(): ByteArray? {
    val base64 = DATA_URI_REGEX.find(this)?.groupValues?.get(1) ?: return null
    return Base64.getDecoder().decode(base64)
}
