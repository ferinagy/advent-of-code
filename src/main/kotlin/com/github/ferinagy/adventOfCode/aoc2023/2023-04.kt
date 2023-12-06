package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import kotlin.math.pow

fun main() {
    val input = readInputLines(2023, "04-input")
    val test1 = readInputLines(2023, "04-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val cards = input.map { Card.from(it) }

    return cards.sumOf { it.worth() }
}

private fun part2(input: List<String>): Int {
    val cards = input.map { Card.from(it) }

    val counts = cards.map { 1 }.toMutableList()

    cards.forEachIndexed { index, card ->
        val matching = card.matching()
        (index + 1 .. (index + matching).coerceAtMost(cards.lastIndex)).forEach {
            counts[it] += counts[index]
        }
    }

    return counts.sum()
}

private data class Card(val id: Int, val winning: Set<String>, val all: List<String>) {
    companion object {

        private val whitespace = """\s+""".toRegex()
        private val separator = """ \|\s+""".toRegex()
        fun from(line: String): Card {
            val (card, numbers) = line.split(": ")
            val (_, id) = card.split(whitespace)

            val (winning, all) = numbers.split(separator)

            return Card(id.toInt(), winning.split(whitespace).toSet(), all.split(whitespace))
        }
    }

    fun matching() = all.count { it in winning }

    fun worth(): Int {
        val count = matching()

        if (count == 0) return 0

        return 2.0.pow(count - 1).toInt()
    }
}
