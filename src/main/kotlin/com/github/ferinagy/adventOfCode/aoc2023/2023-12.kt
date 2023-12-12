package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2023, "12-input")
    val test1 = readInputLines(2023, "12-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Long = input.sumOf { line ->
    val (row, groups) = line.split(' ').let { (row, g) -> row to g.split(',').map(String::toInt) }
    possible(row, groups, mutableMapOf())
}

private fun part2(input: List<String>): Long = input.sumOf { line ->
    val (row, groups) = line.split(' ').let { (row, g) -> row to g.split(',').map(String::toInt) }
    val newRows = List(5) { row }.joinToString(separator = "?")
    val newGroups = List(5) { groups }.flatten()
    possible(newRows, newGroups, mutableMapOf())
}

private fun possible(row: String, groups: List<Int>, cache: MutableMap<Pair<String, List<Int>>, Long>): Long = when {
    row to groups in cache -> cache[row to groups]!!
    row.isEmpty() && groups.isEmpty() -> 1
    row.isEmpty() && groups.isNotEmpty() -> 0
    groups.isEmpty() -> if (row.any { it == '#' }) 0 else 1
    else -> {
        val head = row.first()
        val tail = row.drop(1)
        when (head) {
            '.' -> possible(tail.dropWhile { it == '.' }, groups, cache)
            '#' -> starting(row, groups, cache)
            '?' -> possible(tail, groups, cache) + starting(row, groups, cache)
            else -> error("impossible")
        }
    }
}.also {
    cache[row to groups] = it
}

private fun starting(row: String, groups: List<Int>, cache: MutableMap<Pair<String, List<Int>>, Long>): Long {
    val first = groups.first()
    val next = groups.drop(1)

    if (row.length < first) return 0
    if (row.take(first).any { it == '.' }) return 0

    val rest = row.drop(first)
    return when {
        rest.isEmpty() && next.isEmpty() -> 1
        rest.isEmpty() && next.isNotEmpty() -> 0
        rest.startsWith('#') -> 0
        else -> possible(rest.drop(1), next, cache)
    }
}
