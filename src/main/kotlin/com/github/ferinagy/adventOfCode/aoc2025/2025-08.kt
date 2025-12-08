package com.github.ferinagy.adventOfCode.aoc2025

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import kotlin.math.abs

fun main() {
    val input = readInputLines(2025, "08-input")
    val test1 = readInputLines(2025, "08-test1")

    println("Part1:")
    part1(test1, 10).println()
    part1(input, 1000).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>, n: Int): Long {
    val boxes = input.map { it.split(',').map { it.toLong() } }

    val sorted = boxes.sortedConnections()

    val circuits = boxes.indices.map { setOf(it) }.toMutableSet()
    repeat (n) {
        circuits.connect(sorted[it])
    }

    return circuits.map { it.size }.sortedDescending().take(3).let { (a, b, c) -> a.toLong() * b * c }
}

private fun part2(input: List<String>): Long {
    val boxes = input.map { it.split(',').map { it.toLong() } }

    val sorted = boxes.sortedConnections()

    val circuits = boxes.indices.map { setOf(it) }.toMutableSet()
    var n = 0
    while (true) {
        circuits.connect(sorted[n])
        if (circuits.size == 1) {
            return boxes[sorted[n].first][0] * boxes[sorted[n].second][0]
        }

        n++
    }
}

private fun MutableSet<Set<Int>>.connect(connection: Pair<Int, Int>) {
    val (left, right) = connection
    val existing = filter { left in it || right in it }
    if (existing.size == 2) {
        this -= existing.toSet()
        this += existing.flatten().toSet()
    }
}

private fun List<List<Long>>.sortedConnections() = (0..<lastIndex).flatMap { i1 ->
    (i1 + 1..lastIndex).map { i2 -> i1 to i2 }
}.sortedBy { (b1, b2) -> dist(this, b1, b2) }

private fun dist(boxes: List<List<Long>>, b1: Int, b2: Int): Long {
    val d1 = abs(boxes[b1][0] - boxes[b2][0])
    val d2 = abs(boxes[b1][1] - boxes[b2][1])
    val d3 = abs(boxes[b1][2] - boxes[b2][2])
    return d1 * d1 + d2 * d2 + d3 * d3
}

private fun findCircuit(current: Int, connections: MutableSet<Pair<Int, Int>>): Set<Int> {
    val connectedLeft = connections.filter { it.first == current }
    val connectedRight = connections.filter { it.second == current }

    connections -= connectedLeft.toSet()
    connections -= connectedRight.toSet()

    return setOf(current) + connectedLeft.map { it.second }.flatMap { findCircuit(it, connections) } + connectedRight.map { it.first }.flatMap { findCircuit(it, connections) }
}
