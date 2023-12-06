package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2023, "01-input")
    val test1 = readInputLines(2023, "01-test1")
    val test2 = readInputLines(2023, "01-test2")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test2).println()
    part2(input).println()
}

private fun part1(input: List<String>) = input.sumOf { line ->
    val d1 = line.first { it.isDigit() }
    val d2 = line.last { it.isDigit() }
    "$d1$d2".toInt()
}

private fun part2(input: List<String>): Int {
    val regex = (names + digits).joinToString(separator = "|").toRegex()
    return input.sumOf { line ->
        val result = regex.findAllOverlapping(line)
        val d1 = result.first()
        val d2 = result.last()
        "${d1.toChar()}${d2.toChar()}".toInt()
    }
}

private fun String.toChar(): Char {
    if (length == 1) return first()

    return (names.indexOf(this) + 1).digitToChar()
}

private fun Regex.findAllOverlapping(text: String): List<String> = buildList {
    var index = 0
    while (index < text.length) {
        val result = find(text, startIndex = index) ?: break
        this += result.value
        index = result.range.first + 1
    }
}

private val names = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
private val digits = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9")
