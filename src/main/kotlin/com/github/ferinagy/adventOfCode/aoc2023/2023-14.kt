package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.CharGrid
import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import com.github.ferinagy.adventOfCode.toCharGrid

fun main() {
    val input = readInputLines(2023, "14-input")
    val test1 = readInputLines(2023, "14-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val grid = input.toCharGrid()
    val (cubes, round) = parse(grid)

    return tilt(round, cubes, grid.width, grid.height, Coord2D(0, -1)).sumOf { grid.height - it.y }
}

private fun part2(input: List<String>): Int {
    val grid = input.toCharGrid()
    val (cubes, round) = parse(grid)

    var newRounds = round
    val cycles = mutableMapOf(newRounds to 0)
    var cycle = 0
    while (true) {
        newRounds = loop(newRounds, cubes, grid)
        cycle++
        if (newRounds in cycles) {
            break
        }

        cycles[newRounds] = cycle
    }

    val cycleStart = cycles[newRounds]!!
    val loopLength = cycle - cycleStart

    repeat((1000000000 - cycleStart) % loopLength) {
        newRounds = loop(newRounds, cubes, grid)
    }

    return newRounds.sumOf { grid.height - it.y }
}

private fun loop(rounds: Set<Coord2D>, cubes: Set<Coord2D>, grid: CharGrid) = directions.fold(rounds) { acc, dir ->
    tilt(acc, cubes, grid.width, grid.height, dir)
}

private fun parse(grid: CharGrid): Pair<Set<Coord2D>, Set<Coord2D>> {
    val cubes = mutableSetOf<Coord2D>()
    val round = mutableSetOf<Coord2D>()

    grid.yRange.forEach { y ->
        grid.xRange.forEach { x ->
            val c = grid[x, y]
            when (c) {
                '#' -> cubes += Coord2D(x, y)
                'O' -> round += Coord2D(x, y)
            }
        }
    }
    return cubes to round
}

private val directions = listOf(Coord2D(0, -1), Coord2D(-1, 0), Coord2D(0, 1), Coord2D(1, 0))

private fun Coord2D.selector(coord: Coord2D) = -(x * coord.x + y * coord.y)

private fun tilt(round: Set<Coord2D>, cubes: Set<Coord2D>, width: Int, height: Int, dir: Coord2D): Set<Coord2D> {
    val newRounds = mutableSetOf<Coord2D>()
    round.sortedBy(dir::selector).forEach {
        var coord = it
        while ((coord + dir).isIn(width, height) && coord + dir !in cubes && coord + dir !in newRounds) coord += dir

        newRounds += coord
    }
    return newRounds
}

private fun Coord2D.isIn(width: Int, height: Int) = x in 0..<width && y in 0..<height
