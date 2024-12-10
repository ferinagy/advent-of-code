package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.*

fun main() {
    val input = readInputLines(2024, "10-input")
    val test1 = readInputLines(2024, "10-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val results = findPaths(input)
    return results.groupBy { it.first() }.map { (_, trails) -> trails.map { it.last() }.toSet().count() }.sum()
}

private fun part2(input: List<String>): Int {
    val results = findPaths(input)
    return results.size
}

private fun findPaths(input: List<String>): MutableList<List<Coord2D>> {
    val grid = input.toCharGrid()
    val starts = grid.xRange.flatMap { x -> grid.yRange.mapNotNull { y -> Coord2D(x, y).takeIf { grid[it] == '0' } } }
    val queue = ArrayDeque<Pair<Coord2D, List<List<Coord2D>>>>(starts.map { it to listOf(listOf(it)) })

    val results = mutableListOf<List<Coord2D>>()
    while (queue.isNotEmpty()) {
        val (coord, paths) = queue.removeFirst()
        if (grid[coord] == '9') {
            results += paths
            continue
        }

        val next = coord.adjacent(false).filter { it in grid && grid[it] - grid[coord] == 1 }
        queue += next.map { pos -> pos to paths.map { it + pos } }
    }
    return results
}
