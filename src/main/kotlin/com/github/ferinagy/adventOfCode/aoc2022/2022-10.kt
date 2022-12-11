package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.toBooleanGrid

fun main() {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput1))
    println(part2(input))
}

private fun part1(input: String): Int {
    val signals = calculateCycles(input)

    return (0 until 6).sumOf { signals.signalStrength(20 + it * 40) }
}

private fun part2(input: String): String {
    val signals = calculateCycles(input)

    val pixels = signals.drop(1).mapIndexed { index, pos -> (index % 40) in pos - 1..pos + 1 }
    return pixels.windowed(40, 40).toBooleanGrid().toString() // ZFBFHGUP
}

private fun calculateCycles(input: String): MutableList<Int> {
    val signals = mutableListOf(1)
    var x = 1
    input.lines().forEach { line ->
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

private const val testInput1 = """addx 15
addx -11
addx 6
addx -3
addx 5
addx -1
addx -8
addx 13
addx 4
noop
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx -35
addx 1
addx 24
addx -19
addx 1
addx 16
addx -11
noop
noop
addx 21
addx -15
noop
noop
addx -3
addx 9
addx 1
addx -3
addx 8
addx 1
addx 5
noop
noop
noop
noop
noop
addx -36
noop
addx 1
addx 7
noop
noop
noop
addx 2
addx 6
noop
noop
noop
noop
noop
addx 1
noop
noop
addx 7
addx 1
noop
addx -13
addx 13
addx 7
noop
addx 1
addx -33
noop
noop
noop
addx 2
noop
noop
noop
addx 8
noop
addx -1
addx 2
addx 1
noop
addx 17
addx -9
addx 1
addx 1
addx -3
addx 11
noop
noop
addx 1
noop
addx 1
noop
noop
addx -13
addx -19
addx 1
addx 3
addx 26
addx -30
addx 12
addx -1
addx 3
addx 1
noop
noop
noop
addx -9
addx 18
addx 1
addx 2
noop
noop
addx 9
noop
noop
noop
addx -1
addx 2
addx -37
addx 1
addx 3
noop
addx 15
addx -21
addx 22
addx -6
addx 1
noop
addx 2
addx 1
noop
addx -10
noop
noop
addx 20
addx 1
addx 2
addx 2
addx -6
addx -11
noop
noop
noop"""

private const val input = """addx 2
addx 3
addx 1
noop
addx 4
noop
noop
noop
addx 5
noop
addx 1
addx 4
addx -2
addx 3
addx 5
addx -1
addx 5
addx 3
addx -2
addx 4
noop
noop
noop
addx -27
addx -5
addx 2
addx -7
addx 3
addx 7
addx 5
addx 2
addx 5
noop
noop
addx -2
noop
addx 3
addx 2
addx 5
addx 2
addx 3
noop
addx 2
addx -29
addx 30
addx -26
addx -10
noop
addx 5
noop
addx 18
addx -13
noop
noop
addx 5
noop
noop
addx 5
noop
noop
noop
addx 1
addx 2
addx 7
noop
noop
addx 3
noop
addx 2
addx 3
noop
addx -37
noop
addx 16
addx -12
addx 29
addx -16
addx -10
addx 5
addx 2
addx -11
addx 11
addx 3
addx 5
addx 2
addx 2
addx -1
addx 2
addx 5
addx 2
noop
noop
noop
addx -37
noop
addx 17
addx -10
addx -2
noop
addx 7
addx 3
noop
addx 2
addx -10
addx 22
addx -9
addx 5
addx 2
addx -5
addx 6
addx 2
addx 5
addx 2
addx -28
addx -7
noop
noop
addx 1
addx 4
addx 17
addx -12
noop
noop
noop
noop
addx 5
addx 6
noop
addx -1
addx -17
addx 18
noop
addx 5
noop
noop
noop
addx 5
addx 4
addx -2
noop
noop
noop
noop
noop"""
