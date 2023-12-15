package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2015, "02-input")
    val test1 = readInputLines(2015, "02-test1")

    println("Part1:")
    calculatePaper(test1).println()
    calculatePaper(input).println()

    println()
    println("Part2:")
    calculateRibbon(test1).println()
    calculateRibbon(input).println()
}

private fun calculatePaper(input: List<String>): Int {
    return input.map { line ->
        val (x, y, z) = line.split('x').map { it.toInt() }
        Triple(x, y, z)
    }.sumOf { (x, y, z) ->
        val s1 = x * y * 2
        val s2 = x * z * 2
        val s3 = y * z * 2

        s1 + s2 + s3 + (minOf(s1, s2, s3) / 2)
    }
}

private fun calculateRibbon(input: List<String>): Int {
    return input.map { line ->
        val (x, y, z) = line.split('x').map { it.toInt() }
        Triple(x, y, z)
    }.sumOf { (x, y, z) ->
        val bow = x * y * z
        val s1 = x + y
        val s2 = x + z
        val s3 = y + z

        bow + minOf(s1, s2, s3) * 2
    }
}
