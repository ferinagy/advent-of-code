package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2021, "01-input")
    val test1 = readInputLines(2021, "01-test1")

    println("Part1:")
    countIncreases(test1).println()
    countIncreases(input).println()

    println()
    println("Part2:")
    countMeasurementWindows(test1).println()
    countMeasurementWindows(input).println()
}

private fun countIncreases(input: List<String>): Int {
    return input
        .map { it.toInt() }
        .windowed(2)
        .count { (x, y) -> x < y }
}

private fun countMeasurementWindows(input: List<String>): Int {
    return input
        .asSequence()
        .map { it.toInt() }
        .windowed(3)
        .map { it.sum() }
        .windowed(2)
        .count { (x, y) -> x < y }
}
