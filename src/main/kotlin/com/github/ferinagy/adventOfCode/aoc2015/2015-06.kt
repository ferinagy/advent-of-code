package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.BooleanGrid
import com.github.ferinagy.adventOfCode.IntGrid
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2015, "06-input")
    val test1 = readInputLines(2015, "06-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val grid = BooleanGrid(1000, 1000, false)

    val regex = """(turn on|turn off|toggle) (\d+),(\d+) through (\d+),(\d+)""".toRegex()

    input.forEach {
        val (command, x1, y1, x2, y2) = regex.matchEntire(it)!!.destructured

        for (x in x1.toInt() .. x2.toInt()) {
            for (y in y1.toInt() .. y2.toInt()) {
                when (command) {
                    "turn off" -> { grid[x, y] = false }
                    "turn on" -> { grid[x, y] = true }
                    "toggle" -> { grid[x, y] = !grid[x, y] }
                }
            }
        }
    }

    return grid.count()
}

private fun part2(input: List<String>): Int {
    val grid = IntGrid(1000, 1000, 0)

    val regex = """(turn on|turn off|toggle) (\d+),(\d+) through (\d+),(\d+)""".toRegex()

    input.forEach {
        val (command, x1, y1, x2, y2) = regex.matchEntire(it)!!.destructured

        for (i in x1.toInt() .. x2.toInt()) {
            for (j in y1.toInt() .. y2.toInt()) {
                when (command) {
                    "turn off" -> { grid[i,j] = (grid[i,j] - 1).coerceAtLeast(0) }
                    "turn on" -> { grid[i,j]++ }
                    "toggle" -> { grid[i,j] += 2 }
                }
            }
        }
    }

    return grid.sum()
}
