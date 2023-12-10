package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2022, "23-input")
    val testInput1 = readInputLines(2022, "23-test1")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(testInput1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val elves = solve(input, 10).first

    val minX = elves.minOf { it.x }
    val minY = elves.minOf { it.y }
    val maxX = elves.maxOf { it.x }
    val maxY = elves.maxOf { it.y }

    return (maxX - minX + 1) * (maxY - minY + 1) - elves.size
}

private fun part2(input: List<String>): Int {
    return solve(input, null).second
}

private fun solve(input: List<String>, max: Int?): Pair<MutableSet<Coord2D>, Int> {
    val elves = mutableSetOf<Coord2D>()
    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c == '#') elves += Coord2D(x, y)
        }
    }

    val dirs = listOf(Coord2D(0, -1), Coord2D(0, 1), Coord2D(-1, 0), Coord2D(1, 0)).map {
        listOf(it, Coord2D(it.x + it.y, it.y + it.x), Coord2D(it.x - it.y, it.y - it.x))
    }
    var iteration = 0
    while (true) {
        val map = mutableMapOf<Coord2D, MutableList<Coord2D>>()
        elves.forEach { e ->
            if (e.adjacent(includeDiagonals = true).any { it in elves }) {
                for (d in 0..3) {
                    val dir = dirs[(d + iteration) % 4]
                    if (dir.none { e + it in elves }) {
                        val next = e + dir.first()
                        map.getOrPut(next) { mutableListOf() }.add(e)
                        break
                    }
                }
            }
        }

        var moved = false
        map.forEach { (dest, src) ->
            if (src.size == 1) {
                elves -= src.single()
                elves += dest
                moved = true
            }
        }

        iteration++
        if (iteration == max) break
        if (!moved) break
    }
    return elves to iteration
}
