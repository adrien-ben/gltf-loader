package com.adrienben.tools.gltf.models

import kotlin.math.sqrt

/**
 * 3-float vector.
 */
class GltfVec3(val x: Float = 0f, val y: Float = 0f, val z: Float = 0f) {

    /**
     * Get the length of the vector.
     */
    internal fun length() = sqrt(x * x + y * y + z * z)

    internal companion object Factory {

        /**
         * Generate a vector from a list of [Number]s. The list must contain 3 elements
         */
        fun fromNumbers(numbers: List<Number>): GltfVec3 {
            if (numbers.size != 3) throw IllegalArgumentException("numbers contains ${numbers.size} elements instead of 3")
            return GltfVec3(numbers[0].toFloat(), numbers[1].toFloat(), numbers[2].toFloat())
        }
    }
}

/**
 * Float quaternion.
 */
class GltfQuaternion(val i: Float = 0f, val j: Float = 0f, val k: Float = 0f, val a: Float = 1f) {

    internal companion object Factory {

        /**
         * Generate a quaternion from a list of [Number]s. The list must contain 4 elements
         */
        fun fromNumbers(numbers: List<Number>): GltfQuaternion {
            if (numbers.size != 4) throw IllegalArgumentException("numbers contains ${numbers.size} elements instead of 4")
            return GltfQuaternion(numbers[0].toFloat(), numbers[1].toFloat(), numbers[2].toFloat(), numbers[3].toFloat())
        }
    }
}

/**
 * 4x4-float matrix
 */
class GltfMat4(
        val m00: Float = 1f, val m01: Float = 0f, val m02: Float = 0f, val m03: Float = 0f, // first column
        val m10: Float = 0f, val m11: Float = 1f, val m12: Float = 0f, val m13: Float = 0f, // second column
        val m20: Float = 0f, val m21: Float = 0f, val m22: Float = 1f, val m23: Float = 0f, // third column
        val m30: Float = 0f, val m31: Float = 0f, val m32: Float = 0f, val m33: Float = 1f  // fourth column
) {

    /**
     * Extract the translation from this matrix.
     */
    internal fun translation() = GltfVec3(m30, m31, m32)

    /**
     * Extract the rotation from this matrix.
     */
    internal fun rotation(): GltfQuaternion {
        val trace = m00 + m11 + m22
        return when {
            trace > 0f -> {
                val s = sqrt(trace + 1f) * 2f
                GltfQuaternion((m21 - m12) / s, (m02 - m20) / s, (m10 - m01) / s, 0.25f * s)
            }
            (m00 > m11) and (m00 > m22) -> {
                val s = sqrt(1f + m00 - m11 - m22) * 2f
                GltfQuaternion(0.25f * s, (m01 + m10) / s, (m02 + m20) / s, (m21 - m12) / s)
            }
            m11 > m22 -> {
                val s = sqrt(1f + m11 - m00 - m22) * 2f
                GltfQuaternion((m01 + m10) / s, 0.25f * s, (m12 + m21) / s, (m02 - m20) / s)
            }
            else -> {
                val s = sqrt(1f + m22 - m00 - m11) * 2f
                GltfQuaternion((m02 + m20) / s, (m12 + m21) / s, 0.25f * s, (m10 - m01) / s)
            }
        }
    }

    /**
     * Extract the scale from this matrix.
     */
    internal fun scale() = GltfVec3(
            GltfVec3(m00, m01, m02).length(),
            GltfVec3(m10, m11, m12).length(),
            GltfVec3(m20, m21, m22).length()
    )

    internal companion object Factory {

        /**
         * Generate a matrix from a list of [Number]s. The list must contain 16 elements
         */
        fun fromNumbers(matrix: List<Number>): GltfMat4 {
            if (matrix.size != 16) throw IllegalArgumentException("matrix contains ${matrix.size} elements instead of 16")
            return GltfMat4(
                    matrix[0].toFloat(), matrix[1].toFloat(), matrix[2].toFloat(), matrix[3].toFloat(),
                    matrix[4].toFloat(), matrix[5].toFloat(), matrix[6].toFloat(), matrix[7].toFloat(),
                    matrix[8].toFloat(), matrix[9].toFloat(), matrix[10].toFloat(), matrix[11].toFloat(),
                    matrix[12].toFloat(), matrix[13].toFloat(), matrix[14].toFloat(), matrix[15].toFloat())
        }
    }
}
