package com.adrien.tools.gltf

import com.adrien.tools.gltf.Matchers.atLeast
import com.adrien.tools.gltf.Matchers.be
import com.adrien.tools.gltf.Matchers.empty
import com.adrien.tools.gltf.Matchers.equalTo
import com.adrien.tools.gltf.Matchers.greateThan
import com.adrien.tools.gltf.Matchers.haveSize
import com.adrien.tools.gltf.Matchers.inRange
import com.adrien.tools.gltf.Matchers.not
import com.adrien.tools.gltf.Matchers.oneOf

private const val REQUIRED_VERSION = "2.0"

private const val MIN_BUFFER_LENGTH = 1
private const val MIN_REF_INDEX = 0
private const val MIN_BYTE_OFFSET = 0
private const val MIN_TEXCOORDS = 0
private const val MIN_ALPHA_CUTOFF = 0.0
private const val MIN_ORTHO_ZFAR = 0.0
private const val MIN_ORTHO_ZNEAR = 0.0
private const val MIN_ASPECT_RATIO = 0.0
private const val MIN_FOV = 0.0
private const val MIN_PERSPECTIVE_ZFAR = 0.0
private const val MIN_PERSPECTIVE_ZNEAR = 0.0
private const val SPARSE_MIN_COUNT = 1
private const val ACCESSOR_MIN_COUNT = 1
private const val COLOR_SIZE = 4
private const val EMISSIVE_FACTOR_SIZE = 3
private const val MATRIX4_SIZE = 16
private const val QUATERNION_SIZE = 4
private const val VECTOR3_SIZE = 3

private val RANGE_BYTE_STRIDE = 4..252
private val OCCLUSION_TEXTURE_STRENGTH = 0.0..1.0
private val METALLIC_FACTOR_RANGE = 0.0..1.0
private val ROUGHNESS_FACTOR_RANGE = 0.0..1.0

private val BUFFER_TARGETS = listOf(34962, 34963)
private val INDICES_COMPONENT_TYPES = listOf(5121, 5123, 5125)
private val ACCESSOR_COMPONENT_TYPES = listOf(5120, 5121, 5122, 5123, 5125, 5126)
private val ACCESSOR_TYPES = listOf("SCALAR", "VEC2", "VEC3", "VEC4", "MAT2", "MAT3", "MAT4")
private val ACCESSOR_MIN_MAX_SIZES = listOf(1, 2, 3, 4, 9, 16)
private val SAMPLER_MIN_FILTERS = listOf(9728, 9729, 9984, 9985, 9986, 9987)
private val SAMPLER_MAG_FILTERS = listOf(9728, 9729)
private val SAMPLER_WRAPS = listOf(10497, 33648, 33071)
private val IMAGE_MIME_TYPES = listOf("image/jpeg", "image/png")
private val MATERIAL_ALPHA_MODES = listOf("OPAQUE", "MASK", "BLEND")
private val PRIMITIVE_MODES = listOf(0, 1, 2, 3, 4, 5, 6)
private val CAMERA_TYPES = listOf("perspective", "orthographic")
private val TARGET_PATHS = listOf("translation", "rotation", "scale", "weights")
private val INTERPOLATION_TYPES = listOf("LINEAR", "STEP", "CUBICSPLINE")

/**
 * Exception thrown is case of validation error.
 */
private class ValidationException(message: String) : Throwable(message)

/**
 * This class is responsible for validating the data loaded from the json.
 * At this point, is the json was successfully loaded we know that required
 * data is present. This class then validates data consistency.
 */
internal class Validator {

