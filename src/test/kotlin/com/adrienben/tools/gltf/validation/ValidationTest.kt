package com.adrienben.tools.gltf.validation

import com.adrienben.tools.gltf.models.*
import org.junit.Test

/**
 * Test class for [Validator]
 */
class ValidationTest {

    @Test(expected = AssertionError::class)
    fun itShouldFailOnVersionError() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.1" // tested error
                        ))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongBufferByteLength() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        buffers = listOf(BufferRaw(
                                byteLength = 0 // tested error
                        )))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongBufferViewBufferRef() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        bufferViews = listOf(BufferViewRaw(
                                buffer = -1, // tested error
                                byteLength = 1
                        )))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongBufferViewByteOffset() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        bufferViews = listOf(BufferViewRaw(
                                buffer = 0,
                                byteOffset = -1, // tested error
                                byteLength = 1
                        )))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongBufferViewByteLength() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        bufferViews = listOf(BufferViewRaw(
                                buffer = 0,
                                byteLength = 0 // tested error
                        )))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongBufferViewByteStride() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        bufferViews = listOf(BufferViewRaw(
                                buffer = 0,
                                byteLength = 1,
                                byteStride = 0 // tested error
                        )))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongBufferViewTarget() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        bufferViews = listOf(BufferViewRaw(
                                buffer = 0,
                                byteLength = 1,
                                target = 0 // tested error
                        )))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongIndicesBufferViewRef() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
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
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongIndicesByteOffset() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
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
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongIndicesComponentType() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
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
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongValuesBufferViewRef() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
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
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongValuesByteOffset() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
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
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongSparseCount() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
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
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongAccessorBufferViewRef() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        accessors = listOf(AccessorRaw(
                                bufferView = -1, // tested error
                                componentType = 5120,
                                count = 1,
                                type = "SCALAR"
                        )))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongAccessorByteOffset() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        accessors = listOf(AccessorRaw(
                                byteOffset = -1, // tested error
                                componentType = 5120,
                                count = 1,
                                type = "SCALAR"
                        )))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongAccessorComponentType() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        accessors = listOf(AccessorRaw(
                                componentType = 0, // tested error
                                count = 1,
                                type = "SCALAR"
                        )))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongAccessorCount() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        accessors = listOf(AccessorRaw(
                                componentType = 5120,
                                count = 0, // tested error
                                type = "SCALAR"
                        )))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongAccessorType() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        accessors = listOf(AccessorRaw(
                                componentType = 5120,
                                count = 1,
                                type = "WRONG" // tested error
                        )))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongAccessorMaxSize() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        accessors = listOf(AccessorRaw(
                                componentType = 5120,
                                count = 1,
                                type = "SCALAR",
                                max = emptyList() // tested error
                        )))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongAccessorMinSize() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        accessors = listOf(AccessorRaw(
                                componentType = 5120,
                                count = 1,
                                type = "SCALAR",
                                min = emptyList() // tested error
                        )))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongSamplerMagFilter() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        samplers = listOf(SamplerRaw(
                                magFilter = 0 // tested error
                        )))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongSamplerMinFilter() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        samplers = listOf(SamplerRaw(
                                minFilter = 0 // tested error
                        )))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongSamplerWrapS() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        samplers = listOf(SamplerRaw(
                                wrapS = 0 // tested error
                        )))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongSamplerWrapT() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        samplers = listOf(SamplerRaw(
                                wrapT = 0 // tested error
                        )))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongImageMimeType() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        images = listOf(ImageRaw(
                                mimeType = "wrong" // tested error
                        )))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongImageBufferViewRef() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        images = listOf(ImageRaw(
                                bufferView = -1 // tested error
                        )))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongTextureSamplerRef() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        textures = listOf(TextureRaw(
                                sampler = -1 // tested error
                        )))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongTextureSourceRef() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        textures = listOf(TextureRaw(
                                source = -1 // tested error
                        )))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongMaterialPbrColor() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        materials = listOf(MaterialRaw(
                                pbrMetallicRoughness = PbrMetallicRoughnessRaw(
                                        baseColorFactor = emptyList() // tested error
                                ))))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongMaterialPbrColorTextureIndex() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        materials = listOf(MaterialRaw(
                                pbrMetallicRoughness = PbrMetallicRoughnessRaw(
                                        baseColorTexture = TextureInfoRaw(
                                                index = -1 // tested error
                                        )
                                ))))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongMaterialPbrColorTextureCoord() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        materials = listOf(MaterialRaw(
                                pbrMetallicRoughness = PbrMetallicRoughnessRaw(
                                        baseColorTexture = TextureInfoRaw(
                                                index = 0,
                                                texCoord = -1 // tested error
                                        )
                                ))))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongMaterialPbrMetallic() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        materials = listOf(MaterialRaw(
                                pbrMetallicRoughness = PbrMetallicRoughnessRaw(
                                        metallicFactor = 2 // tested error
                                ))))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongMaterialPbrRoughness() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        materials = listOf(MaterialRaw(
                                pbrMetallicRoughness = PbrMetallicRoughnessRaw(
                                        roughnessFactor = 2 // tested error
                                ))))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongMaterialPbrMetallicRoughnessTextureIndex() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        materials = listOf(MaterialRaw(
                                pbrMetallicRoughness = PbrMetallicRoughnessRaw(
                                        metallicRoughnessTexture = TextureInfoRaw(
                                                index = -1 // tested error
                                        )
                                ))))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongMaterialPbrMetallicRoughnessTextureCoord() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        materials = listOf(MaterialRaw(
                                pbrMetallicRoughness = PbrMetallicRoughnessRaw(
                                        metallicRoughnessTexture = TextureInfoRaw(
                                                index = 0,
                                                texCoord = -1 // tested error
                                        )
                                ))))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongMaterialEmissiveTextureIndex() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        materials = listOf(MaterialRaw(
                                emissiveTexture = TextureInfoRaw(
                                        index = -1 // tested error
                                ))))))
    }

    @Test(expected = AssertionError::class)
    fun itShouldFailOnWrongMaterialEmissiveTextureCoord() {
        Validator().validate(GltfRaw(
                data = emptyList(),
                gltfAssetRaw = GltfAssetRaw(
                        asset = AssetRaw(
                                version = "2.0"),
                        materials = listOf(MaterialRaw(
                                emissiveTexture = TextureInfoRaw(
                                        index = 0,
                                        texCoord = -1 // tested error
                                ))))))
    }
}