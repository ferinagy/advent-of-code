package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2021, "04-input")
    val test1 = readInputText(2021, "04-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: String): Int {
    val bingo = Bingo(input)
    return bingo.firstWin()
}

private fun part2(input: String): Int {
    val bingo = Bingo(input)
    return bingo.lastWin()
}

private class Bingo(input: String) {

    private val numbers: List<Int>
    private val boards: List<Board>

    init {
        val split = input.split("\n\n")
        numbers = split.first().split(",").map { it.toInt() }
        boards = split.drop(1).map { Board(it) }
    }

    fun firstWin(): Int {
        var position = 0
        do {
            val n = numbers[position]
            boards.forEach {
                it.play(n)
            }

            position++
        } while (!boards.any { it.isWinning() })

        val sum = boards.first { it.isWinning() }.winningSum
        val winningNum = numbers[position - 1]

        return winningNum * sum
    }

    fun lastWin(): Int {
        var position = 0
        var lastBoardIndex = 0
        do {
            val n = numbers[position]
            boards.forEachIndexed { index, board ->
                if (board.isWinning()) return@forEachIndexed

                board.play(n)
                if (board.isWinning()) lastBoardIndex = index
            }

            position++
        } while (boards.any { !it.isWinning() })

        val sum = boards[lastBoardIndex].winningSum
        val winningNum = numbers[position - 1]

        return winningNum * sum
    }

    class Board(input: String) {

        data class Field(val number: Int, var checked: Boolean = false)

        val area: List<List<Field>>

        private var won = false

        val winningSum
            get() = area.sumOf { line ->
                line.filter { !it.checked }.sumOf { it.number }
            }

        fun isWinning(): Boolean {
            if (won) return true

            area.forEach {
                if (it.all { it.checked }) {
                    won = true
                    return true
                }
            }

            repeat(5) { column ->
                if (area.all { it[column].checked }) {
                    won = true
                    return true
                }
            }

            return false
        }

        fun play(n: Int) {
            area.forEach { line ->
                line.forEach {
                    if (it.number == n) it.checked = true
                }
            }
        }

        init {
            area = input.lines().map {
                it.windowed(3, 3, true).map { Field(it.trim().toInt()) }
            }
        }
    }
}
