package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import kotlin.math.absoluteValue

fun main() {
    val input = readInputLines(2022, "09-input")
    val testInput1 = readInputLines(2022, "09-test1")
    val testInput2 = readInputLines(2022, "09-test2")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(testInput2).println()
    part2(input).println()
}

private fun part1(input: List<String>) = solve(input, 2)

private fun part2(input: List<String>) = solve(input, 10)

private fun solve(input: List<String>, size: Int): Int {
    val knots = MutableList(size) { Coord2D(0, 0) }
    val visited = mutableSetOf(knots.last())
    input.forEach { line ->
        val (dir, s) = line.split(' ')
        val steps = s.toInt()
        repeat(steps) {
            when (dir) {
                "R" -> knots[0] = knots[0].copy(x = knots[0].x + 1)
                "L" -> knots[0] = knots[0].copy(x = knots[0].x - 1)
                "U" -> knots[0] = knots[0].copy(y = knots[0].y - 1)
                "D" -> knots[0] = knots[0].copy(y = knots[0].y + 1)
            }
            for (i in 1 until size) {
                knots[i] = knots[i].follow(knots[i - 1])
            }
            visited += knots.last()
        }
    }

    return visited.size
}

private fun Coord2D.follow(other: Coord2D): Coord2D {
    return if (2 <= (x - other.x).absoluteValue ||  2 <= (y - other.y).absoluteValue) copy(
        x = x + (other.x - x).coerceIn(-1, 1),
        y = y + (other.y - y).coerceIn(-1, 1),
    ) else this
}
