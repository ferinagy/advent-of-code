package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.*

fun main() {
    val input = readInputLines(2024, "18-input")
    val test1 = readInputLines(2024, "18-test1")

    println("Part1:")
    part1(test1, 7, 12).println()
    part1(input, 71, 1024).println()

    println()
    println("Part2:")
    part2(test1, 7).println()
    part2(input, 71).println()
}

private fun part1(input: List<String>, width: Int, count: Int): Int {
    val coords = input.map { it.split(',').let { Coord2D(it[0].toInt(), it[1].toInt()) } }.take(count).toSet()
    val range = 0..<width

    return searchGraph(
        start = Coord2D(0, 0),
        isDone = { it == Coord2D(width - 1, width - 1) },
        nextSteps = {
            it.adjacent(false).filter { it.x in range && it.y in range && it !in coords }.toSet().singleStep()
        }
    )
}

private fun part2(input: List<String>, width: Int): String {
    var l = 0
    var r = input.lastIndex
    while (l < r) {
        val next = (l + r) / 2
        if (part1(input, width, next) == -1) r = next - 1 else l = next + 1
    }
    return input[r]
}
