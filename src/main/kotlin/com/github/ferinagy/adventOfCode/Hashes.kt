package com.github.ferinagy.adventOfCode

import java.security.MessageDigest
import kotlin.experimental.and

private val MD5 = MessageDigest.getInstance("MD5")

fun String.md5toBytes() = toByteArray().md5()

fun ByteArray.md5(): ByteArray = MD5.digest(this)

fun ByteArray.startsWithZeros(count: Int): Boolean {
    val zeroBytes = count / 2

    var index = 0
    repeat(zeroBytes) {
        if (get(it) != 0.toByte()) return false
        index++
    }

    if (count % 2 == 1) return 0 <= get(index) && get(index) <= 15

    return true
}

operator fun Byte.component1(): Byte = (toUByte() / 16u).toByte()

operator fun Byte.component2(): Byte = this and 0xf
