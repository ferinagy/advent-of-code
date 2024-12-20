package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.*

fun main() {
    val input = readInputLines(2024, "20-input")
    val test1 = readInputLines(2024, "20-test1")

    println("Part1:")
    part1(test1, 20).println()
    part1(input, 100).println()

    println()
    println("Part2:")
    part2(test1, 50).println()
    part2(input, 100).println()
}

private fun part1(input: List<String>, minSave: Int): Int {
    val grid = input.toCharGrid()
    val start = grid.positionOfFirst { it == 'S' }

    val path = findPath(grid, start)

    return findCheats(path, 2, minSave)
}

private fun part2(input: List<String>, minSave: Int): Int {
    val grid = input.toCharGrid()
    val start = grid.positionOfFirst { it == 'S' }

    val path = findPath(grid, start)

    return findCheats(path, 20, minSave)
}

private fun findPath(grid: CharGrid, pos: Coord2D): List<Coord2D> {
    val path = mutableListOf<Coord2D>()
    var current: Coord2D? = pos
    while (current != null) {
        path += current
        current = current.adjacent(false)
            .singleOrNull { grid[it] != '#' && it != path.getOrNull(path.lastIndex - 1) }
    }

    return path
}

private fun findCheats(path: List<Coord2D>, maxCheat: Int, minSave: Int): Int {
    val groups = mutableMapOf<Int, Int>()
    for (i in 0..<path.lastIndex) {
        for (j in i + 3..path.lastIndex) {
            if (path[i].distanceTo(path[j]) <= maxCheat) {
                val save = j - i - path[i].distanceTo(path[j])
                groups[save] = groups.getOrDefault(save, 0) + 1
            }
        }
    }
    return groups.filter { minSave <= it.key }.values.sum()
}