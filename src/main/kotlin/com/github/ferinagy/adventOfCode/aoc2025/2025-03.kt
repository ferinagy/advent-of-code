package com.github.ferinagy.adventOfCode.aoc2025

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2025, "03-input")
    val test1 = readInputLines(2025, "03-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>) = input.sumOf {
    it.maxJoltage(2).toLong()
}

private fun part2(input: List<String>) = input.sumOf {
    it.maxJoltage(12).toLong()
}

private fun String.maxJoltage(n: Int): String = if (n == 0) "" else {
    val m1 = substring(0, length - n + 1).max()

    val m2 = substring(indexOf(m1) + 1).maxJoltage(n - 1)

    m1 + m2
}
