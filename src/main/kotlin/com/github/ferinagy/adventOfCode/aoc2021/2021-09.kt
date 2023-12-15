package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.filterIn
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2021, "09-input")
    val test1 = readInputLines(2021, "09-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    var result = 0
    input.forEachIndexed { row, line ->
        line.forEachIndexed { col, c ->
            val neighbors = input.getNeighboringValues(row, col)
            if (neighbors.all { c < it }) result += c.digitToInt() + 1
        }
    }

    return result
}

private fun part2(input: List<String>): Int {
    val visited = mutableSetOf<Coord2D>()
    val height = input.size
    val width = input.first().length

    input.forEachIndexed { row, line ->
        line.forEachIndexed { col, c ->
            if (c == '9') visited += Coord2D(col, row)
        }
    }

    val basins = mutableListOf<Int>()
    while (visited.size != width * height) {
        for (row in input.indices) {
            for (col in input[row].indices) {
                val coord = Coord2D(col, row)
                if (coord !in visited) {
                    basins += input.findBasin(coord, visited)
                }
            }
        }
    }

    return basins.sortedDescending().take(3).fold(1) { acc, item -> acc * item }
}

private fun List<String>.findBasin(coord: Coord2D, visited: MutableSet<Coord2D>): Int {
    if (coord in visited) return 0

    visited += coord
    return getNeighborCoords(coord).map { findBasin(it, visited) }.sum() + 1
}

private fun List<String>.getNeighboringValues(row: Int, col: Int): List<Char> {
    return getNeighborCoords(Coord2D(col, row)).map { this[it.y][it.x] }
}

private fun List<String>.getNeighborCoords(coord2D: Coord2D): List<Coord2D> {
    return coord2D.adjacent(false)
        .filterIn(first().indices, indices)
}
