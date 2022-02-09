package com.github.ferinagy.adventOfCode.aoc2016

import java.util.*

fun main() {
    println("Part1:")
    println(solve(testInput1, 10))
    println(solve(input, 40))

    println()
    println("Part2:")
    println(solve(input, 400000))
}

private fun solve(input: String, height: Int): Int {
    val width = input.length

    var result = 0

    var bits = BitSet(width)
    input.forEachIndexed { index, c -> bits.set(index, c == '^') }

    result += width - bits.cardinality()
    for (y in 1 until height) {
        bits = bits.nextRow(width)
        result += width - bits.cardinality()
    }

    return result
}

private fun BitSet.nextRow(size: Int): BitSet {
    val result = BitSet(size)
    for (x in 0 until size) {
        val l = if (x != 0) get(x-1) else false
        val c = get(x)
        val r = if (x != size - 1) get(x+1) else false

        result.set(x, l && c && !r || c && r && !l || l && !c && !r || r && !c && !l)
    }
    return result
}

private const val testInput1 = """.^^.^.^^^^"""

private const val input = """^.^^^.^..^....^^....^^^^.^^.^...^^.^.^^.^^.^^..^.^...^.^..^.^^.^..^.....^^^.^.^^^..^^...^^^...^...^."""
