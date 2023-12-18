@file:OptIn(ExperimentalStdlibApi::class)

package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2023, "18-input")
    val test1 = readInputLines(2023, "18-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Long {
    val parsed = input.map {
        val (dir, steps) = regex.matchEntire(it)!!.destructured
        steps.toInt() to dir
    }
    val points = mutableListOf(Coord2D(0, 0))
    parsed.forEach { (steps, d) ->
        points += dirs[d]!! * steps + points.last()
    }

    return calculateArea(points)
}

@OptIn(ExperimentalStdlibApi::class)
private fun part2(input: List<String>): Long {
    val parsed = input.map {
        val hex = regex.matchEntire(it)!!.groupValues[3]
        hex.take(5).hexToInt() to hex[5]
    }

    val points = mutableListOf(Coord2D(0, 0))
    parsed.forEach { (steps, d) ->
        points += hexDirs[d]!! * steps + points.last()
    }

    return calculateArea(points)
}

private fun calculateArea(points: List<Coord2D>): Long {
    val shoelace = points.windowed(2).sumOf { (p1, p2) ->
        p1.x.toLong() * p2.y.toLong() - p2.x.toLong() * p1.y.toLong()
    }
    val len = points.windowed(2).sumOf { (p1, p2) -> p1.distanceTo(p2).toLong() }

    return (shoelace + len) / 2 + 1
}

private val hexDirs = mapOf(
    '0' to Coord2D(1, 0),
    '1' to Coord2D(0, 1),
    '2' to Coord2D(-1, 0),
    '3' to Coord2D(0, -1),
)

private val dirs = mapOf(
    "U" to Coord2D(0, -1),
    "D" to Coord2D(0, 1),
    "L" to Coord2D(-1, 0),
    "R" to Coord2D(1, 0),
)

private val regex = """([UDLR]) (\d+) \(#([a-f0-9]{6})\)""".toRegex()
