package com.adrienben.tools.gltf.validation

import com.adrienben.tools.gltf.models.*
import org.junit.Test
import kotlin.test.fail

/**
 * Test class for [Validator]
 */
class ValidationTest {

    private val validAsset = AssetRaw(version = "2.0")

    @Test
    fun itShouldFailOnVersionError() = itShouldFailToValidate(
            failingField = "asset.version",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = AssetRaw(
                                    version = "2.1" // tested error
                            ))))

    @Test
    fun itShouldFailOnWrongBufferByteLength() = itShouldFailToValidate(
            failingField = "buffers[0].byteLength",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            buffers = listOf(BufferRaw(
                                    byteLength = 0 // tested error
                            )))))

    @Test
    fun itShouldFailOnEmptyBuffers() = itShouldFailToValidate(
            failingField = "buffers",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            buffers = emptyList() // tested error
                    )))

    @Test
    fun itShouldFailOnWrongBufferViewBufferRef() = itShouldFailToValidate(
            failingField = "bufferViews[0].buffer",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            bufferViews = listOf(BufferViewRaw(
                                    buffer = -1, // tested error
                                    byteLength = 1
                            )))))

    @Test
    fun itShouldFailOnWrongBufferViewByteOffset() = itShouldFailToValidate(
            failingField = "bufferViews[0].byteOffset",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            bufferViews = listOf(BufferViewRaw(
                                    buffer = 0,
                                    byteOffset = -1, // tested error
                                    byteLength = 1
                            )))))

    @Test
    fun itShouldFailOnWrongBufferViewByteLength() = itShouldFailToValidate(
            failingField = "bufferViews[0].byteLength",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            bufferViews = listOf(BufferViewRaw(
                                    buffer = 0,
                                    byteLength = 0 // tested error
                            )))))

    @Test
    fun itShouldFailOnWrongBufferViewByteStride() = itShouldFailToValidate(
            failingField = "bufferViews[0].byteStride",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            bufferViews = listOf(BufferViewRaw(
                                    buffer = 0,
                                    byteLength = 1,
                                    byteStride = 0 // tested error
                            )))))

    @Test
    fun itShouldFailOnWrongBufferViewTarget() = itShouldFailToValidate(
            failingField = "bufferViews[0].target",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            bufferViews = listOf(BufferViewRaw(
                                    buffer = 0,
                                    byteLength = 1,
                                    target = 0 // tested error
                            )))))

    @Test
    fun itShouldFailOnEmptyBufferView() = itShouldFailToValidate(
            failingField = "bufferViews",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            bufferViews = emptyList() // tested error
                    )))

    @Test
    fun itShouldFailOnWrongIndicesBufferViewRef() = itShouldFailToValidate(
            failingField = "accessors[0].sparse.indices.bufferView",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            accessors = listOf(AccessorRaw(
                                    componentType = 5120,
                                    count = 1,
                                    type = "SCALAR",
                                    sparse = SparseRaw(
                                            count = 1,
                                            indices = IndicesRaw(
                                                    bufferView = -1, // tested error
                                                    componentType = 5121
                                            ),
                                            values = ValuesRaw(
                                                    bufferView = 0
                                            )))))))

    @Test
    fun itShouldFailOnWrongIndicesByteOffset() = itShouldFailToValidate(
            failingField = "accessors[0].sparse.indices.byteOffset",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            accessors = listOf(AccessorRaw(
                                    componentType = 5120,
                                    count = 1,
                                    type = "SCALAR",
                                    sparse = SparseRaw(
                                            count = 1,
                                            indices = IndicesRaw(
                                                    bufferView = 0,
                                                    byteOffset = -1, // tested error
                                                    componentType = 5121
                                            ),
                                            values = ValuesRaw(
                                                    bufferView = 0
                                            )))))))

    @Test
    fun itShouldFailOnWrongIndicesComponentType() = itShouldFailToValidate(
            failingField = "accessors[0].sparse.indices.componentType",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            accessors = listOf(AccessorRaw(
                                    componentType = 5120,
                                    count = 1,
                                    type = "SCALAR",
                                    sparse = SparseRaw(
                                            count = 1,
                                            indices = IndicesRaw(
                                                    bufferView = 0,
                                                    byteOffset = 0,
                                                    componentType = 0 // tested error
                                            ),
                                            values = ValuesRaw(
                                                    bufferView = 0
                                            )))))))

    @Test
    fun itShouldFailOnWrongValuesBufferViewRef() = itShouldFailToValidate(
            failingField = "accessors[0].sparse.values.bufferView",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            accessors = listOf(AccessorRaw(
                                    componentType = 5120,
                                    count = 1,
                                    type = "SCALAR",
                                    sparse = SparseRaw(
                                            count = 1,
                                            indices = IndicesRaw(
                                                    bufferView = 0,
                                                    componentType = 5121
                                            ),
                                            values = ValuesRaw(
                                                    bufferView = -1 // tested error
                                            )))))))

    @Test
    fun itShouldFailOnWrongValuesByteOffset() = itShouldFailToValidate(
            failingField = "accessors[0].sparse.values.byteOffset",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            accessors = listOf(AccessorRaw(
                                    componentType = 5120,
                                    count = 1,
                                    type = "SCALAR",
                                    sparse = SparseRaw(
                                            count = 1,
                                            indices = IndicesRaw(
                                                    bufferView = 0,
                                                    componentType = 5121
                                            ),
                                            values = ValuesRaw(
                                                    bufferView = 0,
                                                    byteOffset = -1 // tested error
                                            )))))))

    @Test
    fun itShouldFailOnWrongSparseCount() = itShouldFailToValidate(
            failingField = "accessors[0].sparse.count",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            accessors = listOf(AccessorRaw(
                                    componentType = 5120,
                                    count = 1,
                                    type = "SCALAR",
                                    sparse = SparseRaw(
                                            count = 0, // tested error
                                            indices = IndicesRaw(
                                                    bufferView = 0,
                                                    componentType = 5121
                                            ),
                                            values = ValuesRaw(
                                                    bufferView = 0
                                            )))))))

    @Test
    fun itShouldFailOnWrongAccessorBufferViewRef() = itShouldFailToValidate(
            failingField = "accessors[0].bufferView",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            accessors = listOf(AccessorRaw(
                                    bufferView = -1, // tested error
                                    componentType = 5120,
                                    count = 1,
                                    type = "SCALAR"
                            )))))

    @Test
    fun itShouldFailOnWrongAccessorByteOffset() = itShouldFailToValidate(
            failingField = "accessors[0].byteOffset",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            accessors = listOf(AccessorRaw(
                                    byteOffset = -1, // tested error
                                    componentType = 5120,
                                    count = 1,
                                    type = "SCALAR"
                            )))))

    @Test
    fun itShouldFailOnWrongAccessorComponentType() = itShouldFailToValidate(
            failingField = "accessors[0].componentType",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            accessors = listOf(AccessorRaw(
                                    componentType = 0, // tested error
                                    count = 1,
                                    type = "SCALAR"
                            )))))

    @Test
    fun itShouldFailOnWrongAccessorCount() = itShouldFailToValidate(
            failingField = "accessors[0].count",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            accessors = listOf(AccessorRaw(
                                    componentType = 5120,
                                    count = 0, // tested error
                                    type = "SCALAR"
                            )))))

    @Test
    fun itShouldFailOnWrongAccessorType() = itShouldFailToValidate(
            failingField = "accessors[0].type",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            accessors = listOf(AccessorRaw(
                                    componentType = 5120,
                                    count = 1,
                                    type = "WRONG" // tested error
                            )))))

    @Test
    fun itShouldFailOnWrongAccessorMaxSize() = itShouldFailToValidate(
            failingField = "accessors[0].max",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            accessors = listOf(AccessorRaw(
                                    componentType = 5120,
                                    count = 1,
                                    type = "SCALAR",
                                    max = emptyList() // tested error
                            )))))

    @Test
    fun itShouldFailOnWrongAccessorMinSize() = itShouldFailToValidate(
            failingField = "accessors[0].min",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            accessors = listOf(AccessorRaw(
                                    componentType = 5120,
                                    count = 1,
                                    type = "SCALAR",
                                    min = emptyList() // tested error
                            )))))

    @Test
    fun itShouldFailOnEmptyAccessors() = itShouldFailToValidate(
            failingField = "accessors",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            accessors = emptyList() // tested error
                    )))

    @Test
    fun itShouldFailOnWrongSamplerMagFilter() = itShouldFailToValidate(
            failingField = "samplers[0].magFilter",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            samplers = listOf(SamplerRaw(
                                    magFilter = 0 // tested error
                            )))))

    @Test
    fun itShouldFailOnWrongSamplerMinFilter() = itShouldFailToValidate(
            failingField = "samplers[0].minFilter",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            samplers = listOf(SamplerRaw(
                                    minFilter = 0 // tested error
                            )))))

    @Test
    fun itShouldFailOnWrongSamplerWrapS() = itShouldFailToValidate(
            failingField = "samplers[0].wrapS",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            samplers = listOf(SamplerRaw(
                                    wrapS = 0 // tested error
                            )))))

    @Test
    fun itShouldFailOnWrongSamplerWrapT() = itShouldFailToValidate(
            failingField = "samplers[0].wrapT",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            samplers = listOf(SamplerRaw(
                                    wrapT = 0 // tested error
                            )))))

    @Test
    fun itShouldFailOnEmptySamplers() = itShouldFailToValidate(
            failingField = "samplers",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            samplers = emptyList() // tested error
                    )))

    @Test
    fun itShouldFailOnWrongImageMimeType() = itShouldFailToValidate(
            failingField = "images[0].mimeType",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            images = listOf(ImageRaw(
                                    mimeType = "wrong" // tested error
                            )))))

    @Test
    fun itShouldFailOnWrongImageBufferViewRef() = itShouldFailToValidate(
            failingField = "images[0].bufferView",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            images = listOf(ImageRaw(
                                    bufferView = -1 // tested error
                            )))))

    @Test
    fun itShouldFailOnEmptyImages() = itShouldFailToValidate(
            failingField = "images",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            images = emptyList() // tested error
                    )))

    @Test
    fun itShouldFailOnWrongTextureSamplerRef() = itShouldFailToValidate(
            failingField = "textures[0].sampler",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            textures = listOf(TextureRaw(
                                    sampler = -1 // tested error
                            )))))

    @Test
    fun itShouldFailOnWrongTextureSourceRef() = itShouldFailToValidate(
            failingField = "textures[0].source",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            textures = listOf(TextureRaw(
                                    source = -1 // tested error
                            )))))

    @Test
    fun itShouldFailOnEmptyTextures() = itShouldFailToValidate(
            failingField = "textures",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            textures = emptyList() // tested error
                    )))

    @Test
    fun itShouldFailOnWrongMaterialPbrColor() = itShouldFailToValidate(
            failingField = "materials[0].pbrMetallicRoughness.baseColorFactor",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            materials = listOf(MaterialRaw(
                                    pbrMetallicRoughness = PbrMetallicRoughnessRaw(
                                            baseColorFactor = emptyList() // tested error
                                    ))))))

    @Test
    fun itShouldFailOnWrongMaterialPbrColorTextureIndex() = itShouldFailToValidate(
            failingField = "materials[0].pbrMetallicRoughness.baseColorTexture.index",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            materials = listOf(MaterialRaw(
                                    pbrMetallicRoughness = PbrMetallicRoughnessRaw(
                                            baseColorTexture = TextureInfoRaw(
                                                    index = -1 // tested error
                                            )
                                    ))))))

    @Test
    fun itShouldFailOnWrongMaterialPbrColorTextureCoord() = itShouldFailToValidate(
            failingField = "materials[0].pbrMetallicRoughness.baseColorTexture.texCoord",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            materials = listOf(MaterialRaw(
                                    pbrMetallicRoughness = PbrMetallicRoughnessRaw(
                                            baseColorTexture = TextureInfoRaw(
                                                    index = 0,
                                                    texCoord = -1 // tested error
                                            )
                                    ))))))

    @Test
    fun itShouldFailOnWrongMaterialPbrMetallic() = itShouldFailToValidate(
            failingField = "materials[0].pbrMetallicRoughness.metallicFactor",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            materials = listOf(MaterialRaw(
                                    pbrMetallicRoughness = PbrMetallicRoughnessRaw(
                                            metallicFactor = 2 // tested error
                                    ))))))

    @Test
    fun itShouldFailOnWrongMaterialPbrRoughness() = itShouldFailToValidate(
            failingField = "materials[0].pbrMetallicRoughness.roughnessFactor",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            materials = listOf(MaterialRaw(
                                    pbrMetallicRoughness = PbrMetallicRoughnessRaw(
                                            roughnessFactor = 2 // tested error
                                    ))))))

    @Test
    fun itShouldFailOnWrongMaterialPbrMetallicRoughnessTextureIndex() = itShouldFailToValidate(
            failingField = "materials[0].pbrMetallicRoughness.metallicRoughnessTexture.index",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            materials = listOf(MaterialRaw(
                                    pbrMetallicRoughness = PbrMetallicRoughnessRaw(
                                            metallicRoughnessTexture = TextureInfoRaw(
                                                    index = -1 // tested error
                                            )
                                    ))))))

    @Test
    fun itShouldFailOnWrongMaterialPbrMetallicRoughnessTextureCoord() = itShouldFailToValidate(
            failingField = "materials[0].pbrMetallicRoughness.metallicRoughnessTexture.texCoord",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            materials = listOf(MaterialRaw(
                                    pbrMetallicRoughness = PbrMetallicRoughnessRaw(
                                            metallicRoughnessTexture = TextureInfoRaw(
                                                    index = 0,
                                                    texCoord = -1 // tested error
                                            )
                                    ))))))

    @Test
    fun itShouldFailOnWrongMaterialNormalTextureIndex() = itShouldFailToValidate(
            failingField = "materials[0].normalTexture.index",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            materials = listOf(MaterialRaw(
                                    normalTexture = NormalTextureInfoRaw(
                                            index = -1 // tested error
                                    ))))))

    @Test
    fun itShouldFailOnWrongMaterialNormalTextureCoord() = itShouldFailToValidate(
            failingField = "materials[0].normalTexture.texCoord",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            materials = listOf(MaterialRaw(
                                    normalTexture = NormalTextureInfoRaw(
                                            index = 0,
                                            texCoord = -1 // tested error
                                    ))))))

    @Test
    fun itShouldFailOnWrongMaterialOcclusionTextureIndex() = itShouldFailToValidate(
            failingField = "materials[0].occlusionTexture.index",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            materials = listOf(MaterialRaw(
                                    occlusionTexture = OcclusionTextureInfoRaw(
                                            index = -1 // tested error
                                    ))))))

    @Test
    fun itShouldFailOnWrongMaterialOcclusionTextureCoord() = itShouldFailToValidate(
            failingField = "materials[0].occlusionTexture.texCoord",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            materials = listOf(MaterialRaw(
                                    occlusionTexture = OcclusionTextureInfoRaw(
                                            index = 0,
                                            texCoord = -1 // tested error
                                    ))))))

    @Test
    fun itShouldFailOnWrongMaterialOcclusionTextureStrength() = itShouldFailToValidate(
            failingField = "materials[0].occlusionTexture.strength",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            materials = listOf(MaterialRaw(
                                    occlusionTexture = OcclusionTextureInfoRaw(
                                            index = 0,
                                            strength = 10 // tested error
                                    ))))))

    @Test
    fun itShouldFailOnWrongMaterialEmissiveTextureIndex() = itShouldFailToValidate(
            failingField = "materials[0].emissiveTexture.index",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            materials = listOf(MaterialRaw(
                                    emissiveTexture = TextureInfoRaw(
                                            index = -1 // tested error
                                    ))))))

    @Test
    fun itShouldFailOnWrongMaterialEmissiveTextureCoord() = itShouldFailToValidate(
            failingField = "materials[0].emissiveTexture.texCoord",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            materials = listOf(MaterialRaw(
                                    emissiveTexture = TextureInfoRaw(
                                            index = 0,
                                            texCoord = -1 // tested error
                                    ))))))

    @Test
    fun itShouldFailOnWrongMaterialEmissiveFactor() = itShouldFailToValidate(
            failingField = "materials[0].emissiveFactor",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            materials = listOf(MaterialRaw(
                                    emissiveFactor = emptyList() // tested error
                            )))))

    @Test
    fun itShouldFailOnWrongMaterialAlphaMode() = itShouldFailToValidate(
            failingField = "materials[0].alphaMode",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            materials = listOf(MaterialRaw(
                                    alphaMode = "WRONG" // tested error
                            )))))

    @Test
    fun itShouldFailOnWrongMaterialAlphaCutoff() = itShouldFailToValidate(
            failingField = "materials[0].alphaCutoff",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            materials = listOf(MaterialRaw(
                                    alphaCutoff = -2.0 // tested error
                            )))))

    @Test
    fun itShouldFailOnEmptyMaterials() = itShouldFailToValidate(
            failingField = "materials",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            materials = emptyList() // tested error
                    )))

    @Test
    fun itShouldFailOnEmptyMeshPrimitiveAttributes() = itShouldFailToValidate(
            failingField = "meshes[0].primitives[0].attributes",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            meshes = listOf(MeshRaw(
                                    primitives = listOf(PrimitiveRaw(
                                            attributes = emptyMap() // tested error
                                    )))))))

    @Test
    fun itShouldFailOnWrongMeshPrimitiveIndicesRef() = itShouldFailToValidate(
            failingField = "meshes[0].primitives[0].indices",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            meshes = listOf(MeshRaw(
                                    primitives = listOf(PrimitiveRaw(
                                            attributes = mapOf("POSITION" to 0),
                                            indices = -1 // tested error
                                    )))))))

    @Test
    fun itShouldFailOnWrongMeshPrimitiveMaterialRef() = itShouldFailToValidate(
            failingField = "meshes[0].primitives[0].material",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            meshes = listOf(MeshRaw(
                                    primitives = listOf(PrimitiveRaw(
                                            attributes = mapOf("POSITION" to 0),
                                            material = -1 // tested error
                                    )))))))

    @Test
    fun itShouldFailOnWrongMeshPrimitiveMode() = itShouldFailToValidate(
            failingField = "meshes[0].primitives[0].mode",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            meshes = listOf(MeshRaw(
                                    primitives = listOf(PrimitiveRaw(
                                            attributes = mapOf("POSITION" to 0),
                                            mode = -1 // tested error
                                    )))))))

    @Test
    fun itShouldFailOnEmptyMeshPrimitiveTargets() = itShouldFailToValidate(
            failingField = "meshes[0].primitives[0].targets",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            meshes = listOf(MeshRaw(
                                    primitives = listOf(PrimitiveRaw(
                                            attributes = mapOf("POSITION" to 0),
                                            targets = emptyList() // tested error
                                    )))))))

    @Test
    fun itShouldFailOnEmptyMeshPrimitiveTargetElement() = itShouldFailToValidate(
            failingField = "meshes[0].primitives[0].targets[0]",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            meshes = listOf(MeshRaw(
                                    primitives = listOf(PrimitiveRaw(
                                            attributes = mapOf("POSITION" to 0),
                                            targets = listOf(
                                                    emptyMap() // tested error
                                            ))))))))

    @Test
    fun itShouldFailOnEmptyMeshPrimitive() = itShouldFailToValidate(
            failingField = "meshes[0].primitives",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            meshes = listOf(MeshRaw(
                                    primitives = emptyList() // tested error
                            )))))

    @Test
    fun itShouldFailOnEmptyMeshWeights() = itShouldFailToValidate(
            failingField = "meshes[0].weights",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            meshes = listOf(MeshRaw(
                                    primitives = listOf(PrimitiveRaw(
                                            attributes = mapOf("POSITION" to 0)
                                    )),
                                    weights = emptyList() // tested error
                            )))))

    @Test
    fun itShouldFailOnEmptyMeshes() = itShouldFailToValidate(
            failingField = "meshes",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            meshes = emptyList() // tested error
                    )))

    @Test
    fun itShouldFailOnWrongOrthographicZFar() = itShouldFailToValidate(
            failingField = "cameras[0].orthographic.zfar",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            cameras = listOf(CameraRaw(
                                    type = "orthographic",
                                    orthographic = OrthographicRaw(
                                            xmag = 1.0,
                                            ymag = 1.0,
                                            zfar = 0.0, // tested error
                                            znear = 1.0
                                    ))))))

    @Test
    fun itShouldFailOnWrongOrthographicZNear() = itShouldFailToValidate(
            failingField = "cameras[0].orthographic.znear",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            cameras = listOf(CameraRaw(
                                    type = "orthographic",
                                    orthographic = OrthographicRaw(
                                            xmag = 1.0,
                                            ymag = 1.0,
                                            zfar = 1.0,
                                            znear = -1 // tested error
                                    ))))))

    @Test
    fun itShouldFailOnWrongPerspectiveAspectRatio() = itShouldFailToValidate(
            failingField = "cameras[0].perspective.aspectRatio",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            cameras = listOf(CameraRaw(
                                    type = "perspective",
                                    perspective = PerspectiveRaw(
                                            aspectRatio = -1, // tested error
                                            yfov = 0.7,
                                            znear = 0.1
                                    ))))))

    @Test
    fun itShouldFailOnWrongPerspectiveYFov() = itShouldFailToValidate(
            failingField = "cameras[0].perspective.yfov",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            cameras = listOf(CameraRaw(
                                    type = "perspective",
                                    perspective = PerspectiveRaw(
                                            yfov = 0.0, // tested error
                                            znear = 0.1
                                    ))))))

    @Test
    fun itShouldFailOnWrongPerspectiveZFar() = itShouldFailToValidate(
            failingField = "cameras[0].perspective.zfar",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            cameras = listOf(CameraRaw(
                                    type = "perspective",
                                    perspective = PerspectiveRaw(
                                            yfov = 0.7,
                                            zfar = 0.0, // tested error
                                            znear = 0.1
                                    ))))))

    @Test
    fun itShouldFailOnWrongPerspectiveZNear() = itShouldFailToValidate(
            failingField = "cameras[0].perspective.znear",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            cameras = listOf(CameraRaw(
                                    type = "perspective",
                                    perspective = PerspectiveRaw(
                                            yfov = 0.7,
                                            znear = 0.0 // tested error
                                    ))))))

    @Test
    fun itShouldFailOnWrongCameraType() = itShouldFailToValidate(
            failingField = "cameras[0].type",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            cameras = listOf(CameraRaw(
                                    type = "wrong" // tested error
                            )))))

    @Test
    fun itShouldFailOnEmptyCameras() = itShouldFailToValidate(
            failingField = "cameras",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            cameras = emptyList() // tested error
                    )))

    @Test
    fun itShouldFailOnWrongNodeCameraRef() = itShouldFailToValidate(
            failingField = "nodes[0].camera",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            nodes = listOf(NodeRaw(
                                    camera = -1 // tested error
                            )))))

    @Test
    fun itShouldFailOnEmptyNodeChildren() = itShouldFailToValidate(
            failingField = "nodes[0].children",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            nodes = listOf(NodeRaw(
                                    children = emptyList() // tested error
                            )))))

    @Test
    fun itShouldFailOnWrongNodeSkinRef() = itShouldFailToValidate(
            failingField = "nodes[0].skin",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            nodes = listOf(NodeRaw(
                                    skin = -1 // tested error
                            )))))

    @Test
    fun itShouldFailOnWrongNodeMatrixSize() = itShouldFailToValidate(
            failingField = "nodes[0].matrix",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            nodes = listOf(NodeRaw(
                                    matrix = emptyList() // tested error
                            )))))

    @Test
    fun itShouldFailOnWrongNodeMeshRef() = itShouldFailToValidate(
            failingField = "nodes[0].mesh",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            nodes = listOf(NodeRaw(
                                    mesh = -1 // tested error
                            )))))

    @Test
    fun itShouldFailOnWrongNodeRotationSize() = itShouldFailToValidate(
            failingField = "nodes[0].rotation",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            nodes = listOf(NodeRaw(
                                    rotation = emptyList() // tested error
                            )))))

    @Test
    fun itShouldFailOnWrongNodeScaleSize() = itShouldFailToValidate(
            failingField = "nodes[0].scale",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            nodes = listOf(NodeRaw(
                                    scale = emptyList() // tested error
                            )))))

    @Test
    fun itShouldFailOnWrongNodeTranslationSize() = itShouldFailToValidate(
            failingField = "nodes[0].translation",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            nodes = listOf(NodeRaw(
                                    translation = emptyList() // tested error
                            )))))

    @Test
    fun itShouldFailOnEmptyNodeWeights() = itShouldFailToValidate(
            failingField = "nodes[0].weights",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            nodes = listOf(NodeRaw(
                                    weights = emptyList() // tested error
                            )))))

    @Test
    fun itShouldFailOnEmptyNodes() = itShouldFailToValidate(
            failingField = "nodes",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            nodes = emptyList() // tested error
                    )))

    @Test
    fun itShouldFailOnWrongSkinMatricesRef() = itShouldFailToValidate(
            failingField = "skins[0].inverseBindMatrices",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            skins = listOf(SkinRaw(
                                    inverseBindMatrices = -1, // tested error
                                    joints = listOf(0)
                            )))))

    @Test
    fun itShouldFailOnWrongSkinSkeletonRef() = itShouldFailToValidate(
            failingField = "skins[0].skeleton",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            skins = listOf(SkinRaw(
                                    skeleton = -1, // tested error
                                    joints = listOf(0)
                            )))))

    @Test
    fun itShouldFailOnEmptySkinJoints() = itShouldFailToValidate(
            failingField = "skins[0].joints",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            skins = listOf(SkinRaw(
                                    joints = emptyList() // tested error
                            )))))

    @Test
    fun itShouldFailOnWrongSkinJointsElement() = itShouldFailToValidate(
            failingField = "skins[0].joints[0]",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            skins = listOf(SkinRaw(
                                    joints = listOf(-1) // tested error
                            )))))

    @Test
    fun itShouldFailOnEmptySkins() = itShouldFailToValidate(
            failingField = "skins",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            skins = emptyList() // tested error
                    )))

    @Test
    fun itShouldFailOnWrongAnimationTargetNodeRef() = itShouldFailToValidate(
            failingField = "animations[0].channels[0].target.node",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            animations = listOf(AnimationRaw(
                                    channels = listOf(ChannelRaw(
                                            sampler = 0,
                                            target = AnimationTargetRaw(
                                                    node = -1, // tested error
                                                    path = "translation"
                                            ))),
                                    samplers = listOf(AnimationSamplerRaw(
                                            input = 0,
                                            interpolation = "LINEAR",
                                            output = 1
                                    )))))))

    @Test
    fun itShouldFailOnWrongAnimationTargetPath() = itShouldFailToValidate(
            failingField = "animations[0].channels[0].target.path",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            animations = listOf(AnimationRaw(
                                    channels = listOf(ChannelRaw(
                                            sampler = 0,
                                            target = AnimationTargetRaw(
                                                    path = "wrong" // tested error
                                            ))),
                                    samplers = listOf(AnimationSamplerRaw(
                                            input = 0,
                                            interpolation = "LINEAR",
                                            output = 1
                                    )))))))

    @Test
    fun itShouldFailOnWrongAnimationChannelSamplerRef() = itShouldFailToValidate(
            failingField = "animations[0].channels[0].sampler",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            animations = listOf(AnimationRaw(
                                    channels = listOf(ChannelRaw(
                                            sampler = -1, // tested error
                                            target = AnimationTargetRaw(
                                                    path = "translation"
                                            ))),
                                    samplers = listOf(AnimationSamplerRaw(
                                            input = 0,
                                            interpolation = "LINEAR",
                                            output = 1
                                    )))))))

    @Test
    fun itShouldFailOnWrongAnimationSamplerInputRef() = itShouldFailToValidate(
            failingField = "animations[0].samplers[0].input",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            animations = listOf(AnimationRaw(
                                    channels = listOf(ChannelRaw(
                                            sampler = 0,
                                            target = AnimationTargetRaw(
                                                    path = "translation"
                                            ))),
                                    samplers = listOf(AnimationSamplerRaw(
                                            input = -1, // tested error
                                            interpolation = "LINEAR",
                                            output = 1
                                    )))))))

    @Test
    fun itShouldFailOnWrongAnimationSamplerInterpolation() = itShouldFailToValidate(
            failingField = "animations[0].samplers[0].interpolation",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            animations = listOf(AnimationRaw(
                                    channels = listOf(ChannelRaw(
                                            sampler = 0,
                                            target = AnimationTargetRaw(
                                                    path = "translation"
                                            ))),
                                    samplers = listOf(AnimationSamplerRaw(
                                            input = 0,
                                            interpolation = "WRONG", // tested error
                                            output = 1
                                    )))))))

    @Test
    fun itShouldFailOnWrongAnimationSamplerOutputRef() = itShouldFailToValidate(
            failingField = "animations[0].samplers[0].output",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            animations = listOf(AnimationRaw(
                                    channels = listOf(ChannelRaw(
                                            sampler = 0,
                                            target = AnimationTargetRaw(
                                                    path = "translation"
                                            ))),
                                    samplers = listOf(AnimationSamplerRaw(
                                            input = 0,
                                            interpolation = "LINEAR",
                                            output = -1 // tested error
                                    )))))))

    @Test
    fun itShouldFailOnEmptyAnimationChannels() = itShouldFailToValidate(
            failingField = "animations[0].channels",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            animations = listOf(AnimationRaw(
                                    channels = emptyList(), // tested error
                                    samplers = listOf(AnimationSamplerRaw(
                                            input = 0,
                                            interpolation = "LINEAR",
                                            output = 1
                                    )))))))

    @Test
    fun itShouldFailOnEmptyAnimationSamplers() = itShouldFailToValidate(
            failingField = "animations[0].samplers",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            animations = listOf(AnimationRaw(
                                    channels = listOf(ChannelRaw(
                                            sampler = 0,
                                            target = AnimationTargetRaw(
                                                    path = "translation"
                                            ))),
                                    samplers = emptyList() // tested error
                            )))))

    @Test
    fun itShouldFailOnEmptyAnimations() = itShouldFailToValidate(
            failingField = "animations",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            animations = emptyList() // tested error
                    )))

    @Test
    fun itShouldFailOnEmptySceneNodes() = itShouldFailToValidate(
            failingField = "scenes[0].nodes",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            scenes = listOf(SceneRaw(
                                    nodes = emptyList() // tested error
                            )))))

    @Test
    fun itShouldFailOnWrongSceneNodesElement() = itShouldFailToValidate(
            failingField = "scenes[0].nodes[0]",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            scenes = listOf(SceneRaw(
                                    nodes = listOf(-1) // tested error
                            )))))

    @Test
    fun itShouldFailOnEmptyScenes() = itShouldFailToValidate(
            failingField = "scenes",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            scenes = emptyList() // tested error
                    )))

    @Test
    fun itShouldFailOnWrongSceneRef() = itShouldFailToValidate(
            failingField = "scene",
            gltfRaw = GltfRaw(
                    data = emptyList(),
                    gltfAssetRaw = GltfAssetRaw(
                            asset = validAsset,
                            scene = -1 // tested error
                    )))

    private fun itShouldFailToValidate(gltfRaw: GltfRaw, failingField: String) = try {
        Validator().validate(gltfRaw)
        fail("Validation should have failed on field $failingField but passed")
    } catch (e: AssertionError) {
        assert(e.message?.startsWith("$failingField ") ?: false) {
            "Validation should have failed on field $failingField but failed for other reasons : ${e.message}"
        }
    }
}
