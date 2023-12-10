package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readInputLines(2022, "14-input")
    val testInput1 = readInputLines(2022, "14-test1")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(testInput1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val (max, occupied) = parse(input)

    var result = 0
    while (true) {
        val next = simulateFall(Coord2D(500, 0), occupied, max, false)
        if (next != null) {
            result++
            occupied += next
        } else {
            return result
        }
    }
}

private fun part2(input: List<String>): Int {
    val (max, occupied) = parse(input)

    var result = 0
    while (true) {
        val next = simulateFall(Coord2D(500, 0), occupied, max + 2, true)!!
        result++
        if (next != Coord2D(500, 0)) {
            occupied += next
        } else {
            return result
        }
    }
}

private fun parse(input: List<String>): Pair<Int, MutableSet<Coord2D>> {
    val paths = input.map { line ->
        line.split(" -> ").map { pair ->
            val (a, b) = pair.split(',').map { it.toInt() }
            Coord2D(a, b)
        }
    }

    val max = paths.maxOf { path -> path.maxOf { it.y } }
    val occupied = paths.flatMap { path ->
        path.windowed(2).flatMap { (a, b) ->
            buildSet {
                for (x in min(a.x, b.x)..max(a.x, b.x)) {
                    for (y in min(a.y, b.y)..max(a.y, b.y)) {
                        this += Coord2D(x, y)
                    }
                }
            }
        }.toSet()
    }.toMutableSet()
    return Pair(max, occupied)
}

private fun simulateFall(current: Coord2D, occupied: Set<Coord2D>, maxY: Int, floor: Boolean = false): Coord2D? {
    if (!floor && maxY < current.y) return null

    fun isOnFloor(y: Int) = if (!floor) false else y == maxY

    val down = current.copy(y = current.y + 1)
    if (down !in occupied && !isOnFloor(down.y)) return simulateFall(down, occupied, maxY, floor)

    val downLeft = current.copy(x = current.x - 1, y = current.y + 1)
    if (downLeft !in occupied && !isOnFloor(downLeft.y)) return simulateFall(downLeft, occupied, maxY, floor)

    val downRight = current.copy(x = current.x + 1, y = current.y + 1)
    if (downRight !in occupied && !isOnFloor(downRight.y)) return simulateFall(downRight, occupied, maxY, floor)

    return current
}
