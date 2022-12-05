package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.transpose

fun main() {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput1))
    println(part2(input))
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

private const val testInput1 = """    [D]    
[N] [C]    
[Z] [M] [P]
 1   2   3 

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2"""

private const val input = """    [C]         [Q]         [V]    
    [D]         [D] [S]     [M] [Z]
    [G]     [P] [W] [M]     [C] [G]
    [F]     [Z] [C] [D] [P] [S] [W]
[P] [L]     [C] [V] [W] [W] [H] [L]
[G] [B] [V] [R] [L] [N] [G] [P] [F]
[R] [T] [S] [S] [S] [T] [D] [L] [P]
[N] [J] [M] [L] [P] [C] [H] [Z] [R]
 1   2   3   4   5   6   7   8   9 

move 2 from 4 to 6
move 4 from 5 to 3
move 6 from 6 to 1
move 4 from 1 to 4
move 4 from 9 to 4
move 7 from 2 to 4
move 1 from 9 to 3
move 1 from 2 to 6
move 2 from 9 to 5
move 2 from 6 to 8
move 5 from 8 to 1
move 2 from 6 to 9
move 5 from 8 to 3
move 1 from 5 to 4
move 3 from 7 to 2
move 10 from 4 to 7
move 7 from 4 to 3
move 1 from 4 to 7
move 1 from 7 to 9
move 1 from 2 to 3
move 11 from 1 to 7
move 12 from 3 to 7
move 8 from 3 to 8
move 29 from 7 to 2
move 3 from 7 to 3
move 3 from 9 to 2
move 4 from 5 to 3
move 7 from 3 to 5
move 28 from 2 to 3
move 1 from 7 to 5
move 2 from 8 to 5
move 2 from 4 to 1
move 2 from 1 to 4
move 1 from 7 to 6
move 1 from 7 to 1
move 3 from 2 to 8
move 1 from 1 to 7
move 9 from 5 to 3
move 12 from 3 to 1
move 1 from 4 to 3
move 1 from 6 to 4
move 3 from 2 to 9
move 16 from 3 to 7
move 2 from 9 to 6
move 5 from 7 to 2
move 1 from 9 to 7
move 1 from 4 to 2
move 13 from 7 to 2
move 13 from 2 to 7
move 12 from 7 to 8
move 2 from 6 to 4
move 16 from 8 to 1
move 4 from 3 to 1
move 3 from 3 to 2
move 1 from 5 to 7
move 1 from 5 to 3
move 3 from 4 to 6
move 19 from 1 to 3
move 5 from 8 to 4
move 6 from 3 to 2
move 5 from 4 to 2
move 1 from 7 to 4
move 1 from 4 to 9
move 3 from 6 to 7
move 1 from 9 to 2
move 16 from 2 to 4
move 9 from 1 to 8
move 10 from 4 to 2
move 2 from 7 to 5
move 5 from 8 to 4
move 12 from 2 to 9
move 2 from 7 to 4
move 12 from 9 to 5
move 11 from 5 to 6
move 3 from 1 to 9
move 1 from 5 to 7
move 2 from 9 to 2
move 10 from 3 to 2
move 1 from 9 to 2
move 2 from 8 to 9
move 1 from 7 to 8
move 1 from 8 to 4
move 7 from 2 to 6
move 1 from 1 to 5
move 5 from 3 to 1
move 1 from 5 to 1
move 2 from 3 to 9
move 2 from 1 to 6
move 3 from 9 to 8
move 14 from 6 to 1
move 1 from 3 to 5
move 5 from 4 to 6
move 1 from 9 to 6
move 7 from 6 to 9
move 1 from 6 to 2
move 8 from 1 to 4
move 7 from 1 to 7
move 10 from 2 to 1
move 4 from 7 to 6
move 10 from 4 to 6
move 5 from 8 to 2
move 1 from 5 to 9
move 2 from 2 to 6
move 2 from 4 to 7
move 1 from 2 to 7
move 5 from 9 to 2
move 1 from 2 to 9
move 14 from 6 to 8
move 2 from 8 to 4
move 1 from 2 to 6
move 4 from 9 to 3
move 2 from 6 to 8
move 5 from 4 to 5
move 5 from 8 to 3
move 1 from 2 to 4
move 3 from 7 to 1
move 2 from 2 to 7
move 1 from 4 to 7
move 1 from 4 to 5
move 1 from 2 to 8
move 1 from 4 to 9
move 8 from 8 to 2
move 3 from 1 to 5
move 7 from 2 to 9
move 8 from 1 to 6
move 6 from 7 to 2
move 2 from 2 to 8
move 5 from 1 to 8
move 3 from 6 to 8
move 4 from 3 to 6
move 3 from 6 to 2
move 8 from 9 to 2
move 11 from 5 to 7
move 12 from 2 to 6
move 2 from 3 to 7
move 12 from 7 to 2
move 10 from 6 to 9
move 1 from 7 to 1
move 12 from 8 to 7
move 2 from 3 to 2
move 8 from 9 to 7
move 6 from 2 to 5
move 1 from 1 to 6
move 3 from 2 to 6
move 1 from 3 to 7
move 5 from 5 to 3
move 10 from 7 to 2
move 2 from 3 to 7
move 8 from 7 to 6
move 20 from 2 to 8
move 5 from 8 to 1
move 5 from 8 to 6
move 1 from 5 to 7
move 1 from 1 to 4
move 4 from 1 to 2
move 1 from 9 to 6
move 3 from 3 to 1
move 4 from 7 to 5
move 1 from 9 to 8
move 11 from 8 to 7
move 1 from 4 to 9
move 2 from 7 to 5
move 31 from 6 to 9
move 4 from 2 to 3
move 6 from 5 to 1
move 4 from 1 to 2
move 7 from 7 to 8
move 1 from 7 to 6
move 1 from 1 to 7
move 24 from 9 to 4
move 2 from 7 to 8
move 2 from 9 to 2
move 2 from 7 to 5
move 2 from 5 to 9
move 3 from 4 to 1
move 20 from 4 to 2
move 1 from 6 to 1
move 16 from 2 to 1
move 4 from 3 to 1
move 1 from 4 to 8
move 5 from 8 to 5
move 5 from 8 to 1
move 1 from 5 to 2
move 3 from 5 to 6
move 33 from 1 to 6
move 6 from 9 to 4
move 15 from 6 to 7
move 6 from 4 to 3
move 1 from 5 to 3
move 7 from 3 to 9
move 11 from 7 to 5
move 10 from 5 to 8
move 2 from 7 to 3
move 5 from 8 to 9
move 1 from 7 to 5
move 1 from 5 to 8
move 1 from 5 to 7
move 2 from 3 to 8
move 2 from 7 to 5
move 2 from 8 to 7
move 1 from 5 to 9
move 1 from 7 to 6
move 3 from 8 to 6
move 22 from 6 to 9
move 1 from 7 to 6
move 27 from 9 to 4
move 18 from 4 to 8
move 5 from 4 to 1
move 1 from 5 to 1
move 3 from 6 to 3
move 2 from 3 to 5
move 2 from 5 to 2
move 1 from 2 to 6
move 1 from 6 to 3
move 9 from 8 to 6
move 3 from 9 to 8
move 9 from 6 to 5
move 1 from 6 to 9
move 15 from 8 to 5
move 1 from 3 to 4
move 6 from 1 to 8
move 1 from 3 to 7
move 8 from 5 to 8
move 2 from 5 to 6
move 3 from 4 to 6
move 1 from 7 to 6
move 2 from 5 to 3
move 5 from 5 to 1
move 2 from 3 to 7
move 1 from 8 to 1
move 10 from 2 to 9
move 5 from 6 to 3
move 7 from 8 to 5
move 4 from 3 to 5
move 1 from 2 to 1
move 2 from 7 to 6
move 5 from 1 to 5
move 1 from 3 to 7
move 1 from 7 to 6
move 3 from 8 to 5
move 4 from 6 to 4
move 1 from 2 to 9
move 5 from 4 to 6
move 21 from 5 to 3
move 2 from 8 to 4
move 3 from 4 to 1
move 1 from 8 to 4
move 18 from 3 to 5
move 2 from 3 to 6
move 2 from 6 to 9
move 2 from 6 to 2
move 1 from 2 to 9
move 19 from 9 to 4
move 3 from 6 to 3
move 2 from 9 to 4
move 1 from 1 to 2
move 1 from 3 to 7
move 16 from 5 to 2
move 4 from 1 to 9
move 3 from 3 to 4
move 4 from 9 to 8
move 3 from 5 to 1
move 22 from 4 to 5
move 1 from 7 to 2
move 22 from 5 to 9
move 2 from 5 to 2
move 2 from 4 to 6
move 10 from 9 to 5
move 1 from 8 to 3
move 13 from 9 to 2
move 1 from 6 to 3
move 19 from 2 to 7
move 2 from 7 to 4
move 1 from 8 to 4
move 1 from 8 to 2
move 11 from 5 to 7
move 3 from 1 to 7
move 8 from 7 to 8
move 1 from 3 to 5
move 1 from 8 to 3
move 1 from 5 to 3
move 6 from 2 to 3
move 1 from 8 to 7
move 1 from 6 to 1
move 1 from 1 to 8
move 4 from 8 to 1
move 1 from 4 to 6
move 8 from 3 to 9
move 2 from 2 to 3
move 3 from 8 to 5
move 1 from 8 to 2
move 4 from 2 to 7
move 5 from 9 to 7
move 1 from 6 to 3
move 4 from 2 to 4
move 23 from 7 to 5
move 4 from 1 to 2
move 3 from 9 to 6
move 2 from 4 to 8
move 2 from 8 to 3
move 2 from 6 to 1
move 1 from 6 to 8
move 8 from 5 to 3
move 5 from 2 to 6
move 5 from 6 to 3
move 1 from 8 to 3
move 4 from 4 to 7
move 15 from 5 to 2
move 1 from 1 to 9
move 2 from 5 to 1
move 4 from 3 to 7
move 1 from 4 to 9
move 4 from 7 to 1
move 2 from 5 to 6
move 7 from 1 to 2
move 6 from 2 to 3
move 16 from 2 to 5
move 1 from 6 to 3
move 1 from 6 to 3
move 9 from 7 to 4
move 6 from 4 to 6
move 1 from 9 to 8
move 23 from 3 to 9
move 1 from 3 to 4
move 3 from 4 to 5
move 9 from 5 to 2
move 6 from 9 to 7
move 7 from 7 to 5
move 5 from 5 to 3
move 1 from 4 to 6
move 3 from 3 to 8
move 6 from 2 to 1
move 3 from 5 to 6
move 4 from 7 to 1
move 2 from 3 to 9
move 5 from 6 to 8
move 19 from 9 to 6
move 1 from 9 to 2
move 9 from 5 to 9
move 4 from 8 to 3
move 5 from 6 to 1
move 4 from 6 to 1
move 2 from 3 to 8
move 17 from 1 to 7
move 2 from 1 to 2
move 6 from 6 to 9
move 4 from 8 to 5
move 3 from 8 to 2
move 3 from 5 to 6
move 4 from 6 to 8
move 2 from 6 to 9
move 4 from 8 to 7
move 9 from 9 to 5
move 5 from 9 to 4
move 7 from 2 to 8
move 1 from 2 to 1
move 3 from 6 to 5
move 6 from 8 to 5
move 1 from 3 to 4
move 1 from 3 to 1
move 12 from 7 to 2
move 5 from 2 to 7
move 8 from 7 to 5
move 1 from 9 to 3
move 5 from 2 to 8
move 3 from 6 to 3
move 2 from 2 to 3
move 1 from 2 to 4
move 2 from 3 to 4
move 1 from 1 to 6
move 14 from 5 to 6
move 1 from 8 to 6
move 3 from 3 to 7
move 4 from 7 to 1
move 9 from 4 to 3
move 3 from 1 to 4
move 1 from 1 to 2
move 1 from 8 to 4
move 8 from 3 to 1
move 1 from 3 to 2
move 5 from 7 to 6
move 3 from 1 to 6
move 2 from 2 to 8
move 13 from 5 to 3
move 5 from 1 to 3
move 3 from 4 to 5
move 1 from 9 to 2
move 4 from 3 to 9
move 1 from 1 to 7
move 2 from 5 to 8
move 1 from 7 to 5
move 2 from 5 to 4
move 1 from 2 to 6
move 1 from 4 to 5
move 7 from 3 to 6
move 31 from 6 to 1
move 25 from 1 to 7
move 2 from 3 to 2
move 13 from 7 to 9
move 1 from 1 to 6
move 1 from 4 to 1
move 2 from 2 to 9
move 1 from 4 to 6
move 3 from 7 to 1
move 7 from 8 to 3
move 1 from 8 to 2
move 1 from 2 to 8
move 4 from 3 to 4
move 1 from 8 to 7
move 3 from 6 to 9
move 5 from 7 to 6
move 1 from 4 to 7
move 5 from 7 to 9
move 5 from 3 to 6
move 3 from 4 to 7
move 1 from 5 to 4
move 4 from 7 to 9
move 32 from 9 to 1
move 1 from 6 to 5
move 1 from 5 to 9
move 4 from 3 to 8
move 5 from 1 to 4
move 4 from 4 to 9
move 6 from 1 to 7
move 4 from 9 to 8
move 4 from 7 to 8
move 1 from 7 to 1
move 1 from 7 to 6
move 7 from 6 to 3
move 1 from 9 to 5
move 2 from 4 to 7
move 25 from 1 to 6
move 1 from 7 to 1
move 1 from 3 to 4
move 18 from 6 to 8
move 1 from 5 to 1
move 3 from 1 to 6
move 21 from 8 to 3
move 1 from 8 to 4
move 2 from 4 to 2
move 1 from 8 to 1
move 1 from 7 to 6
move 5 from 6 to 3
move 30 from 3 to 1
move 4 from 8 to 6
move 1 from 2 to 9
move 1 from 8 to 5
move 9 from 6 to 5
move 2 from 8 to 7
move 3 from 5 to 9
move 2 from 3 to 4
move 1 from 2 to 1
move 1 from 5 to 8
move 1 from 8 to 3
move 2 from 4 to 6
move 1 from 3 to 1
move 1 from 5 to 6
move 5 from 5 to 7
move 4 from 6 to 8
move 3 from 8 to 2
move 1 from 1 to 5
move 1 from 8 to 7
move 4 from 9 to 6
move 1 from 5 to 1
move 4 from 6 to 8
move 6 from 7 to 3
move 4 from 3 to 9
move 2 from 2 to 7
move 1 from 3 to 5
move 3 from 7 to 6
move 2 from 9 to 8
move 1 from 2 to 4
move 1 from 3 to 4
move 5 from 8 to 4
move 1 from 9 to 2
move 1 from 7 to 5
move 3 from 4 to 5
move 1 from 9 to 1
move 1 from 2 to 7
move 1 from 7 to 1
move 5 from 5 to 4
move 4 from 1 to 4
move 19 from 1 to 9
move 6 from 6 to 2
move 12 from 9 to 1
move 1 from 8 to 6
move 1 from 9 to 4
move 4 from 4 to 8
move 1 from 6 to 5
move 1 from 5 to 3
move 2 from 8 to 9
move 5 from 4 to 6
move 5 from 9 to 4
move 1 from 4 to 3
move 2 from 2 to 9
move 1 from 6 to 5
move 1 from 6 to 9
move 7 from 1 to 5
move 1 from 3 to 1
move 2 from 8 to 3
move 1 from 5 to 7
move 2 from 9 to 8"""
