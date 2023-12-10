package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.IntGrid
import com.github.ferinagy.adventOfCode.get
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import com.github.ferinagy.adventOfCode.toIntGrid

fun main() {
    val input = readInputLines(2022, "08-input")
    val testInput1 = readInputLines(2022, "08-test1")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(testInput1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val grid = input.map { line -> line.map { it.digitToInt() } }.toIntGrid()
    val visible = mutableSetOf<Coord2D>()

    fun Coord2D.rotate(times: Int): Coord2D =
        if (times == 0) this else Coord2D(y, grid.width - 1 - x).rotate(times - 1)

    repeat(4) { dir ->
        for (y in 0 until  grid.height) {
            var max = -1
            for (x in 0 until grid.width) {
                val coord = Coord2D(x, y).rotate(dir)

                if (max < grid[coord]) {
                    visible += coord
                    max = grid[coord]
                }
            }
        }
    }

    return visible.size
}

private fun part2(input: List<String>): Int {
    val grid = input.map { line -> line.map { it.digitToInt() } }.toIntGrid()
    val scores = IntGrid(grid.width, grid.height) { x, y -> scenicScore(grid, x, y) }

    return scores.maxOrNull()!!
}

private fun scenicScore(grid: IntGrid, x: Int, y: Int): Int {
    val current = grid[x, y]

    val up = visibleTrees(current, isAtEdge = { y - it == 0 }, next = { grid[x, y - it] })
    val down = visibleTrees(current, isAtEdge = { y + it == grid.height - 1 }, next = { grid[x, y + it] })
    val left = visibleTrees(current, isAtEdge = { x - it == 0 }, next = { grid[x - it, y] })
    val right = visibleTrees(current, isAtEdge = { x + it == grid.width - 1 }, next = { grid[x + it, y] })

    return up * down * left * right
}

private fun visibleTrees(current: Int, isAtEdge: (Int) -> Boolean, next: (Int) -> Int): Int {
    var up = 0
    if (!isAtEdge(up)) {
        while (true) {
            up++
            if (isAtEdge(up) || current <= next(up)) break
        }
    }
    return up
}
