package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2023, "02-input")
    val test1 = readInputLines(2023, "02-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val max = mapOf("red" to 12, "green" to 13, "blue" to 14)

    val ids = input.mapNotNull { line ->
        val (id, game) = regex.matchEntire(line)!!.destructured
        val rounds = game.split("; ")
        val possible = rounds.all { round ->
            round.split(", ").all {
                val (count, color) = it.split(" ")
                count.toInt() <= max[color]!!
            }
        }

        id.takeIf { possible }
    }

    return ids.sumOf { it.toInt() }
}

private fun part2(input: List<String>): Int {
    val powers = input.map { line ->
        val (_, game) = regex.matchEntire(line)!!.destructured
        val map = mutableMapOf("red" to 0, "green" to 0, "blue" to 0)

        val rounds = game.split("; ")
        rounds.forEach { round ->
            round.split(", ").forEach {
                val (c, color) = it.split(" ")
                val count = c.toInt()
                if (map[color]!! < count) map[color] = count
            }
        }

        map.values.reduce { a, b -> a * b }
    }

    return powers.sum()
}

private val regex = """Game (\d+): (.*)""".toRegex()
