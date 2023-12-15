package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText
import kotlin.math.min

fun main() {
    val input = readInputText(2015, "19-input")
    val test1 = readInputText(2015, "19-test1")
    val test2 = readInputText(2015, "19-test2")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test2).println()
    part2(input).println()
}

private fun part1(input: String): Int {
    val (rules, initial) = parse(input)

    val replacements = initial.doReplacements(rules).toSet()
    return replacements.size
}

private fun part2(input: String): Int {
    val (rules, target) = parse(input)

    val cnfRules = rules.toCnf()
    val cache = mutableMapOf<CacheKey, Int>()

    for (s in target.indices) {
        cache[CacheKey(1, s, target[s])] = 0
    }

    for (l in 2 .. target.size) { // length of span
        for (s in 0 .. target.size - l) { // start of span
            for (p in 1 until l) { // partition of span
                cnfRules.forEach { rule ->
                    val left = CacheKey(p, s, rule.right[0])
                    val right = CacheKey(l - p, s + p, rule.right[1])
                    if (left in cache && right in cache) {
                        val newKey = CacheKey(l, s, rule.left)
                        val existing = cache.getOrDefault(newKey, Int.MAX_VALUE)
                        val newSteps = cache[left]!! + cache[right]!! + rule.steps
                        cache[newKey] = min(existing, newSteps)
                    }
                }
            }
        }
    }

    val result = cache[CacheKey(target.size, 0, "e")]
    require(result != null)

    return result
}

private data class CacheKey(val length: Int, val start: Int, val nonTerminal: String)

private fun Map<String, List<Molecule>>.toCnf(): MutableList<CnfRule> {
    var nextChar = 'A'
    val artificial = mutableListOf<CnfRule>()
    forEach { (key, list) ->
        list.forEach {
            if (it.size <= 2) {
                artificial += CnfRule(key, it, 1)
            } else {
                val char = nextChar++
                artificial += CnfRule(key, listOf(it.first(), char + "1"), 1)
                for (i in 1 until it.size - 2) {
                    artificial += CnfRule(char + i.toString(), listOf(it[i], char + (i + 1).toString()), 0)
                }
                artificial += CnfRule(char + (it.size - 2).toString(), listOf(it[it.lastIndex - 1], it.last()), 0)
            }
        }
    }

    while (true) {
        val unit = artificial.firstOrNull { it.right.size == 1 } ?: break
        artificial.remove(unit)
        val sub = artificial.filter { it.left == unit.right.single() }
        sub.forEach {
            artificial += CnfRule(unit.left, it.right, it.steps + 1)
        }
    }

    return artificial
}

private data class CnfRule(val left: String, val right: List<String>, val steps: Int)

private fun Molecule.doReplacements(rules: Map<String, List<Molecule>>): List<Molecule> {
    return flatMapIndexed { index, element ->
        rules.getOrDefault(element, emptyList()).map {
            subList(0, index) + it + subList(index + 1, size)
        }
    }
}

private typealias Molecule = List<String>

private fun parse(input: String): Pair<Map<String, List<Molecule>>, Molecule> {
    val regex = """(?=[A-Z][a-z]?)(?<=[A-Za-z])""".toRegex()
    val (rules, target) = input.split("\n\n")
    val map: Map<String, List<Molecule>> = rules.lines()
        .map {
            val (source, product) = it.split(" => ")
            source to product.split(regex)
        }.groupBy { it.first }
        .mapValues { it.value.map { it.second } }

    val targetList = target.split(regex)

    return map to targetList
}
