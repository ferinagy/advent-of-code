package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2021, "02-input")
    val test1 = readInputLines(2021, "02-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    var dist = 0
    var depth = 0

    val regex = """(\w+) (\d+)""".toRegex()
    input.forEach {
        val (command, size) = regex.matchEntire(it)!!.destructured
        when (command) {
            "forward" -> { dist += size.toInt() }
            "down" -> { depth += size.toInt() }
            "up" -> { depth -= size.toInt() }
        }
    }

    return dist * depth
}

private fun part2(input: List<String>): Int {
    var dist = 0
    var depth = 0
    var aim = 0

    val regex = """(\w+) (\d+)""".toRegex()
    input.forEach {
        val (command, size) = regex.matchEntire(it)!!.destructured
        when (command) {
            "forward" -> {
                dist += size.toInt()
                depth += aim * size.toInt()
            }
            "down" -> { aim += size.toInt() }
            "up" -> { aim -= size.toInt() }
        }
    }

    return dist * depth
}
