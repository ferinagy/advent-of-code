package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import kotlin.time.measureTime

fun main() {
    val input = readInputLines(2023, "11-input")
    val test1 = readInputLines(2023, "11-test1")

    measureTime {
        println("Part1:")
        part1(test1).println()
        part1(input).println()

        println()
        println("Part2:")
        part2(test1, 10).println()
        part2(test1, 100).println()
        part2(input, 1000000).println()
    }.also { it.println() }
}

private fun part1(input: List<String>) = solve(input, 2)

private fun part2(input: List<String>, times: Int) = solve(input, times)

private fun solve(input: List<String>, times: Int): Long {
    val galaxies = input.flatMapIndexed { y: Int, line: String ->
        line.mapIndexedNotNull { x, c -> c.takeIf { it == '#' }?.let { Coord2D(x, y) } }
    }

    val emptyRows = input.indices.filter { row -> input[row].all { it == '.' } }
    val emptyCols = (0..<input.first().length).filter { input.all { line -> line[it] == '.' } }

    val expanded = galaxies.map { (x, y) ->
        Coord2D(x + emptyCols.count { it < x } * (times - 1), y + emptyRows.count { it < y } * (times - 1))
    }

    return expanded.indices.sumOf { i ->
        (i + 1..expanded.lastIndex).sumOf { j ->
            expanded[i].distanceTo(expanded[j]).toLong()
        }
    }
}
