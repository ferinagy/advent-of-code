package com.github.ferinagy.adventOfCode.aoc2016

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import com.github.ferinagy.adventOfCode.transpose

fun main() {
    val input = readInputLines(2016, "03-input")
    val test1 = readInputLines(2016, "03-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    return input.map { line -> line.trim().split("\\s+".toPattern()).map { it.toInt() } }
        .map { it.sorted() }
        .count { (a, b, c) -> c < a + b }
}

private fun part2(input: List<String>): Int {
    return input.map { line -> line.trim().split("\\s+".toPattern()).map { it.toInt() } }
        .transpose()
        .flatten()
        .chunked(3)
        .map { it.sorted() }
        .count { (a, b, c) -> c < a + b }
}
