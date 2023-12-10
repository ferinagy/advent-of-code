package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2022, "06-input")
    val testInput1 = readInputText(2022, "06-test1")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(testInput1).println()
    part2(input).println()
}

private fun part1(input: String): Int {
    return findMarker(input, 4)
}

private fun part2(input: String): Int {
    return findMarker(input, 14)
}

private fun findMarker(input: String, size: Int) =
    input.windowed(size).indexOfFirst { it.toSet().size == it.length } + size
