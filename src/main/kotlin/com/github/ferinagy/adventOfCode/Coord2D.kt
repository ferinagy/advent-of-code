package com.github.ferinagy.adventOfCode

import kotlin.math.abs

data class Coord2D(val x: Int, val y: Int) {

    companion object {
        fun parse(input: String) = input.split(",").let { (x, y) -> Coord2D(x.toInt(), y.toInt()) }
    }

    override fun toString() = "[$x, $y]"

    val manhattanDist: Int
        get() = abs(x) + abs(y)

    fun adjacent(includeDiagonals: Boolean): List<Coord2D> = buildList {
        setOf(-1 to 0, 0 to -1, 1 to 0, 0 to 1).forEach {
            this += copy(x = x + it.first, y = y + it.second)
        }
        if (includeDiagonals) {
            setOf(-1 to -1, -1 to 1, 1 to -1, 1 to 1).forEach {
                this += copy(x = x + it.first, y = y + it.second)
            }
        }
    }

    operator fun plus(other: Coord2D) = copy(x = x + other.x, y = y + other.y)

    operator fun minus(other: Coord2D) = copy(x = x - other.x, y = y - other.y)
    operator fun unaryMinus() = copy(x = -x, y = -y)

    operator fun times(n: Int) = Coord2D(x * n, y * n)

    fun distanceTo(other: Coord2D) = abs(x - other.x) + abs(y - other.y)
}

fun Coord2D.rotateCw() = Coord2D(y, -x)

fun Coord2D.rotateCcw() = Coord2D(-y, x)

fun Coord2D.isIn(xRange: IntRange, yRange: IntRange) = x in xRange && y in yRange

fun List<Coord2D>.filterIn(xRange: IntRange, yRange: IntRange) = filter { it.isIn(xRange, yRange) }

data class Coord3D(val x: Int, val y: Int, val z: Int) {

    companion object {
        fun parse(input: String) = input.split(",").let { (x, y, z) -> Coord3D(x.toInt(), y.toInt(), z.toInt()) }
    }

    operator fun plus(other: Coord3D) = copy(x = x + other.x, y = y + other.y, z = z + other.z)

    val manhattanDist: Int
        get() = abs(x) + abs(y) + abs(z)
}
