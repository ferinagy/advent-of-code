package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2024, "03-input")
    val test1 = readInputText(2024, "03-test1")
    val test2 = readInputText(2024, "03-test2")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test2).println()
    part2(input).println()
}

private fun part1(input: String): Int {
    val regex = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()
    return regex.findAll(input).sumOf { multiply(it) }
}

private fun part2(input: String): Int {
    val regex = """mul\((\d{1,3}),(\d{1,3})\)|do\(\)|don't\(\)""".toRegex()
    return regex.findAll(input).fold(0 to true) { (sum, enabled), match ->
        when (match.value) {
            "do()" -> sum to true
            "don't()" -> sum to false
            else -> (sum + if (enabled) multiply(match) else 0) to enabled
        }
    }.first
}

private fun multiply(match: MatchResult) = match.groups[1]!!.value.toInt() * match.groups[2]!!.value.toInt()
