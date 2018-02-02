package com.adrien.tools.gltf

/**
 * Mappers common interface.
 */
interface Mapper<in I, out O> {
    fun map(input: I): O
}

/**
 * [ComponentType] mapper.
 */
object ComponentTypeMapper : Mapper<Int, ComponentType> {
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
object TypeMapper : Mapper<String, Type> {
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
object AlphaModeMapper : Mapper<String, AlphaMode> {
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
object FilterMapper : Mapper<Int, Filter> {
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
object WrapModeMapper : Mapper<Int, WrapMode> {
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
object BufferTargetMapper : Mapper<Int, BufferTarget> {
    override fun map(input: Int) = when (input) {
        BufferTarget.ARRAY_BUFFER.code -> BufferTarget.ARRAY_BUFFER
        BufferTarget.ELEMENT_ARRAY_BUFFER.code -> BufferTarget.ELEMENT_ARRAY_BUFFER
        else -> throw UnsupportedOperationException("Unsupported buffer target $input")
    }
}
