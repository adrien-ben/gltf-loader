package com.adrien.tools.gltf

import java.util.*

/**
 * Mappers common interface.
 */
private interface Mapper<in I, out O> {
    fun map(input: I): O
}

/**
 * [ComponentType] mapper.
 */
private object ComponentTypeMapper : Mapper<Int, ComponentType?> {
    override fun map(input: Int) = when (input) {
        ComponentType.BYTE.code -> ComponentType.BYTE
        ComponentType.UNSIGNED_BYTE.code -> ComponentType.UNSIGNED_BYTE
        ComponentType.SHORT.code -> ComponentType.SHORT
        ComponentType.UNSIGNED_SHORT.code -> ComponentType.UNSIGNED_SHORT
        ComponentType.UNSIGNED_INT.code -> ComponentType.UNSIGNED_INT
        ComponentType.FLOAT.code -> ComponentType.FLOAT
        else -> throw UnsupportedOperationException("Unsupported component type $input")
    }
}

/**
 * [Type] mapper.
 */
private object TypeMapper : Mapper<String, Type> {
    override fun map(input: String) = when (input) {
        Type.SCALAR.code -> Type.SCALAR
        Type.VEC2.code -> Type.VEC2
        Type.VEC3.code -> Type.VEC3
        Type.VEC4.code -> Type.VEC4
        Type.MAT2.code -> Type.MAT2
        Type.MAT3.code -> Type.MAT3
        Type.MAT4.code -> Type.MAT4
        else -> throw UnsupportedOperationException("Unsupported type $input")
    }
}

/**
 * [AlphaMode] mapper.
 */
private object AlphaModeMapper : Mapper<String, AlphaMode> {
    override fun map(input: String) = when (input) {
        AlphaMode.OPAQUE.code -> AlphaMode.OPAQUE
        AlphaMode.MASK.code -> AlphaMode.MASK
        AlphaMode.BLEND.code -> AlphaMode.BLEND
        else -> throw UnsupportedOperationException("Unsupported alpha mode $input")
    }
}

/**
 * [Filter] mapper.
 */
private object FilterMapper : Mapper<Int, Filter> {
    override fun map(input: Int) = when (input) {
        Filter.NEAREST.code -> Filter.NEAREST
        Filter.LINEAR.code -> Filter.LINEAR
        Filter.NEAREST_MIPMAP_NEAREST.code -> Filter.NEAREST_MIPMAP_NEAREST
        Filter.LINEAR_MIPMAP_NEAREST.code -> Filter.LINEAR_MIPMAP_NEAREST
        Filter.NEAREST_MIPMAP_LINEAR.code -> Filter.NEAREST_MIPMAP_LINEAR
        Filter.LINEAR_MIPMAP_LINEAR.code -> Filter.LINEAR_MIPMAP_LINEAR
        else -> throw UnsupportedOperationException("Unsupported texture filter $input")
    }
}

/**
 * [WrapMode] mapper.
 */
private object WrapModeMapper : Mapper<Int, WrapMode> {
    override fun map(input: Int) = when (input) {
        WrapMode.CLAMP_TO_EDGE.code -> WrapMode.CLAMP_TO_EDGE
        WrapMode.MIRRORED_REPEAT.code -> WrapMode.MIRRORED_REPEAT
        WrapMode.REPEAT.code -> WrapMode.REPEAT
        else -> throw UnsupportedOperationException("Unsupported texture wrap mode $input")
    }
}

/**
 * [BufferTarget] mapper.
 */
private object BufferTargetMapper : Mapper<Int, BufferTarget> {
    override fun map(input: Int) = when (input) {
        BufferTarget.ARRAY_BUFFER.code -> BufferTarget.ARRAY_BUFFER
        BufferTarget.ELEMENT_ARRAY_BUFFER.code -> BufferTarget.ELEMENT_ARRAY_BUFFER
        else -> throw UnsupportedOperationException("Unsupported buffer target $input")
    }
}

/**
 * [PrimitiveMode] mapper.
 */
