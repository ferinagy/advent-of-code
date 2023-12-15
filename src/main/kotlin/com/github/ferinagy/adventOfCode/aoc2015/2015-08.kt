package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2015, "08-input")
    val test1 = readInputLines(2015, "08-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>) = input.sumOf { it.length - it.inMemory() }

private fun part2(input: List<String>) = input.sumOf { it.inCode() - it.length }

private fun String.inMemory(): Int {
    var i = 1
    var count = 0
    while (i < length - 1) {
        if (get(i) == '\\') {
            i++
            if (get(i) == 'x') {
                i += 2
            }
        }

        count++
        i++
    }

    return count
}

private fun String.inCode(): Int {
    var count = 0
    forEach {
        if (it == '"' || it == '\\') {
            count += 2
        } else {
            count++
        }
    }
    return count + 2
}
