package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import com.github.ferinagy.adventOfCode.toBooleanGrid

fun main() {
    val input = readInputLines(2022, "10-input")
    val testInput1 = readInputLines(2022, "10-test1")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(testInput1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val signals = calculateCycles(input)

    return (0 until 6).sumOf { signals.signalStrength(20 + it * 40) }
}

private fun part2(input: List<String>): String {
    val signals = calculateCycles(input)

    val pixels = signals.drop(1).mapIndexed { index, pos -> (index % 40) in pos - 1..pos + 1 }
    return pixels.windowed(40, 40).toBooleanGrid().toString() // ZFBFHGUP
}

private fun calculateCycles(input: List<String>): MutableList<Int> {
    val signals = mutableListOf(1)
    var x = 1
    input.forEach { line ->
        val parts = line.split(' ')
        when (parts[0]) {
            "addx" -> {
                signals += x
                signals += x
                x += parts[1].toInt()
            }

            "noop" -> {
                signals += x
            }
        }
    }
    return signals
}

private fun List<Int>.signalStrength(cycle: Int): Int = cycle * get(cycle)
