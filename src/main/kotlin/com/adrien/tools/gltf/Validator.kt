package com.adrien.tools.gltf

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

private val BUFFER_TARGETS = arrayOf(34962, 34963)
private val INDICES_COMPONENT_TYPES = arrayOf(5121, 5123, 5125)
private val ACCESSOR_COMPONENT_TYPES = arrayOf(5120, 5121, 5122, 5123, 5125, 5126)
private val ACCESSOR_TYPES = arrayOf("SCALAR", "VEC2", "VEC3", "VEC4", "MAT2", "MAT3", "MAT4")
private val ACCESSOR_MIN_MAX_SIZES = arrayOf(1, 2, 3, 4, 9, 16)
private val SAMPLER_MIN_FILTERS = arrayOf(9728, 9729, 9984, 9985, 9986, 9987)
private val SAMPLER_MAG_FILTERS = arrayOf(9728, 9729)
private val SAMPLER_WRAPS = arrayOf(10497, 33648, 33071)
private val IMAGE_MIME_TYPES = arrayOf("image/jpeg", "image/png")
private val MATERIAL_ALPHA_MODES = arrayOf("OPAQUE", "MASK", "BLEND")
private val PRIMITIVE_MODES = arrayOf(0, 1, 2, 3, 4, 5, 6)
private val CAMERA_TYPES = arrayOf("perspective", "orthographic")
private val TARGET_PATHS = arrayOf("translation", "rotation", "scale", "weights")
private val INTERPOLATION_TYPES = arrayOf("LINEAR", "STEP", "CUBICSPLINE")

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
            buffers?.isNotEmpty("Buffers should not be empty")?.forEach(BufferRaw::validate)
            bufferViews?.isNotEmpty("Buffer view should ne be empty")?.forEach(BufferViewRaw::validate)
            accessors?.isNotEmpty("Accessors should not be empty")?.forEach(AccessorRaw::validate)
            samplers?.isNotEmpty("Samplers should not be empty")?.forEach(SamplerRaw::validate)
            images?.isNotEmpty("Images should not be empty")?.forEach(ImageRaw::validate)
            textures?.isNotEmpty("Textures should not be empty")?.forEach(TextureRaw::validate)
            materials?.isNotEmpty("Materials should not be empty")?.forEach(MaterialRaw::validate)
            meshes?.isNotEmpty("Meshes should not be empty")?.forEach(MeshRaw::validate)
            cameras?.isNotEmpty("Cameras should not be empty")?.forEach(CameraRaw::validate)
            nodes?.isNotEmpty("Nodes should not be empty")?.forEach(NodeRaw::validate)
            skins?.isNotEmpty("Skins should not be empty")?.forEach(SkinRaw::validate)
            animations?.isNotEmpty("Animations should not be empty")?.forEach(AnimationRaw::validate)
            scenes?.isNotEmpty("Scenes should not be empty")?.forEach(SceneRaw::validate)
        }
        return gltfRaw
    }
}

private fun BufferRaw.validate() {
    byteLength.isAtLeast(MIN_BUFFER_LENGTH, "Buffer's byte length should be >= $MIN_BUFFER_LENGTH")
}

private fun BufferViewRaw.validate() {
    buffer.isAtLeast(MIN_REF_INDEX, "Buffer view's buffer index should be at least $MIN_REF_INDEX")
    byteOffset?.isAtLeast(MIN_BYTE_OFFSET, "Buffer view's buffer byte offset should be at least $MIN_BYTE_OFFSET")
    byteLength.isAtLeast(MIN_BUFFER_LENGTH, "Buffer view's byte length should be >= $MIN_BUFFER_LENGTH")
    byteStride?.isInRange(RANGE_BYTE_STRIDE, "Buffer view's byte stride should be in range $RANGE_BYTE_STRIDE")
    target?.isOneOf(BUFFER_TARGETS, "Buffer view's target should be one of $BUFFER_TARGETS")
}

