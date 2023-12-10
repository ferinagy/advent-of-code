package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import java.util.LinkedList

fun main() {
    val input = readInputLines(2022, "20-input")
    val testInput1 = readInputLines(2022, "20-test1")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(testInput1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Long = solve(input, 1, 1)

private fun part2(input: List<String>): Long = solve(input, 811589153, 10)

private fun solve(input: List<String>, key: Int, times: Int): Long {
    val original = input.map { it.toLong() * key }
    val list = LinkedList(List(original.size) { it })
    repeat(times) {
        repeat(original.size) {
            val x = original[it]
            val index = list.indexOf(it)
            list.removeAt(index)
            val newIndex = (x + index).mod(original.size - 1)
            list.add(newIndex, it)
        }
    }

    val final = List(original.size) { original[list[it]] }

    val zeroIndex = final.indexOf(0)
    val x1 = final[(zeroIndex + 1000).mod(final.size)]
    val x2 = final[(zeroIndex + 2000).mod(final.size)]
    val x3 = final[(zeroIndex + 3000).mod(final.size)]

    return x1 + x2 + x3
}
