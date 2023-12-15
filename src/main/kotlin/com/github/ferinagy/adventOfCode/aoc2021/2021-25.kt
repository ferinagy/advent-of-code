package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2021, "25-input")
    val test1 = readInputLines(2021, "25-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()
}

private fun part1(input: List<String>): Int {
    var floor = SeaFloor.parse(input)

    var steps = 0
    var done = false
    while (!done) {
        steps++
        val step = floor.step()
        floor = step.first
        done = !step.second
    }
//    floor.print()

    return steps
}

private data class SeaFloor(val width: Int, val height: Int, val cucumbers: Map<Coord2D, Boolean>) {
    companion object {
        fun parse(input: List<String>): SeaFloor {
            val height = input.size
            val width = input.first().length
            val map = mutableMapOf<Coord2D, Boolean>()
            input.forEachIndexed { y, line ->
                line.forEachIndexed { x, c ->
                    if (c == '>') map[Coord2D(x, y)] = true
                    if (c == 'v') map[Coord2D(x, y)] = false
                }
            }

            return SeaFloor(width, height, map)
        }
    }
}

private fun SeaFloor.print() {
    for (y in 0 until height) {
        for (x in 0 until width) {
            when (cucumbers[Coord2D(x, y)]) {
                true -> print('>')
                false -> print('v')
                null -> print('.')
            }
        }
        println()
    }
}

private fun SeaFloor.step(): Pair<SeaFloor, Boolean> {
    var moved = false
    val intermediate = cucumbers.mapKeys { (key, value) ->
        if (value) {
            val newPosition = eastOf(key)
            if (cucumbers[newPosition] == null) newPosition.also { moved = true } else key
        } else {
            key
        }
    }

    val newCucumbers = intermediate.mapKeys { (key, value) ->
        if (value) {
            key
        } else {
            val newPosition = southOf(key)
            if (intermediate[newPosition] == null) newPosition.also { moved = true } else key
        }
    }


    return copy(cucumbers = newCucumbers) to moved
}

private fun SeaFloor.eastOf(coord: Coord2D): Coord2D {
    return if (coord.x + 1 < width) coord.copy(x = coord.x + 1) else coord.copy(x = 0)
}

private fun SeaFloor.southOf(coord: Coord2D): Coord2D {
    return if (coord.y + 1 < height) coord.copy(y = coord.y + 1) else coord.copy(y = 0)
}