private fun IndicesRaw.validate() {
    bufferView.isAtLeast(MIN_REF_INDEX, "Indices' buffer view index should be at least $MIN_REF_INDEX")
    byteOffset?.isAtLeast(MIN_BYTE_OFFSET, "Indices' byte offset should be at least $MIN_BYTE_OFFSET")
    componentType.isOneOf(INDICES_COMPONENT_TYPES, "Indices' component type should be one of $INDICES_COMPONENT_TYPES")
}

private fun ValuesRaw.validate() {
    bufferView.isAtLeast(MIN_REF_INDEX, "Values' buffer view index should be at least $MIN_REF_INDEX")
    byteOffset?.isAtLeast(MIN_BYTE_OFFSET, "Values' byte offset should be at least $MIN_BYTE_OFFSET")
}

private fun SparseRaw.validate() {
    count.isAtLeast(SPARSE_MIN_COUNT, "Sparse's count should be at least $SPARSE_MIN_COUNT")
    indices.validate()
    values.validate()
}

private fun AccessorRaw.validate() {
    bufferView?.isAtLeast(MIN_REF_INDEX, "Accessor's buffer view index should be at least $MIN_REF_INDEX")
    byteOffset?.isAtLeast(MIN_BYTE_OFFSET, "Accessor's byte offset should be at least $MIN_BYTE_OFFSET")
    componentType.isOneOf(ACCESSOR_COMPONENT_TYPES, "Accessor's component type should be one of $ACCESSOR_COMPONENT_TYPES")
    count.isAtLeast(ACCESSOR_MIN_COUNT, "Accessor's count should be at least $ACCESSOR_MIN_COUNT")
    type.isOneOf(ACCESSOR_TYPES, "Accessor's type should be one of $ACCESSOR_TYPES")
    max?.size?.isOneOf(ACCESSOR_MIN_MAX_SIZES, "Accessor's max array's length should be one of $ACCESSOR_MIN_MAX_SIZES")
    min?.size?.isOneOf(ACCESSOR_MIN_MAX_SIZES, "Accessor's min array's length should be one of $ACCESSOR_MIN_MAX_SIZES")
    sparse?.validate()
}

private fun SamplerRaw.validate() {
    magFilter?.isOneOf(SAMPLER_MAG_FILTERS, "Sampler's mag filter should be one of $SAMPLER_MAG_FILTERS")
    minFilter?.isOneOf(SAMPLER_MIN_FILTERS, "Sampler's min filter should be one of $SAMPLER_MIN_FILTERS")
    wrapS?.isOneOf(SAMPLER_WRAPS, "Sampler's s wrap should be one of $SAMPLER_WRAPS")
    wrapT?.isOneOf(SAMPLER_WRAPS, "Sampler's t wrap should be one of $SAMPLER_WRAPS")
}

private fun ImageRaw.validate() {
    mimeType?.isOneOf(IMAGE_MIME_TYPES, "Image's mime type should be one of $IMAGE_MIME_TYPES")
    bufferView?.isAtLeast(MIN_REF_INDEX, "Image's buffer view index should be at least $MIN_REF_INDEX")
}

private fun TextureRaw.validate() {
    sampler?.isAtLeast(MIN_REF_INDEX, "Texture's sampler index should be at least $MIN_REF_INDEX")
    source?.isAtLeast(MIN_REF_INDEX, "Texture's source index should be at least $MIN_REF_INDEX")
}

private fun TextureInfoRaw.validate() {
    index.isAtLeast(MIN_REF_INDEX, "Texture info's texture index should be at least $MIN_REF_INDEX")
    texCoord?.isAtLeast(MIN_TEXCOORDS, "Texture info's texture coordinate should be at least $MIN_TEXCOORDS")
}

private fun NormalTextureInfoRaw.validate() {
    index.isAtLeast(MIN_REF_INDEX, "Normal texture info's texture index should be at least $MIN_REF_INDEX")
    texCoord?.isAtLeast(MIN_TEXCOORDS, "Normal texture info's texture coordinate should be at least $MIN_TEXCOORDS")
}

