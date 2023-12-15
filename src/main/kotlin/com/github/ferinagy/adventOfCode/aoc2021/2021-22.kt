package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.Coord3D
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readInputLines(2021, "22-input")
    val test1 = readInputLines(2021, "22-test1")
    val test2 = readInputLines(2021, "22-test2")

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
    val ops = input.map {
        val (op, x1, x2, y1, y2, z1, z2) = regex.matchEntire(it)!!.destructured
        val cuboid = Cuboid(Coord3D(x1.toInt(), y1.toInt(), z1.toInt()), Coord3D(x2.toInt(), y2.toInt(), z2.toInt()))
        Operation(op == "on", cuboid)
    }

    val cubes = mutableMapOf<Coord3D, Boolean>().withDefault { false }
    ops.forEach {
        for (x in it.cuboid.start.x.coerceAtLeast(-50) .. it.cuboid.end.x.coerceAtMost(50)) {
            for (y in it.cuboid.start.y.coerceAtLeast(-50) .. it.cuboid.end.y.coerceAtMost(50)) {
                for (z in it.cuboid.start.z.coerceAtLeast(-50) .. it.cuboid.end.z.coerceAtMost(50)) {
                    val cube = Coord3D(x, y, z)
                    cubes[cube] = it.turnOn
                }
            }
        }
    }

    return cubes.count { it.value }
}

private fun part2(input: List<String>): Long {
    val ops = input.map {
        val (op, x1, x2, y1, y2, z1, z2) = regex.matchEntire(it)!!.destructured
        val cuboid = Cuboid(Coord3D(x1.toInt(), y1.toInt(), z1.toInt()), Coord3D(x2.toInt(), y2.toInt(), z2.toInt()))
        Operation(op == "on", cuboid)
    }

    val cuboidsOn = mutableSetOf<Cuboid>()
    val queue = ArrayDeque<Cuboid>()
    ops.forEach { op ->
        queue += op.cuboid
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            var didIntersect = false
            for (c in cuboidsOn) {
                if (!current.intersects(c)) continue

                didIntersect = true
                val (inter, first, second) = current.intersect(c)
                cuboidsOn.remove(c)
                if (op.turnOn) {
                    cuboidsOn += inter
                }
                cuboidsOn += second
                queue += first

                break
            }

            if (!didIntersect && op.turnOn) cuboidsOn += current
        }
    }

    return cuboidsOn.sumOf { it.size() }
}

private data class Cuboid(val start: Coord3D, val end: Coord3D)

private fun Cuboid.size(): Long = xRange.count().toLong() * yRange.count() * zRange.count()

private val Cuboid.xRange get() = start.x..end.x
private val Cuboid.yRange get() = start.y..end.y
private val Cuboid.zRange get() = start.z..end.z

private data class IntersectionResult(val intersect: Cuboid, val fromFirst: List<Cuboid>, val fromSecond: List<Cuboid>)

private fun Cuboid.intersects(other: Cuboid): Boolean {
    val startX = max(start.x, other.start.x)
    val startY = max(start.y, other.start.y)
    val startZ = max(start.z, other.start.z)
    val endX = min(end.x, other.end.x)
    val endY = min(end.y, other.end.y)
    val endZ = min(end.z, other.end.z)

    return startX <= endX && startY <= endY && startZ <= endZ
}

private fun Cuboid.intersect(other: Cuboid): IntersectionResult {
    val c1 = Coord3D(max(start.x, other.start.x), max(start.y, other.start.y), max(start.z, other.start.z))
    val c2 = Coord3D(min(end.x, other.end.x), min(end.y, other.end.y), min(end.z, other.end.z))
    val intersect = Cuboid(c1, c2)

    val first = this - intersect
    val second = other - intersect

    return IntersectionResult(intersect, first, second)
}

private operator fun Cuboid.minus(other: Cuboid): List<Cuboid> {
    return listOf(
        Cuboid(
            Coord3D(start.x, start.y, start.z),
            Coord3D(other.start.x - 1, end.y, end.z),
        ),
        Cuboid(
            Coord3D(other.start.x, start.y, start.z),
            Coord3D(other.end.x, end.y, other.start.z - 1),
        ),
        Cuboid(
            Coord3D(other.start.x, start.y, other.start.z),
            Coord3D(other.end.x, other.start.y - 1, other.end.z),
        ),
        Cuboid(
            Coord3D(other.start.x, other.end.y + 1, other.start.z),
            Coord3D(other.end.x, end.y, other.end.z),
        ),
        Cuboid(
            Coord3D(other.start.x, start.y, other.end.z + 1),
            Coord3D(other.end.x, end.y, end.z),
        ),
        Cuboid(
            Coord3D(other.end.x + 1, start.y, start.z),
            Coord3D(end.x, end.y, end.z),
        )
    ).filter { it.size() != 0L }
}

private data class Operation(val turnOn: Boolean, val cuboid: Cuboid)

private val regex = """(on|off) x=(-?\d+)..(-?\d+),y=(-?\d+)..(-?\d+),z=(-?\d+)..(-?\d+)""".toRegex()
