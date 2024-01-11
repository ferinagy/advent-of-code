package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.CharGrid
import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.IntGrid
import com.github.ferinagy.adventOfCode.contains
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import com.github.ferinagy.adventOfCode.set
import com.github.ferinagy.adventOfCode.toCharGrid
import java.util.LinkedList

fun main() {
    val input = readInputLines(2023, "21-input")
    val test1 = readInputLines(2023, "21-test1")

    println("Part1:")
    part1(test1, 6).println()
    part1(input, 64).println()

    println()
    println("Part2:")
    bruteForce(test1.toCharGrid(), 6).println()
    bruteForce(test1.toCharGrid(), 10).println()
    part2(input, 26501365).println()
}

private fun part1(input: List<String>, maxSteps: Int): Int {
    val grid = input.toCharGrid()
    val start = grid.yRange.map { y ->
        val x = grid.xRange.singleOrNull { x -> grid[x, y] == 'S' }
        x to y
    }.single { it.first != null }.let { Coord2D(it.first!!, it.second) }

    return createStepGrid(grid, start).count { it != -2 && it <= maxSteps && it % 2 == maxSteps % 2 }
}

private fun part2(input: List<String>, maxSteps: Int): Long {
    val grid = input.toCharGrid()
    val start = grid.yRange.map { y ->
        val x = grid.xRange.singleOrNull { x -> grid[x, y] == 'S' }
        x to y
    }.single { it.first != null }.let { Coord2D(it.first!!, it.second) }

    require(grid.width == grid.height) { "grid must be square" }
    val radius = grid.width / 2
    require(maxSteps % grid.width == radius) { "steps must end at edge" }

    val stepGrid = createStepGrid(grid, start)

    val even = stepGrid.count { 0 <= it && it % 2 == 0 }
    val odd = stepGrid.count { 0 <= it && it % 2 == 1 }
    val evenCorners = stepGrid.count { radius < it && it % 2 == 0 }
    val oddCorners = stepGrid.count { radius < it && it % 2 == 1 }

    val evenRoot = maxSteps.toLong() / grid.width
    val oddRoot = evenRoot + 1

    return (oddRoot * oddRoot) * odd - oddRoot * oddCorners + evenRoot * evenRoot * even + evenRoot * evenCorners
}

private fun createStepGrid(grid: CharGrid, start: Coord2D): IntGrid {
    val stepGrid = IntGrid(grid.width, grid.height) { x, y -> -1 }
    for (x in grid.xRange) {
        for (y in grid.yRange) {
            if (grid[x, y] == '#') stepGrid[x, y] = -2
        }
    }

    val visited = mutableSetOf<Coord2D>()
    val queue = LinkedList<Pair<Coord2D, Int>>()
    queue += start to 0

    while (queue.isNotEmpty()) {
        val (pos, steps) = queue.removeFirst()

        if (pos in visited) continue
        visited += pos
        if (pos !in grid) continue
        stepGrid[pos] = steps


        pos.adjacent(false).filter { (totalX, totalY) ->
            val x = totalX.mod(grid.width)
            val y = totalY.mod(grid.height)
            grid[x, y] != '#'
        }.forEach {
            queue += it to steps + 1
        }
    }

    return stepGrid
}

private fun bruteForce(grid: CharGrid, maxSteps: Int): Int {
    val visited = mutableSetOf<Coord2D>()
    val queue = LinkedList<Pair<Coord2D, Int>>()
    val start = Coord2D(grid.width / 2, grid.height / 2)
    queue += start to 0

    var result = 0
    while (queue.isNotEmpty()) {
        val (pos, steps) = queue.removeFirst()

        if (pos in visited) continue
        visited += pos

        if (maxSteps % 2 == steps % 2) result++

        if (steps == maxSteps) continue


        pos.adjacent(false).filter { (totalX, totalY) ->
            val x = totalX.mod(grid.width)
            val y = totalY.mod(grid.height)
            grid[x, y] != '#'
        }.forEach {
            queue += it to steps + 1
        }
    }

    return result
}
