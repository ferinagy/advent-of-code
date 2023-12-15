package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import kotlin.math.abs
import kotlin.math.max

fun main() {
    val input = readInputLines(2021, "05-input")
    val test1 = readInputLines(2021, "05-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val parsed = parse(input)
    val filtered = parsed.filter { (start, end) -> start.x == end.x || start.y == end.y }

    return countOverlaps(filtered)
}

private fun part2(input: List<String>): Int {
    val parsed = parse(input)

    return countOverlaps(parsed)
}

private fun countOverlaps(lines: List<Pair<Coord2D, Coord2D>>): Int {
    val field = mutableMapOf<Coord2D, Int>()
    lines.forEach { (start, end) ->
        for (c in range(start, end)) {
            val count = field.getOrDefault(c, 0)
            field[c] = count + 1
        }
    }

    return field.count { 2 <= it.value }
}

private fun parse(input: List<String>): List<Pair<Coord2D, Coord2D>> = input.map {
    it.split(" -> ").let { (c1, c2) -> Coord2D.parse(c1) to Coord2D.parse(c2) }
}

private fun range(start: Coord2D, end: Coord2D): List<Coord2D> = buildList {
    val dx = end.x - start.x
    val dy = end.y - start.y

    val max = max(abs(dx), abs(dy))
    val stepX = dx / max
    val stepY = dy / max

    repeat(max + 1) {
        this += Coord2D(start.x + stepX * it, start.y + stepY * it)
    }
}

