package com.adrien.tools.gltf

/**
 * Component types with their byte size and their original constant value.
 */
enum class ComponentType(val byteSize: Int, val code: Int) {
    BYTE(1, 5120),
    UNSIGNED_BYTE(1, 5121),
    SHORT(2, 5122),
    UNSIGNED_SHORT(2, 5123),
    UNSIGNED_INT(4, 5125),
    FLOAT(4, 5126)
}

/**
 * Element types with their number of component and their original constant value.
 */
enum class Type(val componentCount: Int, val code: String) {
    SCALAR(1, "SCALAR"),
    VEC2(2, "VEC2"),
    VEC3(3, "VEC3"),
    VEC4(4, "VEC4"),
    MAT2(4, "MAT2"),
    MAT3(9, "MAT3"),
    MAT4(16, "MAT4")
}

/**
 * Alpha rendering mode.
 */
enum class AlphaMode(val code: String) {
    OPAQUE("OPAQUE"),
    MASK("MASK"),
    BLEND("BLEND")
}

/**
 * Texture filtering mode. Mipmap modes should only be used as magnification
 * filter.
 */
enum class Filter(val code: Int) {
    NEAREST(9728),
    LINEAR(9729),
    NEAREST_MIPMAP_NEAREST(9984),
    LINEAR_MIPMAP_NEAREST(9985),
    NEAREST_MIPMAP_LINEAR(9986),
    LINEAR_MIPMAP_LINEAR(9987)
}

/**
 * Texture wrap mode.
 */
enum class WrapMode(val code: Int) {
    CLAMP_TO_EDGE(33071),
    MIRRORED_REPEAT(33648),
    REPEAT(10497)
}

/**
 * GPU buffer binding target.
 */
enum class BufferTarget(val code: Int) {
    ARRAY_BUFFER(34962),
    ELEMENT_ARRAY_BUFFER(34963)
}

/**
 * The type of primitive to render.
 */
enum class PrimitiveMode(val code: Int) {
    POINTS(0),
    LINES(1),
    LINE_LOOP(2),
    LINE_STRIP(3),
    TRIANGLES(4),
    TRIANGLE_STRIP(5),
    TRIANGLE_FAN(6)
}

/**
 * Mime types
 */
enum class MimeType(val value: String) {
    JPEG("image/jpeg"),
    PNG("image/png")
}

/**
 * 3-float vector.
 */
class Vec3f(val x: Float = 0f, val y: Float = 0f, val z: Float = 0f)

/**
 * RGBA color.
 */
class Color(val r: Float = 1f, val g: Float = 1f, val b: Float = 1f, val a: Float = 1f)

/**
 * Pointer to byte buffer. [uri] can reference an external file, in that case
 * it will contain the path of the file relative the current .glsl file. It can
 * also reference embedded base64 data.
 */
class Buffer(
        val uri: String? = null,
        val byteLength: Int,
        val data: ByteArray,
        val name: String? = null
)

/**
 * View of a buffer. It can represent the full buffer or just a part of it.
 */
class BufferView(
        val buffer: Buffer,
        val byteOffset: Int = 0,
        val byteLength: Int,
        val byteStride: Int? = null,
        val target: BufferTarget? = null,
        val name: String? = null
)

/**
 * Indices of the elements of a sparse buffer that must be replaced.
 */
class Indices(
        val bufferView: BufferView,
        val byteOffset: Int = 0,
        val componentType: ComponentType
)

/**
 * Replacement value for sparse buffer.
 */
class Values(
        val bufferView: BufferView,
        val byteOffset: Int = 0
)

/**
 * Sparse provide information on how to generate sparse data of a buffer.
 * [indices] contains the indices of the sparse elements and [values] the
 * values to use.
 */
class Sparse(
        val count: Int,
        val indices: Indices,
        val values: Values
)

/**
 * Accessor provides type information about the data contained in a
 * buffer view. If an accessor does not point to a buffer view then
 * the data must be generated from the sparse accessor.
 */
