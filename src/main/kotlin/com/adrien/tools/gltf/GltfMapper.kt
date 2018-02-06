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

        buffers = asset.buffers?.mapIndexed { index, buffer -> mapBuffer(index, buffer, input) } ?: emptyList()
        bufferViews = asset.bufferViews?.mapIndexed(::mapBufferView) ?: emptyList()
        accessors = asset.accessors?.mapIndexed(::mapAccessor) ?: emptyList()
        samplers = asset.samplers?.mapIndexed(::mapSampler) ?: emptyList()
        images = asset.images?.mapIndexed(::mapImage) ?: emptyList()
        textures = asset.textures?.mapIndexed(::mapTexture) ?: emptyList()
        materials = asset.materials?.mapIndexed(::mapMaterial) ?: emptyList()
        meshes = asset.meshes?.mapIndexed(::mapMesh) ?: emptyList()
        cameras = asset.cameras?.mapIndexed(::mapCamera) ?: emptyList()

        // map nodes recursively, skin are omitted
        val tmpNode = Array<GltfNode?>(asset.nodes?.size ?: 0) { null }
        asset.nodes?.forEachIndexed { index, nodeRaw -> mapNodeRec(index, nodeRaw, asset, tmpNode) }
        nodes = tmpNode.requireNoNulls().asList()

        // map skins then update nodes with loaded skins
        skins = asset.skins?.mapIndexed(::mapSkin) ?: emptyList()
        asset.nodes?.forEachIndexed { index, nodeRaw -> nodes[index].skin = nodeRaw.skin?.let { skins[it] } }

        val animations = asset.animations?.map(::mapAnimation) ?: emptyList()
        val scenes = asset.scenes?.mapIndexed(::mapScene) ?: emptyList()

        return GltfAsset(
                mapMetadata(asset.asset),
                asset.extensionsUsed?.toList(),
                asset.extensionsRequired?.toList(),
                buffers, bufferViews, accessors,
                samplers, images, textures,
                materials, meshes,
                cameras,
                nodes, skins, animations,
                scenes, asset.scene?.let(scenes::get))
    }

    private fun mapMetadata(assetRaw: AssetRaw) = GltfMetadata(
            assetRaw.copyright,
            assetRaw.generator,
            assetRaw.version,
            assetRaw.minVersion
    )

    private fun mapBuffer(index: Int, bufferRaw: BufferRaw, gltfRaw: GltfRaw): GltfBuffer {
        val uri = bufferRaw.uri

        val data = uri?.decodeDataUri()
                ?: gltfRaw.dataByURI[uri]
                ?: throw IllegalArgumentException("Buffer data is not embedded and does not reference a .bin file that could be found")

        return GltfBuffer(index, uri, bufferRaw.byteLength, data, bufferRaw.name)
    }

    private fun mapBufferView(index: Int, bufferViewRaw: BufferViewRaw) = GltfBufferView(
            index,
            buffers[bufferViewRaw.buffer],
            bufferViewRaw.byteOffset,
            bufferViewRaw.byteLength,
            bufferViewRaw.byteStride,
            bufferViewRaw.target?.let(GltfBufferTarget.Factory::fromCode),
            bufferViewRaw.name
    )

    private fun mapIndices(indicesRaw: IndicesRaw) = GltfIndices(
            bufferViews[indicesRaw.bufferView],
            indicesRaw.byteOffset,
            GltfComponentType.fromCode(indicesRaw.componentType)
    )

    private fun mapValues(valuesRaw: ValuesRaw) = GltfValues(
            bufferViews[valuesRaw.bufferView],
            valuesRaw.byteOffset
    )

    private fun mapSparse(sparseRaw: SparseRaw) = GltfSparse(
            sparseRaw.count,
            mapIndices(sparseRaw.indices),
            mapValues(sparseRaw.values)
    )

    private fun mapAccessor(index: Int, accessorRaw: AccessorRaw) = GltfAccessor(
            index,
            accessorRaw.bufferView?.let(bufferViews::get),
            accessorRaw.byteOffset,
            GltfComponentType.fromCode(accessorRaw.componentType),
            accessorRaw.normalized,
            accessorRaw.count,
            GltfType.fromCode(accessorRaw.type),
            accessorRaw.max?.map(Number::toFloat)?.toList(),
            accessorRaw.min?.map(Number::toFloat)?.toList(),
            accessorRaw.sparse?.let(::mapSparse),
            accessorRaw.name
    )

    private fun mapSampler(index: Int, samplerRaw: SamplerRaw) = GltfSampler(
            index,
            samplerRaw.magFilter?.let(GltfFilter.Factory::fromCode),
            samplerRaw.minFilter?.let(GltfFilter.Factory::fromCode),
            GltfWrapMode.fromCode(samplerRaw.wrapS),
            GltfWrapMode.fromCode(samplerRaw.wrapT),
            samplerRaw.name
    )

    private fun mapImage(index: Int, imageRaw: ImageRaw) = GltfImage(
            index,
            imageRaw.uri,
            imageRaw.uri?.decodeDataUri(),
            imageRaw.mimeType?.let(GltfMimeType.Factory::fromCode),
            imageRaw.bufferView?.let(bufferViews::get),
            imageRaw.name
    )

    private fun mapTexture(index: Int, textureRaw: TextureRaw) = GltfTexture(
            index,
            textureRaw.sampler?.let(samplers::get) ?: GltfSampler(-1),
            textureRaw.source?.let(images::get),
            textureRaw.name
    )

    private fun mapTextureInfo(textureInfoRaw: TextureInfoRaw) = GltfTextureInfo(
            textures[textureInfoRaw.index],
            textureInfoRaw.texCoord
    )

    private fun mapNormalTextureInfo(textureInfoRaw: NormalTextureInfoRaw) = GltfNormalTextureInfo(
            textures[textureInfoRaw.index],
            textureInfoRaw.texCoord,
            textureInfoRaw.scale.toFloat()
    )

    private fun mapOcclusionTextureInfo(textureInfoRaw: OcclusionTextureInfoRaw) = GltfOcclusionTextureInfo(
            textures[textureInfoRaw.index],
            textureInfoRaw.texCoord,
            textureInfoRaw.strength.toFloat()
    )

    private fun mapPbrMetallicRoughness(pbrRaw: PbrMetallicRoughnessRaw) = GltfPbrMetallicRoughness(
            GltfColor.fromNumbers(pbrRaw.baseColorFactor),
            pbrRaw.baseColorTexture?.let(::mapTextureInfo),
            pbrRaw.metallicFactor.toFloat(),
            pbrRaw.roughnessFactor.toFloat(),
            pbrRaw.metallicRoughnessTexture?.let(::mapTextureInfo)
    )

    private fun mapMaterial(index: Int, materialRaw: MaterialRaw) = GltfMaterial(
            index,
            mapPbrMetallicRoughness(materialRaw.pbrMetallicRoughness),
            materialRaw.normalTexture?.let(::mapNormalTextureInfo),
            materialRaw.occlusionTexture?.let(::mapOcclusionTextureInfo),
            materialRaw.emissiveTexture?.let(::mapTextureInfo),
            GltfColor.fromNumbers(materialRaw.emissiveFactor),
            GltfAlphaMode.fromCode(materialRaw.alphaMode),
            materialRaw.alphaCutoff.toFloat(),
            materialRaw.doubleSided,
            materialRaw.name
    )

    private fun mapPrimitive(primitiveRaw: PrimitiveRaw) = GltfPrimitive(
            primitiveRaw.attributes.map { Pair(it.key, accessors[it.value]) }.toMap(),
            primitiveRaw.indices?.let(accessors::get),
            primitiveRaw.material?.let(materials::get) ?: GltfMaterial(-1),
            GltfPrimitiveMode.fromCode(primitiveRaw.mode),
            primitiveRaw.targets?.map {
                it.mapValues { (_, accessorId) -> accessors[accessorId] }
            }
    )

    private fun mapMesh(index: Int, meshRaw: MeshRaw) = GltfMesh(
            index,
            meshRaw.primitives.map(::mapPrimitive),
            meshRaw.weights?.map(Number::toFloat),
            meshRaw.name
    )

    private fun mapOrthographic(orthographicRaw: OrthographicRaw) = GltfOrthographic(
            orthographicRaw.xmag.toFloat(),
            orthographicRaw.ymag.toFloat(),
            orthographicRaw.zfar.toFloat(),
            orthographicRaw.znear.toFloat()
    )

    private fun mapPerspective(perspectiveRaw: PerspectiveRaw) = GltfPerspective(
            perspectiveRaw.aspectRatio?.toFloat(),
            perspectiveRaw.yfov.toFloat(),
            perspectiveRaw.zfar?.toFloat(),
            perspectiveRaw.znear.toFloat()
    )

    private fun mapCamera(index: Int, cameraRaw: CameraRaw) = GltfCamera(
            index,
            cameraRaw.orthographic?.let(::mapOrthographic),
            cameraRaw.perspective?.let(::mapPerspective),
            GltfCameraType.fromCode(cameraRaw.type),
            cameraRaw.name
    )

    /**
     * Map a node
     *
     * If the node has unmapped children, it maps them first
     */
    private fun mapNodeRec(index: Int, nodeRaw: NodeRaw, assetRaw: GltfAssetRaw, nodes: Array<GltfNode?>) {
        if (nodes[index] != null) return

        val shouldMapChildren = nodeRaw.children?.any { nodes[it] == null } ?: false
        if (shouldMapChildren) {
            nodeRaw.children
                    ?.map { Pair(it, assetRaw.nodes?.get(it)) }
                    ?.filter { it.second != null }
                    ?.forEach { mapNodeRec(it.first, it.second!!, assetRaw, nodes) }
        }

        if (nodes[index] == null) nodes[index] = mapNode(index, nodeRaw, nodes)
    }

    private fun mapNode(index: Int, nodeRaw: NodeRaw, nodes: Array<GltfNode?>) = GltfNode(
            index,
            nodeRaw.camera?.let(cameras::get),
            nodeRaw.children?.map(nodes::get)?.requireNoNulls(),
            null,
            GltfMat4.fromNumbers(nodeRaw.matrix),
            nodeRaw.mesh?.let(meshes::get),
            GltfQuaternion.fromNumbers(nodeRaw.rotation),
            GltfVec3.fromNumbers(nodeRaw.scale),
            GltfVec3.fromNumbers(nodeRaw.translation),
            nodeRaw.weights?.map(Number::toFloat),
            nodeRaw.name
    )

    private fun mapSkin(index: Int, skinRaw: SkinRaw) = GltfSkin(
            index,
            skinRaw.inverseBindMatrices?.let(accessors::get),
            skinRaw.skeleton?.let(nodes::get),
            skinRaw.joints.map(nodes::get).requireNoNulls(),
            skinRaw.name
    )

    private fun mapAnimationSampler(animationSamplerRaw: AnimationSamplerRaw) = GltfAnimationSampler(
            accessors[animationSamplerRaw.input],
            GltfInterpolationType.fromCode(animationSamplerRaw.interpolation),
            accessors[animationSamplerRaw.output]
    )

    private fun mapAnimationTarget(targetRaw: AnimationTargetRaw) = GltfAnimationTarget(
            targetRaw.node?.let(nodes::get),
            GltfAnimationTargetPath.fromCode(targetRaw.path)
    )

    private fun mapChannel(channelRaw: ChannelRaw, samplers: List<GltfAnimationSampler>) = GltfChannel(
            samplers[channelRaw.sampler],
            mapAnimationTarget(channelRaw.target)
    )

    private fun mapAnimation(animationRaw: AnimationRaw): GltfAnimation {
        val samplers = animationRaw.samplers.map(::mapAnimationSampler)
        return GltfAnimation(
                animationRaw.channels.map { mapChannel(it, samplers) },
                samplers,
                animationRaw.name
        )
    }

    private fun mapScene(index: Int, sceneRaw: SceneRaw) = GltfScene(
            index,
            sceneRaw.nodes?.map(nodes::get),
            sceneRaw.name
    )
}
