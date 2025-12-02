package com.github.ferinagy.adventOfCode.aoc2025

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2025, "02-input")
    val test1 = readInputText(2025, "02-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: String) = input.split(',').sumOf { ranges ->
    val (low, high) = ranges.split('-')

    (low.toLong()..high.toLong()).filter { id ->
        val textId = id.toString()
        val len = textId.length
        len % 2 == 0 && textId.take(len / 2) == textId.takeLast(len / 2)
    }.sum()
}

private fun part2(input: String) = input.split(',').sumOf { ranges ->
    val (low, high) = ranges.split('-')

    (low.toLong()..high.toLong()).filter { id ->
        val textId = id.toString()
        val len = textId.length
        (1..len / 2).any { windowSize ->
            len % windowSize == 0 && textId.windowed(windowSize, windowSize).all { it == textId.take(windowSize) }
        }
    }.sum()
}
