package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.CharGrid
import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.contains
import com.github.ferinagy.adventOfCode.get
import com.github.ferinagy.adventOfCode.positionOfFirst
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import com.github.ferinagy.adventOfCode.set
import com.github.ferinagy.adventOfCode.toCharGrid

fun main() {
    val input = readInputLines(2023, "10-input")
    val test1 = readInputLines(2023, "10-test1")
    val test2 = readInputLines(2023, "10-test2")
    val test3 = readInputLines(2023, "10-test3")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test2).println()
    part2(test3).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val grid = input.toCharGrid()
    val loop = getLoop(grid)

    return loop.size / 2
}

private fun part2(input: List<String>): Int {
    val grid = input.toCharGrid()
    val loop = getLoop(grid)
    grid.replaceStart()
    return grid.yRange.sumOf { y ->
        var inside = false
        grid.xRange.count { x ->
            val pos = Coord2D(x, y)
            when {
                pos !in loop && inside -> true
                pos in loop && grid[pos] in "|F7'" -> {
                    inside = !inside
                    false
                }
                else -> false
            }
        }
    }
}

private fun getLoop(grid: CharGrid): MutableList<Coord2D> {
    val start = grid.positionOfFirst { it == 'S' }
    var current = start
    var previous = start
    val loop = mutableListOf(start)
    do {
        val adjacent = current.adjacent(includeDiagonals = false) - previous
        previous = current
        current = adjacent.first { grid.isConnected(current, it) }
        loop += current
    } while (current != start)
    return loop
}

private fun CharGrid.replaceStart() {
    val start = positionOfFirst { it == 'S' }
    val options = "|-F7JL"
    options.forEach { candidate ->
        set(start, candidate)
        if (start.adjacent(includeDiagonals = false).filter { this.isConnected(start, it) }.size == 2) return
    }
}

private fun CharGrid.isConnected(current: Coord2D, other: Coord2D): Boolean {
    if (other !in this) return false

    val pipe = this[current]
    val otherPipe = this[other]

    val right = current.x < other.x && current.y == other.y && otherPipe in "-J7S"
    val left = current.x > other.x && current.y == other.y && otherPipe in "-LFS"
    val top = current.x == other.x && current.y > other.y && otherPipe in "|F7S"
    val bottom = current.x == other.x && current.y < other.y && otherPipe in "|LJS"

   return when (pipe) {
        '|' -> top || bottom
        '-' -> left || right
        'L' -> top || right
        'J' -> top || left
        '7' -> bottom || left
        'F' -> bottom || right
        '.' -> false
        'S' -> top || bottom || left || right
        else -> error("unexpected '$pipe' vs '$otherPipe'")
    }
}
