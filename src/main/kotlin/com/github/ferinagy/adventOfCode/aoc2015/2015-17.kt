package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2015, "17-input")
    val test1 = readInputLines(2015, "17-test1")

    println("Part1:")
    part1(test1, 25).println()
    part1(input, 150).println()

    println()
    println("Part2:")
    part2(test1, 25).println()
    part2(input, 150).println()
}

private fun part1(input: List<String>, total: Int): Int {
    val list = input.map { it.toInt() }
    val combinations = combinations(Combo(list, emptyList(), 0))

    return combinations.filter { it.sum == total }.size
}

private fun part2(input: List<String>, total: Int): Int {
    val list = input.map { it.toInt() }
    val combinations = combinations(Combo(list, emptyList(), 0)).filter { it.sum == total }
    val min = combinations.minOf { it.usedContainers.size }

    return combinations.filter { it.usedContainers.size == min }.size
}

private fun combinations(partial: Combo): List<Combo> {
    if (partial.availableContainers.isEmpty()) {
        return listOf(partial)
    }

    val tail = partial.availableContainers.drop(1)

    val sub = combinations(Combo(tail, emptyList(), 0))
    val current = partial.availableContainers.first()

    return sub + sub.map { it.copy(usedContainers = it.usedContainers + current, sum = it.sum + current) }
}

private data class Combo(val availableContainers: List<Int>, val usedContainers: List<Int>, val sum: Int)
