package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText
import com.github.ferinagy.adventOfCode.transpose

fun main() {
    val input = readInputText(2022, "05-input")
    val testInput1 = readInputText(2022, "05-test1")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(testInput1).println()
    part2(input).println()
}

private fun part1(input: String): String {
    val (crates, moves) = parse(input)

    moves.forEach { move ->
        repeat(move.count) {
            crates[move.to] += crates[move.from].last()
            crates[move.from].removeLast()
        }
    }

    return crates.map { it.last() }.joinToString(separator = "")
}

private fun part2(input: String): String {
    val (crates, moves) = parse(input)

    moves.forEach { move ->
        crates[move.to] += crates[move.from].subList(crates[move.from].size - move.count, crates[move.from].size)
        repeat(move.count) {
            crates[move.from].removeLast()
        }
    }

    return crates.map { it.last() }.joinToString(separator = "")
}

private fun parse(input: String): Pair<List<MutableList<Char>>, List<Move>> {
    val (start, movesText) = input.split("\n\n")

    val crates = start.lines().dropLast(1).reversed().map { line ->
        line.mapIndexedNotNull { index, c ->
            c.takeIf { (index - 1) % 4 == 0 }
        }
    }.transpose().map { it.dropLastWhile { it == ' ' }.toMutableList() }

    val moves = movesText.lines().map {
        val (count, from, to) = regex.matchEntire(it)!!.destructured
        Move(count.toInt(), from.toInt() - 1, to.toInt() - 1)
    }
    return Pair(crates, moves)
}

private val regex = """move (\d+) from (\d+) to (\d+)""".toRegex()

private data class Move(val count: Int, val from: Int, val to: Int)
