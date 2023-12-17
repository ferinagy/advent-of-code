package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.IntGrid
import com.github.ferinagy.adventOfCode.get
import com.github.ferinagy.adventOfCode.isIn
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import com.github.ferinagy.adventOfCode.rotateCcw
import com.github.ferinagy.adventOfCode.rotateCw
import com.github.ferinagy.adventOfCode.searchGraph
import com.github.ferinagy.adventOfCode.toIntGrid

fun main() {
    val input = readInputLines(2023, "17-input")
    val test1 = readInputLines(2023, "17-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val grid = input.map { line -> line.map { it.digitToInt() } }.toIntGrid()

    return navigateCrucible(grid, 0, 3)
}

private fun part2(input: List<String>): Int {
    val grid = input.map { line -> line.map { it.digitToInt() } }.toIntGrid()

    return navigateCrucible(grid, 4, 10)
}

private fun navigateCrucible(grid: IntGrid, minStraight: Int, maxStraight: Int) = searchGraph(
    startingSet = setOf(Triple(Coord2D(0, 0), Coord2D(0, 1), 0), Triple(Coord2D(0, 0), Coord2D(1, 0), 0)),
    isDone = { it.first.x == grid.xRange.last && it.first.y == grid.yRange.last && minStraight <= it.third },
    nextSteps = { (pos, dir, s) ->
        buildSet {
            val left = dir.rotateCw()
            val c1 = pos + left
            if (minStraight <= s && c1.isIn(grid.xRange, grid.yRange)) this += Triple(c1, left, 1) to grid[c1]

            val right = dir.rotateCcw()
            val c2 = pos + right
            if (minStraight <= s && c2.isIn(grid.xRange, grid.yRange)) this += Triple(c2, right, 1) to grid[c2]

            val c3 = pos + dir
            if (c3.isIn(grid.xRange, grid.yRange) && s < maxStraight) this += Triple(c3, dir, s + 1) to grid[c3]
        }
    },
    heuristic = { Coord2D(grid.xRange.last, grid.yRange.last).distanceTo(it.first) }
)
