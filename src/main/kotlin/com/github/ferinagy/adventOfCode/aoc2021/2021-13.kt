package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2021, "13-input")
    val test1 = readInputText(2021, "13-test1")


    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: String): Int {
    val (dots, folds) = parse(input)
    val result = dots.fold(folds.first())

    return result.size
}

private fun part2(input: String): String {
    val (dots, folds) = parse(input)
    val result = folds.fold(dots) { acc, f -> acc.fold(f) }

    val maxX = result.maxOf { it.x }
    val maxY = result.maxOf { it.y }

    return buildString {
        for (col in 0..maxY) {
            for (row in 0..maxX) {
                if (Coord2D(row, col) in result) append('#') else append('.')
            }
            append('\n')
        }
    }
}

private fun parse(input: String): Pair<Set<Coord2D>, List<Fold>> {
    val (dotsString, foldsString) = input.split("\n\n")
    val dots = dotsString.lines().map { it.split(',').let { (x, y) -> Coord2D(x.toInt(), y.toInt()) } }.toSet()
    val folds = foldsString.lines().map {
        val (axis, value) = regex.matchEntire(it)!!.destructured
        Fold(axis.single(), value.toInt())
    }
    return Pair(dots, folds)
}

private fun Set<Coord2D>.fold(fold: Fold): Set<Coord2D> {
    val result = mutableSetOf<Coord2D>()
    if (fold.axis == 'x') {
        forEach {
            result += if (fold.value < it.x) {
                it.copy(x = fold.value - (it.x - fold.value))
            } else {
                it
            }
        }
    } else {
        forEach {
            result += if (fold.value < it.y) {
                it.copy(y = fold.value - (it.y - fold.value))
            } else {
                it
            }
        }
    }

    return result
}

private val regex = """fold along ([xy])=(\d+)""".toRegex()

private data class Fold(val axis: Char, val value: Int)
