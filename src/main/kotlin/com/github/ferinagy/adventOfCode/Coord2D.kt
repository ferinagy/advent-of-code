package com.github.ferinagy.adventOfCode

@JvmInline value class Coord2DValue(val value: Long) {
}

data class Coord2D(val x: Int, val y: Int) {

    companion object {
        fun parse(input: String) = input.split(",").let { (x, y) -> Coord2D(x.toInt(), y.toInt()) }
    }

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