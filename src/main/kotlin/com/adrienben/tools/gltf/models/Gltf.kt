package com.adrienben.tools.gltf.models

import com.adrienben.tools.gltf.Loader
import com.adrienben.tools.gltf.Mapper

/**
 * RGBA color.
 */
class GltfColor(val r: Float = 1f, val g: Float = 1f, val b: Float = 1f, val a: Float = 1f) {

    internal companion object Factory {

        /**
         * Generate a color from a list of [Number]s. The list must contain 3 or 4 elements. If it contains
         * only 3 elements, alpha will be set to 1f.
         */
        fun fromNumbers(color: List<Number>): GltfColor {
            if (color.size !in 3..4) throw IllegalArgumentException("color contains ${color.size} elements instead of 3 or 4")
            val alpha = if (color.size == 3) 1f else color[3].toFloat()
            return GltfColor(color[0].toFloat(), color[1].toFloat(), color[2].toFloat(), alpha)
        }
    }
}

/**
 * Pointer to byte buffer. [uri] can reference an external file, in that case
 * it will contain the path of the file relative the current .glsl file. It can
 * also reference embedded base64 models.
 */
class GltfBuffer(
        val index: Int,
        val uri: String? = null,
        val byteLength: Int,
        val data: ByteArray,
        val name: String? = null
)

/**
 * View of a buffer. It can represent the full buffer or just a part of it.
 */
class GltfBufferView(
        val index: Int,
        val buffer: GltfBuffer,
        val byteOffset: Int,
        val byteLength: Int,
        val byteStride: Int? = null,
        val target: GltfBufferTarget? = null,
        val name: String? = null
)

/**
 * Indices of the elements of a sparse buffer that must be replaced.
 */
class GltfIndices(
        val bufferView: GltfBufferView,
        val byteOffset: Int = 0,
        val componentType: GltfComponentType
)

/**
 * Replacement value for sparse buffer.
 */
class GltfValues(
        val bufferView: GltfBufferView,
        val byteOffset: Int = 0
)

/**
 * Sparse provide information on how to generate sparse models of a buffer.
 * [indices] contains the indices of the sparse elements and [values] the
 * values to use.
 */
class GltfSparse(
        val count: Int,
        val indices: GltfIndices,
        val values: GltfValues
)

/**
 * Accessor provides type information about the models contained in a
 * buffer view. If an accessor does not point to a buffer view then
 * the models must be generated from the sparse accessor.
 */
class GltfAccessor(
        val index: Int,
        val bufferView: GltfBufferView? = null,
        val byteOffset: Int = 0,
        val componentType: GltfComponentType,
        val normalized: Boolean = false,
        val count: Int,
        val type: GltfType,
        val max: List<Float>? = null,
        val min: List<Float>? = null,
        val sparse: GltfSparse? = null,
        val name: String? = null
)

/**
 * Texture sampler information. Contains info on filtering and wrapping
 * mode of the texture.
 */
class GltfSampler(
        val index: Int,
        val magFilter: GltfFilter? = null,
        val minFilter: GltfFilter? = null,
        val wrapS: GltfWrapMode = GltfWrapMode.REPEAT,
        val wrapT: GltfWrapMode = GltfWrapMode.REPEAT,
        val name: String? = null
)

/**
 * Image descriptor.
 *
 * [uri] can reference an external file, in that case it will contain the path of
 * the file relative the current .glsl file. [uri] can also contain a models uri, in
 * this case, the uri will be decoded and its content will be stored in [data].
 *
 * If [bufferView] is present use it instead of the uri.
 */
class GltfImage(
        val index: Int,
        val uri: String? = null,
        val data: ByteArray? = null,
        val mimeType: GltfMimeType? = null,
        val bufferView: GltfBufferView? = null,
        val name: String? = null
)

/**
 * Texture models.
 */
class GltfTexture(
        val index: Int,
        val sampler: GltfSampler,
        val source: GltfImage? = null,
        val name: String? = null
)

/**
 * Texture info.
 */
class GltfTextureInfo(
        val texture: GltfTexture,
        val texCoord: Int = 0
)

/**
 * Information about normal texture.
 */
class GltfNormalTextureInfo(
        val texture: GltfTexture,
        val texCoord: Int = 0,
        val scale: Float = 1f
)

/**
 * Information about occlusion texture.
 */
class GltfOcclusionTextureInfo(
        val texture: GltfTexture,
        val texCoord: Int = 0,
        val strength: Float = 1f
)

/**
 * PBR material models.
 */
class GltfPbrMetallicRoughness(
        val baseColorFactor: GltfColor = GltfColor(),
        val baseColorTexture: GltfTextureInfo? = null,
        val metallicFactor: Float = 1f,
        val roughnessFactor: Float = 1f,
        val metallicRoughnessTexture: GltfTextureInfo? = null
)

