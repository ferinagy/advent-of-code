package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.Coord2D

fun main(args: Array<String>) {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput1))
    println(part2(input))
}

private fun part1(input: String): Int {
    val cave = OctopusCave(input)

    var flashes = 0
    repeat(100) {
        flashes += cave.step()
    }

    return flashes
}

private fun part2(input: String): Int {
    val cave = OctopusCave(input)

    var step = 1
    while (true) {
        val flashes = cave.step()
        if (flashes == 100) return step

        step++
    }
}

private class OctopusCave(input: String) {

    private val area: List<List<Octopus>>

    init {
        area = input.lines().mapIndexed { row, line ->
            line.mapIndexed { col, c ->
                Octopus(Coord2D(col, row), c.digitToInt())
            }
        }
    }

    fun print() {
        area.forEach { row ->
            row.forEach {
                print(it.energy)
            }
            println()
        }
        println()
    }

    fun step(): Int {
        area.forEach { row ->
            row.forEach { it.energy++ }
        }

        val newFlashes = mutableSetOf<Octopus>()
        do {
            newFlashes.clear()
            newFlashes += area.flatMap { line -> line.filter { !it.didFlash && 9 < it.energy } }.toSet()
            newFlashes.forEach { it.didFlash = true }

            newFlashes.forEach { new ->
                new.neighbors()
                    .filter { !it.didFlash }
                    .forEach { it.energy++ }
            }
        } while (newFlashes.isNotEmpty())

        var flashes = 0
        area.forEach { row ->
            row.forEach {
                if (it.didFlash) {
                    it.didFlash = false
                    it.energy = 0
                    flashes++
                }
            }
        }

        return flashes
    }

    private fun Octopus.neighbors(): List<Octopus> {
        val diffs = listOf(
            Coord2D(-1, -1),
            Coord2D(0, -1),
            Coord2D(1, -1),
            Coord2D(-1, 0),
            Coord2D(1, 0),
            Coord2D(-1, 1),
            Coord2D(0, 1),
            Coord2D(1, 1)
        )

        return diffs.mapNotNull {
            val newY = coord.y + it.y
            val newX = coord.x + it.x
            if (newY in area.indices && newX in area[newY].indices) area[newY][newX] else null
        }
    }
}

private class Octopus(val coord: Coord2D, var energy: Int) {
    var didFlash = false
}

private const val testInput1 = """5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526"""

private const val input = """6111821767
1763611615
3512683131
8582771473
8214813874
2325823217
2222482823
5471356782
3738671287
8675226574"""
