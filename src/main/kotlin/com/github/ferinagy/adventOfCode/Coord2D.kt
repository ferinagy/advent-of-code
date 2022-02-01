package com.github.ferinagy.adventOfCode

import kotlin.math.abs

@JvmInline value class Coord2DValue(val value: Long) {
}

data class Coord2D(val x: Int, val y: Int) {

    companion object {
        fun parse(input: String) = input.split(",").let { (x, y) -> Coord2D(x.toInt(), y.toInt()) }
    }

    val manhattanDist: Int
        get() = abs(x) + abs(y)

    @OptIn(ExperimentalStdlibApi::class)
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
}

fun List<Coord2D>.filterIn(xRange: IntRange, yRange: IntRange) = filter { it.x in xRange && it.y in yRange }

data class Coord3D(val x: Int, val y: Int, val z: Int) {

    companion object {
        fun parse(input: String) = input.split(",").let { (x, y, z) -> Coord3D(x.toInt(), y.toInt(), z.toInt()) }
    }

    operator fun plus(other: Coord3D) = copy(x = x + other.x, y = y + other.y, z = z + other.z)

    val manhattanDist: Int
        get() = abs(x) + abs(y) + abs(z)
}