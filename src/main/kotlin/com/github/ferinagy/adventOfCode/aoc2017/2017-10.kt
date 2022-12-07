package com.github.ferinagy.adventOfCode.aoc2017

fun main() {
    println("Part1:")
    println(part1(testInput1, 5))
    println(part1(input, 256))

    println()
    println("Part2:")
    println(part2(testInput2))
    println(part2(input))
}

private fun part1(input: String, size: Int): Int {
    val lengths = input.split(',').map { it.toInt() }

    val hasher = Hasher(size)
    lengths.forEach { length ->
        hasher.knotHash(length)
    }

    return hasher.array[0] * hasher.array[1]
}

private fun part2(input: String): String {
    val lengths = input.map { it.code } + listOf(17, 31, 73, 47, 23)

    val hasher = Hasher(256)
    repeat(64) {
        lengths.forEach { length ->
            hasher.knotHash(length)
        }
    }

    return hasher.array.toList().windowed(16, 16)
        .map { window -> window.reduce { acc, i -> acc xor i } }
        .map { "%02x".format(it) }
        .joinToString(separator = "")
}

private class Hasher(size: Int) {
    val array = IntArray(size) { it }

    private val temp = IntArray(size)

    private var position = 0
    private var skip = 0

    fun knotHash(length: Int) {
        val rest = length.coerceAtMost(array.size - position)
        val overflow = length - rest
        array.copyInto(temp, destinationOffset = 0, startIndex = position, endIndex = position + rest)
        array.copyInto(temp, destinationOffset = rest, startIndex = 0, endIndex = overflow)

        temp.reverse(0, length)

        temp.copyInto(array, destinationOffset = position, startIndex = 0, endIndex = rest)
        temp.copyInto(array, destinationOffset = 0, startIndex = rest, endIndex = length)

        position += length + skip
        position %= array.size
        skip++
    }
}

private const val testInput1 = """3,4,1,5"""
private const val testInput2 = """1,2,3"""

private const val input = """227,169,3,166,246,201,0,47,1,255,2,254,96,3,97,144"""
