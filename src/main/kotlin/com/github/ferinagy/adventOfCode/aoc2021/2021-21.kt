package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import kotlin.math.max

fun main() {
    val input = readInputLines(2021, "21-input")
    val test1 = readInputLines(2021, "21-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val players = input.map { Player(regex.matchEntire(it)!!.groupValues[1].toInt()) }.toMutableList()
    val die = DeterministicDie(100)

    var game = Game(players)

    while (true) {
        game = game.play(die.roll(3))
        if (game.wins(1000) != -1) return die.count * game.players.minOf { it.score }
    }
}

private fun part2(input: List<String>): Long {
    val players = input.map { Player(regex.matchEntire(it)!!.groupValues[1].toInt()) }
    val initialGame = Game(players)

    cache.clear()
    val results = initialGame.playDirac(21)

    return max(results.first, results.second)
}

private val cache = mutableMapOf<Game, Pair<Long, Long>>()

private fun Game.playDirac(goal: Int): Pair<Long, Long> {
    val cached = cache[this]
    if (cached != null) return cached

    var totalP1 = 0L
    var totalP2 = 0L
    possibleDiracRolls.forEach { (roll, count) ->
        val newGame = play(roll)
        val winner = newGame.wins(goal)
        if (winner != -1) {
            if (winner == 0) {
                totalP1 += count
            } else {
                totalP2 += count
            }
        } else {
            val (p1, p2) = newGame.playDirac(goal)
            totalP1 += p1 * count
            totalP2 += p2 * count
        }
    }

    return (totalP1 to totalP2).also { cache[this] = it }
}

private val possibleDiracRolls: Map<Int, Int> by lazy {
    val possible = mutableListOf<Int>()
    for (i in 1..3) {
        for (j in 1..3) {
            for (k in 1..3) {
                possible += i + j + k
            }
        }
    }
    possible.groupingBy { it }.eachCount()
}

private data class Game(val players: List<Player>, val currentPlayer: Int = 0)

private fun Game.play(roll: Int): Game {
    val newPlayer = players[currentPlayer].move(roll)
    val newPlayers = players.mapIndexed { index, player -> if (index != currentPlayer) player else newPlayer }
    val nextIndex = (currentPlayer + 1) % players.size
    return Game(newPlayers, nextIndex)
}

private fun Game.wins(goal: Int): Int = players.indexOfFirst { goal <= it.score }

private data class Player(val position: Int, val score: Int = 0)

private fun Player.move(roll: Int): Player {
    var newPosition = position
    newPosition += (roll % 10)
    if (10 < newPosition) newPosition -= 10

    return Player(newPosition, score + newPosition)
}

private class DeterministicDie(val max: Int) {
    private var current = 1

    var count: Int = 0
        private set

    fun roll(): Int {
        count++
        val result = current
        current += 1
        if (max < current) current -= max
        return result
    }

    fun roll(count: Int): Int {
        var result = 0
        repeat(count) { result += roll() }

        return result
    }
}

private val regex = """Player \d starting position: (\d+)""".toRegex()
