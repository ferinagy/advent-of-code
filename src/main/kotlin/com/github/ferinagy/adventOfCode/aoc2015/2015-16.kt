package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2015, "16-input")

    println("Part1:")
    println(part1(input))

    println()
    println("Part2:")
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    val infos = input.map { SueInfo.parse(it) }

    val tape = """
        children: 3
        cats: 7
        samoyeds: 2
        pomeranians: 3
        akitas: 0
        vizslas: 0
        goldfish: 5
        trees: 3
        cars: 2
        perfumes: 1
    """.trimIndent()
        .lines().associate { it.split(": ").let { (a, b) -> a to b.toInt() } }

    return infos.single { it.map.all { (key, value) -> tape[key] == value } }.num
}

private fun part2(input: List<String>): Int {
    val infos = input.map { SueInfo.parse(it) }

    val tape = mapOf(
        "children" to 3 .. 3,
        "cats" to 8 .. 1000,
        "samoyeds" to 2 .. 2,
        "pomeranians" to -1000 .. 2,
        "akitas" to 0 .. 0,
        "vizslas" to 0 .. 0,
        "goldfish" to -1000 .. 4,
        "trees" to 4 .. 1000,
        "cars" to 2 .. 2,
        "perfumes" to 1 .. 1,
    )

    return infos.single { it.map.all { (key, value) -> value in tape[key]!! } }.num
}

private data class SueInfo(val num: Int, val map: Map<String, Int>) {
    companion object {
        fun parse(input: String): SueInfo {
            val (num, i1, c1, i2, c2, i3, c3) = regex.matchEntire(input)!!.destructured
            val map = mapOf(
                i1 to c1.toInt(),
                i2 to c2.toInt(),
                i3 to c3.toInt(),
            )
            return SueInfo(num.toInt(), map)
        }
    }
}

private val regex = """Sue (\d+): (\w+): (\d+), (\w+): (\d+), (\w+): (\d+)""".toRegex()
