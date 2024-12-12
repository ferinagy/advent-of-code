package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.*
import kotlin.time.measureTime

fun main() {
    val input = readInputLines(2024, "12-input")
    val test1 = readInputLines(2024, "12-test1")
    val test2 = readInputLines(2024, "12-test2")
    val test3 = readInputLines(2024, "12-test3")

    println("Part1:")
    part1(test1).println()
    part1(test2).println()
    part1(test3).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(test2).println()
    part2(test3).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val grid = input.toCharGrid()
    val areas = split(grid)

    return areas.sumOf { it.size * fences(it.first(), it, mutableSetOf()) }
}

private fun part2(input: List<String>): Int {
    val grid = input.toCharGrid()
    val areas = split(grid)

    return areas.sumOf { it.size * sides(it) }
}

private fun split(grid: CharGrid): List<Set<Coord2D>> {
    val positions = grid.xRange.flatMap { x -> grid.yRange.map { y -> Coord2D(x, y) } }.toMutableSet()
    return buildList {
        while (positions.isNotEmpty()) {
            val start = positions.first()
            val squares = flood(start, mutableSetOf(), grid)
            positions -= squares
            this += squares
        }
    }
}

private fun flood(start: Coord2D, visited: MutableSet<Coord2D>, grid: CharGrid): Set<Coord2D> {
    if (start in visited) return emptySet()

    visited += start

    return start
        .adjacent(false)
        .asSequence()
        .filter { it in grid && grid[it] == grid[start] }
        .filter { it !in visited }
        .flatMapTo(mutableSetOf()) { flood(it, visited, grid) } + start
}

private fun fences(start: Coord2D, area: Set<Coord2D>, visited: MutableSet<Coord2D>): Int {
    if (start in visited) return 0

    visited += start
    val neighbors = start.adjacent(false).filter { it in area }

    return 4 - neighbors.size + neighbors
        .asSequence()
        .filter { it !in visited }
        .map { fences(it, area, visited) }
        .sum()
}

private fun sides(area: Set<Coord2D>) = Coord2D(0, 0).adjacent(false).sumOf { edges(area, it) }

private fun edges(area: Set<Coord2D>, dir: Coord2D): Int {
    fun Coord2D.groupingDir() = if (dir.x == 0) y else x
    fun Coord2D.sortingDir() = if (dir.x == 0) x else y

    return area.filter { it + dir !in area }
        .groupBy({ it.groupingDir() })
        .map { g -> g.value.sortedBy { it.sortingDir() } }
        .sumOf { edges ->
            edges.fold(-2 to 0) { (last, acc), next ->
                next.sortingDir() to acc + if (next.sortingDir() != last + 1) 1 else 0
            }.second
        }
}
