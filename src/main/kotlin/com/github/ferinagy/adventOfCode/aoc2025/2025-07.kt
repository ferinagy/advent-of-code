package com.github.ferinagy.adventOfCode.aoc2025

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2025, "07-input")
    val test1 = readInputLines(2025, "07-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Long {
    val start = setOf(input.first().indexOf('S'))
    var result = 0L
    input.drop(1).fold(start) { acc, row ->
        acc.flatMapTo(mutableSetOf()) {
            if (row[it] == '.') setOf(it) else {
                result++
                setOf(it + 1, it - 1)
            }
        }
    }

    return result
}

private fun part2(input: List<String>): Long {
    val start = input.first().indexOf('S')

    return routes(start, input.drop(1), mutableMapOf())
}

private fun routes(start: Int, rows: List<String>, cache: MutableMap<Pair<Int, Int>, Long>): Long {
    if (rows.isEmpty())
        return 1

    if (cache[start to rows.size] != null) return cache[start to rows.size]!!

    return if (rows.first()[start] == '.') {
        routes(start, rows.drop(1), cache)
    } else {
        routes(start + 1, rows.drop(1), cache) + routes(start - 1, rows.drop(1), cache)
    }.also {
        cache[start to rows.size] = it
    }
}
