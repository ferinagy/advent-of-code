package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2015, "05-input")
    val test1 = readInputLines(2015, "05-test1")
    val test2 = readInputLines(2015, "05-test2")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test2).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    return input.count { isNice1(it) }
}

private fun part2(input: List<String>): Int {
    return input.count { isNice2(it) }
}

private fun isNice1(input: String): Boolean {
    if (input.count { it in "aeiou" } < 3) return false

    if (setOf("ab", "cd", "pq", "xy").any { it in input }) return false

    if (input.windowed(2).all { it[0] != it[1] }) return false

    return true
}

private fun isNice2(input: String): Boolean {
    if (input.windowed(3).all { it[0] != it[2] }) return false

    val windowed = input.windowed(2)
    for (i in 0 .. windowed.lastIndex - 2) {
        for (j in i + 2 .. windowed.lastIndex) {
            if (windowed[i] == windowed[j]) return true
        }
    }

    return false
}
