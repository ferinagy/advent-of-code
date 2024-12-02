package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import kotlin.math.abs

fun main() {
    val input = readInputLines(2024, "02-input")
    val test1 = readInputLines(2024, "02-test")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val levels = input.map { it.split(' ').map { it.toInt() } }

    return levels.count { isSafe(it) }
}

private fun part2(input: List<String>): Int {
    val levels = input.map { it.split(' ').map { it.toInt() } }

    return levels.count { isSafe2(it) }
}

private fun isSafe(levels: List<Int>): Boolean {
    val diffs = levels.windowed(size = 2) { (a, b) -> a - b }
    return diffs.all { abs(it) in 1 .. 3 } && (diffs.all { it > 0 } || diffs.all { it < 0 })
}

private fun isSafe2(levels: List<Int>): Boolean {
    return isSafe(levels) || levels.indices.any { isSafe(levels.toMutableList().apply { removeAt(it) }) }
}
