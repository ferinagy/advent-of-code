package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2022, "02-input")
    val testInput1 = readInputLines(2022, "02-test1")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(testInput1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val score = mapOf("X" to 1, "Y" to 2, "Z" to 3)
    fun result(a: String, b: String) = when {
        a == "A" && b == "X" || a == "B" && b == "Y" || a == "C" && b == "Z" -> 3
        a == "A" && b == "Y" || a == "B" && b == "Z" || a == "C" && b == "X" -> 6
        else -> 0
    }

    val map = input.map { line ->
        val (a, b) = line.split(' ')
        val s = score[b]!!
        val result = result(a, b)

        s + result
    }
    return map.sum()
}

private fun part2(input: List<String>): Int {
    val score = mapOf("X" to 0, "Y" to 3, "Z" to 6)
    fun result(a: String, b: String) = when {
        a == "A" && b == "Y" || a == "B" && b == "X" || a == "C" && b == "Z" -> 1
        a == "A" && b == "Z" || a == "B" && b == "Y" || a == "C" && b == "X" -> 2
        else -> 3
    }

    val map = input.map { line ->
        val (a, b) = line.split(' ')
        val s = score[b]!!
        val result = result(a, b)

        s + result
    }
    return map.sum()
}
