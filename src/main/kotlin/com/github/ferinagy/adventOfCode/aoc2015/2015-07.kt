package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2015, "07-input")
    val test1 = readInputLines(2015, "07-test1")

    println("Part1:")
    part1(test1, "g").println()
    part1(input, "a").println()

    println()
    println("Part2:")
    part2(input).println()
}

private fun part1(input: List<String>, wire: String): UShort {
    val map = parse(input)

    val gates = LogicGates(map)
    return gates.solveWire(wire)
}

private fun part2(input: List<String>): UShort {
    val map = parse(input)

    map["b"] = "16076"
    val gates = LogicGates(map)
    return gates.solveWire("a")
}

private class LogicGates(private val map: Map<String, String>) {

    private val solved = mutableMapOf<String, UShort>()

    fun solveWire(wire: String): UShort {
        val const = """\d+""".toRegex()
        if (const.matches(wire)) return wire.toUShort()

        if (wire in solved) return solved[wire]!!

        val gate = map[wire]!!

        val pipe = """\w+""".toRegex()
        val not = """NOT ([a-z]+)""".toRegex()
        val andGate = """(\w+) AND (\w+)""".toRegex()
        val orGate = """([a-z]+) OR ([a-z]+)""".toRegex()
        val rshift = """([a-z]+) RSHIFT (\d+)""".toRegex()
        val lshift = """([a-z]+) LSHIFT (\d+)""".toRegex()

        val result: UShort = when {
            pipe.matches(gate) -> { solveWire(gate) }
            not.matches(gate) -> {
                val g = not.matchEntire(gate)!!.groupValues[1]
                solveWire(g).inv()
            }
            andGate.matches(gate) -> {
                val (g1, g2) = andGate.matchEntire(gate)!!.destructured
                solveWire(g1) and solveWire(g2)
            }
            orGate.matches(gate) -> {
                val (g1, g2) = orGate.matchEntire(gate)!!.destructured
                solveWire(g1) or solveWire(g2)
            }
            lshift.matches(gate) -> {
                val (g1, g2) = lshift.matchEntire(gate)!!.destructured
                (solveWire(g1).toInt() shl g2.toInt()).toUShort()
            }
            rshift.matches(gate) -> {
                val (g1, g2) = rshift.matchEntire(gate)!!.destructured
                (solveWire(g1).toInt() shr g2.toInt()).toUShort()
            }
            else -> error("Should not happen")
        }


        return result.also { solved[wire] = it }
    }
}

private fun parse(input: List<String>): MutableMap<String, String> {
    val map = mutableMapOf<String, String>()

    input.forEach {
        val (src, dst) = it.split(" -> ")
        map[dst] = src
    }
    return map
}
