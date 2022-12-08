package com.github.ferinagy.adventOfCode.aoc2017

import java.util.LinkedList

fun main() {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2())
}

private fun part1(input: String): Int {
    val steps = input.toInt()

    val list = LinkedList<Int>()
    list += 0

    var position = 0
    for (x in 1 .. 2017) {
        position += steps
        position %= list.size
        list.add(position + 1, x)

        position++

    }

    position++
    position %= list.size

    return list[position]
}

private fun part2(): Int {
    val steps = input.toInt()

    var result = -1
    var position = 0
    for (x in 1 .. 50_000_000) {
        position += steps
        position %= x
        if (position == 0) result = x

        position++
    }

    return result
}

private const val testInput1 = """3"""

private const val input = """329"""
