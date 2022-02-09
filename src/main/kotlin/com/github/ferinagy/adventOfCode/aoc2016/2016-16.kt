package com.github.ferinagy.adventOfCode.aoc2016

import java.util.*

fun main() {
    println("Part1:")
    println(solve2(testInput1, 20))
    println(solve2(input, 272))

    println()
    println("Part2:")
    println(solve2(input, 35651584))
}

private fun solve2(input: String, length: Int): String {
    val bits = BitSet(length / 64 + 1)
    input.forEachIndexed { index, c -> bits.set(index, c == '1') }
    var size = input.length

    while (size < length) {
        size = bits.grow(size)
    }
    size = length

    while (size % 2 == 0) {
        size = bits.checkSum(size)
    }

    return buildString {
        for (i in 0 until size) {
            if (bits.get(i)) append('1') else append('0')
        }
    }
}

private fun BitSet.grow(size: Int): Int {
    set(size, false)
    for (i in 0 until size) {
        val bit = get(size - i - 1)
        set(size + 1 + i, !bit)
    }

    return size * 2 + 1
}

private fun BitSet.checkSum(size: Int): Int {
    val newSize = size / 2
    for (i in 0 until newSize) {
        val new = get(2 * i) == get(2 * i + 1)
        set(i, new)
    }
    return size / 2
}

private const val testInput1 = """10000"""

private const val input = """10111011111001111"""
