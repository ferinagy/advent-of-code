package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2024, "25-input")
    val test1 = readInputText(2024, "25-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

}

private fun part1(input: String): Int {
    val (keyGrids, lockGrids) = input.split("\n\n")
        .map { it.lines() }
        .partition { it[0].all { it == '.' } }

    val keys = keyGrids.map { it.columns() }
    val locks = lockGrids.map { it.columns() }

    return keys.sumOf { key ->
        locks.count { lock ->
            key.zip(lock).all { (k, l) -> k + l <= 5 }
        }
    }
}

private fun List<String>.columns():List<Int> = List(5) { index -> count { line -> line[index] == '#' } - 1 }
