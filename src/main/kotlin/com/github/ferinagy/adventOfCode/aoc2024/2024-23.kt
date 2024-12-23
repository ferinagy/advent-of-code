package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2024, "23-input")
    val test1 = readInputLines(2024, "23-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val graph = mutableMapOf<String, Set<String>>()
    input.forEach {
        val (a, b) = it.split('-')
        graph[a] = graph.getOrDefault(a, emptySet()) + b
        graph[b] = graph.getOrDefault(b, emptySet()) + a
    }
    val triples = mutableSetOf<Set<String>>()
    graph.forEach { (a, bs) ->
        bs.forEach { b ->
            graph[b]!!.forEach { c ->
                if (a in graph[c]!!) triples += setOf(a, b, c)
            }
        }
    }

    return triples.filter { t -> t.any { it[0] == 't' } }.size
}

private fun part2(input: List<String>): String {
    val graph = mutableMapOf<String, Set<String>>()
    input.forEach {
        val (a, b) = it.split('-')
        graph[a] = graph.getOrDefault(a, emptySet()) + b
        graph[b] = graph.getOrDefault(b, emptySet()) + a
    }

    val cliques = bronKerbosch(emptySet(), graph.keys, emptySet(), graph)

    return cliques.maxBy { it.size }.sorted().joinToString(separator = ",")
}

private fun bronKerbosch(r: Set<String>, initialP: Set<String>, initialX: Set<String>, graph: Map<String, Set<String>>): Set<Set<String>> {
    if (initialP.isEmpty() && initialX.isEmpty()) return setOf(r)

    var p = initialP
    var x = initialX

    val result = mutableSetOf<Set<String>>()
    p.forEach { v ->
        result += bronKerbosch(r + v, p intersect graph[v]!!, x intersect graph[v]!!, graph)
        p = p - v
        x = x + v
    }

    return result
}
