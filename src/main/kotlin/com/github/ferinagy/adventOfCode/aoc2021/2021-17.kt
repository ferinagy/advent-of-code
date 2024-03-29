package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2021, "17-input")
    val test1 = readInputText(2021, "17-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: String): Int {
    val (bottomLeft, _) = parse(input)
    val maxY = -bottomLeft.y - 1
    return (maxY * (maxY + 1)) / 2
}

private fun part2(input: String): Int {
    val (bottomLeft, topRight) = parse(input)

    val maxX = topRight.x
    val minX = 0
    val maxY = -bottomLeft.y - 1
    val minY = bottomLeft.y

    var result = 0
    for (dx in minX .. maxX) {
        for (dy in minY .. maxY) {
            val velocity = Coord2D(dx, dy)
            if (willHit(velocity, bottomLeft, topRight)) result++
        }
    }

    return result
}

private fun willHit(initialVelocity: Coord2D, bottomLeft: Coord2D, topRight: Coord2D): Boolean {
    var position = Coord2D(0, 0)
    var velocity = initialVelocity
    while (position.x <= topRight.x && position.y >= bottomLeft.y) {
        position += velocity
        velocity = velocity.copy(x = (velocity.x - 1).coerceAtLeast(0), y = velocity.y - 1)

        if (position.x in bottomLeft.x .. topRight.x && position.y in bottomLeft.y .. topRight.y) {
            return true
        }
    }

    return false
}

private fun parse(input: String): Pair<Coord2D, Coord2D> {
    val regex = """target area: x=(\d+)..(\d+), y=(-?\d+)..(-?\d+)""".toRegex()
    val (x1, x2, y1, y2) = regex.matchEntire(input)!!.destructured
    return Coord2D(x1.toInt(), y1.toInt()) to Coord2D(x2.toInt(), y2.toInt())
}
