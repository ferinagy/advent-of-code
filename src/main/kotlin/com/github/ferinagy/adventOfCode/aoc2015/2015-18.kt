package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.BooleanGrid
import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.toBooleanGrid
import com.github.ferinagy.adventOfCode.contains
import com.github.ferinagy.adventOfCode.get
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2015, "18-input")
    val test1 = readInputLines(2015, "18-test1")

    println("Part1:")
    part1(test1, 4).println()
    part1(input, 100).println()

    println()
    println("Part2:")
    part2(test1, 5).println()
    part2(input, 100).println()
}

private fun part1(input: List<String>, steps: Int): Int {
    var grid = input.toBooleanGrid { it == '#' }
    repeat(steps) {
        grid = grid.update()
    }

    return grid.count()
}

private fun part2(input: List<String>, steps: Int): Int {
    var grid = input.toBooleanGrid { it == '#' }.stuckCorners()
    repeat(steps) {
        grid = grid.update().stuckCorners()
    }

    return grid.count()
}

private fun BooleanGrid.update(): BooleanGrid = mapIndexed { coord: Coord2D, b: Boolean ->
    val adjacent = coord.adjacent(true).count { it in this && this[it] }

    b && adjacent in 2..3 || !b && adjacent == 3
}

private fun BooleanGrid.stuckCorners() = apply {
    this[0, 0] = true
    this[0, height - 1] = true
    this[width - 1, 0] = true
    this[width - 1, height - 1] = true
}
