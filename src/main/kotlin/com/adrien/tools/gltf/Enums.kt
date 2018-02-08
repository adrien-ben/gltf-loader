package com.adrien.tools.gltf

/**
 * Component types with their byte size and their original constant value.
 */
enum class GltfComponentType(val byteSize: Int, val code: Int) {
    BYTE(1, 5120),
    UNSIGNED_BYTE(1, 5121),
    SHORT(2, 5122),
    UNSIGNED_SHORT(2, 5123),
    UNSIGNED_INT(4, 5125),
    FLOAT(4, 5126);

    companion object Factory {

        /**
         * Generate a component type from a gltf code.
         */
        fun fromCode(code: Int) = when (code) {
            BYTE.code -> BYTE
            UNSIGNED_BYTE.code -> UNSIGNED_BYTE
            SHORT.code -> SHORT
            UNSIGNED_SHORT.code -> UNSIGNED_SHORT
            UNSIGNED_INT.code -> UNSIGNED_INT
            FLOAT.code -> FLOAT
            else -> throw IllegalArgumentException("Unknown component type $code")
        }
    }
}

/**
 * Element types with their number of component and their original constant value.
 */
enum class GltfType(val componentCount: Int, val code: String) {
    SCALAR(1, "SCALAR"),
    VEC2(2, "VEC2"),
    VEC3(3, "VEC3"),
    VEC4(4, "VEC4"),
    MAT2(4, "MAT2"),
    MAT3(9, "MAT3"),
    MAT4(16, "MAT4");

    companion object Factory {

        /**
         * Generate a type from a gltf code.
         */
        fun fromCode(code: String) = when (code) {
            SCALAR.code -> SCALAR
            VEC2.code -> VEC2
            VEC3.code -> VEC3
            VEC4.code -> VEC4
            MAT2.code -> MAT2
            MAT3.code -> MAT3
            MAT4.code -> MAT4
            else -> throw IllegalArgumentException("Unknown type $code")
        }
    }
}

/**
 * Alpha rendering mode.
 */
enum class GltfAlphaMode(val code: String) {
    OPAQUE("OPAQUE"),
    MASK("MASK"),
    BLEND("BLEND");

    companion object Factory {

        /**
         * Generate a alpha mode from a gltf code.
         */
        fun fromCode(code: String) = when (code) {
            OPAQUE.code -> OPAQUE
            MASK.code -> MASK
            BLEND.code -> BLEND
            else -> throw IllegalArgumentException("Unknown alpha mode $code")
        }
    }
}

/**
 * Texture filtering mode. Mipmap modes should only be used as magnification
 * filter.
 */
enum class GltfFilter(val code: Int) {
    NEAREST(9728),
    LINEAR(9729),
    NEAREST_MIPMAP_NEAREST(9984),
    LINEAR_MIPMAP_NEAREST(9985),
    NEAREST_MIPMAP_LINEAR(9986),
    LINEAR_MIPMAP_LINEAR(9987);

    companion object Factory {

        /**
         * Generate a filter from a gltf code.
         */
        fun fromCode(code: Int) = when (code) {
            NEAREST.code -> NEAREST
            LINEAR.code -> LINEAR
            NEAREST_MIPMAP_NEAREST.code -> NEAREST_MIPMAP_NEAREST
            LINEAR_MIPMAP_NEAREST.code -> LINEAR_MIPMAP_NEAREST
            NEAREST_MIPMAP_LINEAR.code -> NEAREST_MIPMAP_LINEAR
            LINEAR_MIPMAP_LINEAR.code -> LINEAR_MIPMAP_LINEAR
            else -> throw IllegalArgumentException("Unknown texture filter $code")
        }
    }
}

/**
 * Texture wrap mode.
 */
enum class GltfWrapMode(val code: Int) {
    CLAMP_TO_EDGE(33071),
    MIRRORED_REPEAT(33648),
    REPEAT(10497);

