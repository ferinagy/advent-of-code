package com.github.ferinagy.adventOfCode.aoc2025

import com.github.ferinagy.adventOfCode.BooleanGrid
import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.contains
import com.github.ferinagy.adventOfCode.get
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import com.github.ferinagy.adventOfCode.toBooleanGrid

fun main() {
    val input = readInputLines(2025, "04-input")
    val test1 = readInputLines(2025, "04-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Long {
    val grid = input.toBooleanGrid { it == '@' }

    return grid.xRange.sumOf { x ->
        grid.yRange.count { y ->
            grid[x, y] && grid.isAccessible(Coord2D(x, y))
        }
    }.toLong()
}

private fun part2(input: List<String>): Long {
    var grid = input.toBooleanGrid { it == '@' }
    val start = grid.count()

    while (true) {
        val next = grid.mapIndexed { coord, roll ->
            roll && !grid.isAccessible(coord)
        }

        if (next == grid) break

        grid = next
    }


    return start - grid.count().toLong()
}

private fun BooleanGrid.isAccessible(coord2D: Coord2D): Boolean =
    coord2D.adjacent(includeDiagonals = true).count { it in this && this[it] } < 4
