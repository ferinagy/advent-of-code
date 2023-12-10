package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2022, "03-input")
    val testInput1 = readInputLines(2022, "03-test1")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(testInput1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val sacks = input.map {
        val half = it.length / 2
        it.substring(0, half) to it.substring(half)
    }
    return sacks.map { (a, b) -> (a.toSet() intersect b.toSet()).single() }
        .sumOf { priorities[it]!! }
}

private fun part2(input: List<String>): Int {
    val groups = input.map { it.toSet() }.windowed(3, 3)

    return groups.map { (a, b, c) -> a intersect b intersect c }.sumOf { priorities[it.single()]!! }
}

private val priorities = (('a'..'z') + ('A'..'Z')).mapIndexed { i, c -> c to i + 1 }.toMap()
