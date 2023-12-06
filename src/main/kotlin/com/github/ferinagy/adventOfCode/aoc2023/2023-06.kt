package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input1 = readInputLines(2023, "06-input")
    val test1 = readInputLines(2023, "06-test1")

    println("Part1:")
    part1(test1).println()
    part1(input1).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input1).println()
}

private fun part1(input: List<String>): Int {
    val times = input[0].split("""\s+""".toRegex()).drop(1).map(String::toLong)
    val distances = input[1].split("""\s+""".toRegex()).drop(1).map(String::toLong)
    return distances.zip(times).fold(1) { acc, (dist, time) -> acc * waysToWin(dist, time) }
}

private fun part2(input: List<String>): Int {
    val time = input[0].split("""\s+""".toRegex()).drop(1).joinToString(separator = "").let(String::toLong)
    val distance = input[1].split("""\s+""".toRegex()).drop(1).joinToString(separator = "").let(String::toLong)
    return waysToWin(distance, time)
}

private fun waysToWin(dist: Long, time: Long) = (1 ..< time).count { dist < it * (time - it) }