private object PrimitiveModeMapper : Mapper<Int, PrimitiveMode> {
    override fun map(input: Int) = when (input) {
        PrimitiveMode.POINTS.code -> PrimitiveMode.POINTS
        PrimitiveMode.LINES.code -> PrimitiveMode.LINES
        PrimitiveMode.LINE_LOOP.code -> PrimitiveMode.LINE_LOOP
        PrimitiveMode.LINE_STRIP.code -> PrimitiveMode.LINE_STRIP
        PrimitiveMode.TRIANGLES.code -> PrimitiveMode.TRIANGLES
        PrimitiveMode.TRIANGLE_STRIP.code -> PrimitiveMode.TRIANGLE_STRIP
        PrimitiveMode.TRIANGLE_FAN.code -> PrimitiveMode.TRIANGLE_FAN
        else -> throw UnsupportedOperationException("Unsupported primitive mode $input")
    }
}

/**
 * [MimeType] mapper.
 */
private object MimeTypeMapper : Mapper<String, MimeType> {
    override fun map(input: String) = when (input) {
        MimeType.JPEG.value -> MimeType.JPEG
        MimeType.PNG.value -> MimeType.PNG
        else -> throw UnsupportedOperationException("Unsupported mime type $input")
    }
}

/**
 * [CameraType] mapper.
 */
private object CameraTypeMapper : Mapper<String, CameraType> {
    override fun map(input: String) = when (input) {
        CameraType.ORTHOGRAPHIC.value -> CameraType.ORTHOGRAPHIC
        CameraType.PERSPECTIVE.value -> CameraType.PERSPECTIVE
        else -> throw UnsupportedOperationException("Unsupported camera type $input")
    }
}

/**
 * [AnimationTargetPath] mapper.
 */
private object AnimationTargetPathMapper : Mapper<String, AnimationTargetPath> {
    override fun map(input: String) = when (input) {
        AnimationTargetPath.TRANSLATION.value -> AnimationTargetPath.TRANSLATION
        AnimationTargetPath.ROTATION.value -> AnimationTargetPath.ROTATION
        AnimationTargetPath.SCALE.value -> AnimationTargetPath.SCALE
        AnimationTargetPath.WEIGHTS.value -> AnimationTargetPath.WEIGHTS
        else -> throw UnsupportedOperationException("Unsupported target path $input")
    }
}

/**
 * [InterpolationType] mapper.
 */
private object InterpolationTypeMapper : Mapper<String, InterpolationType> {
    override fun map(input: String) = when (input) {
        InterpolationType.LINEAR.value -> InterpolationType.LINEAR
        InterpolationType.STEP.value -> InterpolationType.STEP
        InterpolationType.CUBICSPLINE.value -> InterpolationType.CUBICSPLINE
        else -> throw UnsupportedOperationException("Unsupported interpolation type $input")
    }
}

/**
 * [Vec3f] mapper.
 */
private object Vec3fMapper : Mapper<List<Number>, Vec3f> {
    override fun map(input: List<Number>): Vec3f {
        if (input.size != 3) throw IllegalArgumentException("A list must contain 3 elements to be mapped into a Vec3f")
        return Vec3f(input[0].toFloat(), input[1].toFloat(), input[2].toFloat())
    }
}

/**
 * [Quaternionf] mapper.
 */
private object QuaternionfMapper : Mapper<List<Number>, Quaternionf> {
    override fun map(input: List<Number>): Quaternionf {
        if (input.size != 4) throw IllegalArgumentException("A list must contain 4 elements to be mapped into a Quaternionf")
        return Quaternionf(input[0].toFloat(), input[1].toFloat(), input[2].toFloat(), input[3].toFloat())
    }
}

/**
 * [Mat4f] mapper.
 */
private object Mat4fMapper : Mapper<List<Number>, Mat4f> {
    override fun map(input: List<Number>): Mat4f {
        if (input.size != 16) throw IllegalArgumentException("A list must contain 16 elements to be mapped into a Mat4f")
        return Mat4f(
                input[0].toFloat(), input[1].toFloat(), input[2].toFloat(), input[3].toFloat(),
                input[4].toFloat(), input[5].toFloat(), input[6].toFloat(), input[7].toFloat(),
                input[8].toFloat(), input[9].toFloat(), input[10].toFloat(), input[11].toFloat(),
                input[12].toFloat(), input[13].toFloat(), input[14].toFloat(), input[15].toFloat())
    }
}

