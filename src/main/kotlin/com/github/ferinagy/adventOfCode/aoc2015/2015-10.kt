package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2015, "10-input")
    val test1 = readInputText(2015, "10-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: String): Int {
    var x = input
    repeat(40) {
        x = lookAndSay(x)
    }

    return x.length
}

private fun part2(input: String): Int {
    var x = input
    repeat(50) {
        x = lookAndSay(x)
    }

    return x.length
}

private fun lookAndSay(input: String): String {
    var runStart = 0
    var runEnd = 0

    return buildString {
        while (runStart < input.length) {
            while (runEnd < input.length && input[runStart] == input[runEnd]) runEnd++

            append((runEnd - runStart))
            append(input[runStart])

            runStart = runEnd
        }
    }
}
