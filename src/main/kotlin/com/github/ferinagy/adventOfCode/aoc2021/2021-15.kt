package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import java.util.*

fun main() {
    val input = readInputLines(2021, "15-input")
    val test1 = readInputLines(2021, "15-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val cave = input.map { line ->
        line.map { it.digitToInt() }
    }

    return findPathRisk(cave)
}

private fun part2(input: List<String>): Int {
    val smallCave = input.map { line ->
        line.map { it.digitToInt() }
    }

    val width = smallCave.first().size
    val height = smallCave.size

    val cave = List(height * 5) { mutableListOf<Int>() }
    repeat(5) { vertical ->
        repeat(5) { horizontal ->
            val startY = vertical * width
            smallCave.forEachIndexed { col, line ->
                cave[startY + col] += line.map {
                    val new = it + horizontal + vertical
                    if (new <= 9) new else new % 9
                }
            }
        }
    }

    return findPathRisk(cave)
}

private fun findPathRisk(cave: List<List<Int>>): Int {
    val end = Coord2D(cave[cave.lastIndex].lastIndex, cave.lastIndex)

    val minDists = Array(cave.size) {
        Array(cave[it].size) { -1 }
    }
    minDists[0][0] = 0

    val queue = PriorityQueue(
        compareBy<Coord2D> { minDists[it.y][it.x] }
            .thenBy { (end.y - it.y) + (end.x - it.x) }
    )

    queue.add(Coord2D(0, 0))

    while (queue.isNotEmpty()) {
        val next = queue.poll()
        if (next == end) {
            return minDists[next.y][next.x]
        }

        fun handleCandidate(candidate: Coord2D) {
            val dist = minDists[next.y][next.x] + cave[candidate.y][candidate.x]
            if (minDists[candidate.y][candidate.x] == -1) {
                minDists[candidate.y][candidate.x] = dist
                queue += candidate
            } else if (dist < minDists[candidate.y][candidate.x]) {
                minDists[candidate.y][candidate.x] = dist
            }
        }

        if (0 < next.x) {
            val candidate = next.copy(x = next.x - 1)
            handleCandidate(candidate)
        }
        if (0 < next.y) {
            val candidate = next.copy(y = next.y - 1)
            handleCandidate(candidate)
        }
        if (next.x < cave[next.y].lastIndex) {
            val candidate = next.copy(x = next.x + 1)
            handleCandidate(candidate)
        }
        if (next.y < cave.lastIndex) {
            val candidate = next.copy(y = next.y + 1)
            handleCandidate(candidate)
        }
    }

    return -1
}