/**
 * [Color] mapper.
 */
private object ColorMapper : Mapper<List<Number>, Color> {
    override fun map(input: List<Number>): Color {
        if (input.size !in 3..4) throw IllegalArgumentException("A list must contain 3 or 4 elements to be mapped into a Color")
        val alpha = if (input.size == 3) 1f else input[3].toFloat()
        return Color(input[0].toFloat(), input[1].toFloat(), input[2].toFloat(), alpha)
    }
}

/**
 * Decode a data URI if it matches the data uri pattern.
 *
 * It returns a [ByteArray] or null if the string does not match the required pattern.
 */
private fun String.decodeDataUri(): ByteArray? {
    if (DATA_URI_REGEX.matches(this)) {
        val matchResult = DATA_URI_REGEX.find(this)
        val base64 = matchResult!!.groupValues[1]
        return Base64.getDecoder().decode(base64)
    }
    return null
}

/**
 * [GltfAsset] mapper.
 */
internal class GltfMapper : Mapper<GltfRaw, GltfAsset> {

    private lateinit var buffers: List<Buffer>
    private lateinit var bufferViews: List<BufferView>
    private lateinit var accessors: List<Accessor>
    private lateinit var samplers: List<Sampler>
    private lateinit var images: List<Image>
    private lateinit var textures: List<Texture>
    private lateinit var materials: List<Material>
    private lateinit var meshes: List<Mesh>
    private lateinit var cameras: List<Camera>
    private lateinit var nodes: List<Node>
    private lateinit var skins: List<Skin>

    override fun map(input: GltfRaw): GltfAsset {

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
        val tmpNode = Array<Node?>(asset.nodes?.size ?: 0) { null }
        asset.nodes?.forEachIndexed { index, nodeRaw -> mapNodeRec(index, nodeRaw, asset, tmpNode) }
        nodes = tmpNode.requireNoNulls().asList()

        // map skins then update nodes with loaded skins
        skins = asset.skins?.mapIndexed(::mapSkin) ?: emptyList()
        asset.nodes?.forEachIndexed { index, nodeRaw -> nodes[index].skin = nodeRaw.skin?.let { skins[it] } }

        val animations = asset.animations?.map(::mapAnimation) ?: emptyList()
        val scenes = asset.scenes?.mapIndexed(::mapScene) ?: emptyList()

        return GltfAsset(
                mapAsset(asset.asset),
                asset.extensionsUsed?.toList(),
                asset.extensionsRequired?.toList(),
                buffers, bufferViews, accessors,
                samplers, images, textures,
                materials, meshes,
                cameras,
                nodes, skins, animations,
                scenes, asset.scene?.let(scenes::get))
    }

    private fun mapAsset(assetRaw: AssetRaw) = Asset(
            assetRaw.copyright,
            assetRaw.generator,
            assetRaw.version,
            assetRaw.minVersion
    )

    private fun mapBuffer(index: Int, bufferRaw: BufferRaw, gltfRaw: GltfRaw): Buffer {
        val uri = bufferRaw.uri

        val data = uri?.decodeDataUri()
                ?: gltfRaw.dataByURI[uri]
                ?: throw IllegalArgumentException("Buffer data is not embedded and does not reference a .bin file that could be found")

        return Buffer(index, uri, bufferRaw.byteLength, data, bufferRaw.name)
    }

    private fun mapBufferView(index: Int, bufferViewRaw: BufferViewRaw) = BufferView(
            index,
            buffers[bufferViewRaw.buffer],
            bufferViewRaw.byteOffset,
            bufferViewRaw.byteLength,
            bufferViewRaw.byteStride,
            bufferViewRaw.target?.let(BufferTargetMapper::map),
            bufferViewRaw.name
    )

    private fun mapIndices(indicesRaw: IndicesRaw): Indices {
        val bufferView = bufferViews[indicesRaw.bufferView]
        val componentType = ComponentTypeMapper.map(indicesRaw.componentType)
        return Indices(bufferView, indicesRaw.byteOffset, componentType)

    }

