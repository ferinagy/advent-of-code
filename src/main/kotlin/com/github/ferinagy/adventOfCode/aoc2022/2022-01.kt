package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2022, "01-input")
    val testInput1 = readInputText(2022, "01-test1")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(testInput1).println()
    part2(input).println()
}

private fun part1(input: String): Int? {
    val elves = input.split("\n\n")
    val calories = elves.map { it.lines().map(String::toInt).sum() }

    return calories.maxOrNull()
}

private fun part2(input: String): Int {
    val elves = input.split("\n\n")
    val calories = elves.map { it.lines().map(String::toInt).sum() }

    return calories.sortedDescending().take(3).sum()
}
