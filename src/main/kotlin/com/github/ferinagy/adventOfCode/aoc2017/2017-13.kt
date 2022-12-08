package com.github.ferinagy.adventOfCode.aoc2017

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
    val setup = parse(input)

    return setup.sumOf { (layer, depth) -> if (isCaught(layer, depth)) layer * depth else 0 }
}

private fun part2(input: String): Int {
    val setup = parse(input)

    var delay = 0
    while (true) {
        if (setup.none { (layer, depth) -> isCaught(layer + delay, depth) }) return delay

        delay++
    }
}

private fun isCaught(time: Int, depth: Int) = time % ((depth - 1) * 2) == 0

private fun parse(input: String): List<Pair<Int, Int>> {
    val initial = input.lines().map {
        val (l, r) = it.split(": ")
        l.toInt() to r.toInt()
    }
    return initial
}

private const val testInput1 = """0: 3
1: 2
4: 4
6: 4"""

private const val input = """0: 3
1: 2
2: 5
4: 4
6: 6
8: 4
10: 8
12: 8
14: 6
16: 8
18: 6
20: 6
22: 8
24: 12
26: 12
28: 8
30: 12
32: 12
34: 8
36: 10
38: 9
40: 12
42: 10
44: 12
46: 14
48: 14
50: 12
52: 14
56: 12
58: 12
60: 14
62: 14
64: 12
66: 14
68: 14
70: 14
74: 24
76: 14
80: 18
82: 14
84: 14
90: 14
94: 17"""
