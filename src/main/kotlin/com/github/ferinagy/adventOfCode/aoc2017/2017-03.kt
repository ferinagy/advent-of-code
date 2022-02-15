package com.github.ferinagy.adventOfCode.aoc2017

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.IntGrid
import com.github.ferinagy.adventOfCode.contains
import com.github.ferinagy.adventOfCode.get
import com.github.ferinagy.adventOfCode.set
import kotlin.math.sqrt

fun main() {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput1))
    println(part2(input))
}

private fun part1(input: Int): Int {
    var counter = 2
    return solve(
        input,
        isDone = { grid, position -> grid[position] == input },
        getValue = { _, _ -> counter++ }
    ).first
}

private fun part2(input: Int): Int {
    return solve(
        input,
        isDone = { grid, position -> grid[position] > input },
        getValue = { grid, position ->
            position.adjacent(includeDiagonals = true).filter { it in grid }.sumOf { grid[it] }
        }
    ).second
}

private fun solve(
    input: Int,
    isDone:  (IntGrid, Coord2D) -> Boolean,
    getValue: (IntGrid, Coord2D) -> Int
): Pair<Int, Int> {
    val size = sqrt(input.toFloat()).toInt() + 2
    val grid = IntGrid(size, size, 0)
    val center = Coord2D(size / 2, size / 2)

    grid[center] = 1

    var direction = Coord2D(1, 0)
    var position = center

    var sideSize = 1
    var sideNum = 0
    var sideStep = 0

    while (!isDone(grid, position)) {
        position += direction
        grid[position] = getValue(grid, position)
        sideStep++

        if (sideStep == sideSize) {
            sideStep = 0
            direction = direction.turnLeft()
            sideNum++
        }
        if (sideNum == 2) {
            sideNum = 0
            sideSize++
        }
    }

    return (position.distanceTo(center)) to grid[position]
}

private fun Coord2D.turnLeft() = copy(x = y, y = -x)

private const val testInput1 = 1024

private const val input = 368078
