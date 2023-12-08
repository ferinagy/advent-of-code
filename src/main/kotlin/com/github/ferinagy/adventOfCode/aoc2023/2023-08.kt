package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.lcm
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2023, "08-input")
    val test1 = readInputLines(2023, "08-test1")
    val test2 = readInputLines(2023, "08-test2")
    val test3 = readInputLines(2023, "08-test3")

    println("Part1:")
    part1(test1).println()
    part1(test2).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test3).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val instructions = input.first()
    val map = input.drop(2).associate {
        val (from, left, right) = regex.matchEntire(it)!!.destructured
        from to (left to right)
    }

    return followMap(
        start = "AAA",
        instructions = instructions,
        map = map,
        endCondition = { _, current -> current == "ZZZ" }
    ).first
}

private fun part2(input: List<String>): Long {
    val instructions = input.first()
    val map = input.drop(2).associate {
        val (from, left, right) = regex.matchEntire(it)!!.destructured
        from to (left to right)
    }


    val start = map.keys.filter { it.endsWith('A') }
    val ends = start.map {
        val result = followMap(
            start = it,
            instructions = instructions,
            map = map,
            endCondition = { _, current -> current.endsWith('Z') }
        )
        val endToEnd = followMap(
            start = result.second,
            instructions = instructions,
            map = map,
            endCondition = { steps, current -> steps != 0 && current.endsWith('Z') },
            startIndex = result.first % instructions.length
        )
        require(endToEnd.second == result.second && endToEnd.first == result.first)
        result.first
    }

    return ends.map { it.toLong() }.reduce { acc, i -> lcm(acc, i) }
}

private fun followMap(
    start: String,
    instructions: String,
    map: Map<String, Pair<String, String>>,
    endCondition: (Int, String) -> Boolean,
    startIndex: Int = 0
): Pair<Int, String> {
    var index = startIndex
    var result = 0
    var current = start
    while (!endCondition(result, current)) {
        current = if (instructions[index] == 'L') {
            map[current]!!.first
        } else {
            map[current]!!.second
        }
        index = (index + 1) % instructions.length
        result++
    }

    return result to current
}

private val regex = """(...) = \((...), (...)\)""".toRegex()
