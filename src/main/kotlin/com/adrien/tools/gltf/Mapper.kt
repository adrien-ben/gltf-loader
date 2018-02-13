package com.adrien.tools.gltf

/**
 * [GltfAsset] mapper.
 */
internal class Mapper {

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
        bufferViews = asset.bufferViews?.mapIndexed { index, view -> view.map(index, buffers) } ?: emptyList()
        accessors = asset.accessors?.mapIndexed { index, accessor -> accessor.map(index, bufferViews) } ?: emptyList()
        samplers = asset.samplers?.mapIndexed { index, sampler -> sampler.map(index) } ?: emptyList()
        images = asset.images?.mapIndexed { index, image -> image.map(index, bufferViews) } ?: emptyList()
        textures = asset.textures?.mapIndexed { index, texture -> texture.map(index, samplers, images) } ?: emptyList()
        materials = asset.materials?.mapIndexed { index, material -> material.map(index, textures) } ?: emptyList()
        meshes = asset.meshes?.mapIndexed { index, mesh -> mesh.map(index, accessors, materials) } ?: emptyList()
        cameras = asset.cameras?.mapIndexed { index, camera -> camera.map(index) } ?: emptyList()

        // map nodes recursively, skin are omitted
        nodes = Array<GltfNode?>(asset.nodes?.size ?: 0) { null }.let {
            asset.nodes?.forEachIndexed { index, nodeRaw -> nodeRaw.map(index, asset, it, cameras, meshes) }
            it.requireNoNulls().asList()
        }

        // map skins then update nodes with loaded skins
        skins = asset.skins?.mapIndexed { index, skin -> skin.map(index, accessors, nodes) } ?: emptyList()
        asset.nodes?.forEachIndexed { index, nodeRaw -> nodes[index].skin = nodeRaw.skin?.let { skins[it] } }

        val animations = asset.animations?.map { it.map(accessors, nodes) } ?: emptyList()
        val scenes = asset.scenes?.mapIndexed { index, scene -> scene.map(index, nodes) } ?: emptyList()

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
}

private fun AssetRaw.map() = GltfMetadata(
        copyright, generator, version, minVersion
)

private fun BufferRaw.map(index: Int, gltfRaw: GltfRaw): GltfBuffer {
    return GltfBuffer(index, uri, byteLength, gltfRaw.data[index], name)
}

private fun BufferViewRaw.map(index: Int, buffers: List<GltfBuffer>) = GltfBufferView(
        index,
        buffers[buffer],
        byteOffset ?: 0,
        byteLength,
        byteStride,
        target?.let(GltfBufferTarget.Factory::fromCode),
        name
)

private fun IndicesRaw.map(bufferViews: List<GltfBufferView>) = GltfIndices(
        bufferViews[bufferView],
        byteOffset ?: 0,
        GltfComponentType.fromCode(componentType)
)

private fun ValuesRaw.map(bufferViews: List<GltfBufferView>) = GltfValues(bufferViews[bufferView], byteOffset ?: 0)

private fun SparseRaw.map(bufferViews: List<GltfBufferView>) = GltfSparse(count, indices.map(bufferViews), values.map(bufferViews))

private fun AccessorRaw.map(index: Int, bufferViews: List<GltfBufferView>) = GltfAccessor(
        index,
        bufferView?.let(bufferViews::get),
        byteOffset ?: 0,
        GltfComponentType.fromCode(componentType),
        normalized ?: false,
        count,
        GltfType.fromCode(type),
        max?.map(Number::toFloat),
        min?.map(Number::toFloat),
        sparse?.map(bufferViews),
        name
)

private fun SamplerRaw.map(index: Int) = GltfSampler(
        index,
        magFilter?.let(GltfFilter.Factory::fromCode),
        minFilter?.let(GltfFilter.Factory::fromCode),
        wrapS?.let(GltfWrapMode.Factory::fromCode) ?: GltfWrapMode.REPEAT,
        wrapT?.let(GltfWrapMode.Factory::fromCode) ?: GltfWrapMode.REPEAT,
        name
)

private fun ImageRaw.map(index: Int, bufferViews: List<GltfBufferView>) = GltfImage(
        index,
        uri,
        uri?.decodeDataUri(),
        mimeType?.let(GltfMimeType.Factory::fromCode),
        bufferView?.let(bufferViews::get),
        name
)

private fun TextureRaw.map(index: Int, samplers: List<GltfSampler>, images: List<GltfImage>) = GltfTexture(
        index,
        sampler?.let(samplers::get) ?: GltfSampler(-1),
        source?.let(images::get),
        name
)

private fun TextureInfoRaw.map(textures: List<GltfTexture>) = GltfTextureInfo(textures[index], texCoord ?: 0)

private fun NormalTextureInfoRaw.map(textures: List<GltfTexture>) = GltfNormalTextureInfo(
        textures[index], texCoord ?: 0, scale?.let(Number::toFloat) ?: 1f
)

private fun OcclusionTextureInfoRaw.map(textures: List<GltfTexture>) = GltfOcclusionTextureInfo(
        textures[index], texCoord ?: 0, strength?.let(Number::toFloat) ?: 1f
)

