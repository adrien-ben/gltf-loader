package com.adrienben.tools.gltf.validation

import com.adrienben.tools.gltf.models.AssetRaw
import com.adrienben.tools.gltf.models.GltfAssetRaw
import com.adrienben.tools.gltf.models.GltfRaw
import org.junit.Test

/**
 * Test class for [Validator]
 */
class ValidationTest {

    @Test(expected = AssertionError::class)
    fun itShouldFailOnVersionError() {
        Validator().validate(GltfRaw(GltfAssetRaw(asset = AssetRaw(version = "2.1")), emptyList()))
    }
}