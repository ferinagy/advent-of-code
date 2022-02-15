package com.github.ferinagy.adventOfCode.aoc2015

fun main() {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput1))
    println(part2(input))
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

private const val testInput1 = """111221"""

private const val input = """1113222113"""
