package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2022, "25-input")
    val testInput1 = readInputLines(2022, "25-test1")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()
}

private fun part1(input: List<String>): String {
    val sum = input.sumOf { snafuToDecimal(it) }

    return decimalToSnafu(sum)
}

private fun snafuToDecimal(snafu: String): Long {
    var result = 0L
    var value = 1L
    val digits = digitsMap.associate { it.second to it.first }
    for (c in snafu.reversed()) {
        val digit = digits[c]!!
        result += digit * value
        value *= 5
    }

    return result
}

private fun decimalToSnafu(number: Long): String {
    var result = ""
    var rest = number
    val digits = digitsMap.toMap()
    while (rest != 0L) {
        var mod = rest % 5
        if (2 < mod) mod -= 5

        result += digits[mod]!!
        rest = (rest - mod) / 5
    }

    return result.reversed()
}

val digitsMap = listOf(-2L to '=', -1L to '-', 0L to '0', 1L to '1', 2L to '2')
