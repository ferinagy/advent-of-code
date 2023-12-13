package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2023, "13-input")
    val test1 = readInputText(2023, "13-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: String) = parse(input).sumOf { vertical(it) + 100 * horizontal(it) }

private fun part2(input: String) = parse(input).sumOf { vertical(it, 1) + 100 * horizontal(it, 1) }

private fun parse(input: String) = input.split("\n\n").map { block ->
    block.lines().flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, c ->
            if (c == '#') Coord2D(x, y) else null
        }
    }
}

private fun vertical(rocks: List<Coord2D>, smudges: Int = 0): Int {
    val min = rocks.minOf { it.x }
    val max = rocks.maxOf { it.x }

    return (min + 1..max).indexOfFirst { mirror ->
        rocks.count {
            val opposite = Coord2D(mirror - (it.x - mirror) - 1, it.y)
            opposite.x in min..max && opposite !in rocks
        } == smudges
    } + 1
}

private fun horizontal(rocks: List<Coord2D>, smudges: Int = 0): Int {
    val min = rocks.minOf { it.y }
    val max = rocks.maxOf { it.y }

    return (min + 1..max).indexOfFirst { mirror ->
        rocks.count {
            val opposite = Coord2D(it.x, mirror - (it.y - mirror) - 1)
            opposite.y in min..max && opposite !in rocks
        } == smudges
    } + 1
}
