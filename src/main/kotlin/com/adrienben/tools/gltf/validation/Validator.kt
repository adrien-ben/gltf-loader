package com.adrienben.tools.gltf.validation

import com.adrienben.tools.gltf.models.*
import com.adrienben.tools.gltf.validation.Matchers.anEmptyMap
import com.adrienben.tools.gltf.validation.Matchers.atLeast
import com.adrienben.tools.gltf.validation.Matchers.be
import com.adrienben.tools.gltf.validation.Matchers.empty
import com.adrienben.tools.gltf.validation.Matchers.equalTo
import com.adrienben.tools.gltf.validation.Matchers.greaterThan
import com.adrienben.tools.gltf.validation.Matchers.haveSize
import com.adrienben.tools.gltf.validation.Matchers.inRange
import com.adrienben.tools.gltf.validation.Matchers.not
import com.adrienben.tools.gltf.validation.Matchers.oneOf

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

private fun Int.shouldBeAValidRef(fieldName: String) = should(be(atLeast(MIN_REF_INDEX)), fieldName)
private fun <T : Map<*, *>> T.shouldNotBeEmpty(fieldName: String) = should(not(be(anEmptyMap())), fieldName)
private fun <T : Collection<*>> T.shouldNotBeEmpty(fieldName: String) = should(not(be(empty())), fieldName)
private fun <T : Collection<*>> T.shouldHaveSize(size: Int, fieldName: String) = should(haveSize(size), fieldName)
private fun <T> T.shouldBeOneOf(collection: Collection<T>, fieldName: String) = should(be(oneOf(collection)), fieldName)
private fun <T : Comparable<T>> T.shouldBeAtLeast(min: T, fieldName: String) = should(be(atLeast(min)), fieldName)
private fun <T : Comparable<T>> T.shouldBeGreaterThan(min: T, fieldName: String) = should(be(greaterThan(min)), fieldName)
private fun <T : Comparable<T>> T.shouldBeInRange(range: ClosedRange<T>, fieldName: String) = should(be(inRange(range)), fieldName)

/**
 * This class is responsible for validating the models loaded from the json.
 * At this point, is the json was successfully loaded we know that required
 * models is present. This class then validates models consistency.
 */
internal class Validator {

    /**
     * Validate the provided asset.
     *
     * @throws ValidationError if the asset is not valid.
     */
    fun validate(gltfRaw: GltfRaw): GltfRaw {
        gltfRaw.gltfAssetRaw.apply {
            asset.validate()
            buffers?.shouldNotBeEmpty("buffers")?.forEachIndexed { index, it -> it.validate(index) }
            bufferViews?.shouldNotBeEmpty("bufferViews")?.forEachIndexed { index, it -> it.validate(index) }
            accessors?.shouldNotBeEmpty("accessors")?.forEachIndexed { index, it -> it.validate(index) }
            samplers?.shouldNotBeEmpty("samplers")?.forEachIndexed { index, it -> it.validate(index) }
            images?.shouldNotBeEmpty("images")?.forEachIndexed { index, it -> it.validate(index) }
            textures?.shouldNotBeEmpty("textures")?.forEachIndexed { index, it -> it.validate(index) }
            materials?.shouldNotBeEmpty("materials")?.forEachIndexed { index, it -> it.validate(index) }
            meshes?.shouldNotBeEmpty("meshes")?.forEachIndexed { index, it -> it.validate(index) }
            cameras?.shouldNotBeEmpty("cameras")?.forEachIndexed { index, it -> it.validate(index) }
            nodes?.shouldNotBeEmpty("nodes")?.forEachIndexed { index, it -> it.validate(index) }
            skins?.shouldNotBeEmpty("skins")?.forEachIndexed { index, it -> it.validate(index) }
            animations?.shouldNotBeEmpty("animations")?.forEachIndexed { index, it -> it.validate(index) }
            scenes?.shouldNotBeEmpty("scenes")?.forEachIndexed { index, it -> it.validate(index) }
            scene?.shouldBeAValidRef("scene")
        }
        return gltfRaw
    }
}

private fun BufferRaw.validate(index: Int) {
    val path = "buffers[$index]"
    byteLength.shouldBeAtLeast(MIN_BUFFER_LENGTH, "$path.byteLength")
}

private fun BufferViewRaw.validate(index: Int) {
    val path = "bufferViews[$index]"
    buffer.shouldBeAValidRef("$path.buffer")
    byteOffset?.shouldBeAtLeast(MIN_BYTE_OFFSET, "$path.byteOffset")
    byteLength.shouldBeAtLeast(MIN_BUFFER_LENGTH, "$path.byteLength")
    byteStride?.shouldBeInRange(RANGE_BYTE_STRIDE, "$path.byteStride")
    target?.shouldBeOneOf(BUFFER_TARGETS, "$path.target")
}

