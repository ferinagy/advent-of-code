package com.github.ferinagy.adventOfCode.aoc2015

fun main() {
    println("Part1:")
    println(part1(testInput1, 25))
    println(part1(input, 150))

    println()
    println("Part2:")
    println(part2(testInput1, 25))
    println(part2(input, 150))
}

private fun part1(input: String, total: Int): Int {
    val list = input.lines().map { it.toInt() }
    val combinations = combinations(Combo(list, emptyList(), 0))

    return combinations.filter { it.sum == total }.size
}

private fun part2(input: String, total: Int): Int {
    val list = input.lines().map { it.toInt() }
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

private const val testInput1 = """20
15
10
5
5"""

private const val input = """43
3
4
10
21
44
4
6
47
41
34
17
17
44
36
31
46
9
27
38"""
