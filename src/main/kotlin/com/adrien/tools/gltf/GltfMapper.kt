package com.adrien.tools.gltf

import java.util.*

/**
 * String extensions to decode a data URI if it matches the data uri pattern.
 *
 * It returns a [ByteArray] or null if the string does not match the required pattern.
 */
private fun String.decodeDataUri(): ByteArray? {
    val base64 = DATA_URI_REGEX.find(this)?.groupValues?.get(1) ?: return null
    return Base64.getDecoder().decode(base64)
}

/**
 * [GltfAsset] mapper.
 */
internal class GltfMapper {

    private lateinit var buffers: List<GltfBuffer>
    private lateinit var bufferViews: List<GltfBufferView>
    private lateinit var accessors: List<GltfAccessor>
    private lateinit var samplers: List<GltfSampler>
    private lateinit var images: List<GltfImage>
    private lateinit var textures: List<GltfTexture>
    private lateinit var materials: List<GltfMaterial>
    private lateinit var meshes: List<GltfMesh>
    private lateinit var cameras: List<GltfCamera>
    private lateinit var nodes: List<GltfNode>
    private lateinit var skins: List<GltfSkin>

    fun map(input: GltfRaw): GltfAsset {

        val asset = input.gltfAssetRaw

        buffers = asset.buffers?.mapIndexed { index, buffer -> buffer.map(index, input) } ?: emptyList()
        bufferViews = asset.bufferViews?.mapIndexed { index, view -> view.map(index) } ?: emptyList()
        accessors = asset.accessors?.mapIndexed { index, accessor -> accessor.map(index) } ?: emptyList()
        samplers = asset.samplers?.mapIndexed { index, sampler -> sampler.map(index) } ?: emptyList()
        images = asset.images?.mapIndexed { index, image -> image.map(index) } ?: emptyList()
        textures = asset.textures?.mapIndexed { index, texture -> texture.map(index) } ?: emptyList()
        materials = asset.materials?.mapIndexed { index, material -> material.map(index) } ?: emptyList()
        meshes = asset.meshes?.mapIndexed { index, mesh -> mesh.map(index) } ?: emptyList()
        cameras = asset.cameras?.mapIndexed { index, camera -> camera.map(index) } ?: emptyList()

        // map nodes recursively, skin are omitted
        nodes = Array<GltfNode?>(asset.nodes?.size ?: 0) { null }.let {
            asset.nodes?.forEachIndexed { index, nodeRaw -> nodeRaw.map(index, asset, it) }
            it.requireNoNulls().asList()
        }

        // map skins then update nodes with loaded skins
        skins = asset.skins?.mapIndexed { index, skin -> skin.map(index) } ?: emptyList()
        asset.nodes?.forEachIndexed { index, nodeRaw -> nodes[index].skin = nodeRaw.skin?.let { skins[it] } }

        val animations = asset.animations?.map { it.map() } ?: emptyList()
        val scenes = asset.scenes?.mapIndexed { index, scene -> scene.map(index) } ?: emptyList()

        return GltfAsset(
                asset.asset.map(),
                asset.extensionsUsed?.toList(),
                asset.extensionsRequired?.toList(),
                buffers, bufferViews, accessors,
                samplers, images, textures,
                materials, meshes,
                cameras,
                nodes, skins, animations,
                scenes, asset.scene?.let(scenes::get))
    }

    private fun AssetRaw.map() = GltfMetadata(
            copyright, generator, version, minVersion
    )

    private fun BufferRaw.map(index: Int, gltfRaw: GltfRaw): GltfBuffer {
        val data = uri?.decodeDataUri()
                ?: gltfRaw.dataByURI[uri]
                ?: throw IllegalArgumentException("Buffer data is not embedded and does not reference a .bin file that could be found")

        return GltfBuffer(index, uri, byteLength, data, name)
    }

    private fun BufferViewRaw.map(index: Int) = GltfBufferView(
            index,
            buffers[buffer],
            byteOffset,
            byteLength,
            byteStride,
            target?.let(GltfBufferTarget.Factory::fromCode),
            name
    )

    private fun IndicesRaw.map() = GltfIndices(
            bufferViews[bufferView],
            byteOffset,
            GltfComponentType.fromCode(componentType)
    )

    private fun ValuesRaw.map() = GltfValues(bufferViews[bufferView], byteOffset)

    private fun SparseRaw.map() = GltfSparse(count, indices.map(), values.map())

    private fun AccessorRaw.map(index: Int) = GltfAccessor(
            index,
            bufferView?.let(bufferViews::get),
            byteOffset,
            GltfComponentType.fromCode(componentType),
            normalized,
            count,
            GltfType.fromCode(type),
            max?.map(Number::toFloat),
            min?.map(Number::toFloat),
            sparse?.map(),
            name
    )

    private fun SamplerRaw.map(index: Int) = GltfSampler(
            index,
            magFilter?.let(GltfFilter.Factory::fromCode),
            minFilter?.let(GltfFilter.Factory::fromCode),
            GltfWrapMode.fromCode(wrapS),
            GltfWrapMode.fromCode(wrapT),
            name
    )

