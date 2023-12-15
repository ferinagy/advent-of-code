package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2021, "06-input")
    val test1 = readInputText(2021, "06-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: String): Long {
    return simulateLanternFish(input, 80)
}

private fun part2(input: String): Long {
    return simulateLanternFish(input, 256)
}

private fun simulateLanternFish(input: String, generations: Int): Long {
    val array = Array(9) { 0L }
    input.split(",")
        .map { it.toInt() }
        .forEach { array[it]++ }

    repeat(generations) {
        val born = array[0]
        for (i in 1 .. array.lastIndex) {
            array[i-1] = array[i]
        }
        array[array.lastIndex] = 0
        array[6] += born
        array[8] += born
    }

    return array.sum()
}
