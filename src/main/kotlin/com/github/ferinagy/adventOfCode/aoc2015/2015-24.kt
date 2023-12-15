package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import java.util.*

fun main() {
    val input = readInputLines(2015, "24-input")
    val test1 = readInputLines(2015, "24-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Long {
    return solve(input, 3)
}

private fun part2(input: List<String>): Long {
    return solve(input, 4)
}

private fun solve(input: List<String>, groupCount: Int): Long {
    val packages = input.map { it.toLong() }.sortedDescending()
    val sum = packages.sum()
    require(sum % groupCount == 0L)
    val target = sum / groupCount

    val sequence = packages.groupsWithSum(target)
    val firstValid = sequence.first { it.isValid(packages, target) }
    val allCandidates = sequence.takeWhile { it.size <= firstValid.size }.dropWhile { it.size < firstValid.size }

    return allCandidates.minOf { it.product() }
}

private fun List<Long>.product(): Long = reduce { acc, l -> acc * l }

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