    private fun ImageRaw.map(index: Int) = GltfImage(
            index,
            uri,
            uri?.decodeDataUri(),
            mimeType?.let(GltfMimeType.Factory::fromCode),
            bufferView?.let(bufferViews::get),
            name
    )

    private fun TextureRaw.map(index: Int) = GltfTexture(
            index,
            sampler?.let(samplers::get) ?: GltfSampler(-1),
            source?.let(images::get),
            name
    )

    private fun TextureInfoRaw.map() = GltfTextureInfo(textures[index], texCoord)

    private fun NormalTextureInfoRaw.map() = GltfNormalTextureInfo(
            textures[index], texCoord, scale.toFloat()
    )

    private fun OcclusionTextureInfoRaw.map() = GltfOcclusionTextureInfo(
            textures[index], texCoord, strength.toFloat()
    )

    private fun PbrMetallicRoughnessRaw.map() = GltfPbrMetallicRoughness(
            GltfColor.fromNumbers(baseColorFactor),
            baseColorTexture?.map(),
            metallicFactor.toFloat(),
            roughnessFactor.toFloat(),
            metallicRoughnessTexture?.map()
    )

    private fun MaterialRaw.map(index: Int) = GltfMaterial(
            index,
            pbrMetallicRoughness.map(),
            normalTexture?.map(),
            occlusionTexture?.map(),
            emissiveTexture?.map(),
            GltfColor.fromNumbers(emissiveFactor),
            GltfAlphaMode.fromCode(alphaMode),
            alphaCutoff.toFloat(),
            doubleSided,
            name
    )

    private fun PrimitiveRaw.map() = GltfPrimitive(
            attributes.mapValues { (_, accessorId) -> accessors[accessorId] },
            indices?.let(accessors::get),
            material?.let(materials::get) ?: GltfMaterial(-1),
            GltfPrimitiveMode.fromCode(mode),
            targets?.map { it.mapValues { (_, accessorId) -> accessors[accessorId] } }
    )

    private fun MeshRaw.map(index: Int) = GltfMesh(
            index,
            primitives.map { it.map() },
            weights?.map(Number::toFloat),
            name
    )

    private fun OrthographicRaw.map() = GltfOrthographic(
            xmag.toFloat(), ymag.toFloat(), zfar.toFloat(), znear.toFloat()
    )

    private fun PerspectiveRaw.map() = GltfPerspective(
            aspectRatio?.toFloat(), yfov.toFloat(), zfar?.toFloat(), znear.toFloat()
    )

    private fun CameraRaw.map(index: Int) = GltfCamera(
            index, orthographic?.map(), perspective?.map(), GltfCameraType.fromCode(type), name
    )

    /**
     * Map a node
     *
     * If the node has unmapped children, it maps them first
     */
    private fun NodeRaw.map(index: Int, assetRaw: GltfAssetRaw, nodes: Array<GltfNode?>) {
        if (nodes[index] != null) return

        val shouldMapChildren = children?.any { nodes[it] == null } ?: false
        if (shouldMapChildren) {
            children?.mapNotNull { Pair(it, assetRaw.nodes?.get(it) ?: return@mapNotNull null) }
                    ?.forEach { (index, child) -> child.map(index, assetRaw, nodes) }
        }

        if (nodes[index] == null) nodes[index] = this.map(index, nodes)
    }

    private fun NodeRaw.map(index: Int, nodes: Array<GltfNode?>) = GltfNode(
            index = index,
            camera = camera?.let(cameras::get),
            children = children?.map(nodes::get)?.requireNoNulls(),
            matrix = GltfMat4.fromNumbers(matrix),
            mesh = mesh?.let(meshes::get),
            rotation = GltfQuaternion.fromNumbers(rotation),
            scale = GltfVec3.fromNumbers(scale),
            translation = GltfVec3.fromNumbers(translation),
            weights = weights?.map(Number::toFloat),
            name = name
    )

    private fun SkinRaw.map(index: Int) = GltfSkin(
            index,
            inverseBindMatrices?.let(accessors::get),
            skeleton?.let(nodes::get),
            joints.map(nodes::get).requireNoNulls(),
            name
    )

    private fun AnimationSamplerRaw.map() = GltfAnimationSampler(
            accessors[input], GltfInterpolationType.fromCode(interpolation), accessors[output]
    )

    private fun AnimationTargetRaw.map() = GltfAnimationTarget(
            node?.let(nodes::get), GltfAnimationTargetPath.fromCode(path)
    )

    private fun ChannelRaw.map(samplers: List<GltfAnimationSampler>) = GltfChannel(
            samplers[sampler], target.map()
    )

    private fun AnimationRaw.map(): GltfAnimation {
        val samplers = samplers.map { it.map() }
        return GltfAnimation(channels.map { it.map(samplers) }, samplers, name)
    }

    private fun SceneRaw.map(index: Int) = GltfScene(
            index, nodes?.map(this@GltfMapper.nodes::get), name
    )
}