private fun OcclusionTextureInfoRaw.validate() {
    index.isAtLeast(MIN_REF_INDEX, "Occlusion texture info's texture index should be at least $MIN_REF_INDEX")
    texCoord?.isAtLeast(MIN_TEXCOORDS, "Occlusion texture info's texture coordinate should be at least $MIN_TEXCOORDS")
    strength?.isInRange(OCCLUSION_TEXTURE_STRENGTH, "Occlusion texture info's strength should be in range $OCCLUSION_TEXTURE_STRENGTH")
}

private fun PbrMetallicRoughnessRaw.validate() {
    baseColorFactor?.hasSize(COLOR_SIZE, "PbrMetallicRoughness's base color should be of size $COLOR_SIZE")
    metallicFactor?.isInRange(METALLIC_FACTOR_RANGE, "PbrMetallicRoughness's metallic factor should be in range $METALLIC_FACTOR_RANGE")
    roughnessFactor?.isInRange(ROUGHNESS_FACTOR_RANGE, "PbrMetallicRoughness's roughness factor should be in range $ROUGHNESS_FACTOR_RANGE")
}

private fun MaterialRaw.validate() {
    pbrMetallicRoughness?.validate()
    normalTexture?.validate()
    occlusionTexture?.validate()
    emissiveTexture?.validate()
    emissiveFactor?.hasSize(EMISSIVE_FACTOR_SIZE, "Material's emissive factor should be of size $EMISSIVE_FACTOR_SIZE")
    alphaMode?.isOneOf(MATERIAL_ALPHA_MODES, "Material's alpha mode should be one of $MATERIAL_ALPHA_MODES")
    alphaCutoff?.isAtLeast(MIN_ALPHA_CUTOFF, "Material's alpha cutoff should be at least $MIN_ALPHA_CUTOFF")
}

private fun PrimitiveRaw.validate() {
    attributes.isNotEmpty("Primitive's primitives should not be empty")
    indices?.isAtLeast(MIN_REF_INDEX, "Primitive's indices accessor index should be at least $MIN_REF_INDEX")
    material?.isAtLeast(MIN_REF_INDEX, "Primitive's material index should be at least $MIN_REF_INDEX")
    mode?.isOneOf(PRIMITIVE_MODES, "Primitive's mode should be one of $PRIMITIVE_MODES")
    targets?.isNotEmpty("Primitive's morph targets should not be empty")?.forEach { it.isNotEmpty("Morph target not be empty") }
}

private fun MeshRaw.validate() {
    primitives.isNotEmpty("Mesh's primitives should not be empty").forEach(PrimitiveRaw::validate)
    weights?.isNotEmpty("Mesh's weights should not be empty")
}

private fun OrthographicRaw.validate() {
    zfar.isGreaterThan(MIN_ORTHO_ZFAR, "Orthographic's zfar should be greater than $MIN_ORTHO_ZFAR")
    znear.isAtLeast(MIN_ORTHO_ZNEAR, "Orthographic's znear should be at least $MIN_ORTHO_ZNEAR")
}

private fun PerspectiveRaw.validate() {
    aspectRatio?.isGreaterThan(MIN_ASPECT_RATIO, "Perspective's aspect ratio should be greater than $MIN_ASPECT_RATIO")
    yfov.isGreaterThan(MIN_FOV, "Perspective's fov should be greater than $MIN_FOV")
    zfar?.isGreaterThan(MIN_PERSPECTIVE_ZFAR, "Perspective's zfar should be greater than $MIN_PERSPECTIVE_ZFAR")
    znear.isGreaterThan(MIN_PERSPECTIVE_ZNEAR, "Perspective's znear should be greater than $MIN_PERSPECTIVE_ZNEAR")
}

private fun CameraRaw.validate() {
    orthographic?.validate()
    perspective?.validate()
    type.isOneOf(CAMERA_TYPES, "Camera's type should be one of $CAMERA_TYPES")
}