    private fun mapValues(valuesRaw: ValuesRaw): Values {
        val bufferView = bufferViews[valuesRaw.bufferView]
        return Values(bufferView, valuesRaw.byteOffset)
    }

    private fun mapSparse(sparseRaw: SparseRaw): Sparse {
        val indices = mapIndices(sparseRaw.indices)
        val values = mapValues(sparseRaw.values)
        return Sparse(sparseRaw.count, indices, values)
    }

    private fun mapAccessor(index: Int, accessorRaw: AccessorRaw) = Accessor(
            index,
            accessorRaw.bufferView?.let(bufferViews::get),
            accessorRaw.byteOffset,
            ComponentTypeMapper.map(accessorRaw.componentType),
            accessorRaw.normalized,
            accessorRaw.count,
            TypeMapper.map(accessorRaw.type),
            accessorRaw.max?.map(Number::toFloat)?.toList(),
            accessorRaw.min?.map(Number::toFloat)?.toList(),
            accessorRaw.sparse?.let(::mapSparse),
            accessorRaw.name
    )

    private fun mapSampler(index: Int, samplerRaw: SamplerRaw) = Sampler(
            index,
            samplerRaw.magFilter?.let(FilterMapper::map),
            samplerRaw.minFilter?.let(FilterMapper::map),
            WrapModeMapper.map(samplerRaw.wrapS),
            WrapModeMapper.map(samplerRaw.wrapT),
            samplerRaw.name
    )

    private fun mapImage(index: Int, imageRaw: ImageRaw) = Image(
            index,
            imageRaw.uri,
            imageRaw.uri?.decodeDataUri(),
            imageRaw.mimeType?.let(MimeTypeMapper::map),
            imageRaw.bufferView?.let(bufferViews::get),
            imageRaw.name
    )

    private fun mapTexture(index: Int, textureRaw: TextureRaw) = Texture(
            index,
            textureRaw.sampler?.let(samplers::get) ?: Sampler(-1),
            textureRaw.source?.let(images::get),
            textureRaw.name
    )

    private fun mapTextureInfo(textureInfoRaw: TextureInfoRaw): TextureInfo {
        val texture = textures[textureInfoRaw.index]
        return TextureInfo(texture, textureInfoRaw.texCoord)
    }

    private fun mapNormalTextureInfo(textureInfoRaw: NormalTextureInfoRaw): NormalTextureInfo {
        val texture = textures[textureInfoRaw.index]
        val scale = textureInfoRaw.scale.toFloat()
        return NormalTextureInfo(texture, textureInfoRaw.texCoord, scale)
    }

    private fun mapOcclusionTextureInfo(textureInfoRaw: OcclusionTextureInfoRaw): OcclusionTextureInfo {
        val texture = textures[textureInfoRaw.index]
        val strength = textureInfoRaw.strength.toFloat()
        return OcclusionTextureInfo(texture, textureInfoRaw.texCoord, strength)
    }

    private fun mapPbrMetallicRoughness(pbrRaw: PbrMetallicRoughnessRaw): PbrMetallicRoughness {
        val color = ColorMapper.map(pbrRaw.baseColorFactor)
        val colorTexture = pbrRaw.baseColorTexture?.let(::mapTextureInfo)
        val metallic = pbrRaw.metallicFactor.toFloat()
        val roughness = pbrRaw.roughnessFactor.toFloat()
        val pbrTexture = pbrRaw.metallicRoughnessTexture?.let(::mapTextureInfo)
        return PbrMetallicRoughness(color, colorTexture, metallic, roughness, pbrTexture)
    }

    private fun mapMaterial(index: Int, materialRaw: MaterialRaw) = Material(
            index,
            mapPbrMetallicRoughness(materialRaw.pbrMetallicRoughness),
            materialRaw.normalTexture?.let(::mapNormalTextureInfo),
            materialRaw.occlusionTexture?.let(::mapOcclusionTextureInfo),
            materialRaw.emissiveTexture?.let(::mapTextureInfo),
            ColorMapper.map(materialRaw.emissiveFactor),
            AlphaModeMapper.map(materialRaw.alphaMode),
            materialRaw.alphaCutoff.toFloat(),
            materialRaw.doubleSided,
            materialRaw.name
    )

