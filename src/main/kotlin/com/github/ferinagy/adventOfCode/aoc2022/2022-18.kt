package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.Coord3D
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import com.github.ferinagy.adventOfCode.searchGraph
import com.github.ferinagy.adventOfCode.singleStep
import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readInputLines(2022, "18-input")
    val testInput1 = readInputLines(2022, "18-test1")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(testInput1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val droplets = input.map {
        val (a, b, c) = it.split(',')
        Coord3D(a.toInt(), b.toInt(), c.toInt())
    }

    return droplets.sumOf {
        it.neighbors().count { it !in droplets }
    }
}

private fun part2(input: List<String>): Int {
    val droplets = input.map {
        val (a, b, c) = it.split(',')
        Coord3D(a.toInt(), b.toInt(), c.toInt())
    }.toSet()

    var (minX, minY, minZ) = droplets.first()
    var (maxX, maxY, maxZ) = droplets.first()

    droplets.forEach {
        minX = min(minX, it.x - 1)
        minY = min(minY, it.y - 1)
        minZ = min(minZ, it.z - 1)
        maxX = max(maxX, it.x + 1)
        maxY = max(maxY, it.y + 1)
        maxZ = max(maxZ, it.z + 1)
    }

    val pockets = mutableSetOf<Coord3D>()

    fun Coord3D.inPocket() = searchGraph(
        start = this,
        isDone = { it.x == minX || it.x == maxX || it.y == minY || it.y == maxY || it.z == minZ || it.z == maxZ },
        nextSteps = {
            if (it in pockets) emptySet() else (it.neighbors().toSet() - droplets).singleStep()
        }
    ) == -1

    return droplets.sumOf { drop ->
        drop.neighbors().count {
            if (it in droplets) {
                false
            } else if (it.inPocket()) {
                pockets += it
                false
            } else true
        }
    }
}

private fun Coord3D.neighbors() = listOf(
    copy(x = x + 1),
    copy(x = x - 1),
    copy(y = y + 1),
    copy(y = y - 1),
    copy(z = z + 1),
    copy(z = z - 1),
)
