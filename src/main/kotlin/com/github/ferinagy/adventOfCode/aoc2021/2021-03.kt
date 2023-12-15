package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2021, "03-input")
    val test1 = readInputLines(2021, "03-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val gamma = getGammaRate(input)
    val epsilon = gamma.map { if (it == '0') '1' else '0' }.joinToString(separator = "")

    return gamma.toInt(2) * epsilon.toInt(2)
}

private fun part2(input: List<String>): Int {
    val o2 = getByCriteria(input, true).toInt(2)
    val co2 = getByCriteria(input, false).toInt(2)

    return o2 * co2
}

private fun getGammaRate(input: List<String>): String {
    val report = input.map { line -> line.map { it.digitToInt() } }

    val size = report.first().size
    return buildString {
        repeat(size) { digit ->
            val count = report.count { it[digit] == 1 }
            append(if (count > report.size / 2) '1' else '0')
        }
    }
}

private fun getByCriteria(input: List<String>, mostCommon: Boolean): String {
    var report = input
    var position = 0

    while (1 < report.size) {
        val sum = report.count { it[position] == '1' }

        report = if (mostCommon && sum >= report.size / 2f || !mostCommon && sum < report.size / 2f) {
            report.filter { it[position] == '1' }
        } else {
            report.filter { it[position] == '0' }
        }
        position++
    }

    return report.first()
}
