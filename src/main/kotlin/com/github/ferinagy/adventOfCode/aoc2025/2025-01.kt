package com.github.ferinagy.adventOfCode.aoc2025

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import kotlin.math.abs

fun main() {
    val input = readInputLines(2025, "01-input")
    val test1 = readInputLines(2025, "01-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    var position = 50
    var result = 0
    val rotations = input.map { parse(it) }

    rotations.forEach {
        position += it
        position = position.mod(100)
        if (position == 0) result++
    }

    return result
}

private fun part2(input: List<String>): Int {
    var position = 50
    var result = 0
    val rotations = input.map { parse(it) }

    rotations.forEach { count ->
        result += abs(count / 100)
        val rem = count % 100
        val next = position + rem
        if (next < 0 && position != 0 || next > 100) {
            result++
        }

        position = next.mod(100)
        if (position == 0) {
            result++
        }
    }

    return result
}

private fun parse(line: String) = line.drop(1).toInt() * if (line.first() == 'L') -1 else 1