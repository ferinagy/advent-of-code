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

    val hasher = KnotHasher(size)
    lengths.forEach { length ->
        hasher.round(length)
    }

    return hasher.array[0] * hasher.array[1]
}

private fun part2(input: String): String {
    val hasher = KnotHasher(256)

    return hasher.hash(input)
}

private const val testInput1 = """3,4,1,5"""
private const val testInput2 = """1,2,3"""

private const val input = """227,169,3,166,246,201,0,47,1,255,2,254,96,3,97,144"""
