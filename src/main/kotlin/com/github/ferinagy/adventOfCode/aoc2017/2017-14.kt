package com.github.ferinagy.adventOfCode.aoc2017

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.searchGraph
import com.github.ferinagy.adventOfCode.singleStep

fun main() {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput1))
    println(part2(input))
}

private fun part1(input: String): Int = parseDisk(input).sumOf { line -> line.count { it == '1' } }

private fun part2(input: String): Int {
    val disk = parseDisk(input)

    val used = mutableSetOf<Coord2D>()
    disk.forEachIndexed { y, row ->
        row.forEachIndexed { x, c ->
            if (c == '1') used += Coord2D(x, y)
        }
    }

    var regions = 0
    while (used.isNotEmpty()) {
        val visited = mutableSetOf<Coord2D>()
        searchGraph(
            start = used.first(),
            isDone = {
                visited += it
                false
            },
            nextSteps = { (it.adjacent(false).toSet() intersect used).singleStep() }
        )

        used -= visited
        regions++
    }

    return regions
}

private fun parseDisk(input: String): List<String> {
    val strings = (0 until 128).map { n ->
        val hasher = KnotHasher()
        val hashInput = "$input-$n"
        hasher.hash(hashInput)
    }

    val binary = strings.map { line ->
        line.map { "%04d".format(it.digitToInt(16).toString(2).toInt()) }.joinToString(separator = "")
    }
    return binary
}

private const val testInput1 = """flqrgnkx"""

private const val input = """amgozmfv"""
