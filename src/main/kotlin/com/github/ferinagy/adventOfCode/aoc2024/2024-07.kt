package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow

fun main() {
    val input = readInputLines(2024, "07-input")
    val test1 = readInputLines(2024, "07-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Long {
    val parsed = parse(input)

    return parsed.filter { (result, list) -> dfs(result, list.first(), list.drop(1))
    }.sumOf { it.first }
}

private fun part2(input: List<String>): Long {
    val parsed = parse(input)

    return parsed.filter { (result, list) -> dfs2(result, list.first(), list.drop(1)) }.sumOf { it.first }
}

private fun parse(input: List<String>) = input.map { line ->
    val (result, list) = line.split(": ")
    result.toLong() to list.split(' ').map { it.toLong() }
}

private fun dfs(result: Long, acc: Long, list: List<Long>): Boolean {
    if (result < acc) return false
    if (list.isEmpty()) return acc == result

    val first = list.first()
    val rest = list.subList(1, list.size)

    return dfs(result, acc + first, rest) || dfs(result, acc * first, rest)
}

private fun dfs2(result: Long, acc: Long, list: List<Long>): Boolean {
    if (result < acc) return false
    if (list.isEmpty()) return acc == result

    val first = list.first()
    val rest = list.subList(1, list.size)

    return dfs2(result, acc + first, rest) ||
            dfs2(result, acc * first, rest) ||
            dfs2(result, concat(acc, first), rest)
}

private fun concat(n1: Long, n2: Long): Long {
    val digits = log10(abs(n2.toDouble())).toLong() + 1
    return n1 * 10.toDouble().pow(digits.toDouble()).toLong() + n2
}