private fun IndicesRaw.validate(fieldPath: String) {
    bufferView.shouldBeAValidRef("$fieldPath.bufferView")
    byteOffset?.shouldBeAtLeast(MIN_BYTE_OFFSET, "$fieldPath.byteOffset")
    componentType.shouldBeOneOf(INDICES_COMPONENT_TYPES, "$fieldPath.componentType")
}

private fun ValuesRaw.validate(fieldPath: String) {
    bufferView.shouldBeAValidRef("$fieldPath.bufferView")
    byteOffset?.shouldBeAtLeast(MIN_BYTE_OFFSET, "$fieldPath.byteOffset")
}

private fun SparseRaw.validate(fieldPath: String) {
    count.shouldBeAtLeast(SPARSE_MIN_COUNT, "$fieldPath.count")
    indices.validate("$fieldPath.indices")
    values.validate("$fieldPath.values")
}

private fun AccessorRaw.validate(index: Int) {
    val path = "accessors[$index]"
    bufferView?.shouldBeAValidRef("$path.bufferView")
    byteOffset?.shouldBeAtLeast(MIN_BYTE_OFFSET, "$path.byteOffset")
    componentType.shouldBeOneOf(ACCESSOR_COMPONENT_TYPES, "$path.componentType")
    count.shouldBeAtLeast(ACCESSOR_MIN_COUNT, "$path.count")
    type.shouldBeOneOf(ACCESSOR_TYPES, "$path.type")
    max?.size?.shouldBeOneOf(ACCESSOR_MIN_MAX_SIZES, "$path.max")
    min?.size?.shouldBeOneOf(ACCESSOR_MIN_MAX_SIZES, "$path.min")
    sparse?.validate("$path.sparse")
}

private fun SamplerRaw.validate(index: Int) {
    val path = "samplers[$index]"
    magFilter?.shouldBeOneOf(SAMPLER_MAG_FILTERS, "$path.magFilter")
    minFilter?.shouldBeOneOf(SAMPLER_MIN_FILTERS, "$path.minFilter")
    wrapS?.shouldBeOneOf(SAMPLER_WRAPS, "$path.wrapS")
    wrapT?.shouldBeOneOf(SAMPLER_WRAPS, "$path.wrapT")
}

private fun ImageRaw.validate(index: Int) {
    val path = "images[$index]"
    mimeType?.shouldBeOneOf(IMAGE_MIME_TYPES, "$path.mimeType")
    bufferView?.shouldBeAValidRef("$path.bufferView")
}

private fun TextureRaw.validate(index: Int) {
    val path = "textures[$index]"
    sampler?.shouldBeAValidRef("$path.sampler")
    source?.shouldBeAValidRef("$path.source")
}

private fun TextureInfoRaw.validate(textureFieldPath: String) {
    index.shouldBeAValidRef("$textureFieldPath.index")
    texCoord?.shouldBeAtLeast(MIN_TEXCOORDS, "$textureFieldPath.texCoord")
}

private fun NormalTextureInfoRaw.validate(normalFieldPath: String) {
    index.shouldBeAValidRef("$normalFieldPath.index")
    texCoord?.shouldBeAtLeast(MIN_TEXCOORDS, "$normalFieldPath.texCoord")
}

private fun OcclusionTextureInfoRaw.validate(occlusionFieldPath: String) {
    index.shouldBeAValidRef("$occlusionFieldPath.index")
    texCoord?.shouldBeAtLeast(MIN_TEXCOORDS, "$occlusionFieldPath.texCoord")
    strength?.toDouble()?.shouldBeInRange(OCCLUSION_TEXTURE_STRENGTH, "$occlusionFieldPath.strength")
}

private fun PbrMetallicRoughnessRaw.validate(fieldPath: String) {
    baseColorFactor?.shouldHaveSize(COLOR_SIZE, "$fieldPath.baseColorFactor")
    baseColorTexture?.validate("$fieldPath.baseColorTexture")
    metallicFactor?.toDouble()?.shouldBeInRange(METALLIC_FACTOR_RANGE, "$fieldPath.metallicFactor")
    roughnessFactor?.toDouble()?.shouldBeInRange(ROUGHNESS_FACTOR_RANGE, "$fieldPath.roughnessFactor")
    metallicRoughnessTexture?.validate("$fieldPath.metallicRoughnessTexture")
}

private fun MaterialRaw.validate(index: Int) {
    val path = "materials[$index]"
    pbrMetallicRoughness?.validate("$path.pbrMetallicRoughness")
    normalTexture?.validate("$path.normalTexture")
    occlusionTexture?.validate("$path.occlusionTexture")
    emissiveTexture?.validate("$path.emissiveTexture")
    emissiveFactor?.shouldHaveSize(EMISSIVE_FACTOR_SIZE, "$path.emissiveFactor")
    alphaMode?.shouldBeOneOf(MATERIAL_ALPHA_MODES, "$path.alphaMode")
    alphaCutoff?.toDouble()?.shouldBeAtLeast(MIN_ALPHA_CUTOFF, "$path.alphaCutoff")
}

