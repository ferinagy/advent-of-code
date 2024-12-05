package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2024, "05-input")
    val test1 = readInputText(2024, "05-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: String): Int {
    val (rules, pages) = parse(input)

    return pages.filter { isValid(it, rules) }.sumOf { it[it.size / 2].toInt() }
}

private fun part2(input: String): Int {
    val (rules, pages) = parse(input)

    return pages.filter { !isValid(it, rules) }.map { orderCorrectly(it, rules) }.sumOf { it[it.size / 2].toInt() }
}

private fun isValid(page: List<String>, rules: Map<String, Set<String>>) = page.indices.all { index ->
    page[index] !in rules || page.subList(0, index).none { it in rules[page[index]]!! }
}

private fun orderCorrectly(page: List<String>, rules: Map<String, Set<String>>): List<String> {
    val result = mutableListOf<String>()
    val remaining = page.toMutableSet()
    while (remaining.isNotEmpty()) {
        val next = remaining.find { it !in rules || (rules[it]!!.intersect(remaining) - result).isEmpty() }!!
        result += next
        remaining -= next
    }
    return result
}

private fun parse(input: String): Pair<Map<String, Set<String>>, List<List<String>>> {
    val (rules, pages) = input.split("\n\n").map { it.lines() }
    val after = rules.map { it.split('|') }
        .map { (a, b) -> a to b }
        .groupingBy { it.first }.fold(emptySet<String>()) { acc, next -> acc + next.second }
    return after to pages.map { it.split(',') }
}