private fun NodeRaw.validate() {
    camera?.isAtLeast(MIN_REF_INDEX, "Node's camera index should be at least $MIN_REF_INDEX")
    children?.isNotEmpty("Node's children should not be empty")
    skin?.isAtLeast(MIN_REF_INDEX, "Node's skin index should be at least $MIN_REF_INDEX")
    matrix?.hasSize(MATRIX4_SIZE, "Node's matrix should contain $MATRIX4_SIZE elements")
    mesh?.isAtLeast(MIN_REF_INDEX, "Node's mesh index should be at least $MIN_REF_INDEX")
    rotation?.hasSize(QUATERNION_SIZE, "Node's rotation should contain $QUATERNION_SIZE")
    scale?.hasSize(VECTOR3_SIZE, "Node's scale should contains $VECTOR3_SIZE elements")
    translation?.hasSize(VECTOR3_SIZE, "Node's translation should contain $VECTOR3_SIZE elements")
    weights?.isNotEmpty("Node's weights should not be empty")
}

private fun SkinRaw.validate() {
    inverseBindMatrices?.isAtLeast(MIN_REF_INDEX, "Skin's inverse bind matrices index should be at least $MIN_REF_INDEX")
    skeleton?.isAtLeast(MIN_REF_INDEX, "Skin's skeleton index shoulde be at least $MIN_REF_INDEX")
    joints.isNotEmpty("Skin's joints should not be empty")
            .forEach { it.isAtLeast(MIN_REF_INDEX, "Skin's joints indices should all be at least $MIN_REF_INDEX") }
}

private fun AnimationTargetRaw.validate() {
    node?.isAtLeast(MIN_REF_INDEX, "Target's node index should be at least $MIN_REF_INDEX")
    path.isOneOf(TARGET_PATHS, "Target's path should be one of $TARGET_PATHS")
}

private fun AnimationSamplerRaw.validate() {
    input.isAtLeast(MIN_REF_INDEX, "Animation sampler's input index should be at least $MIN_REF_INDEX")
    interpolation?.isOneOf(INTERPOLATION_TYPES, "Animation sampler's interpolation should be one of $INTERPOLATION_TYPES")
    output.isAtLeast(MIN_REF_INDEX, "Animation sampler's output should be at least $MIN_REF_INDEX")
}

private fun ChannelRaw.validate() {
    sampler.isAtLeast(MIN_REF_INDEX, "Channel's sampler index should be at least $MIN_REF_INDEX")
    target.validate()
}

private fun AnimationRaw.validate() {
    channels.isNotEmpty("Animation's channels should not be empty").forEach(ChannelRaw::validate)
    samplers.isNotEmpty("Animation's samplers should not be empty").forEach(AnimationSamplerRaw::validate)
}

private fun SceneRaw.validate() {
    nodes?.isNotEmpty("Scene's nodes should not be empty")
            ?.forEach { it.isAtLeast(MIN_REF_INDEX, "Scene's nodes indices should all be at least $MIN_REF_INDEX") }
}

private fun AssetRaw.validate() {
    version.isEqualTo(REQUIRED_VERSION, "File version should be $REQUIRED_VERSION")
}

private fun <T> T.isEqualTo(value: T, message: String) = this.apply { require(this == value) { message } }

private fun <T> Comparable<T>.isAtLeast(min: T, message: String) = this.apply { require(this >= min) { message } }

private fun <T> Comparable<T>.isGreaterThan(min: T, message: String) = this.apply { require(this > min) { message } }

private fun <T : Comparable<T>> T.isInRange(range: ClosedRange<T>, message: String) = this.apply { require(this in range) { message } }

private fun <T> T.isOneOf(array: Array<T>, message: String) = this.apply { require(array.contains(this)) { message } }

private fun Collection<*>.hasSize(size: Int, message: String) = this.apply { require(this.size == size) { message } }

private fun <T> Collection<T>.isNotEmpty(message: String) = this.apply { require(this.isNotEmpty()) { message } }

private fun <K, V> Map<K, V>.isNotEmpty(message: String) = this.apply { require(this.isNotEmpty()) { message } }