private fun PrimitiveRaw.validate(fieldPath: String) {
    attributes.shouldNotBeEmpty("$fieldPath.attributes")
    indices?.shouldBeAValidRef("$fieldPath.indices")
    material?.shouldBeAValidRef("$fieldPath.material")
    mode?.shouldBeOneOf(PRIMITIVE_MODES, "$fieldPath.mode")
    targets?.shouldNotBeEmpty("$fieldPath.targets")
            ?.forEachIndexed { index, it -> it.shouldNotBeEmpty("$fieldPath.targets[$index]") }
}

private fun MeshRaw.validate(index: Int) {
    val path = "meshes[$index]"
    primitives.shouldNotBeEmpty("$path.primitives")
            .forEachIndexed { pIndex, it -> it.validate("$path.primitives[$pIndex]") }
    weights?.shouldNotBeEmpty("$path.weights")
}

private fun OrthographicRaw.validate(fieldPath: String) {
    zfar.toDouble().shouldBeGreaterThan(MIN_ORTHO_ZFAR, "$fieldPath.zfar")
    znear.toDouble().shouldBeAtLeast(MIN_ORTHO_ZNEAR, "$fieldPath.znear")
}

private fun PerspectiveRaw.validate(fieldPath: String) {
    aspectRatio?.toDouble()?.shouldBeGreaterThan(MIN_ASPECT_RATIO, "$fieldPath.aspectRatio")
    yfov.toDouble().shouldBeGreaterThan(MIN_FOV, "$fieldPath.yfov")
    zfar?.toDouble()?.shouldBeGreaterThan(MIN_PERSPECTIVE_ZFAR, "$fieldPath.zfar")
    znear.toDouble().shouldBeGreaterThan(MIN_PERSPECTIVE_ZNEAR, "$fieldPath.znear")
}

private fun CameraRaw.validate(index: Int) {
    val path = "cameras[$index]"
    orthographic?.validate("$path.orthographic")
    perspective?.validate("$path.perspective")
    type.shouldBeOneOf(CAMERA_TYPES, "$path.type")
}

private fun NodeRaw.validate(index: Int) {
    val path = "nodes[$index]"
    camera?.shouldBeAValidRef("$path.camera")
    children?.shouldNotBeEmpty("$path.children")
    skin?.shouldBeAValidRef("$path.skin")
    matrix?.shouldHaveSize(MATRIX4_SIZE, "$path.matrix")
    mesh?.shouldBeAValidRef("$path.mesh")
    rotation?.shouldHaveSize(QUATERNION_SIZE, "$path.rotation")
    scale?.shouldHaveSize(VECTOR3_SIZE, "$path.scale")
    translation?.shouldHaveSize(VECTOR3_SIZE, "$path.translation")
    weights?.shouldNotBeEmpty("$path.weights")
}

private fun SkinRaw.validate(index: Int) {
    val path = "skins[$index]"
    inverseBindMatrices?.shouldBeAValidRef("$path.inverseBindMatrices")
    skeleton?.shouldBeAValidRef("$path.skeleton")
    joints.shouldNotBeEmpty("$path.joints")
            .forEachIndexed { jIndex, it -> it.shouldBeAValidRef("$path.joints[$jIndex]") }
}

private fun AnimationTargetRaw.validate(fieldPath: String) {
    node?.shouldBeAValidRef("$fieldPath.node")
    path.shouldBeOneOf(TARGET_PATHS, "$fieldPath.path")
}

private fun AnimationSamplerRaw.validate(fieldPath: String) {
    input.shouldBeAValidRef("$fieldPath.input")
    interpolation?.shouldBeOneOf(INTERPOLATION_TYPES, "$fieldPath.interpolation")
    output.shouldBeAValidRef("$fieldPath.output")
}

private fun ChannelRaw.validate(fieldPath: String) {
    sampler.shouldBeAValidRef("$fieldPath.sampler")
    target.validate("$fieldPath.target")
}

private fun AnimationRaw.validate(index: Int) {
    val path = "animations[$index]"
    channels.shouldNotBeEmpty("$path.channels")
            .forEachIndexed { cIndex, it -> it.validate("$path.channels[$cIndex]") }
    samplers.shouldNotBeEmpty("$path.samplers")
            .forEachIndexed { sIndex, it -> it.validate("$path.samplers[$sIndex]") }
}

private fun SceneRaw.validate(index: Int) {
    val path = "scenes[$index]"
    nodes?.shouldNotBeEmpty("$path.nodes")
            ?.forEachIndexed { nIndex, it -> it.shouldBeAValidRef("$path.nodes[$nIndex]") }
}

private fun AssetRaw.validate() {
    version.should(be(equalTo(REQUIRED_VERSION)), "asset.version")
}
