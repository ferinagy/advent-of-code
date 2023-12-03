package com.github.ferinagy.adventOfCode.aoc2016

import com.github.ferinagy.adventOfCode.Coord2D

fun main() {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2("R8, R4, R4, R8"))
    println(part2(input))
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

private const val testInput1 = """R5, L5, R5, R3"""

private const val input = """R1, R3, L2, L5, L2, L1, R3, L4, R2, L2, L4, R2, L1, R1, L2, R3, L1, L4, R2, L5, R3, R4, L1, R2, L1, R3, L4, R5, L4, L5, R5, L3, R2, L3, L3, R1, R3, L4, R2, R5, L4, R1, L1, L1, R5, L2, R1, L2, R188, L5, L3, R5, R1, L2, L4, R3, R5, L3, R3, R45, L4, R4, R72, R2, R3, L1, R1, L1, L1, R192, L1, L1, L1, L4, R1, L2, L5, L3, R5, L3, R3, L4, L3, R1, R4, L2, R2, R3, L5, R3, L1, R1, R4, L2, L3, R1, R3, L4, L3, L4, L2, L2, R1, R3, L5, L1, R4, R2, L4, L1, R3, R3, R1, L5, L2, R4, R4, R2, R1, R5, R5, L4, L1, R5, R3, R4, R5, R3, L1, L2, L4, R1, R4, R5, L2, L3, R4, L4, R2, L2, L4, L2, R5, R1, R4, R3, R5, L4, L4, L5, L5, R3, R4, L1, L3, R2, L2, R1, L3, L5, R5, R5, R3, L4, L2, R4, R5, R1, R4, L3"""
