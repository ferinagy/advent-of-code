package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import com.github.ferinagy.adventOfCode.searchGraph
import kotlin.math.min

fun main() {
    val input = readInputLines(2022, "16-input")
    val testInput1 = readInputLines(2022, "16-test1")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(testInput1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val caves = VolcanicCaves(input)
    val totalTime = 30

    val bits = (1 shl caves.openable.size) - 1
    return caves.maxPossible(totalTime) - caves.dfs(totalTime, "AA", bits)
}

private fun part2(input: List<String>): Int {
    val caves = VolcanicCaves(input)
    val totalTime = 26

    val bits = (1 shl caves.openable.size) - 1
    return caves.maxPossible(totalTime) - caves.aStar(totalTime, "AA", bits)
}
private class VolcanicCaves(input: List<String>) {

    val regex = """Valve (\w+) has flow rate=(\d+); tunnels? leads? to valves? ((\w+)(, (\w+))*)""".toRegex()
    val valves = input.map {
        val (from, rate, into) = regex.matchEntire(it)!!.destructured
        Valve(from, rate.toInt(), into.split(", ").toSet())
    }

    val openable = valves.filter { it.rate != 0 }.map { it.name }
    val openableByName = openable.mapIndexed { index, s -> s to index }.toMap()

    val dists = computeDistances()

    fun maxPossible(
        time: Int,
        bitSet: Int = (1 shl openable.size) - 1
    ) = calculateWasted(bitSet, time)

    private val cache = mutableMapOf<Triple<Int, String, Int>, Int>()
    fun dfs(time: Int, position: String, closed: Int): Int {
        val key = Triple(time, position, closed)
        if (key in cache) return cache[key]!!

        var min = calculateWasted(closed, time)
        closed.bitIndices(openable.size) { index ->
            val next = openable[index]
            val d = dists[position]!![next]!! + 1
            if (d <= time) {
                val wasted = calculateWasted(closed, d)
                min = min(dfs(time - d, next, closed.removeBit(index)) + wasted, min)
            }
        }

        cache[key] = min
        return min
    }

    fun aStar(totalTime: Int, start: String, allValves: Int): Int {
        data class State(val time: Int, val p1: String, val t1: Int, val p2: String, val t2: Int, val closed: Int)

        val best = searchGraph(
            start = State(totalTime, start, 0, start, 0, allValves),
            isDone = { it.time == 0 },
            nextSteps = {  state ->
                val set = mutableSetOf<Pair<State, Int>>()
                if (state.t1 == 0) {
                    val newClosed = if (state.p1 == start) state.closed else state.closed.removeBit(openableByName[state.p1]!!)

                    newClosed.bitIndices(openable.size) { index ->
                        val next = openable[index]
                        if (next != state.p2) {
                            val d = dists[state.p1]!![next]!! + 1
                            set += state.copy(p1 = next, t1 = d, closed = newClosed) to 0
                        }
                    }

                    if (set.isEmpty()) {
                        set += state.copy(t1 = totalTime, closed = newClosed) to 0
                    }
                } else if (state.t2 == 0) {
                    set += state.copy(p1 = state.p2, t1 = 0, p2 = state.p1, t2 = state.t1) to 0
                } else {
                    val t = min(state.t1, state.t2).coerceAtMost(state.time)
                    set += state.copy(time = state.time - t, t1 = state.t1 - t, t2 = state.t2 - t) to calculateWasted(state.closed, t)
                }

                set
            },
        )

        return best
    }

    private fun calculateWasted(closed: Int, time: Int) = valves.sumOf {
        val open = openableByName[it.name]
        if (open != null && open in closed) it.rate * time else 0
    }

    private fun computeDistances(): Map<String, Map<String, Int>> {
        val map = mutableMapOf<String, MutableMap<String, Int>>()
        val named = valves.associateBy { it.name }
        for (i in valves.indices) {
            val m1 = mutableMapOf<String, Int>()
            for (j in valves.indices) {
                m1[valves[j].name] = valves.size
            }
            map[valves[i].name] = m1
        }

        valves.forEach { v ->
            map[v.name]!![v.name] = 0
        }

        valves.forEach { v1 ->
            v1.connections.forEach { v2 ->
                map[v1.name]!![named[v2]!!.name] = 1
            }
        }

        for (k in valves) {
            for (i in valves) {
                for (j in valves) {
                    val newDist = map[i.name]!![k.name]!! + map[k.name]!![j.name]!!
                    if (map[i.name]!![j.name]!! > newDist) {
                        map[i.name]!![j.name] = newDist
                    }
                }
            }
        }

        return map
    }
}

private fun Int.bitIndices(size: Int, block: (Int) -> Unit) {
    var n = 0
    var i = 1
    while (n < size) {
        if (i and this@bitIndices != 0) block(n)

        n++
        i = i shl 1
    }
}

private fun Int.removeBit(index: Int): Int {
    val n = 1 shl index
    return this and n.inv()
}

private operator fun Int.contains(index: Int): Boolean {
    val n = 1 shl index
    return this and n != 0
}

private data class Valve(val name: String, val rate: Int, val connections: Set<String>)
