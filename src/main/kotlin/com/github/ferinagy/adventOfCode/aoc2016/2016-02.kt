package com.github.ferinagy.adventOfCode.aoc2016

import com.github.ferinagy.adventOfCode.*

fun main() {
    val input = readInputLines(2016, "02-input")
    val test1 = readInputLines(2016, "02-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    var position =  Coord2D(1, 1)
    var result = 0
    input.forEach { line ->
        line.forEach {
            position = when (it) {
                'U' -> position.copy(y = (position.y - 1).coerceAtLeast(0))
                'D' -> position.copy(y = (position.y + 1).coerceAtMost(2))
                'L' -> position.copy(x = (position.x - 1).coerceAtLeast(0))
                'R' -> position.copy(x = (position.x + 1).coerceAtMost(2))
                else -> error("Unknown op: $it")
            }
        }
        result = result * 10 + keyPad1[position]
    }

    return result
}

private fun part2(input: List<String>): String {
    var position =  Coord2D(0, 2)
    var result = ""
    input.forEach { line ->
        line.forEach {
            val newPosition = when (it) {
                'U' -> position.copy(y = (position.y - 1).coerceAtLeast(0))
                'D' -> position.copy(y = (position.y + 1).coerceAtMost(4))
                'L' -> position.copy(x = (position.x - 1).coerceAtLeast(0))
                'R' -> position.copy(x = (position.x + 1).coerceAtMost(4))
                else -> error("Unknown op: $it")
            }
            if (keyPad2[newPosition] != ' ') position = newPosition
        }
        result += keyPad2[position]
    }

    return result
}

private val keyPad1 = listOf(
    listOf(1, 2, 3),
    listOf(4, 5, 6),
    listOf(7, 8, 9)
).toIntGrid()

private val keyPad2 = listOf(
    "  1  ",
    " 234 ",
    "56789",
    " ABC ",
    "  D  ",
).toCharGrid()
