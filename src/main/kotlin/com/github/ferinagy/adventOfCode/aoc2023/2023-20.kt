package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.lcm
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import java.util.LinkedList

fun main() {
    val input = readInputLines(2023, "20-input")
    val test1 = readInputLines(2023, "20-test1")
    val test2 = readInputLines(2023, "20-test2")

    println("Part1:")
    part1(test1).println()
    part1(test2).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val moduleMap = parseModules(input)

    var totalLow = 0
    var totalHigh = 0

    repeat(1000) {
        runCycle(moduleMap, "broadcaster", onPulseSent = { _, value -> if (value) totalHigh++ else totalLow++ })
    }

    return totalLow * totalHigh
}

private fun part2(input: List<String>): Long {
    val moduleMap = parseModules(input)

    val modules = moduleMap.values.toList()
    val collector = modules.single { "rx" in it.out }.name

    val ends = modules.filter { collector in it.out }.map { it.name }

    val periods = ends.map { end ->
        val (start, visited) = findSubGraph(end, modules)

        val subModules = parseModules(input).filter { it.key in visited }
        val (period, offset) = findCycle(subModules, start)

        reset(subModules)
        val interesting = mutableListOf<Int>()
        repeat(period + offset) {
            runCycle(subModules, start, onPulseSent = { node, value ->
                if (node == end && !value) {
                    interesting += it + 1
                }
            })
        }
        check(interesting.single() == period)
        period.toLong()
    }

    return periods.reduce { acc, period -> lcm(acc, period) }
}

private inline fun runCycle(moduleMap: Map<String, Module>, start: String, print: Boolean = false, onPulseSent: (String, Boolean) -> Unit) {
    val queue = LinkedList<Triple<String, String, Boolean>>()
    queue += Triple("button", start, false, )

    while (queue.isNotEmpty()) {
        val (source, dest, value) = queue.removeFirst().also {
            if (print) it.println()
        }
        onPulseSent(dest, value)

        val module = moduleMap[dest] ?: continue
        if (print) module.println()
        when (module) {
            is Broadcaster -> module.out.forEach {
                queue += Triple(module.name, it, value)
            }

            is Conjunction -> {
                module.states[source] = value
                val output = module.states.values.any { !it }
                module.out.forEach {
                    queue += Triple(module.name, it, output)
                }
            }

            is FlipFlop -> {
                if (!value) {
                    module.state = !module.state
                    module.out.forEach {
                        queue += Triple(module.name, it, module.state)
                    }
                }
            }
        }
    }
}

private fun findSubGraph(end: String, modules: List<Module>): Pair<String, Set<String>> {
    val queue = mutableListOf(end)
    val visited = mutableSetOf<String>()
    lateinit var start: String
    while (queue.isNotEmpty()) {
        val current = queue.removeLast()

        if (current in visited) continue
        visited += current
        val next = modules.filter { current in it.out }.map { it.name }
        if ("broadcaster" in next) {
            start = current
        }
        queue += next
    }

    return start to visited
}

private fun findCycle(moduleMap: Map<String, Module>, start: String): Pair<Int, Int> {
    val state = getState(moduleMap)
    val states = mutableMapOf(state to 0)
    var counter = 0
    val period: Int
    val offset: Int
    while (true) {
        counter++
        runCycle(moduleMap = moduleMap, start = start, onPulseSent = { _, _ -> })
        val newState = getState(moduleMap)
        if (newState in states) {
            offset = states[newState]!!
            period = counter - offset
            break
        }
        states[newState] = counter
    }

    return period to offset
}

private fun getState(moduleMap: Map<String, Module>): Pair<Map<String, Boolean>, Map<String, Map<String, Boolean>>> {
    val flipFlops = moduleMap.values.filterIsInstance<FlipFlop>().associate { it.name to it.state }
    val cons = moduleMap.values.filterIsInstance<Conjunction>().associate { it.name to it.states.toMap() }
    return flipFlops to cons
}

private fun reset(moduleMap: Map<String, Module>) {
    moduleMap.forEach { (_, module) ->
        when (module) {
            is Broadcaster -> Unit
            is Conjunction -> module.states.keys.forEach { module.states[it] = false }
            is FlipFlop -> module.state = false
        }
    }
}

private fun parseModules(input: List<String>): Map<String, Module> {
    val modules = input.map { line ->
        val (source, dests) = line.split(" -> ")
        val out = dests.split(", ")
        when {
            source.startsWith('%') -> FlipFlop(source.drop(1), out)
            source.startsWith('&') -> Conjunction(source.drop(1), out)
            source == "broadcaster" -> Broadcaster(source, out)
            else -> error("unknown module: '$line'")
        }
    }
    modules.filterIsInstance<Conjunction>().forEach { con ->
        modules.filter { con.name in it.out }.map { it.name }.forEach { con.states[it] = false }
    }

    return modules.associateBy { it.name }
}

private sealed interface Module {
    val name: String
    val out: List<String>
}

private data class Broadcaster(
    override val name: String,
    override val out: List<String>,
) : Module

private data class FlipFlop(
    override val name: String,
    override val out: List<String>,
    var state: Boolean = false
) : Module

private data class Conjunction(
    override val name: String,
    override val out: List<String>,
    val states: MutableMap<String, Boolean> = mutableMapOf()
) : Module
