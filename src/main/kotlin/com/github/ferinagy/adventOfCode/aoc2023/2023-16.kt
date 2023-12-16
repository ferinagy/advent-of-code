package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.CharGrid
import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.get
import com.github.ferinagy.adventOfCode.isIn
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import com.github.ferinagy.adventOfCode.rotateCcw
import com.github.ferinagy.adventOfCode.rotateCw
import com.github.ferinagy.adventOfCode.searchGraph
import com.github.ferinagy.adventOfCode.singleStep
import com.github.ferinagy.adventOfCode.toCharGrid

fun main() {
    val input = readInputLines(2023, "16-input")
    val test1 = readInputLines(2023, "16-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val grid = input.toCharGrid()

    return energize(grid, Coord2D(0, 0) to Coord2D(1, 0))
}

private fun part2(input: List<String>): Int {
    val grid = input.toCharGrid()

    val initials = grid.xRange.map { Coord2D(it, 0) to Coord2D(0, 1) } +
            grid.xRange.map { Coord2D(it, grid.yRange.last) to Coord2D(0, -1) } +
            grid.yRange.map { Coord2D(0, it) to Coord2D(1, 0) } +
            grid.yRange.map { Coord2D(grid.xRange.last, it) to Coord2D(-1, 0) }

    return initials.maxOf { energize(grid, it) }
}

private fun energize(grid: CharGrid, start: State): Int {
    val visited = mutableSetOf<Coord2D>()
    searchGraph(
        start = start,
        isDone = {
            visited += it.first
            false
        },
        nextSteps = {
            next(it, grid[it.first]).filter { it.first.isIn(grid.xRange, grid.yRange) }.toSet().singleStep()
        }
    )

    return visited.size
}

private fun next(current: State, tile: Char): List<State> = when (tile) {
    '.' -> listOf(current.second)
    '/' -> {
        val newDir = if (current.second.x == 0) current.second.rotateCcw() else current.second.rotateCw()
        listOf(newDir)
    }
    '\\' -> {
        val newDir = if (current.second.x == 0) current.second.rotateCw() else current.second.rotateCcw()
        listOf(newDir)
    }
    '|' -> if (current.second.x == 0) {
        listOf(current.second)
    } else {
        listOf(Coord2D(0, -1), Coord2D(0, 1))
    }
    '-' -> if (current.second.y == 0) {
        listOf(current.second)
    } else {
        listOf(Coord2D(-1, 0), Coord2D(1, 0))
    }
    else -> error("bad tile")
}.map { current.first + it to it}

private typealias State = Pair<Coord2D, Coord2D>
