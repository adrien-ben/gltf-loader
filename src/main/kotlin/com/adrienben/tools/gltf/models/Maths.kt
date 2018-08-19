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
         * Construct a quaternion from a matrix by first normalizing the rotation part and then extracting the quaternion.
         */
        fun fromMatrix(matrix: GltfMat4): GltfQuaternion {
            return matrix.run {
                var nm00 = m00
                var nm01 = m01
                var nm02 = m02
                var nm10 = m10
                var nm11 = m11
                var nm12 = m12
                var nm20 = m20
                var nm21 = m21
                var nm22 = m22
                val lenX = (1.0 / Math.sqrt((m00 * m00 + m01 * m01 + m02 * m02).toDouble())).toFloat()
                val lenY = (1.0 / Math.sqrt((m10 * m10 + m11 * m11 + m12 * m12).toDouble())).toFloat()
                val lenZ = (1.0 / Math.sqrt((m20 * m20 + m21 * m21 + m22 * m22).toDouble())).toFloat()
                nm00 *= lenX; nm01 *= lenX; nm02 *= lenX
                nm10 *= lenY; nm11 *= lenY; nm12 *= lenY
                nm20 *= lenZ; nm21 *= lenZ; nm22 *= lenZ
                fromNormalizedMatrix(nm00, nm01, nm02, nm10, nm11, nm12, nm20, nm21, nm22)
            }
        }

        /**
         * Construct a quaternion from a the rotation part of a 4x4 matrix.
         */
        fun fromNormalizedMatrix(m00: Float, m01: Float, m02: Float, m10: Float, m11: Float, m12: Float, m20: Float, m21: Float, m22: Float): GltfQuaternion {
            val tr = m00 + m11 + m22
            return when {
                tr > 0f -> {
                    var t = Math.sqrt((tr + 1.0f).toDouble()).toFloat()
                    val w = t * 0.5f
                    t = 0.5f / t
                    GltfQuaternion((m12 - m21) * t, (m20 - m02) * t, (m01 - m10) * t, w)
                }
                (m00 >= m11 && m00 >= m22) -> {
                    var t = Math.sqrt(m00 - (m11 + m22) + 1.0).toFloat()
                    val x = t * 0.5f
                    t = 0.5f / t
                    GltfQuaternion(x, (m10 + m01) * t, (m02 + m20) * t, (m12 - m21) * t)
                }
                m11 > m22 -> {
                    var t = Math.sqrt(m11 - (m22 + m00) + 1.0).toFloat()
                    val y = t * 0.5f
                    t = 0.5f / t
                    GltfQuaternion((m10 + m01) * t, y, (m21 + m12) * t, (m20 - m02) * t)
                }
                else -> {
                    var t = Math.sqrt(m22 - (m00 + m11) + 1.0).toFloat()
                    val z = t * 0.5f
                    t = 0.5f / t
                    GltfQuaternion((m02 + m20) * t, (m21 + m12) * t, z, (m01 - m10) * t)
                }
            }
        }

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
    internal fun rotation() = GltfQuaternion.fromMatrix(this)

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
