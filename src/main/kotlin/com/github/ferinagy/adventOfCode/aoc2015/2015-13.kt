package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.TspGraph
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2015, "13-input")
    val test1 = readInputLines(2015, "13-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val graph = parse(input)

    return graph.solveCircularTsp { o1, o2 -> o2 - o1 }
}

private fun part2(input: List<String>): Int {
    val graph = parse(input)

    return graph.solveTsp { o1, o2 -> o2 - o1 }
}

private fun parse(input: List<String>): TspGraph {
    val map = mutableMapOf<Set<String>, Int>()
    input.map {
        val (p1, op, value, p2) = regex.matchEntire(it)!!.destructured

        val set = setOf(p1, p2)
        val newValue = map.getOrDefault(set, 0) + if (op == "gain") value.toInt() else -value.toInt()
        map[set] = newValue
    }

    val graph = TspGraph()
    map.forEach { (key, value) ->
        val (p1, p2) = key.toList()
        graph.addBidirectionalEdge(p1, p2, value)
    }
    return graph
}

private val regex = """(\w+) would (gain|lose) (\d+) happiness units by sitting next to (\w+).""".toRegex()
