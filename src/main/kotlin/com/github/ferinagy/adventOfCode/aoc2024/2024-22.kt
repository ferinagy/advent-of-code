package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import kotlin.time.measureTime

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
    measureTime {
        part2(input).println()
    }.println()
}

private fun part1(input: List<String>) = input.sumOf { get2000th(it.toUInt()).last().toULong() }


private fun part2(input: List<String>): Int {
    val cache =  mutableMapOf<List<Int>, Int>()
    input.forEach {
        val secrets = get2000th(it.toUInt())
        val prices = secrets.map { it.toInt() % 10 }
        val diffs = prices.windowed(2).map { (a, b) -> b - a }

        val seen = mutableSetOf<List<Int>>()
        diffs.windowed(4).forEachIndexed { index, ints ->
            if (ints !in seen) {
                seen += ints
                cache[ints] = cache.getOrDefault(ints, 0) + prices[index + 4]
            }
        }
    }
    return cache.maxOf { it.value }
}

private fun get2000th(secret: UInt): List<UInt> = generateSequence(secret) { nextSecret(it) }.take(2001).toList()

private fun nextSecret(current: UInt): UInt {
    val step1 = prune(mix(current shl 6, current))
    val step2 = prune(mix(step1 shr 5, step1))
    val step3 = prune(mix(step2 shl 11, step2))
    return step3
}

private fun mix(a: UInt, b: UInt) = a xor b

private fun prune(a: UInt) = a % 16777216u