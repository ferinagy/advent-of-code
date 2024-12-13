package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2024, "13-input")
    val test1 = readInputLines(2024, "13-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>) = parse(input)
    .mapNotNull { solve(it) }
    .filter { it.first <= 100 && it.second <= 100 }
    .sumOf { (a, b) -> a * 3 + b }

private fun part2(input: List<String>) = parse(input)
    .map { it.take(2) + (it[2].first + 10000000000000 to it[2].second + 10000000000000) }
    .mapNotNull { solve(it) }
    .sumOf { (a, b) -> a * 3 + b }

private fun parse(input: List<String>) = input.windowed(3, 4).map {
    val (x1, y1) = regex1.matchEntire(it[0])!!.destructured
    val (x2, y2) = regex1.matchEntire(it[1])!!.destructured
    val (x3, y3) = regex2.matchEntire(it[2])!!.destructured
    listOf(x1.toLong() to y1.toLong(), x2.toLong() to y2.toLong(), x3.toLong() to y3.toLong())
}

private fun solve(pairs: List<Pair<Long, Long>>): Pair<Long, Long>? {
    val (p1, p2, p3) = pairs

    val (x1, y1) = p1
    val (x2, y2) = p2
    val (x3, y3) = p3
    val d = x1 * y2 - y1 * x2
    val n1 = y3 * x1 - x3 * y1
    if (n1 % d != 0L) return null

    val b = n1 / d

    val n2 = x3 - x2 * b
    if (n2 % x1 != 0L) return null

    val a = n2 / x1

    return a to b
}

private val regex1 = """Button .: X\+(\d+), Y\+(\d+)""".toRegex()
private val regex2 = """Prize: X=(\d+), Y=(\d+)""".toRegex()