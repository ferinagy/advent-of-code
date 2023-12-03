package com.github.ferinagy.adventOfCode.aoc2021

fun main() {
    println("Part1:")
    println(part1(testInput1))
    println(part1(testInput2))
    println(part1(testInput3))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput1))
    println(part2(testInput2))
    println(part2(testInput3))
    println(part2(input))
}

private fun part1(input: String): Int {
    val graph = CaveGraph(input)

    return graph.solve(false)
}

private fun part2(input: String): Int {
    val graph = CaveGraph(input)

    return graph.solve(true)
}

private class CaveGraph(input: String) {

    private val map = mutableMapOf<Cave, MutableSet<Cave>>()

    init {
        input.lines()
            .map { it.split('-') }
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
        object Start : Cave() {
            override fun toString() = "Start"
        }
        object End : Cave() {
            override fun toString() = "End"
        }
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

private const val testInput1 = """start-A
start-b
A-c
A-b
b-d
A-end
b-end"""

private const val testInput2 = """dc-end
HN-start
start-kj
dc-start
dc-HN
LN-dc
HN-end
kj-sa
kj-HN
kj-dc"""

private const val testInput3 = """fs-end
he-DX
fs-he
start-DX
pj-DX
end-zg
zg-sl
zg-pj
pj-he
RW-he
fs-DX
pj-RW
zg-RW
start-pj
he-WI
zg-he
pj-fs
start-RW"""

private const val input = """kc-qy
qy-FN
kc-ZP
end-FN
li-ZP
yc-start
end-qy
yc-ZP
wx-ZP
qy-li
yc-li
yc-wx
kc-FN
FN-li
li-wx
kc-wx
ZP-start
li-kc
qy-nv
ZP-qy
nv-xr
wx-start
end-nv
kc-nv
nv-XQ"""