/**
 * Material defines the appearance of a primitive.
 */
class GltfMaterial(
        val index: Int,
        val pbrMetallicRoughness: GltfPbrMetallicRoughness = GltfPbrMetallicRoughness(),
        val normalTexture: GltfNormalTextureInfo? = null,
        val occlusionTexture: GltfOcclusionTextureInfo? = null,
        val emissiveTexture: GltfTextureInfo? = null,
        val emissiveFactor: GltfColor = GltfColor(0f, 0f, 0f, 1f),
        val alphaMode: GltfAlphaMode = GltfAlphaMode.OPAQUE,
        val alphaCutoff: Float = 0.5f,
        val doubleSided: Boolean = false,
        val name: String? = null
)

/**
 * Geometry to render.
 */
class GltfPrimitive(
        val attributes: Map<String, GltfAccessor>,
        val indices: GltfAccessor? = null,
        val material: GltfMaterial,
        val mode: GltfPrimitiveMode = GltfPrimitiveMode.TRIANGLES,
        val targets: List<Map<String, GltfAccessor>>? = null
)

/**
 * A set of primitives to render.
 */
class GltfMesh(
        val index: Int,
        val primitives: List<GltfPrimitive>,
        val weights: List<Float>? = null,
        val name: String? = null
)

/**
 * Orthographic projection camera.
 */
class GltfOrthographic(
        val xMag: Float,
        val yMag: Float,
        val zFar: Float,
        val zNear: Float
)

/**
 * Perspective projection cameras.
 */
class GltfPerspective(
        val aspectRatio: Float? = null,
        val yFov: Float,
        val zFar: Float? = null,
        val zNear: Float
)

/**
 * Camera.
 */
class GltfCamera(
        val index: Int,
        val orthographic: GltfOrthographic? = null,
        val perspective: GltfPerspective? = null,
        val type: GltfCameraType,
        val name: String? = null
)

/**
 * A node of a scene.
 */
class GltfNode(
        val index: Int,
        val camera: GltfCamera?,
        val children: List<GltfNode>?,
        skin: GltfSkin? = null,
        val matrix: GltfMat4,
        val mesh: GltfMesh?,
        val rotation: GltfQuaternion,
        val scale: GltfVec3,
        val translation: GltfVec3,
        val weights: List<Float>?,
        val name: String?
) {
    var skin = skin
        internal set
}

/**
 * Skin.
 */
class GltfSkin(
        val index: Int,
        val inverseBindMatrices: GltfAccessor? = null,
        val skeleton: GltfNode? = null,
        val joints: List<GltfNode>,
        val name: String? = null
)

/**
 * Node/TRS target of an animation.
 */
class GltfAnimationTarget(
        val node: GltfNode? = null,
        val path: GltfAnimationTargetPath
)

/**
 * Animation sampler.
 */
class GltfAnimationSampler(
        val input: GltfAccessor,
        val interpolation: GltfInterpolationType = GltfInterpolationType.LINEAR,
        val output: GltfAccessor
)

/**
 * Animation channel.
 */
class GltfChannel(
        val sampler: GltfAnimationSampler,
        val target: GltfAnimationTarget
)

/**
 * Animation.
 */
class GltfAnimation(
        val channels: List<GltfChannel>,
        val samplers: List<GltfAnimationSampler>,
        val name: String? = null
)

/**
 * A scene of an asset.
 */
class GltfScene(
        val index: Int,
        val nodes: List<GltfNode>?,
        val name: String?
)

/**
 * Asset information.
 */
class GltfMetadata(
        val copyright: String?,
        val generator: String?,
        val version: String,
        val minVersion: String?
)

/**
 * Gltf asset
 */
class GltfAsset(
        val asset: GltfMetadata,
        val extensionsUsed: List<String>? = null,
        val extensionsRequired: List<String>? = null,
        val buffers: List<GltfBuffer>,
        val bufferViews: List<GltfBufferView>,
        val accessors: List<GltfAccessor>,
        val samplers: List<GltfSampler>,
        val images: List<GltfImage>,
        val textures: List<GltfTexture>,
        val materials: List<GltfMaterial>,
        val meshes: List<GltfMesh>,
        val cameras: List<GltfCamera>,
        val nodes: List<GltfNode>,
        val skin: List<GltfSkin>?,
        val animations: List<GltfAnimation>,
        val scenes: List<GltfScene>,
        val scene: GltfScene?
) {

    /**
     * Companion factory.
     */
    companion object Factory {

        /**
         * Load a gltf asset model from the gltf json file [path]
         */
        fun fromFile(path: String): GltfAsset? {
            val asset = Loader.fromExtension(path.substringAfterLast('.')).load(path) ?: return null
            return Mapper(asset).map()
        }
    }
}
