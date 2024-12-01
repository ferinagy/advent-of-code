package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import kotlin.math.abs

fun main() {
    val input = readInputLines(2024, "01-input")
    val test1 = readInputLines(2024, "01-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private val regex = """(\d+)\s+(\d+)""".toRegex()

private fun part1(input: List<String>): Long {
    val (l1, l2) = parse(input)

    val s1 = l1.sorted()
    val s2 = l2.sorted()

    return s1.foldIndexed(0L) { index, acc, next -> acc + abs(next - s2[index]) }
}

private fun part2(input: List<String>): Long {
    val (l1, l2) = parse(input)

    return l1.sumOf { id -> l2.count { id == it } * id }
}

private fun parse(text: List<String>) = text.map {
    val match = regex.matchEntire(it)!!
    match.groups[1]!!.value.toLong() to match.groups[2]!!.value.toLong()
}.unzip()
