package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.lcm
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import com.github.ferinagy.adventOfCode.searchGraph
import com.github.ferinagy.adventOfCode.singleStep

fun main() {
    val input = readInputLines(2022, "24-input")
    val testInput1 = readInputLines(2022, "24-test1")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(testInput1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val bb = BlizzardBasin(input)
    return bb.search(bb.entry, bb.exit, 0)
}

private fun part2(input: List<String>): Int {
    val bb = BlizzardBasin(input)
    val t1 =  bb.search(bb.entry, bb.exit, 0)
    val t2 = bb.search(bb.exit, bb.entry, t1)
    val t3 =  bb.search(bb.entry, bb.exit, t1 + t2)

    return t1 + t2 + t3
}

private class BlizzardBasin(lines: List<String>) {
    private data class Blizzard(val position: Coord2D, val direction: Coord2D)

    val height = lines.size
    val width = lines.first().length

    val entry = Coord2D(lines.first().indexOf('.'), 0)
    val exit = Coord2D(lines.last().indexOf('.'), lines.lastIndex)

    private val blizzards = buildList {
        for (x in 0 until width) {
            for (y in 0 until height) {
                when (lines[y][x]) {
                    '>' -> this += Blizzard(Coord2D(x, y), Coord2D(1, 0))
                    '<' -> this += Blizzard(Coord2D(x, y), Coord2D(-1, 0))
                    '^' -> this += Blizzard(Coord2D(x, y), Coord2D(0, -1))
                    'v' -> this += Blizzard(Coord2D(x, y), Coord2D(0, 1))
                }
            }
        }
    }

    private val loop = lcm((width - 2), (height - 2))
    private val precomputed = (0 until loop).map { time -> blizzards.map { it.positionAt(time) }.toSet() }

    fun search(start: Coord2D, goal: Coord2D, startTime: Int): Int {
        return searchGraph(
            start = start to startTime,
            isDone = { (position, _) -> position == goal },
            nextSteps = { state ->
                val (position, time) = state
                val all = position.adjacent(false) + position
                val possible = all.filter { candidate ->
                    candidate.x in 1 .. width - 2 &&
                            (candidate.y in 1 .. height - 2 || candidate == entry || candidate == exit) &&
                            candidate !in precomputed[(time + 1) % loop]
                }
                possible.map { it to time + 1 }.toSet().singleStep()
            },
        )
    }

    private fun Blizzard.positionAt(time: Int): Coord2D {
        val c = position + (direction * time)
        return Coord2D(x = ((c.x - 1).mod(width - 2)) + 1, y = ((c.y - 1).mod(height - 2)) + 1)
    }
}
