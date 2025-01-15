package com.github.ferinagy.adventOfCode.aoc2016

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val test1 = readInputText(2016, "01-test1")
    val test2 = readInputText(2016, "01-test2")
    val input = readInputText(2016, "01-input")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test2).println()
    part2(input).println()
}

private fun part1(input: String): Int {
    val instructions = parse(input)
    var position = Coord2D(0, 0)
    var direction = Coord2D(0, 1)
    instructions.forEach { (dir, steps) ->
        direction = direction.turn(dir)
        position = position.walk(direction, steps)
    }

    return position.manhattanDist
}

private fun part2(input: String): Int {
    val instructions = parse(input)
    var position = Coord2D(0, 0)
    var direction = Coord2D(0, 1)

    val visited = mutableSetOf(position)
    instructions.forEach { (dir, steps) ->
        direction = direction.turn(dir)
        val newPosition = position.walk(direction, steps)

        for (x in increasingRange(position.x, newPosition.x)) {
            for (y in increasingRange(position.y, newPosition.y)) {
                val p = Coord2D(x, y)
                if (p == position) continue

                if (p in visited) return p.manhattanDist
                visited += p
            }
        }

        position = newPosition
    }

    return -1
}

private fun Coord2D.turn(dir: Char) = if (dir == 'R') copy(x = y, y = x * -1) else copy(x = y * -1, y = x)

private fun Coord2D.walk(direction: Coord2D, steps: Int) =
    copy(x = x + direction.x * steps, y = y + direction.y * steps)

private fun increasingRange(x: Int, y: Int): IntRange = if (x <= y) x..y else y..x

private fun parse(input: String) = input.split(", ").map { it.first() to it.drop(1).toInt() }
