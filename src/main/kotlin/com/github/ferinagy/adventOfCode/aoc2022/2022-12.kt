package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.CharGrid
import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.filterIn
import com.github.ferinagy.adventOfCode.get
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import com.github.ferinagy.adventOfCode.set
import com.github.ferinagy.adventOfCode.searchGraph
import com.github.ferinagy.adventOfCode.singleStep
import com.github.ferinagy.adventOfCode.toCharGrid

fun main() {
    val input = readInputLines(2022, "12-input")
    val testInput1 = readInputLines(2022, "12-test1")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(testInput1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val grid = input.toCharGrid()

    val start = grid.getCoords('S')
    grid[start] = 'a'
    val end = grid.getCoords('E')
    grid[end] = 'z'

    return searchGraph(
        start = start,
        isDone = { it == end },
        nextSteps = { current ->
            val height = grid[current].code
            val neighbors = current.adjacent(false)
                .filterIn(grid.xRange, grid.yRange)
                .filter { grid[it].code - height <= 1 }

            neighbors.toSet().singleStep()
        },
    )
}

private fun part2(input: List<String>): Int {
    val grid = input.toCharGrid()

    val start = grid.getCoords('S')
    grid[start] = 'a'
    val end = grid.getCoords('E')
    grid[end] = 'z'

    return searchGraph(
        start = end,
        isDone = { grid[it] == 'a' },
        nextSteps = { current ->
            val height = grid[current].code
            val neighbors = current.adjacent(false)
                .filterIn(grid.xRange, grid.yRange)
                .filter { height - grid[it].code <= 1 }

            neighbors.toSet().singleStep()
        },
    )
}

private fun CharGrid.getCoords(c: Char): Coord2D {
    for (x in xRange) {
        for (y in yRange) {
            if (get(x, y) == c) return Coord2D(x, y)
        }
    }

    error("'$c' not found")
}
