package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import kotlin.time.measureTime

fun main() {
    val input = readInputLines(2023, "25-input")
    val test1 = readInputLines(2023, "25-test1")

    println("Part1:")
    part1(test1).println()
    measureTime {
        part1(input).println()
    }.println()
}

private fun part1(input: List<String>): Int {
    val nodes = mutableMapOf<String, MutableSet<String>>()

    input.forEach {
        val (src, dests) = it.split(": ")
        dests.split(' ').forEach { dest ->
            nodes.getOrPut(src, defaultValue = { mutableSetOf() }) += dest
            nodes.getOrPut(dest, defaultValue = { mutableSetOf() }) += src
        }
    }

    val list = nodes.keys.toList()

    val vector = List(list.size) { row ->
        MutableList(list.size) { col -> if (list[col] in nodes[list[row]]!!) 1 else 0 }
    }

    val half = globalMinCut(vector)

    return half * (list.size - half)
}

private fun globalMinCut(mat: List<MutableList<Int>>): Int {
    val n = mat.size
    val co = List(n) { mutableListOf(it) }


    for (phase in 1 ..< n) {
        val w = mat[0].toMutableList()
        var s = 0
        var t = 0
        repeat(n - phase) {
            w[t] = Int.MIN_VALUE
            s = t
            t = w.indices.maxBy { w[it] }
            for (i in 0 ..< n) {
                w[i] += mat[t][i]
            }
        }
        val cut = w[t] - mat[t][t]
        if (cut == 3) {
            return co[t].size
        }
        co[s] += co[t]

        for (i in 0 ..< n) mat[s][i] += mat[t][i]
        for (i in 0 ..< n) mat[i][s] = mat[s][i]
        mat[0][t] = Int.MIN_VALUE
    }

    error("not 3?")
}