private fun PbrMetallicRoughnessRaw.map(textures: List<GltfTexture>) = GltfPbrMetallicRoughness(
        baseColorFactor?.let(GltfColor.Factory::fromNumbers) ?: GltfColor(),
        baseColorTexture?.map(textures),
        metallicFactor?.let(Number::toFloat) ?: 1f,
        roughnessFactor?.let(Number::toFloat) ?: 1f,
        metallicRoughnessTexture?.map(textures)
)

private fun MaterialRaw.map(index: Int, textures: List<GltfTexture>) = GltfMaterial(
        index,
        pbrMetallicRoughness?.map(textures) ?: GltfPbrMetallicRoughness(),
        normalTexture?.map(textures),
        occlusionTexture?.map(textures),
        emissiveTexture?.map(textures),
        emissiveFactor?.let(GltfColor.Factory::fromNumbers) ?: GltfColor(0f, 0f, 0f),
        alphaMode?.let(GltfAlphaMode.Factory::fromCode) ?: GltfAlphaMode.OPAQUE,
        alphaCutoff?.let(Number::toFloat) ?: 0.5f,
        doubleSided ?: false,
        name
)

private fun PrimitiveRaw.map(accessors: List<GltfAccessor>, materials: List<GltfMaterial>) = GltfPrimitive(
        attributes.mapValues { (_, accessorId) -> accessors[accessorId] },
        indices?.let(accessors::get),
        material?.let(materials::get) ?: GltfMaterial(-1),
        mode?.let(GltfPrimitiveMode.Factory::fromCode) ?: GltfPrimitiveMode.TRIANGLES,
        targets?.map { it.mapValues { (_, accessorId) -> accessors[accessorId] } }
)

private fun MeshRaw.map(index: Int, accessors: List<GltfAccessor>, materials: List<GltfMaterial>) = GltfMesh(
        index,
        primitives.map { it.map(accessors, materials) },
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
private fun NodeRaw.map(index: Int, assetRaw: GltfAssetRaw, nodes: Array<GltfNode?>, cameras: List<GltfCamera>, meshes: List<GltfMesh>) {
    if (nodes[index] != null) return

    val shouldMapChildren = children?.any { nodes[it] == null } ?: false
    if (shouldMapChildren) {
        children?.mapNotNull { Pair(it, assetRaw.nodes?.get(it) ?: return@mapNotNull null) }
                ?.forEach { (index, child) -> child.map(index, assetRaw, nodes, cameras, meshes) }
    }

    if (nodes[index] == null) nodes[index] = this.map(index, nodes, cameras, meshes)
}

private fun NodeRaw.map(index: Int, nodes: Array<GltfNode?>, cameras: List<GltfCamera>, meshes: List<GltfMesh>): GltfNode {
    val mat = matrix?.let(GltfMat4.Factory::fromNumbers)

    return GltfNode(
            index = index,
            camera = camera?.let(cameras::get),
            children = children?.map(nodes::get)?.requireNoNulls(),
            matrix = mat ?: GltfMat4(),
            mesh = mesh?.let(meshes::get),
            rotation = mat?.let(GltfMat4::rotation)
                    ?: rotation?.let(GltfQuaternion.Factory::fromNumbers)
                    ?: GltfQuaternion(),
            scale = mat?.let(GltfMat4::scale)
                    ?: scale?.let(GltfVec3.Factory::fromNumbers)
                    ?: GltfVec3(1f, 1f, 1f),
            translation = mat?.let(GltfMat4::translation)
                    ?: translation?.let(GltfVec3.Factory::fromNumbers)
                    ?: GltfVec3(),
            weights = weights?.map(Number::toFloat),
            name = name)
}

private fun SkinRaw.map(index: Int, accessors: List<GltfAccessor>, nodes: List<GltfNode>) = GltfSkin(
        index,
        inverseBindMatrices?.let(accessors::get),
        skeleton?.let(nodes::get),
        joints.map(nodes::get).requireNoNulls(),
        name
)

private fun AnimationSamplerRaw.map(accessors: List<GltfAccessor>) = GltfAnimationSampler(
        accessors[input],
        interpolation?.let(GltfInterpolationType.Factory::fromCode) ?: GltfInterpolationType.LINEAR,
        accessors[output]
)

private fun AnimationTargetRaw.map(nodes: List<GltfNode>) = GltfAnimationTarget(
        node?.let(nodes::get), GltfAnimationTargetPath.fromCode(path)
)

private fun ChannelRaw.map(samplers: List<GltfAnimationSampler>, nodes: List<GltfNode>) = GltfChannel(
        samplers[sampler], target.map(nodes)
)

private fun AnimationRaw.map(accessors: List<GltfAccessor>, nodes: List<GltfNode>): GltfAnimation {
    val samplers = samplers.map { it.map(accessors) }
    return GltfAnimation(channels.map { it.map(samplers, nodes) }, samplers, name)
}

private fun SceneRaw.map(index: Int, gltfNodes: List<GltfNode>) = GltfScene(
        index, nodes?.map(gltfNodes::get), name
)
