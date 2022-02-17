package com.github.ferinagy.adventOfCode.aoc2017

fun main() {
    println("Part1+2:")
    println(solve(testInput1))
    println(solve(input))
}

private fun solve(input: String): Pair<Int, Int> {
    var memory = input.split("""\s+""".toRegex()).map { it.toInt() }
    val visited = mutableMapOf<List<Int>, Int>()
    var counter = 0
    while (memory !in visited) {
        visited += memory to counter
        memory = memory.redistribute()
        counter++
    }
    return counter to (counter - visited[memory]!!)
}

private fun List<Int>.redistribute(): List<Int> {
    val max = maxOrNull()!!
    val from = indexOfFirst { it == max }
    val result = toMutableList()
    val count = get(from)
    result[from] = 0
    var index = from.inc() % size

    repeat(count) {
        result[index] = result[index].inc()
        index = index.inc() % size
    }

    return result
}

private const val testInput1 = """0 2 7 0"""

private const val input = """14	0	15	12	11	11	3	5	1	6	8	4	9	1	8	4"""
