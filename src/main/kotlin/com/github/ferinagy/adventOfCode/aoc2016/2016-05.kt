package com.github.ferinagy.adventOfCode.aoc2016

import com.github.ferinagy.adventOfCode.*

fun main() {
    val input = readInputText(2016, "05-input")
    val test1 = readInputText(2016, "05-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(input).println()
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
