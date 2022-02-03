package com.github.ferinagy.adventOfCode

import java.security.MessageDigest

fun String.md5toBytes(): ByteArray {
    val md = MessageDigest.getInstance("MD5")

    return md.digest(toByteArray())
}

private val ZERO_BYTE: Byte = 0
private val byteWithFirstZeroRange = 0..15

fun ByteArray.startsWithZeros(count: Int): Boolean {
    val zeroBytes = count / 2

    var index = 0
    repeat(zeroBytes) {
        if (get(it) != ZERO_BYTE) return false
        index++
    }

    if (count % 2 == 1) return get(index) in byteWithFirstZeroRange

    return true
}

fun ByteArray.toHexString() = joinToString(separator = "") { "%02x".format(it) }
