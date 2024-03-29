package com.github.ferinagy.adventOfCode

import java.security.MessageDigest
import kotlin.experimental.and

fun String.md5toBytes() = toByteArray().md5()

fun ByteArray.md5(): ByteArray {
    val md = MessageDigest.getInstance("MD5")

    return md.digest(this)
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

operator fun Byte.component1(): Byte = (toUByte() / 16u).toByte()

operator fun Byte.component2(): Byte = this and 0xf
