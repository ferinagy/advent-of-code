package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2022, "13-input")
    val testInput1 = readInputText(2022, "13-test1")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(testInput1).println()
    part2(input).println()
}

private fun part1(input: String): Int {
    val pairs = input.split("\n\n")
        .map { pair -> pair.lines().map { parse(it).first } }
        .map { (a, b) -> a to b }

    return pairs.map { compare(it.first, it.second) }
        .mapIndexed { index, comp -> if (0 < comp) index + 1 else 0 }
        .sum()
}

private fun part2(input: String): Int {
    val decoders = listOf("[[2]]", "[[6]]").map { parse(it).first }

    val packets = input.lines().filter { it.isNotBlank() }.map { parse(it).first } + decoders

    val sorted = packets.sortedWith(::compare).reversed()

    val i1 = sorted.indexOf(decoders[0]) + 1
    val i2 = sorted.indexOf(decoders[1]) + 1

    return i1 * i2
}

private sealed class Value {
    data class List(val values: kotlin.collections.List<Value>): Value() {
        override fun toString(): String {
            return values.toString()
        }
    }

    data class Number(val value: Int): Value() {
        override fun toString(): String {
            return value.toString()
        }
    }
}

private fun compare(left: Value, right: Value): Int {
    when {
        left is Value.Number && right is Value.Number -> return right.value - left.value
        left is Value.List && right is Value.List -> {
            var index = 0
            while (index < left.values.size && index < right.values.size) {
                val next = compare(left.values[index], right.values[index])
                if (next != 0) return next

                index++
            }
            return right.values.size - left.values.size
        }
        left is Value.Number && right is Value.List -> return compare(Value.List(listOf(left)), right)
        left is Value.List && right is Value.Number -> return compare(left, Value.List(listOf(right)))
        else -> error("impossibiru")
    }
}

private fun parse(text: String, position: Int = 0): Pair<Value, Int> {
    return if (text[position] == '[') {
        val list = mutableListOf<Value>()
        var nextPos = position + 1
        while (text[nextPos] != ']') {
            val next = parse(text, nextPos)
            list += next.first
            nextPos = next.second
            if (text[nextPos] == ',') nextPos++
        }
        Value.List(list) to nextPos + 1
    } else {
        var nextPos = position + 1
        while (text[nextPos].isDigit()) nextPos++

        Value.Number(text.substring(position, nextPos).toInt()) to nextPos
    }
}
