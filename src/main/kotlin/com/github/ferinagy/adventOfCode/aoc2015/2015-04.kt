package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.md5toBytes
import com.github.ferinagy.adventOfCode.startsWithZeros

fun main() {
    println("Part1:")
    println(starsWithZeros(testInput1, 5))
    println(starsWithZeros(input, 5))

    println()
    println("Part2:")
    println(starsWithZeros(testInput1, 6))
    println(starsWithZeros(input, 6))
}

private fun starsWithZeros(input: String, num: Int): Int {
    var n = 1
    while (true) {
        val hash = (input + n).md5toBytes()

        if (hash.startsWithZeros(num)) return n

        n++
    }
}

private const val testInput1 = """abcdef"""

private const val input = """bgvyzdsv"""
