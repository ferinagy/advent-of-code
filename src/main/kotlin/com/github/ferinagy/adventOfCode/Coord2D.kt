package com.github.ferinagy.adventOfCode

data class Coord2D(val x: Int, val y: Int) {

    companion object {
        fun parse(input: String) = input.split(",").let { (x, y) -> Coord2D(x.toInt(), y.toInt()) }
    }
}