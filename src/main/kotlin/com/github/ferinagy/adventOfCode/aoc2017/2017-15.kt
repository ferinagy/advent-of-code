package com.github.ferinagy.adventOfCode.aoc2017

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
    val (startA, startB) = input.split(',')
    var a = startA.toLong()
    var b = startB.toLong()

    var result = 0
    repeat(40_000_000) {
        a *= 16807
        a %= 2147483647

        b *= 48271
        b %= 2147483647

        if (a.lowest16() == b.lowest16()) result++
    }

    return result
}

private fun part2(input: String): Int {
    val (startA, startB) = input.split(',')
    var a = startA.toLong()
    var b = startB.toLong()

    var result = 0
    repeat(5_000_000) {
        do {
            a *= 16807
            a %= 2147483647
        } while (a % 4 != 0L)

        do {
            b *= 48271
            b %= 2147483647
        } while (b % 8 != 0L)

        if (a.lowest16() == b.lowest16()) result++
    }

    return result
}

private fun Long.lowest16() = this % 65536

private const val testInput1 = """65,8921"""

private const val input = """289,629"""
