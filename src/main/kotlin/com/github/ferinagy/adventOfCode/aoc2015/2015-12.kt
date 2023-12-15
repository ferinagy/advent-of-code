package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2015, "12-input")
    val test1 = readInputText(2015, "12-test1")
    val test2 = readInputText(2015, "12-test2")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test2).println()
    part2(input).println()
}

private fun part1(input: String): Int {
    val (json, rest) = parseJson(input)
    assert(rest == input.length)

    return json.sum()
}

private fun part2(input: String): Int {
    val (json, rest) = parseJson(input)
    assert(rest == input.length)

    return json.sumWithoutRed()
}

private sealed class Json {
    abstract fun sum(): Int
    abstract fun sumWithoutRed(): Int

    class Array(val array: List<Json>) : Json() {
        override fun sum() = array.sumOf { it.sum() }
        override fun sumWithoutRed() = array.sumOf { it.sumWithoutRed() }
    }

    class Object(val map: Map<String, Json>) : Json() {
        override fun sum() = map.values.sumOf { it.sum() }
        override fun sumWithoutRed(): Int {
            val hasRed = map.values.any { it is String && it.value == "red" }

            return if (hasRed) 0 else  map.values.sumOf { it.sumWithoutRed() }
        }
    }

    class Number(val value: Int) : Json() {
        override fun sum() = value
        override fun sumWithoutRed() = value
    }

    class String(val value: kotlin.String) : Json() {
        override fun sum() = 0
        override fun sumWithoutRed() = 0
    }
}

private fun parseJson(input: String, from: Int = 0): Pair<Json, Int> {
    val numRegex = """-?\d+""".toRegex()
    var position = from
    when (input[position]) {
        '"' -> {
            val endIndex = input.indexOf('"', position + 1)
            return Json.String(input.substring(from + 1 until endIndex)) to endIndex + 1
        }
        '[' -> {
            val list = mutableListOf<Json>()
            position++
            while (input[position] != ']') {
                val (item, rest) = parseJson(input, position)
                list += item
                position = if (input[rest] == ',') rest + 1 else rest
            }
            return Json.Array(list) to position + 1
        }
        '{' -> {
            val map = mutableMapOf<Json.String, Json>()
            position++
            while (input[position] != '}') {
                val (key, rest1) = parseJson(input, position)
                val (value, rest2) = parseJson(input, rest1 + 1)
                map[key as Json.String] = value
                position = if (input[rest2] == ',') rest2 + 1 else rest2
            }
            return Json.Object(map) to position + 1
        }
        else -> {
            val match = numRegex.matchAt(input, position)!!
            return Json.Number(match.value.toInt()) to position + match.value.length
        }
    }
}
