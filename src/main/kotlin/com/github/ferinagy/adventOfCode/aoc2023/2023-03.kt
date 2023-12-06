package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2023, "03-input")
    val test1 = readInputLines(2023, "03-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val numberRegex = """\d+""".toRegex()
    val symbolRegex = """[^\d.]""".toRegex()

    val symbols = mutableSetOf<Coord2D>()
    val numbers = mutableSetOf<Pair<Int, IntRange>>()

    input.forEachIndexed { index, line ->
        numberRegex.findAll(line).forEach {
            numbers += index to it.range
        }

        symbolRegex.findAll(line).forEach {
            symbols += Coord2D(it.range.first, index)
        }
    }

    val parts = numbers.filter { (row, range) ->
        symbols.any { it.x in range.first - 1 .. range.last + 1 && it.y in row - 1 .. row + 1 }
    }

    return parts.sumOf {  it.toInt(input) }
}

private fun part2(input: List<String>): Int {
    val numberRegex = """\d+""".toRegex()
    val gearRegex = """\*""".toRegex()

    val numbers = mutableSetOf<Pair<Int, IntRange>>()
    val gears = mutableSetOf<Coord2D>()

    input.forEachIndexed { index, line ->
        numberRegex.findAll(line).forEach {
            numbers += index to it.range
        }

        gearRegex.findAll(line).forEach {
            gears += Coord2D(it.range.first, index)
        }
    }

    return gears.sumOf { gear ->
        val adjacent = numbers.filter { (row, range) ->
            gear.x in range.first - 1 .. range.last + 1 && gear.y in row - 1 .. row + 1
        }
        if (adjacent.size == 2) adjacent[0].toInt(input) * adjacent[1].toInt(input) else 0
    }
}

private fun Pair<Int, IntRange>.toInt(lines: List<String>): Int = lines[first].substring(second).toInt()
