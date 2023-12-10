package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2022, "11-input")
    val testInput1 = readInputText(2022, "11-test1")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(testInput1).println()
    part2(input).println()
}

private fun part1(input: String): Long {
    return solve(input, 20, reducer = { it / 3 })
}

private fun part2(input: String): Long {
    return solve(input, 10000, reducer = { it })
}

private fun solve(input: String, n: Int, reducer: (Long) -> Long): Long {
    val monkeys = monkeys(input)
    val max = monkeys.map { it.test }.reduce(Long::times)

    val counts = LongArray(monkeys.size)
    repeat(n) {
        monkeys.forEachIndexed { index, monkey ->
            while (monkey.items.isNotEmpty()) {
                var current = monkey.items.removeFirst()
                current = monkey.operation(current)
                current = reducer(current)
                current %= max
                counts[index]++

                if (current % monkey.test == 0L) {
                    monkeys[monkey.ifTrue].items += current
                } else {
                    monkeys[monkey.ifFalse].items += current
                }
            }
        }
    }

    return counts.sortedDescending().take(2).let { it[0] * it[1] }
}

fun parseOp(s: String): (Long) -> Long {
    val regex = """  Operation: new = old ([*+]) (\d+|old)""".toRegex()
    val (op, value) = regex.matchEntire(s)!!.destructured

    val op2: Long.(Long)->Long = if (op == "*") Long::times else Long::plus

    return { it.op2(value.toLongOrNull() ?: it) }
}
fun parseTest(s: String): Long {
    require(s.startsWith("  Test: divisible by "))

    return s.substring(21).toLong()
}

private fun monkeys(input: String): List<Monkey> {
    val monkeys = input.split("\n\n").map { config ->
        val lines = config.lines()
        Monkey(
            items = lines[1].substring(18).split(", ").map { it.toLong() }.toMutableList(),
            operation = parseOp(lines[2]),
            test = parseTest(lines[3]),
            ifTrue = lines[4].substring(29).toInt(),
            ifFalse = lines[5].substring(30).toInt(),
        )
    }
    return monkeys
}

private data class Monkey(
    val items: MutableList<Long>,
    val operation: (Long) -> Long,
    val test: Long,
    val ifTrue: Int,
    val ifFalse: Int
)
