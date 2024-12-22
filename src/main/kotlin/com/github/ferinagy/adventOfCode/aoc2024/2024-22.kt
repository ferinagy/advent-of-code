package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2024, "22-input")
    val test1 = readInputLines(2024, "22-test1")
    val test2 = readInputLines(2024, "22-test2")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test2).println()
    part2(input).println()
}

private fun part1(input: List<String>) = input.sumOf { get2000th(it.toUInt()).last().toULong() }


private fun part2(input: List<String>): Int {
    val maps = input.map {
        val secrets = get2000th(it.toUInt())
        val prices = secrets.map { it.toInt() % 10 }
        val diffs = prices.windowed(2).map { (a, b) -> (b % 10) - (a % 10) }

        diffs.windowed(4).foldIndexed(mutableMapOf<List<Int>, Int>()) { index, acc, ints ->
            acc.also { if (ints !in it) it[ints] = prices[index + 4] }
        }
    }

    return maps.flatMapTo(mutableSetOf()) { it.keys }.maxOf { seq ->
        maps.sumOf { it.getOrDefault(seq, 0) }
    }
}

private fun get2000th(secret: UInt): List<UInt> = generateSequence(secret) { nextSecret(it) }.take(2001).toList()

private fun nextSecret(current: UInt): UInt {
    val step1 = prune(mix(current * 64u, current))
    val step2 = prune(mix(step1 / 32u, step1))
    val step3 = prune(mix(step2 * 2048u, step2))
    return step3
}

private fun mix(a: UInt, b: UInt) = a xor b

private fun prune(a: UInt) = a % 16777216u