    /**
     * Validate the provided asset.
     *
     * @throws ValidationException if the asset is not valid.
     */
    fun validate(gltfRaw: GltfRaw): GltfRaw {
        gltfRaw.gltfAssetRaw.apply {
            asset.validate()
            buffers?.should(not(be(empty())), "buffers")?.forEachIndexed { index, it -> it.validate(index) }
            bufferViews?.should(not(be(empty())), "bufferViews")?.forEachIndexed { index, it -> it.validate(index) }
            accessors?.should(not(be(empty())), "accessors")?.forEachIndexed { index, it -> it.validate(index) }
            samplers?.should(not(be(empty())), "samplers")?.forEachIndexed { index, it -> it.validate(index) }
            images?.should(not(be(empty())), "images")?.forEachIndexed { index, it -> it.validate(index) }
            textures?.should(not(be(empty())), "textures")?.forEachIndexed { index, it -> it.validate(index) }
            materials?.should(not(be(empty())), "materials")?.forEachIndexed { index, it -> it.validate(index) }
            meshes?.should(not(be(empty())), "meshes")?.forEachIndexed { index, it -> it.validate(index) }
            cameras?.should(not(be(empty())), "cameras")?.forEachIndexed { index, it -> it.validate(index) }
            nodes?.should(not(be(empty())), "nodes")?.forEachIndexed { index, it -> it.validate(index) }
            skins?.should(not(be(empty())), "skins")?.forEachIndexed { index, it -> it.validate(index) }
            animations?.should(not(be(empty())), "animations")?.forEachIndexed { index, it -> it.validate(index) }
            scenes?.should(not(be(empty())), "scenes")?.forEachIndexed { index, it -> it.validate(index) }
        }
        return gltfRaw
    }
}

private fun BufferRaw.validate(index: Int) {
    val path = "buffers[$index]"
    byteLength.should(be(atLeast(MIN_BUFFER_LENGTH)), "$path.byteLength")
}

private fun BufferViewRaw.validate(index: Int) {
    val path = "bufferViews[$index]"
    buffer.should(be(atLeast(MIN_REF_INDEX)), "$path.buffer")
    byteOffset?.should(be(atLeast(MIN_BYTE_OFFSET)), "$path.byteOffset")
    byteLength.should(be(atLeast(MIN_BUFFER_LENGTH)), "$path.byteLength")
    byteStride?.should(be(inRange(RANGE_BYTE_STRIDE)), "$path.byteStride")
    target?.should(be(oneOf(BUFFER_TARGETS)), "$path.target")
}

private fun IndicesRaw.validate(fieldPath: String) {
    bufferView.should(be(atLeast(MIN_REF_INDEX)), "$fieldPath.bufferView")
    byteOffset?.should(be(atLeast(MIN_BYTE_OFFSET)), "$fieldPath.byteOffset")
    componentType.should(be(oneOf(INDICES_COMPONENT_TYPES)), "$fieldPath.componentType")
}

private fun ValuesRaw.validate(fieldPath: String) {
    bufferView.should(be(atLeast(MIN_REF_INDEX)), "$fieldPath.bufferView")
    byteOffset?.should(be(atLeast(MIN_BYTE_OFFSET)), "$fieldPath.byteOffset")
}

private fun SparseRaw.validate(fieldPath: String) {
    count.should(be(atLeast(SPARSE_MIN_COUNT)), "$fieldPath.count")
    indices.validate("$fieldPath.indices")
    values.validate("$fieldPath.values")
}

private fun AccessorRaw.validate(index: Int) {
    val path = "accessors[$index]"
    bufferView?.should(be(atLeast(MIN_REF_INDEX)), "$path.bufferView")
    byteOffset?.should(be(atLeast(MIN_BYTE_OFFSET)), "$path.byteOffset")
    componentType.should(be(oneOf(ACCESSOR_COMPONENT_TYPES)), "$path.componentType")
    count.should(be(atLeast(ACCESSOR_MIN_COUNT)), "$path.count")
    type.should(be(oneOf(ACCESSOR_TYPES)), "$path.type")
    max?.size?.should(be(oneOf(ACCESSOR_MIN_MAX_SIZES)), "$path.max.size")
    min?.size?.should(be(oneOf(ACCESSOR_MIN_MAX_SIZES)), "$path.min.size")
    sparse?.validate("$path.sparse")
}

