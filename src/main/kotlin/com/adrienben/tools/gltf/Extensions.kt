package com.adrienben.tools.gltf

import java.util.*

private val DATA_URI_REGEX = Regex("data:.*(?:;base64)?,(.*)")

/**
 * String extensions to decode a models URI if it matches the models uri pattern.
 *
 * It returns a [ByteArray] or null if the string does not match the required pattern.
 */
internal fun String.decodeDataUri(): ByteArray? {
    val base64 = DATA_URI_REGEX.find(this)?.groupValues?.get(1) ?: return null
    return Base64.getDecoder().decode(base64)
}
