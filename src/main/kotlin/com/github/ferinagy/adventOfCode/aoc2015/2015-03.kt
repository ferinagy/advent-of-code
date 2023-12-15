package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2015, "03-input")
    val test1 = readInputText(2015, "03-test1")

    println("Part1:")
    println(visitsAtLeastOnce(test1))
    println(visitsAtLeastOnce(input))

    println()
    println("Part2:")
    println(visitsAtLeastOnceWithRobot(test1))
    println(visitsAtLeastOnceWithRobot(input))
}

private fun visitsAtLeastOnce(input: String): Int {
    return visitedHouses(input.toList()).size
}

private fun visitsAtLeastOnceWithRobot(input: String): Int {
    val (l1, l2) = input.withIndex().partition { it.index % 2 == 0 }.let { (l1, l2) ->
        l1.map { it.value } to l2.map { it.value }
    }
    val v1 = visitedHouses(l1)
    val v2 = visitedHouses(l2)

    return (v1 + v2).size
}

private fun visitedHouses(input: List<Char>): Set<Coord2D> {
    val map = mapOf(
        '^' to Coord2D(0, -1),
        'v' to Coord2D(0, 1),
        '>' to Coord2D(1, 0),
        '<' to Coord2D(-1, 0),
    )

    var current = Coord2D(0, 0)
    val visited = mutableSetOf(current)
    input.forEach {
        current += map[it]!!
        visited += current
    }

    return visited
}
