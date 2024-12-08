package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2024, "08-input")
    val test1 = readInputLines(2024, "08-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val (map, width, height) = parse(input)

    return map.flatMapTo(mutableSetOf()) { (_, coords) ->
        (0..<coords.lastIndex).flatMapTo(mutableSetOf()) { i ->
            (i + 1..coords.lastIndex).flatMapTo(mutableSetOf()) { j ->
                buildSet {
                    val d = coords[i] - coords[j]
                    val c1 = coords[j] - d
                    val c2 = coords[i] + d

                    if (c1.y in 0..<height && c1.x in 0..<width) this += c1
                    if (c2.y in 0..<height && c2.x in 0..<width) this += c2
                }
            }
        }
    }.size
}

private fun part2(input: List<String>): Int {
    val (map, width, height) = parse(input)

    return map.flatMapTo(mutableSetOf()) { (_, coords) ->
        (0..<coords.lastIndex).flatMapTo(mutableSetOf()) { i ->
            (i + 1..coords.lastIndex).flatMapTo(mutableSetOf()) { j ->
                val d = coords[i] - coords[j]
                getAllCandidates(coords[i], d, width, height) + getAllCandidates(coords[j], -d, width, height)
            }
        }
    }.size
}

private fun getAllCandidates(from: Coord2D, dir: Coord2D, width: Int, height: Int) = buildSet {
    var k = 0
    while (true) {
        val c = from - (dir * k)
        if (c.y in 0..<height && c.x in 0..<width) this += c else break
        k++
    }
}

private fun parse(input: List<String>): Triple<MutableMap<Char, List<Coord2D>>, Int, Int> {
    val map = mutableMapOf<Char, List<Coord2D>>()
    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c != '.') map[c] = map[c].orEmpty() + Coord2D(x, y)
        }
    }
    val width = input.first().length
    val height = input.size
    return Triple(map, width, height)
}
