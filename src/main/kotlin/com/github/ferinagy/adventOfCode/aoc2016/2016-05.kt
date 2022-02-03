package com.github.ferinagy.adventOfCode.aoc2016

import com.github.ferinagy.adventOfCode.md5toBytes
import com.github.ferinagy.adventOfCode.startsWithZeros
import com.github.ferinagy.adventOfCode.toHexString

fun main() {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput1))
    println(part2(input))
}

private fun part1(input: String): String {
    var index = 0
    var result = ""
    while (true) {
        val hash = (input + index).md5toBytes()
        index++
        if (hash.startsWithZeros(5)) {
            result += hash.toHexString()[5]
            if (result.length == 8) return result
        }
    }

}

private fun part2(input: String): String {
    var index = 0
    var result = "________"
    while (true) {
        val hash = (input + index).md5toBytes()
        index++
        if (hash.startsWithZeros(5)) {
            val hex = hash.toHexString()
            val position = hex[5].digitToIntOrNull() ?: continue
            if (7 < position || result[position] != '_') continue

            result = result.substring(0, position) + hex[6] + result.substring(position + 1)

            if ('_' !in result) return result
        }
    }
}

private const val testInput1 = """abc"""

private const val input = """reyedfim"""
