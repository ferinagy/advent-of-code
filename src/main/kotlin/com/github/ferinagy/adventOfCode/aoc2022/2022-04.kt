package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2022, "04-input")
    val testInput1 = readInputLines(2022, "04-test1")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(testInput1).println()
    part2(input).println()
}

private fun String.toRange(): IntRange = split('-').let { (a,b) -> a.toInt()..b.toInt() }

private fun part1(input: List<String>): Int {
    val ranges = parseRanges(input)

    return ranges.count { (a, b) -> a.first in b && a.last in b || b.first in a && b.last in a }
}

private fun part2(input: List<String>): Int {
    val ranges = parseRanges(input)

    return ranges.count { (a, b) -> a.first in b || a.last in b || b.first in a || b.last in a }
}

private fun parseRanges(input: List<String>) = input
    .map { it.split(',') }
    .map { (a, b) -> a.toRange() to b.toRange() }
