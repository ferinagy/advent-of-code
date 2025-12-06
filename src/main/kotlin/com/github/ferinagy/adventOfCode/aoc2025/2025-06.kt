package com.github.ferinagy.adventOfCode.aoc2025

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import com.github.ferinagy.adventOfCode.transpose
import kotlin.collections.fold

fun main() {
    val input = readInputLines(2025, "06-input")
    val test1 = readInputLines(2025, "06-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Long {
    val problems = input.map {
        it.split(' ').filter { it.isNotEmpty() }
    }.transpose()

    return problems.sumOf {
        when (val sign = it.last()) {
            "+" -> it.dropLast(1).fold(0L) { acc, next -> acc + next.toLong() }
            "*" -> it.dropLast(1).fold(1L) { acc, next -> acc * next.toLong() }
            else -> error("Unknown sign $sign")
        }
    }
}

private fun part2(input: List<String>): Long {
    val signs = input.last()
    val indices = signs.foldIndexed(emptyList<Int>()) { i, acc, next ->
        if (next != ' ') acc + i else acc
    } + (signs.length + 1)

    val numbers = input.dropLast(1)

    return indices.windowed(2).sumOf { (start, end) ->
        val problem = (start ..< end-1).map { index ->
            numbers.map { it[index] }.joinToString(separator = "").trim().toLong()
        }

        when (val sign = signs[start]) {
            '+' -> problem.fold(0L) { acc, next -> acc + next }
            '*' -> problem.fold(1L) { acc, next -> acc * next }
            else -> error("Unknown sign $sign")
        }
    }
}
