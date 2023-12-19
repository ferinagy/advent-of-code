package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readInputLines(2023, "19-input")
    val test1 = readInputLines(2023, "19-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val rules = input.takeWhile { it.isNotBlank() }.map(Rule::parse).associateBy { it.name }

    val accepted = acceptedRanges(rules)

    val parts = input.drop(rules.size + 1).map(Part::parse)
    return parts.filter { part -> accepted.any { ranges -> part.rates.all { it.value in ranges[it.key]!! } } }.sumOf { it.rating }
}

private fun part2(input: List<String>): Long {
    val rules = input.takeWhile { it.isNotBlank() }.map(Rule::parse).associateBy { it.name }

    val accepted = acceptedRanges(rules)
    return accepted.sumOf { it.values.fold(1L) { acc, range -> acc * (range.last - range.first + 1) } }
}

private fun acceptedRanges(
    rules: Map<String, Rule>,
    current: String = "in",
    ranges: Map<Char, LongRange> = mapOf('x' to 1..4000L, 'm' to 1..4000L, 'a' to 1..4000L, 's' to 1..4000L)
): List<Map<Char, LongRange>> {
    if (current == "A") return listOf(ranges)
    if (current == "R") return emptyList()

    return buildList {
        val rule = rules[current]!!
        var newRanges = ranges
        rule.conditions.forEach {
            val range = newRanges[it.name]!!
            this += acceptedRanges(rules, it.output, newRanges + (it.name to (it.range intersect range)))
            newRanges = newRanges + (it.name to (it.oppositeRange intersect range))
        }
        this += acceptedRanges(rules, rule.fallback, newRanges)
    }
}

private infix fun LongRange.intersect(other: LongRange): LongRange = max(first, other.first)..min(last, other.last)

private data class Part(val rates: Map<Char, Int>) {

    val rating: Int = rates.values.sum()

    companion object {
        fun parse(text: String): Part {
            val map = text.substring(1, text.length - 1).split(',')
                .associate { it.split('=').let { (name, value) -> name.single() to value.toInt() } }
            return Part(map)
        }
    }
}

private class Rule(val name: String, val conditions: List<Condition>, val fallback: String) {
    companion object {
        val regex = """(\w+)\{(.*)}""".toRegex()

        fun parse(text: String): Rule {
            val (name, c) = regex.matchEntire(text)!!.destructured
            val list = c.split(',')

            return Rule(name, list.dropLast(1).map { Condition.parse(it) }, list.last())
        }
    }
}

private data class Condition(val name: Char, val range: LongRange, val output: String) {

    val oppositeRange = if (range.first == 1L) range.last + 1..4000 else 1..<range.first

    companion object {
        val regex = """([xmas])([<>])(\d+):(\w+)""".toRegex()

        fun parse(text: String): Condition {
            val (name, op, value, output) = regex.matchEntire(text)!!.destructured

            val range = if (op == "<") 1..<value.toLong() else value.toLong() + 1..4000L
            return Condition(name.single(), range, output)
        }
    }
}
