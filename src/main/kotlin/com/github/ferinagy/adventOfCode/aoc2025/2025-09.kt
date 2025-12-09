package com.github.ferinagy.adventOfCode.aoc2025

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import kotlin.math.abs

fun main() {
    val input = readInputLines(2025, "09-input")
    val test1 = readInputLines(2025, "09-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Long {
    val coords = input.map(Coord2D::parse)

    return coords.maxOf { c1 ->
        coords.maxOf { c2 ->
            area(c1, c2)
        }
    }
}

private fun part2(input: List<String>): Long {
    val coords = input.map(Coord2D::parse)

    val lines = coords.indices.map { coords[it % coords.size] to coords[(it + 1) % coords.size] }

    return coords.maxOf { c1 ->
        coords.maxOf { c2 ->
            if (!isValid(c1, c2, lines)) 0 else area(c1, c2)
        }
    }
}

private fun area(c1: Coord2D, c2: Coord2D) = (abs(c1.x.toLong() - c2.x) + 1) * (abs(c1.y.toLong() - c2.y) + 1)

private fun isValid(c1: Coord2D, c2: Coord2D, lines: List<Pair<Coord2D, Coord2D>>): Boolean {
    val minX = minOf(c1.x, c2.x)
    val minY = minOf(c1.y, c2.y)
    val maxX = maxOf(c1.x, c2.x)
    val maxY = maxOf(c1.y, c2.y)

    val corners = listOf(
        Coord2D(minX, minY),
        Coord2D(maxX, minY),
        Coord2D(minX, maxY),
        Coord2D(maxX, maxY),
    )

    if (!corners.all { it.isWithin(lines) }) return false

    val xs = (minX..maxX).withoutEnds()
    val ys = (minY..maxY).withoutEnds()
    val verticals = lines.filter { it.first.x == it.second.x }.filter { it.first.x in xs }
    val horizontals = lines.filter { it.first.y == it.second.y }.filter { it.first.y in ys }

    val intersectsHorizontally = horizontals.any {
        val range = toRange(it.first.x, it.second.x).withoutEnds()
        minX in range || maxX in range
    }
    val intersectsVertically = verticals.any {
        val range = toRange(it.first.y, it.second.y).withoutEnds()
        minY in range || maxY in range
    }
    return !intersectsHorizontally && !intersectsVertically
}

private fun toRange(a: Int, b: Int) = if (a < b) a..b else b..a

private fun IntRange.withoutEnds() = start + 1..<endInclusive

private fun Coord2D.isWithin(lines: List<Pair<Coord2D, Coord2D>>): Boolean {
    val verticals = lines.filter { it.first.x == it.second.x }
    val horizontals = lines.filter { it.first.y == it.second.y }

    if (verticals.any { it.first.x == x && y in toRange(it.first.y, it.second.y)}) return true
    if (horizontals.any { it.first.y == y && x in toRange(it.first.x, it.second.x)}) return true

    return verticals.count {
        val vert = toRange(it.first.y, it.second.y).let { it.first .. it.last - 1 }
        x < it.first.x && y in vert
    } % 2 == 1
}
