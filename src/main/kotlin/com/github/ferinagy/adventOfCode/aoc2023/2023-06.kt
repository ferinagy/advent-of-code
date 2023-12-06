package com.github.ferinagy.adventOfCode.aoc2023

fun main() {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput1))
    println(part2(input))
}

private fun part1(input: String): Int {
    val lines = input.lines()
    val times = lines[0].split("""\s+""".toRegex()).drop(1).map(String::toLong)
    val distances = lines[1].split("""\s+""".toRegex()).drop(1).map(String::toLong)
    return distances.zip(times).fold(1) { acc, (dist, time) -> acc * waysToWin(dist, time) }
}

private fun part2(input: String): Int {
    val lines = input.lines()
    val time = lines[0].split("""\s+""".toRegex()).drop(1).joinToString(separator = "").let(String::toLong)
    val distance = lines[1].split("""\s+""".toRegex()).drop(1).joinToString(separator = "").let(String::toLong)
    return waysToWin(distance, time)
}

private fun waysToWin(dist: Long, time: Long) = (1 ..< time).count { dist < it * (time - it) }

private const val testInput1 = """Time:      7  15   30
Distance:  9  40  200"""

private const val input = """Time:        38     67     76     73
Distance:   234   1027   1157   1236"""