    companion object Factory {

        /**
         * Generate a wrap mode from a gltf code.
         */
        fun fromCode(code: Int) = when (code) {
            CLAMP_TO_EDGE.code -> CLAMP_TO_EDGE
            MIRRORED_REPEAT.code -> MIRRORED_REPEAT
            REPEAT.code -> REPEAT
            else -> throw IllegalArgumentException("Unknown texture wrap mode $code")
        }
    }
}

/**
 * GPU buffer binding target.
 */
enum class GltfBufferTarget(val code: Int) {
    ARRAY_BUFFER(34962),
    ELEMENT_ARRAY_BUFFER(34963);

    companion object Factory {

        /**
         * Generate a buffer target from a gltf code.
         */
        fun fromCode(code: Int) = when (code) {
            ARRAY_BUFFER.code -> ARRAY_BUFFER
            ELEMENT_ARRAY_BUFFER.code -> ELEMENT_ARRAY_BUFFER
            else -> throw IllegalArgumentException("Unknown buffer target $code")
        }
    }
}

/**
 * The type of primitive to render.
 */
enum class GltfPrimitiveMode(val code: Int) {
    POINTS(0),
    LINES(1),
    LINE_LOOP(2),
    LINE_STRIP(3),
    TRIANGLES(4),
    TRIANGLE_STRIP(5),
    TRIANGLE_FAN(6);

    companion object Factory {

        /**
         * Generate a primitive mode from a gltf code.
         */
        fun fromCode(code: Int) = when (code) {
            POINTS.code -> POINTS
            LINES.code -> LINES
            LINE_LOOP.code -> LINE_LOOP
            LINE_STRIP.code -> LINE_STRIP
            TRIANGLES.code -> TRIANGLES
            TRIANGLE_STRIP.code -> TRIANGLE_STRIP
            TRIANGLE_FAN.code -> TRIANGLE_FAN
            else -> throw IllegalArgumentException("Unknown primitive mode $code")
        }
    }
}

/**
 * Mime types
 */
enum class GltfMimeType(val code: String) {
    JPEG("image/jpeg"),
    PNG("image/png");

    companion object Factory {

        /**
         * Generate a mime type from a gltf code.
         */
        fun fromCode(code: String) = when (code) {
            JPEG.code -> JPEG
            PNG.code -> PNG
            else -> throw IllegalArgumentException("Unknown mime type $code")
        }
    }
}

/**
 * Camera types.
 */
enum class GltfCameraType(val code: String) {
    ORTHOGRAPHIC("orthographic"),
    PERSPECTIVE("perspective");

    companion object Factory {

        /**
         * Generate a camera type from a gltf code.
         */
        fun fromCode(code: String) = when (code) {
            ORTHOGRAPHIC.code -> ORTHOGRAPHIC
            PERSPECTIVE.code -> PERSPECTIVE
            else -> throw IllegalArgumentException("Unknown camera type $code")
        }
    }
}

/**
 * Animation target paths.
 */
enum class GltfAnimationTargetPath(val code: String) {
    TRANSLATION("translation"),
    ROTATION("rotation"),
    SCALE("scale"),
    WEIGHTS("weights");

    companion object {

        /**
         * Generate an animation target path from a gltf code.
         */
        fun fromCode(code: String) = when (code) {
            TRANSLATION.code -> TRANSLATION
            ROTATION.code -> ROTATION
            SCALE.code -> SCALE
            WEIGHTS.code -> WEIGHTS
            else -> throw IllegalArgumentException("Unknown target path $code")
        }
    }
}

/**
 * Interpolation types.
 */
enum class GltfInterpolationType(val code: String) {
    LINEAR("LINEAR"),
    STEP("STEP"),
    CUBICSPLINE("CUBICSPLINE");

    companion object Factory {

        /**
         * Generate an interpolation type from a gltf code.
         */
        fun fromCode(code: String) = when (code) {
            LINEAR.code -> LINEAR
            STEP.code -> STEP
            CUBICSPLINE.code -> CUBICSPLINE
            else -> throw IllegalArgumentException("Unknown interpolation type $code")
        }
    }
}
