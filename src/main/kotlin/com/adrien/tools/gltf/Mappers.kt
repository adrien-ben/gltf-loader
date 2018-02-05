package com.adrien.tools.gltf

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
 * [GltfAsset] mapper.
 */
class GltfMapper : Mapper<GltfRaw, GltfAsset> {

    private lateinit var buffers: List<Buffer>
    private lateinit var bufferViews: List<BufferView>
    private lateinit var accessors: List<Accessor>
    private lateinit var samplers: List<Sampler>
    private lateinit var images: List<Image>
    private lateinit var textures: List<Texture>
    private lateinit var materials: List<Material>
    private lateinit var meshes: List<Mesh>

    override fun map(input: GltfRaw): GltfAsset {
        buffers = input.gltfAssetRaw.buffers?.map { mapBuffer(it, input) } ?: emptyList()
        bufferViews = input.gltfAssetRaw.bufferViews?.map(::mapBufferView) ?: emptyList()
        accessors = input.gltfAssetRaw.accessors?.map(::mapAccessor) ?: emptyList()
        samplers = input.gltfAssetRaw.samplers?.map(::mapSampler) ?: emptyList()
        images = input.gltfAssetRaw.images?.map(::mapImage) ?: emptyList()
        textures = input.gltfAssetRaw.textures?.map(::mapTexture) ?: emptyList()
        materials = input.gltfAssetRaw.materials?.map(::mapMaterial) ?: emptyList()
        meshes = input.gltfAssetRaw.meshes?.map(::mapMesh) ?: emptyList()

        return GltfAsset(
                input.gltfAssetRaw.extensionsUsed?.toList(),
                input.gltfAssetRaw.extensionsRequired?.toList(),
                buffers,
                bufferViews,
                accessors,
                samplers,
                images,
                textures,
                materials,
                meshes)
    }

    private fun mapBuffer(bufferRaw: BufferRaw, gltfRaw: GltfRaw): Buffer {
        if (gltfRaw.dataByURI == null) throw UnsupportedOperationException("Cannot map a buffer with no uri")
        val data = gltfRaw.dataByURI[bufferRaw.uri] ?: throw IllegalStateException(
                "No data found for buffer ${bufferRaw.uri}")
        return Buffer(bufferRaw.uri, bufferRaw.byteLength, data, bufferRaw.name)
    }

    private fun mapBufferView(bufferViewRaw: BufferViewRaw): BufferView {
        val buffer = buffers[bufferViewRaw.buffer]
        val target = bufferViewRaw.target?.let(BufferTargetMapper::map)
        return BufferView(buffer, bufferViewRaw.byteOffset, bufferViewRaw.byteLength, bufferViewRaw.byteStride,
                target, bufferViewRaw.name)
    }

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

    private fun mapAccessor(accessorRaw: AccessorRaw): Accessor {
        val bufferView = accessorRaw.bufferView?.let(bufferViews::get)
        val componentType = ComponentTypeMapper.map(accessorRaw.componentType)
        val type = TypeMapper.map(accessorRaw.type)
        val max = accessorRaw.max?.map(Number::toFloat)?.toList()
        val min = accessorRaw.min?.map(Number::toFloat)?.toList()
        val sparse = accessorRaw.sparse?.let(::mapSparse)

        return Accessor(bufferView, accessorRaw.byteOffset, componentType, accessorRaw.normalized, accessorRaw.count,
                type, max, min, sparse, accessorRaw.name)
    }

    private fun mapSampler(samplerRaw: SamplerRaw): Sampler {
        val magFilter = samplerRaw.magFilter?.let(FilterMapper::map)
        val minFilter = samplerRaw.minFilter?.let(FilterMapper::map)
        val wrapS = WrapModeMapper.map(samplerRaw.wrapS)
        val wrapT = WrapModeMapper.map(samplerRaw.wrapT)
        return Sampler(magFilter, minFilter, wrapS, wrapT, samplerRaw.name)
    }

    private fun mapImage(imageRaw: ImageRaw): Image {
        val bufferView = imageRaw.bufferView?.let(bufferViews::get)
        val mimeType = imageRaw.mimeType?.let(MimeTypeMapper::map)
        return Image(imageRaw.uri, mimeType, bufferView, imageRaw.name)
    }

    private fun mapTexture(textureRaw: TextureRaw): Texture {
        val sampler = textureRaw.sampler?.let(samplers::get) ?: Sampler()
        val image = textureRaw.source?.let(images::get)
        return Texture(sampler, image, textureRaw.name)
    }

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

    private fun mapMaterial(materialRaw: MaterialRaw): Material {
        val pbr = mapPbrMetallicRoughness(materialRaw.pbrMetallicRoughness)
        val normalTexture = materialRaw.normalTexture?.let(::mapNormalTextureInfo)
        val occlusionTexture = materialRaw.occlusionTexture?.let(::mapOcclusionTextureInfo)
        val emissiveTexture = materialRaw.emissiveTexture?.let(::mapTextureInfo)
        val emissiveColor = ColorMapper.map(materialRaw.emissiveFactor)
        val alphaMode = AlphaModeMapper.map(materialRaw.alphaMode)
        val alphaCutoff = materialRaw.alphaCutoff.toFloat()

        return Material(pbr, normalTexture, occlusionTexture, emissiveTexture, emissiveColor, alphaMode, alphaCutoff,
                materialRaw.doubleSided, materialRaw.name)
    }

    private fun mapPrimitive(primitiveRaw: PrimitiveRaw): Primitive {
        val attributes = primitiveRaw.attributes.map { Pair(it.key, accessors[it.value]) }.toMap()
        val indices = primitiveRaw.indices?.let(accessors::get)
        val material = primitiveRaw.material?.let(materials::get) ?: Material()
        val mode = PrimitiveModeMapper.map(primitiveRaw.mode)
        val targets = primitiveRaw.targets?.map {
            it.mapValues { (_, accessorId) -> accessors[accessorId] }
        }
        return Primitive(attributes, indices, material, mode, targets)
    }

    private fun mapMesh(meshRaw: MeshRaw): Mesh {
        val primitives = meshRaw.primitives.map(::mapPrimitive)
        val weights = meshRaw.weights?.map(Number::toFloat)
        return Mesh(primitives, weights, meshRaw.name)
    }
}