class Accessor(
        val bufferView: BufferView? = null,
        val byteOffset: Int = 0,
        val componentType: ComponentType,
        val normalized: Boolean = false,
        val count: Int,
        val type: Type,
        val max: List<Float>? = null,
        val min: List<Float>? = null,
        val sparse: Sparse? = null,
        val name: String? = null
)

/**
 * Texture sampler information. Contains info on filtering and wrapping
 * mode of the texture.
 */
class Sampler(
        val magFilter: Filter? = null,
        val minFilter: Filter? = null,
        val wrapS: WrapMode = WrapMode.REPEAT,
        val wrapT: WrapMode = WrapMode.REPEAT,
        val name: String? = null
)

/**
 * Image descriptor. [uri] can reference an external file, in that case
 * it will contain the path of the file relative the current .glsl file.
 * If [bufferView] is present use it instead of the uri.
 */
class Image(
        val uri: String? = null,
        val mimeType: MimeType? = null,
        val bufferView: BufferView? = null,
        val name: String? = null
)

/**
 * Texture data.
 */
class Texture(
        val sampler: Sampler,
        val source: Image? = null,
        val name: String? = null
)

/**
 * Texture info.
 */
class TextureInfo(
        val texture: Texture,
        val texCoord: Int = 0
)

/**
 * Information about normal texture.
 */
class NormalTextureInfo(
        val texture: Texture,
        val texCoord: Int = 0,
        val scale: Float = 1f
)

/**
 * Information about occlusion texture.
 */
class OcclusionTextureInfo(
        val texture: Texture,
        val texCoord: Int = 0,
        val strength: Float = 1f
)

/**
 * PBR material data.
 */
class PbrMetallicRoughness(
        val baseColorFactor: Color = Color(),
        val baseColorTexture: TextureInfo? = null,
        val metallicFactor: Float = 1f,
        val roughnessFactor: Float = 1f,
        val metallicRoughnessTexture: TextureInfo? = null
)

/**
 * Material defines the appearance of a primitive.
 */
class Material(
        val pbrMetallicRoughness: PbrMetallicRoughness = PbrMetallicRoughness(),
        val normalTexture: NormalTextureInfo? = null,
        val occlusionTexture: OcclusionTextureInfo? = null,
        val emissiveTexture: TextureInfo? = null,
        val emissiveFactor: Color = Color(0f, 0f, 0f, 1f),
        val alphaMode: AlphaMode = AlphaMode.OPAQUE,
        val alphaCutoff: Float = 0.5f,
        val doubleSided: Boolean = false,
        val name: String? = null
)

/**
 * Geometry to render.
 */
class Primitive(
        val attributes: Map<String, Accessor>,
        val indices: Accessor? = null,
        val material: Material,
        val mode: PrimitiveMode = PrimitiveMode.TRIANGLES,
        val targets: List<Map<String, Int>>? = null
)

/**
 * A set of primitives to render.
 */
class Mesh(
        val primitives: List<Primitive>,
        val weights: List<Float>? = null,
        val name: String? = null
)

class GltfAsset(
        val buffers: List<Buffer>,
        val bufferViews: List<BufferView>,
        val accessors: List<Accessor>,
        val samplers: List<Sampler>,
        val images: List<Image>,
        val textures: List<Texture>,
        val materials: List<Material>,
        val meshes: List<Mesh>
) {
    companion object Factory {

        /**
         * Load a gltf asset model from the gltf json file [path]
         */
        fun fromGltfFile(path: String): GltfAsset? {
            val gltfModel = GltfRaw.fromGltfFile(path) ?: return null
            return GltfMapper().map(gltfModel)
        }

        /**
         * Load a gltf asset from the glb file [path]
         */
        fun fromGlbFile(path: String): GltfAsset? {
            throw NotImplementedError("Loading .glb files is not yet supported")
        }

    }
}