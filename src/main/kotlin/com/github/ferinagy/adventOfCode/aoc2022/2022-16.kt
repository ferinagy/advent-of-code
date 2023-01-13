package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.searchGraph
import kotlin.math.min

fun main() {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput1))
    println(part2(input))
}

private fun part1(input: String): Int {
    val caves = VolcanicCaves(input)
    val totalTime = 30

    val bits = (1 shl caves.openable.size) - 1
    return caves.maxPossible(totalTime) - caves.dfs(totalTime, "AA", bits)
}

private fun part2(input: String): Int {
    val caves = VolcanicCaves(input)
    val totalTime = 26

    val bits = (1 shl caves.openable.size) - 1
    return caves.maxPossible(totalTime) - caves.aStar(totalTime, "AA", bits)
}
private class VolcanicCaves(input: String) {

    val regex = """Valve (\w+) has flow rate=(\d+); tunnels? leads? to valves? ((\w+)(, (\w+))*)""".toRegex()
    val valves = input.lines().map {
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

private const val testInput1 = """Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
Valve BB has flow rate=13; tunnels lead to valves CC, AA
Valve CC has flow rate=2; tunnels lead to valves DD, BB
Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
Valve EE has flow rate=3; tunnels lead to valves FF, DD
Valve FF has flow rate=0; tunnels lead to valves EE, GG
Valve GG has flow rate=0; tunnels lead to valves FF, HH
Valve HH has flow rate=22; tunnel leads to valve GG
Valve II has flow rate=0; tunnels lead to valves AA, JJ
Valve JJ has flow rate=21; tunnel leads to valve II"""

private const val input = """Valve QJ has flow rate=11; tunnels lead to valves HB, GL
Valve VZ has flow rate=10; tunnel leads to valve NE
Valve TX has flow rate=19; tunnels lead to valves MG, OQ, HM
Valve ZI has flow rate=5; tunnels lead to valves BY, ON, RU, LF, JR
Valve IH has flow rate=0; tunnels lead to valves YB, QS
Valve QS has flow rate=22; tunnel leads to valve IH
Valve QB has flow rate=0; tunnels lead to valves QX, ES
Valve NX has flow rate=0; tunnels lead to valves UH, OP
Valve PJ has flow rate=0; tunnels lead to valves OC, UH
Valve OR has flow rate=6; tunnels lead to valves QH, BH, HB, JD
Valve OC has flow rate=7; tunnels lead to valves IZ, JR, TA, ZH, PJ
Valve UC has flow rate=0; tunnels lead to valves AA, BY
Valve QX has flow rate=0; tunnels lead to valves AA, QB
Valve IZ has flow rate=0; tunnels lead to valves OC, SX
Valve AG has flow rate=13; tunnels lead to valves NW, GL, SM
Valve ON has flow rate=0; tunnels lead to valves MO, ZI
Valve XT has flow rate=18; tunnels lead to valves QZ, PG
Valve AX has flow rate=0; tunnels lead to valves UH, MO
Valve JD has flow rate=0; tunnels lead to valves OR, SM
Valve HM has flow rate=0; tunnels lead to valves TX, QH
Valve LF has flow rate=0; tunnels lead to valves ZI, UH
Valve QH has flow rate=0; tunnels lead to valves OR, HM
Valve RT has flow rate=21; tunnel leads to valve PG
Valve NE has flow rate=0; tunnels lead to valves VZ, TA
Valve OQ has flow rate=0; tunnels lead to valves TX, GE
Valve AA has flow rate=0; tunnels lead to valves QZ, UC, OP, QX, EH
Valve UH has flow rate=17; tunnels lead to valves PJ, NX, AX, LF
Valve GE has flow rate=0; tunnels lead to valves YB, OQ
Valve EH has flow rate=0; tunnels lead to valves AA, MO
Valve MG has flow rate=0; tunnels lead to valves TX, NW
Valve YB has flow rate=20; tunnels lead to valves IH, GE, XG
Valve MO has flow rate=15; tunnels lead to valves EH, ON, AX, ZH, CB
Valve JR has flow rate=0; tunnels lead to valves ZI, OC
Valve GL has flow rate=0; tunnels lead to valves AG, QJ
Valve SM has flow rate=0; tunnels lead to valves JD, AG
Valve HB has flow rate=0; tunnels lead to valves OR, QJ
Valve TA has flow rate=0; tunnels lead to valves OC, NE
Valve PG has flow rate=0; tunnels lead to valves RT, XT
Valve XG has flow rate=0; tunnels lead to valves CB, YB
Valve ES has flow rate=9; tunnels lead to valves QB, FL
Valve BH has flow rate=0; tunnels lead to valves RU, OR
Valve FL has flow rate=0; tunnels lead to valves SX, ES
Valve CB has flow rate=0; tunnels lead to valves MO, XG
Valve QZ has flow rate=0; tunnels lead to valves AA, XT
Valve BY has flow rate=0; tunnels lead to valves UC, ZI
Valve ZH has flow rate=0; tunnels lead to valves MO, OC
Valve OP has flow rate=0; tunnels lead to valves NX, AA
Valve NW has flow rate=0; tunnels lead to valves MG, AG
Valve RU has flow rate=0; tunnels lead to valves ZI, BH
Valve SX has flow rate=16; tunnels lead to valves IZ, FL"""
