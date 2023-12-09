package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2023, "09-input")
    val test1 = readInputLines(2023, "09-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Long {
    val sequences = input.map { it.split(" ").map(String::toLong) }

    return sequences.sumOf { nextNumber(it) }
}

private fun part2(input: List<String>): Long {
    val sequences = input.map { it.split(" ").map(String::toLong) }

    return sequences.sumOf { previousNumber(it) }
}

private fun nextNumber(sequence: List<Long>): Long {
    val sequences = buildStepDiffs(sequence)
    return sequences.sumOf { it.last() }
}

private fun previousNumber(sequence: List<Long>): Long {
    val sequences = buildStepDiffs(sequence)
    return sequences.foldRight(0) { list, acc -> list.first() - acc }
}

private fun buildStepDiffs(sequence: List<Long>): List<List<Long>> {
    val sequences = buildList<List<Long>> {
        this += sequence
        repeat(sequence.size - 1) {
            val last = last()
            this += List(last.size - 1) { last[it + 1] - last[it] }
        }
    }
    return sequences
}
