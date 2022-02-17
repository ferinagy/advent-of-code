package com.github.ferinagy.adventOfCode.aoc2016

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.searchGraph
import com.github.ferinagy.adventOfCode.singleStep

fun main() {
    println("Part1:")
    println(part1(testInput1, Coord2D(7, 4)))
    println(part1(input, Coord2D(31, 39)))

    println()
    println("Part2:")
    println(part2())
}

private fun part1(input: Int, target: Coord2D): Int {
    val start = Coord2D(1, 1)

    return searchGraph(
        start = start,
        isDone = { it == target },
        nextSteps = { from -> adjacent(from, input).toSet().singleStep() },
        heuristic = { it.distanceTo(target) }
    )
}

private fun part2(): Int {
    val start = Coord2D(1, 1)
    val canVisit = mutableSetOf<Coord2D>()

    val steps = mutableMapOf(start to 0)
    searchGraph(
        start = start,
        isDone = { 50 < steps[it]!! },
        nextSteps = { from ->
            canVisit += from
            val currentSteps = steps[from]!!
            val next = adjacent(from, input)
            next.forEach { steps[it] = currentSteps + 1 }

            next.toSet().singleStep()
        }
    )
    return canVisit.size
}

private fun adjacent(
    from: Coord2D,
    input: Int
) = from.adjacent(includeDiagonals = false).filter { 0 <= it.x && 0 <= it.y && !it.isWall(input) }

private fun Coord2D.isWall(n: Int): Boolean {
    val num = x * x + 3 * x + 2 * x * y + y + y * y + n
    return num.countOneBits() % 2 == 1
}

private const val testInput1 = 10

private const val input = 1364
