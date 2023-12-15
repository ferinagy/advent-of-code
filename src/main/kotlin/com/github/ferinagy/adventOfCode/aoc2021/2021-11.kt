package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.filterIn
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2021, "11-input")
    val test1 = readInputLines(2021, "11-test1")


    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val cave = OctopusCave(input)

    var flashes = 0
    repeat(100) {
        flashes += cave.step()
    }

    return flashes
}

private fun part2(input: List<String>): Int {
    val cave = OctopusCave(input)

    var step = 1
    while (true) {
        val flashes = cave.step()
        if (flashes == 100) return step

        step++
    }
}

private class OctopusCave(input: List<String>) {

    private val area = input.mapIndexed { row, line ->
        line.mapIndexed { col, c ->
            Octopus(Coord2D(col, row), c.digitToInt())
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
        val diffs = coord.adjacent(true).filterIn(area.first().indices, area.indices)

        return diffs.map {  area[it.y][it.x] }
    }
}

private class Octopus(val coord: Coord2D, var energy: Int) {
    var didFlash = false
}
