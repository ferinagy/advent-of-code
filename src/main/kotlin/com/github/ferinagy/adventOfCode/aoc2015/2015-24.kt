package com.github.ferinagy.adventOfCode.aoc2015

import java.util.*

fun main() {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput1))
    println(part2(input))
}

private fun part1(input: String): Long {
    return solve(input, 3)
}

private fun part2(input: String): Long {
    return solve(input, 4)
}

private fun solve(input: String, groupCount: Int): Long {
    val packages = input.lines().map { it.toLong() }.sortedDescending()
    val sum = packages.sum()
    require(sum % groupCount == 0L)
    val target = sum / groupCount

    val sequence = packages.groupsWithSum(target)
    val firstValid = sequence.first { it.isValid(packages, target) }
    val allCandidates = sequence.takeWhile { it.size <= firstValid.size }.dropWhile { it.size < firstValid.size }

    return allCandidates.minOf { it.product() }
}

private fun List<Long>.product(): Long = reduce { acc, l -> acc * l }

@OptIn(ExperimentalStdlibApi::class)
private fun List<Long>.groupsWithSum(target: Long): Sequence<List<Long>> = sequence {
    val queue = LinkedList<Pair<List<Long>, List<Long>>>()
    queue.addLast(emptyList<Long>() to this@groupsWithSum)

    while (queue.isNotEmpty()) {
        val (next, remaining) = queue.removeFirst()

        val sum = next.sum()
        for (i in remaining.indices) {
            if (target < sum + remaining[i]) continue

            val new = next + remaining[i]
            if (target == sum + remaining[i]) {
                yield(new)
                continue
            }

            queue += new to remaining.subList(i + 1, remaining.size)
        }
    }
}

private fun List<Long>.isValid(all: List<Long>, target: Long): Boolean {
    val rest = all - this.toSet()
    return rest.groupsWithSum(target).any()
}

private val testInput1 = """1
    |2
    |3
    |4
    |5
    |7
    |8
    |9
    |10
    |11
""".trimMargin()

private const val input = """1
2
3
5
7
13
17
19
23
29
31
37
41
43
53
59
61
67
71
73
79
83
89
97
101
103
107
109
113"""
