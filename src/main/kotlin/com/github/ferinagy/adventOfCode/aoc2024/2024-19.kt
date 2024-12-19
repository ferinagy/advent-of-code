package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2024, "19-input")
    val test1 = readInputLines(2024, "19-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val towels = input[0].split(", ")
    val designs = input.drop(2)

    return designs.count { check(towels, it) }
}

private fun part2(input: List<String>): Long {
    val towels = input[0].split(", ")
    val designs = input.drop(2)

    val cache = mutableMapOf<String, Long>()
    return designs.sumOf { combinations(towels, it, cache) }
}

private fun check(towels: List<String>, design: String): Boolean {
    if (design.isEmpty()) return true

    return towels.filter { design.startsWith(it) }.any { check(towels, design.substring(it.length)) }
}

private fun combinations(towels: List<String>, design: String, cache: MutableMap<String, Long>): Long {
    cache[design]?.let { return it }
    if (design.isEmpty()) return 1

    return towels
        .sumOf { if (design.startsWith(it)) combinations(towels, design.substring(it.length), cache) else 0 }
        .also { cache[design] = it }
}