    private fun mapPrimitive(primitiveRaw: PrimitiveRaw): Primitive {
        val attributes = primitiveRaw.attributes.map { Pair(it.key, accessors[it.value]) }.toMap()
        val indices = primitiveRaw.indices?.let(accessors::get)
        val material = primitiveRaw.material?.let(materials::get) ?: Material(-1)
        val mode = PrimitiveModeMapper.map(primitiveRaw.mode)
        val targets = primitiveRaw.targets?.map {
            it.mapValues { (_, accessorId) -> accessors[accessorId] }
        }
        return Primitive(attributes, indices, material, mode, targets)
    }

    private fun mapMesh(index: Int, meshRaw: MeshRaw) = Mesh(
            index,
            meshRaw.primitives.map(::mapPrimitive),
            meshRaw.weights?.map(Number::toFloat),
            meshRaw.name
    )

    private fun mapOrthographic(orthographicRaw: OrthographicRaw) = Orthographic(
            orthographicRaw.xmag.toFloat(),
            orthographicRaw.ymag.toFloat(),
            orthographicRaw.zfar.toFloat(),
            orthographicRaw.znear.toFloat()
    )

    private fun mapPerspective(perspectiveRaw: PerspectiveRaw) = Perspective(
            perspectiveRaw.aspectRatio?.toFloat(),
            perspectiveRaw.yfov.toFloat(),
            perspectiveRaw.zfar?.toFloat(),
            perspectiveRaw.znear.toFloat()
    )

    private fun mapCamera(index: Int, cameraRaw: CameraRaw) = Camera(
            index,
            cameraRaw.orthographic?.let(::mapOrthographic),
            cameraRaw.perspective?.let(::mapPerspective),
            CameraTypeMapper.map(cameraRaw.type),
            cameraRaw.name
    )

    /**
     * Map a node
     *
     * If the node has unmapped children, it maps them first
     */
    private fun mapNodeRec(index: Int, nodeRaw: NodeRaw, assetRaw: GltfAssetRaw, nodes: Array<Node?>) {
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

    private fun mapNode(index: Int, nodeRaw: NodeRaw, nodes: Array<Node?>) = Node(
            index,
            nodeRaw.camera?.let(cameras::get),
            nodeRaw.children?.map(nodes::get)?.requireNoNulls(),
            null,
            Mat4fMapper.map(nodeRaw.matrix),
            nodeRaw.mesh?.let(meshes::get),
            QuaternionfMapper.map(nodeRaw.rotation),
            Vec3fMapper.map(nodeRaw.scale),
            Vec3fMapper.map(nodeRaw.translation),
            nodeRaw.weights?.map(Number::toFloat),
            nodeRaw.name
    )

    private fun mapSkin(index: Int, skinRaw: SkinRaw) = Skin(
            index,
            skinRaw.inverseBindMatrices?.let(accessors::get),
            skinRaw.skeleton?.let(nodes::get),
            skinRaw.joints.map(nodes::get).requireNoNulls(),
            skinRaw.name
    )

    private fun mapAnimationSampler(animationSamplerRaw: AnimationSamplerRaw) = AnimationSampler(
            accessors[animationSamplerRaw.input],
            InterpolationTypeMapper.map(animationSamplerRaw.interpolation),
            accessors[animationSamplerRaw.output]
    )

    private fun mapAnimationTarget(targetRaw: AnimationTargetRaw) = AnimationTarget(
            targetRaw.node?.let(nodes::get),
            AnimationTargetPathMapper.map(targetRaw.path)
    )

    private fun mapChannel(channelRaw: ChannelRaw, samplers: List<AnimationSampler>) = Channel(
            samplers[channelRaw.sampler],
            mapAnimationTarget(channelRaw.target)
    )

    private fun mapAnimation(animationRaw: AnimationRaw): Animation {
        val samplers = animationRaw.samplers.map(::mapAnimationSampler)
        return Animation(
                animationRaw.channels.map { mapChannel(it, samplers) },
                samplers,
                animationRaw.name
        )
    }

    private fun mapScene(index: Int, sceneRaw: SceneRaw) = Scene(
            index,
            sceneRaw.nodes?.map(nodes::get),
            sceneRaw.name
    )
}
