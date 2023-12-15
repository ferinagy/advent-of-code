package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2021, "14-input")
    val test1 = readInputText(2021, "14-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: String): Long {
    return doPairInsertion(input, 10)
}

private fun part2(input: String): Long {
    return doPairInsertion(input, 40)
}

private fun doPairInsertion(input: String, count: Int): Long {
    val (initial, temp) = input.split("\n\n")

    val templates: Map<String, String> = temp.lines().associate {
        val (from, to) = it.split(" -> ")
        from to to + from[1]
    }

    val letterMap = mutableMapOf<Char, Long>()
    val cache = mutableMapOf<Pair<String, Int>, Map<Char, Long>>()
    for (i in 0 until initial.lastIndex) {
        val subLetters = letterMap(initial.substring(i .. i + 1), count, templates, cache)
        letterMap.add(subLetters)
    }
    val first = letterMap[initial.first()]!!
    letterMap[initial.first()] = first + 1

    val sorted = letterMap.map { it.value }.sorted()

    return sorted.last() - sorted.first()
}

private fun MutableMap<Char, Long>.add(other: Map<Char, Long>) {
    other.forEach {
        val count = getOrDefault(it.key, 0)
        put(it.key, count + it.value)
    }
}

private fun letterMap(
    key: String,
    depth: Int,
    templates: Map<String, String>,
    cache: MutableMap<Pair<String, Int>, Map<Char, Long>>
): Map<Char, Long> {
    val existing = cache[key to depth]
    if (existing != null) return existing

    if (depth == 1) {
        return templates[key]!!.toCharArray().letterMap().also { cache[key to depth] = it }
    }

    val result = mutableMapOf<Char, Long>()

    val sub = templates[key]!!
    for (i in 1..sub.lastIndex) {
        val subLetters = letterMap(sub.substring(i - 1..i), depth - 1, templates, cache)
        result.add(subLetters)
    }
    val subLetters = letterMap("${key[0]}${sub[0]}", depth - 1, templates, cache)
    result.add(subLetters)

    return result.also { cache[key to depth] = it }
}

private fun CharArray.letterMap() = groupBy { it }.mapValues { it.value.size.toLong() }
