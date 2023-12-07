package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2023, "07-input")
    val test1 = readInputLines(2023, "07-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val hands = input.map { line ->
        line.split(" ").let { Hand(it[0], it[1].toInt()) }
    }

    return hands.sorted().withIndex().sumOf { (index, hand) -> (index + 1) * hand.bid }
}

private fun part2(input: List<String>): Int {
    val hands = input.map { line ->
        line.split(" ").let { HandWithJoker(it[0], it[1].toInt()) }
    }

    return hands.sorted().withIndex().sumOf { (index, hand) -> (index + 1) * hand.bid }
}

private enum class HandType {
    Five, Four, FullHouse, Three, TwoPair, Pair, HighCard
}

private data class Hand(val cards: String, val bid: Int): Comparable<Hand> {

    companion object {
        val labels = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
    }

    private val type: HandType

    init {
        val counts = cards.groupingBy { it }.eachCount().entries.sortedByDescending { it.value }
        type =  when {
            counts.size == 1 -> HandType.Five
            counts[0].value == 4 -> HandType.Four
            counts[0].value == 3 && counts[1].value == 2 -> HandType.FullHouse
            counts[0].value == 3 -> HandType.Three
            counts[0].value == 2 && counts[1].value == 2 -> HandType.TwoPair
            counts[0].value == 2 -> HandType.Pair
            else -> HandType.HighCard
        }
    }

    override fun compareTo(other: Hand): Int {
        if (type != other.type) return other.type.ordinal - type.ordinal

        cards.forEachIndexed { index, c ->
            if (c != other.cards[index]) return labels.indexOf(other.cards[index]) - labels.indexOf(c)
        }
        return 0
    }

}

private data class HandWithJoker(val cards: String, val bid: Int): Comparable<HandWithJoker> {

    companion object {
        val labels = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')
    }

    private val type: HandType

    init {
        val jokers = cards.count { it == 'J' }
        val counts = cards.groupingBy { it }.eachCount().let { it - 'J' }.entries.sortedByDescending { it.value }

        type = when {
            jokers == 5 || counts[0].value + jokers == 5 -> HandType.Five
            counts[0].value + jokers == 4 -> HandType.Four
            counts[0].value == 3 && counts[1].value == 2 -> HandType.FullHouse
            counts[0].value == 2 && counts[1].value == 2 && jokers == 1 -> HandType.FullHouse
            counts[0].value + jokers == 3 -> HandType.Three
            counts[0].value == 2 && counts[1].value == 2 || counts[0].value == 2 && jokers == 1 -> HandType.TwoPair
            counts[0].value + jokers == 2 -> HandType.Pair
            else -> HandType.HighCard
        }
    }

    override fun compareTo(other: HandWithJoker): Int {
        if (type != other.type) return other.type.ordinal - type.ordinal

        cards.forEachIndexed { index, c ->
            if (c != other.cards[index]) return labels.indexOf(other.cards[index]) - labels.indexOf(c)
        }
        return 0
    }

}
