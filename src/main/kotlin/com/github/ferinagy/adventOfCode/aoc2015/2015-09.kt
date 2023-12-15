package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.TspGraph
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2015, "09-input")
    val test1 = readInputLines(2015, "09-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val graph = readGraph(input)
    return graph.solveTsp { d1, d2 -> d1 - d2 }
}

private fun part2(input: List<String>): Int {
    val graph = readGraph(input)
    return graph.solveTsp { d1, d2 -> d2 - d1 }
}

private fun readGraph(input: List<String>): TspGraph {
    val graph = TspGraph()
    val regex = """(\w+) to (\w+) = (\d+)""".toRegex()

    input.forEach {
        val (from, to, dist) = regex.matchEntire(it)!!.destructured
        graph.addBidirectionalEdge(from, to, dist.toInt())
    }

    return graph
}