private fun SamplerRaw.validate(index: Int) {
    val path = "samplers[$index]"
    magFilter?.should(be(oneOf(SAMPLER_MAG_FILTERS)), "$path.magFilter")
    minFilter?.should(be(oneOf(SAMPLER_MIN_FILTERS)), "$path.minFilter")
    wrapS?.should(be(oneOf(SAMPLER_WRAPS)), "$path.wrapS")
    wrapT?.should(be(oneOf(SAMPLER_WRAPS)), "$path.wrapT")
}

private fun ImageRaw.validate(index: Int) {
    val path = "images[$index]"
    mimeType?.should(be(oneOf(IMAGE_MIME_TYPES)), "$path.mimeType")
    bufferView?.should(be(atLeast(MIN_REF_INDEX)), "$path.bufferView")
}

private fun TextureRaw.validate(index: Int) {
    val path = "textures[$index]"
    sampler?.should(be(atLeast(MIN_REF_INDEX)), "$path.sampler")
    source?.should(be(atLeast(MIN_REF_INDEX)), "$path.source")
}

private fun TextureInfoRaw.validate(fieldPath: String) {
    index.should(be(atLeast(MIN_REF_INDEX)), "$fieldPath.index")
    texCoord?.should(be(atLeast(MIN_TEXCOORDS)), "$fieldPath.texCoord")
}

private fun NormalTextureInfoRaw.validate(fieldPath: String) {
    index.should(be(atLeast(MIN_REF_INDEX)), "$fieldPath.index")
    texCoord?.should(be(atLeast(MIN_TEXCOORDS)), "$fieldPath.texCoord")
}

private fun OcclusionTextureInfoRaw.validate(fieldPath: String) {
    index.should(be(atLeast(MIN_REF_INDEX)), "$fieldPath.index")
    texCoord?.should(be(atLeast(MIN_TEXCOORDS)), "$fieldPath.texCoord")
    strength?.toDouble()?.should(be(inRange(OCCLUSION_TEXTURE_STRENGTH)), "$fieldPath.strength")
}

private fun PbrMetallicRoughnessRaw.validate(fieldPath: String) {
    baseColorFactor?.should(haveSize(COLOR_SIZE), "$fieldPath.baseColorFactor")
    metallicFactor?.toDouble()?.should(be(inRange(METALLIC_FACTOR_RANGE)), "$fieldPath.metallicFactor")
    roughnessFactor?.toDouble()?.should(be(inRange(ROUGHNESS_FACTOR_RANGE)), "$fieldPath.roughnessFactor")
}

private fun MaterialRaw.validate(index: Int) {
    val path = "materials[$index]"
    pbrMetallicRoughness?.validate("$path.pbrMetallicRoughness")
    normalTexture?.validate("$path.normalTexture")
    occlusionTexture?.validate("$path.occlusionTexture")
    emissiveTexture?.validate("$path.emissiveTexture")
    emissiveFactor?.should(haveSize(EMISSIVE_FACTOR_SIZE), "$path.emissiveFactor")
    alphaMode?.should(be(oneOf(MATERIAL_ALPHA_MODES)), "$path.alphaMode")
    alphaCutoff?.toDouble()?.should(be(atLeast(MIN_ALPHA_CUTOFF)), "$path.alphaCutoff")
}

private fun PrimitiveRaw.validate(fieldPath: String) {
    attributes.should(not(be(Matchers.emptyMap())), "$fieldPath.attributes")
    indices?.should(be(atLeast(MIN_REF_INDEX)), "$fieldPath.indices")
    material?.should(be(atLeast(MIN_REF_INDEX)), "$fieldPath.material")
    mode?.should(be(oneOf(PRIMITIVE_MODES)), "$fieldPath.mode")
    targets?.should(not(be(empty())), "$fieldPath.targets")
            ?.forEachIndexed { index, it ->
                it.should(not(be(Matchers.emptyMap())), "$fieldPath.targets[$index]")
            }
}

private fun MeshRaw.validate(index: Int) {
    val path = "meshes[$index]"
    primitives.should(not(be(empty())), "$path.primitives")
            .forEachIndexed { pIndex, it -> it.validate("$path.primitives[$pIndex]") }
    weights?.should(not(be(empty())), "$path.weights")
}

