package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2021, "10-input")
    val test1 = readInputLines(2021, "10-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    return input.map { it.status() }.filterIsInstance<Status.Corrupted>().sumOf { it.score }
}

private fun part2(input: List<String>): Long {
    val sorted = input.map { it.status() }.filterIsInstance<Status.Incomplete>().map { it.score }.sorted()

    return sorted[sorted.size / 2]
}

private sealed class Status {
    class Corrupted(val score: Int) : Status()
    class Incomplete(val score: Long) : Status()
}

private fun String.status(): Status {
    val corruptedTable = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
    val incompleteTable = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)

    val stack = ArrayDeque<Char>()
    var position = 0
    while (position < length) {
        when (val current = get(position)) {
            '(' -> {
                stack.addFirst(')')
            }
            '[' -> {
                stack.addFirst(']')
            }
            '<' -> {
                stack.addFirst('>')
            }
            '{' -> {
                stack.addFirst('}')
            }
            '>', ')', ']', '}' -> {
                val first = stack.removeFirst()
                if (first != current) return Status.Corrupted(corruptedTable[current]!!)
            }
            else -> error("Unexpected char $current")
        }

        position++
    }

    if (stack.isEmpty()) error("Complete line")

    val score = stack.fold(0L) { acc, char -> acc * 5 + incompleteTable[char]!! }
    return Status.Incomplete(score)
}
