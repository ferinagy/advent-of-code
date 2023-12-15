package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2015, "20-input").toInt()
    val test1 = readInputText(2015, "20-test1").toInt()

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: Int): Int {
    val houses = IntArray(1000000)

    for (elf in 1 .. houses.size) {
        for (house in (elf - 1) .. houses.lastIndex step elf) {
            houses[house] = houses[house] + elf * 10
        }
    }
    val index = houses.indexOfFirst { input <= it }

    return index + 1
}

private fun part2(input: Int): Int {
    val houses = IntArray(1000000)

    for (elf in 1 .. houses.size) {
        for (house in ((elf - 1) .. houses.lastIndex step elf).take(50)) {
            houses[house] = houses[house] + elf * 11
        }
    }
    val index = houses.indexOfFirst { input <= it }

    return index + 1
}
