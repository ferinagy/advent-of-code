package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2015, "01-input")

    println("Part1:")
    calculateFloor(input).println()

    println()
    println("Part2:")
    calculateBasement(input).println()
}

private fun calculateFloor(input: String): Int {
    val map = mapOf('(' to 1, ')' to -1)
    return input.sumOf { map[it]!! }
}

private fun calculateBasement(input: String): Int {
    var positon = 0
    var floor = 0
    val map = mapOf('(' to 1, ')' to -1)
    while (0 <= floor) {
        floor += map[input[positon]]!!
        positon++
    }

    return positon
}
