package com.github.ferinagy.adventOfCode.aoc2025

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2025, "05-input")
    val test1 = readInputLines(2025, "05-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Long {
    val split = input.indexOf("")
    val ranges = input.take(split).map {
        val (start, end) = it.split('-')
        start.toLong() .. end.toLong()
    }

    val ingredients = input.drop(split + 1).map { it.toLong() }
    input
    return ingredients.count { ing -> ranges.any { ing in it } }.toLong()
}

private fun part2(input: List<String>): Long {
    val split = input.indexOf("")
    var ranges = input.take(split).map {
        val (start, end) = it.split('-')
        start.toLong() .. end.toLong()
    }

    while (true) {
        val newRanges = ranges.compact()

        if (newRanges == ranges) return ranges.sumOf { it.last - it.first + 1 }

        ranges = newRanges
    }
}

private fun List<LongRange>.compact(): List<LongRange> {
    val new = mutableListOf<LongRange>()

    for(i in 0 .. lastIndex) {
        var range = this[i]

        for (j in i + 1 .. lastIndex) {
            val other = this[j]

            if (range.intersects(other)) {
                range = range.join(other)
            }
        }
        if (new.none { it.first <= range.first && range.last <= it.last }) new += range
    }

    return new
}

private fun LongRange.intersects(other: LongRange) = endInclusive >= other.first && other.last >= start

private fun LongRange.join(other: LongRange) = minOf(start, other.first) .. maxOf(endInclusive, other.last)
