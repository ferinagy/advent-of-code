package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2021, "08-input")
    val test1 = readInputLines(2021, "08-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val entries = input.map { Entry.parse(it) }

    val lengths = setOf(2, 4, 3, 7)

    return entries.sumOf { it.value.count { it.size in lengths } }
}

private fun part2(input: List<String>): Int {
    val entries = input.map { Entry.parse(it) }

    return entries.sumOf { it.decode() }
}

private fun Entry.decode(): Int {
    val one = patterns.first { it.size == 2 }
    val four = patterns.first { it.size == 4 }
    val seven = patterns.first { it.size == 3 }
    val eight = patterns.first { it.size == 7 }

    val top = (seven - one).single()
    val bottom = patterns.eliminate(6, four + seven)
    val middle = patterns.eliminate(5, seven + bottom)
    val topLeft = (four - one - middle).single()
    val bottomRight = patterns.eliminate(5, setOf(top, topLeft, middle, bottom))
    val topRigth = (one - bottomRight).single()
    val bottomLeft = (eight - top - bottom - middle - bottomRight - topLeft - topRigth).single()

    val mapping = mapOf(
        top to 'a',
        topLeft to 'b',
        topRigth to 'c',
        middle to 'd',
        bottomLeft to 'e',
        bottomRight to 'f',
        bottom to 'g'
    )

    fun Set<Char>.toDigit(): Int = segmentsMap[this.map { mapping[it] }.toSet()]!!

    val digits = value.map { it.toDigit() }

    return digits.fold(0) { acc, item -> 10 * acc + item }
}

private fun List<Set<Char>>.eliminate(size: Int, set: Set<Char>) = filter { it.size == size }
    .single { it.containsAll(set) }
    .let { it - set }
    .single()

private data class Entry(val patterns: List<Set<Char>>, val value: List<Set<Char>>) {
    companion object {
        fun parse(input: String): Entry {
            val (patterns, value) = input.split(" | ")

            fun String.toSets() = split(" ").map { it.toSet() }

            return Entry(patterns.toSets(), value.toSets())
        }
    }
}

private val segmentsMap = mapOf(
    "abcefg".toSet() to 0,
    "cf".toSet() to 1,
    "acdeg".toSet() to 2,
    "acdfg".toSet() to 3,
    "bcdf".toSet() to 4,
    "abdfg".toSet() to 5,
    "abdefg".toSet() to 6,
    "acf".toSet() to 7,
    "abcdefg".toSet() to 8,
    "abcdfg".toSet() to 9
)

/*
  0:      1:      2:      3:      4:
 aaaa    ....    aaaa    aaaa    ....
b    c  .    c  .    c  .    c  b    c
b    c  .    c  .    c  .    c  b    c
 ....    ....    dddd    dddd    dddd
e    f  .    f  e    .  .    f  .    f
e    f  .    f  e    .  .    f  .    f
 gggg    ....    gggg    gggg    ....

  5:      6:      7:      8:      9:
 aaaa    aaaa    aaaa    aaaa    aaaa
b    .  b    .  .    c  b    c  b    c
b    .  b    .  .    c  b    c  b    c
 dddd    dddd    ....    dddd    dddd
.    f  e    f  .    f  e    f  .    f
.    f  e    f  .    f  e    f  .    f
 gggg    gggg    ....    gggg    gggg
 */
