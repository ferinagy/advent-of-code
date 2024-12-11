package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText
import kotlin.math.log10
import kotlin.math.pow

fun main() {
    val input = readInputText(2024, "11-input")
    val test1 = readInputText(2024, "11-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: String): Long {
    val nums = input.split(' ').map { it.toLong() }

    return nums.sumOf { dfs(25, it) }
}

private fun part2(input: String): Long {
    val nums = input.split(' ').map { it.toLong() }

    return nums.sumOf { dfs(75, it) }
}

private val cache = mutableMapOf<Pair<Int, Long>, Long>()

private fun dfs(depth: Int, num: Long): Long {
    cache[depth to num]?.let { return it }

    if (depth == 0) return 1

    val digits = log10(num.toFloat()).toInt() + 1
    return when {
        num == 0L -> dfs(depth - 1, 1)
        digits % 2 == 0 -> {
            val mid = 10.toFloat().pow(digits / 2).toInt()
            dfs(depth - 1, num / mid) + dfs(depth - 1, num % mid)
        }
        else -> dfs(depth - 1, num * 2024)
    }.also { cache[depth to num] = it }
}
