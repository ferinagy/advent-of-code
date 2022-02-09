package com.github.ferinagy.adventOfCode.aoc2016

import com.github.ferinagy.adventOfCode.component1
import com.github.ferinagy.adventOfCode.component2
import com.github.ferinagy.adventOfCode.md5
import com.github.ferinagy.adventOfCode.md5toBytes

fun main() {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput1))
    println(part2(input))
}

private fun part1(input: String): Int {
    return solve(input) { it.md5toBytes().toHexByteArray() }
}

private fun part2(input: String): Int {
    return solve(input) { hash2(it) }
}

private fun solve(input: String, hash: (String) -> ByteArray): Int {
    val hashes = mutableListOf<ByteArray>()
    var keys = 0
    var index = 0

    repeat(25_000) {
        hashes += hash("$input$it")
    }

    while (keys < 64) {
        val current = hashes[index]
        val triple = current.getTriple()
        if (triple != null) {
            for (i in index + 1..index + 1000) {
                val next = hashes[i]
                if (next.hasQuintet(triple)) {
                    keys++
                    break
                }
            }
        }
        index++
    }
    return index - 1
}

private fun ByteArray.getTriple(): Byte? {
    for (i in 0..size - 3) {
        if (get(i) == get(i + 1) && get(i) == get(i + 2)) return get(i)
    }

    return null
}

private fun ByteArray.hasQuintet(byte: Byte): Boolean {
    for (i in 0..size - 5) {
        if (get(i) == get(i + 1)
            && get(i) == get(i + 2)
            && get(i) == get(i + 3)
            && get(i) == get(i + 4)
            && get(i) == byte
        ) {
            return true
        }
    }

    return false
}

private fun hash2(input: String): ByteArray {
    var hash = input.md5toBytes()
    repeat(2016) {
        hash = hash.toHexByteArray().md5()
    }
    return hash.toHexByteArray()
}

private fun ByteArray.toHexByteArray(): ByteArray {
    val result = ByteArray(size * 2)

    for (i in indices) {
        val (high, low) = get(i)
        result[2 * i] = high.toHexByte()
        result[2 * i + 1] = low.toHexByte()
    }

    return result
}

private fun Byte.toHexByte(): Byte = if (this < 10) (this + 48).toByte() else (this + 87).toByte()

private const val testInput1 = """abc"""

private const val input = """ahsbgdzn"""
