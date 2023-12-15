package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2021, "12-input")
    val test1 = readInputLines(2021, "12-test1")
    val test2 = readInputLines(2021, "12-test2")
    val test3 = readInputLines(2021, "12-test3")


    println("Part1:")
    println(part1(test1))
    println(part1(test2))
    println(part1(test3))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(test1))
    println(part2(test2))
    println(part2(test3))
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    val graph = CaveGraph(input)

    return graph.solve(false)
}

private fun part2(input: List<String>): Int {
    val graph = CaveGraph(input)

    return graph.solve(true)
}

private class CaveGraph(input: List<String>) {

    private val map = mutableMapOf<Cave, MutableSet<Cave>>()

    init {
        input.map { it.split('-') }
            .forEach { (from, to) ->
                val cave1 = Cave.parse(from)
                val cave2 = Cave.parse(to)
                val set1 = map.getOrElse(cave1) { mutableSetOf() }
                set1 += cave2
                map[cave1] = set1

                val set2 = map.getOrElse(cave2) { mutableSetOf() }
                set2 += cave1
                map[cave2] = set2
            }
    }

    fun solve(allowVisitingSmallTwice: Boolean): Int {
        var partialPaths = listOf(Path(listOf(Cave.Start), visitedSmallTwice = !allowVisitingSmallTwice))
        val finishedPaths = mutableListOf<Path>()
        while (partialPaths.isNotEmpty()) {
            val newPaths = mutableListOf<Path>()
            partialPaths.forEach { partial ->
                val end = partial.caves.last()
                val connections = map[end] ?: return@forEach
                connections.forEach { conn ->
                    when (conn) {
                        Cave.Start -> {}
                        Cave.End -> { finishedPaths += partial.copy(caves = partial.caves + conn) }
                        is Cave.Big -> { newPaths += partial.copy(caves = partial.caves + conn) }
                        is Cave.Small -> {
                            if (conn !in partial.caves) {
                                newPaths += partial.copy(caves = partial.caves + conn)
                            } else if (!partial.visitedSmallTwice) {
                                newPaths += partial.copy(caves = partial.caves + conn, visitedSmallTwice = true)
                            }
                        }
                    }
                }
            }
            partialPaths = newPaths
        }

        return finishedPaths.size
    }

    private data class Path(val caves: List<Cave>, val visitedSmallTwice: Boolean = false)

    sealed class Cave {
        data object Start : Cave()
        data object End : Cave()
        data class Big(val name: String) : Cave()
        data class Small(val name: String) : Cave()

        companion object {
            fun parse(name: String) = when {
                name == "start" -> Start
                name == "end" -> End
                name.all { it.isUpperCase() } -> Big(name)
                else -> Small(name)
            }
        }
    }
}
