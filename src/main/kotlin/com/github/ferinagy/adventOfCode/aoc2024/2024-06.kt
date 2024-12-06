package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.*

fun main() {
    val input = readInputLines(2024, "06-input")
    val test1 = readInputLines(2024, "06-test1")

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
    val guard = grid.positionOfFirst { it == '^' }

    val visited = visitedPath(guard, grid)

    return visited.map { it.first }.toSet().count()
}

private fun part2(input: List<String>): Int {
    val grid = input.toCharGrid()
    val guard = grid.positionOfFirst { it == '^' }

    val visited = visitedPath(guard, grid)

    return visited.filterIndexed { index, (position, direction) ->
        position + direction !in visited.subList(0, index).map { it.first } && isLoop(position, direction, grid)
    }.size
}

private fun visitedPath(initialGuard: Coord2D, grid: CharGrid) = buildList {
    var guard = initialGuard
    var dir = Coord2D(0, -1)
    while (guard in grid) {
        this += guard to dir
        val next = guard + dir
        if (next in grid && grid[next] == '#') {
            dir = dir.rotateCcw()
        } else {
            guard += dir
        }
    }
}

private fun isLoop(initialGuard: Coord2D, initialDirection: Coord2D, grid: CharGrid): Boolean {
    var guard = initialGuard
    var dir = initialDirection
    val obstacle = initialGuard + initialDirection
    if (obstacle !in grid || grid[obstacle] == '#') return false

    val visited = mutableSetOf<Pair<Coord2D, Coord2D>>()
    while (guard in grid) {
        if (guard to dir in visited) return true
        visited += guard to dir
        val next = guard + dir
        if (next in grid && grid[next] == '#' || next == obstacle) {
            dir = dir.rotateCcw()
        } else {
            guard += dir
        }
    }

    return false
}