private fun OrthographicRaw.validate(fieldPath: String) {
    zfar.toDouble().should(be(greateThan(MIN_ORTHO_ZFAR)), "$fieldPath.zfar")
    znear.toDouble().should(be(atLeast(MIN_ORTHO_ZNEAR)), "$fieldPath.znear")
}

private fun PerspectiveRaw.validate(fieldPath: String) {
    aspectRatio?.toDouble()?.should(be(greateThan(MIN_ASPECT_RATIO)), "$fieldPath.aspectRatio")
    yfov.toDouble().should(be(greateThan(MIN_FOV)), "$fieldPath.yfov")
    zfar?.toDouble()?.should(be(greateThan(MIN_PERSPECTIVE_ZFAR)), "$fieldPath.zfar")
    znear.toDouble().should(be(greateThan(MIN_PERSPECTIVE_ZNEAR)), "$fieldPath.znear")
}

private fun CameraRaw.validate(index: Int) {
    val path = "cameras[$index]"
    orthographic?.validate("$path.orthographic")
    perspective?.validate("$path.perspective")
    type.should(be(oneOf(CAMERA_TYPES)), "$path.type")
}

private fun NodeRaw.validate(index: Int) {
    val path = "nodes[$index]"
    camera?.should(be(atLeast(MIN_REF_INDEX)), "$path.camera")
    children?.should(not(be(empty())), "$path.children")
    skin?.should(be(atLeast(MIN_REF_INDEX)), "$path.skin")
    matrix?.should(haveSize(MATRIX4_SIZE), "$path.matrix")
    mesh?.should(be(atLeast(MIN_REF_INDEX)), "$path.mesh")
    rotation?.should(haveSize(QUATERNION_SIZE), "$path.rotation")
    scale?.should(haveSize(VECTOR3_SIZE), "$path.scale")
    translation?.should(haveSize(VECTOR3_SIZE), "$path.translation")
    weights?.should(not(be(empty())), "$path.weights")
}

private fun SkinRaw.validate(index: Int) {
    val path = "skins[$index]"
    inverseBindMatrices?.should(be(atLeast(MIN_REF_INDEX)), "$path.inverseBindMatrices")
    skeleton?.should(be(atLeast(MIN_REF_INDEX)), "$path.skeleton")
    joints.should(not(be(empty())), "$path.joints")
            .forEachIndexed { jIndex, it -> it.should(be(atLeast(MIN_REF_INDEX)), "$path.joints[$jIndex]") }
}

private fun AnimationTargetRaw.validate(fieldPath: String) {
    node?.should(be(atLeast(MIN_REF_INDEX)), "$fieldPath.node")
    path.should(be(oneOf(TARGET_PATHS)), "$fieldPath.path")
}

private fun AnimationSamplerRaw.validate(fieldPath: String) {
    input.should(be(atLeast(MIN_REF_INDEX)), "$fieldPath.input")
    interpolation?.should(be(oneOf(INTERPOLATION_TYPES)), "$fieldPath.interpolation")
    output.should(be(atLeast(MIN_REF_INDEX)), "$fieldPath.output")
}

private fun ChannelRaw.validate(fieldPath: String) {
    sampler.should(be(atLeast(MIN_REF_INDEX)), "$fieldPath.sampler")
    target.validate("$fieldPath.target")
}

private fun AnimationRaw.validate(index: Int) {
    val path = "animations[$index]"
    channels.should(not(be(empty())), "$path.channels")
            .forEachIndexed { cIndex, it -> it.validate("$path.channels[$cIndex]") }
    samplers.should(not(be(empty())), "$path.samplers")
            .forEachIndexed { sIndex, it -> it.validate("$path.samplers[$sIndex]") }
}

private fun SceneRaw.validate(index: Int) {
    val path = "scenes[$index]"
    nodes?.should(not(be(empty())), "$path.nodes")
            ?.forEachIndexed { nIndex, it ->
                it.should(be(atLeast(MIN_REF_INDEX)), "$path.nodes[$nIndex]")
            }
}

private fun AssetRaw.validate() {
    version.should(be(equalTo(REQUIRED_VERSION)), "asset.version")
}
