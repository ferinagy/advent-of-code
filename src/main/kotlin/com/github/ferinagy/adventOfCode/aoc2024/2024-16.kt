package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.*
import java.util.PriorityQueue

fun main() {
    val input = readInputLines(2024, "16-input")
    val test1 = readInputLines(2024, "16-test1")
    val test2 = readInputLines(2024, "16-test2")

    println("Part1:")
    part1(test1).println()
    part1(test2).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(test2).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val (grid, start, end) = parse(input)

    return searchGraph(
        start = start,
        isDone = { it.position == end },
        nextSteps = {
            buildSet {
                if (grid[it.position + it.direction] != '#') this += it.copy(position = it.position + it.direction) to 1
                this += it.copy(direction = it.direction.rotateCw()) to 1000
                this += it.copy(direction = it.direction.rotateCcw()) to 1000
            }
        },
    )
}

private fun part2(input: List<String>): Int {
    val (grid, start, end) = parse(input)

    val dists = mutableMapOf<Step, Pair<Int, Set<Coord2D>>>()
    dists[start] = 0 to setOf(start.position)

    val queue = PriorityQueue(compareBy<Step> { dists[it]!!.first })
    queue += start

    while (queue.isNotEmpty()) {
        val current: Step = queue.remove()
        val dist = dists[current]!!

        if (current.position == end) {
            return dist.second.size
        }

        val nextSteps = buildSet {
            if (grid[current.position + current.direction] != '#') {
                this += current.copy(position = current.position + current.direction) to 1
            }
            this += current.copy(direction = current.direction.rotateCw()) to 1000
            this += current.copy(direction = current.direction.rotateCcw()) to 1000
        }

        for ((next, stepSize) in nextSteps) {
            val nextDist = dist.first + stepSize

            if (dists[next] == null || nextDist < dists[next]!!.first) {
                dists[next] = nextDist to (dist.second + next.position)
                queue += next
            } else if (nextDist == dists[next]?.first) {
                dists[next] = nextDist to (dist.second + dists[next]!!.second)
            }
        }
    }

    return -1
}

private fun parse(input: List<String>): Triple<CharGrid, Step, Coord2D> {
    val grid = input.toCharGrid()
    val start = Step(grid.positionOfFirst { it == 'S' })
    val end = grid.positionOfFirst { it == 'E' }
    return Triple(grid, start, end)
}

private data class Step(val position: Coord2D, val direction: Coord2D = Coord2D(1, 0))
