package com.github.ferinagy.adventOfCode.aoc2022

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

private const val testInput1 = """Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1"""

private const val input = """Monkey 0:
  Starting items: 57, 58
  Operation: new = old * 19
  Test: divisible by 7
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 66, 52, 59, 79, 94, 73
  Operation: new = old + 1
  Test: divisible by 19
    If true: throw to monkey 4
    If false: throw to monkey 6

Monkey 2:
  Starting items: 80
  Operation: new = old + 6
  Test: divisible by 5
    If true: throw to monkey 7
    If false: throw to monkey 5

Monkey 3:
  Starting items: 82, 81, 68, 66, 71, 83, 75, 97
  Operation: new = old + 5
  Test: divisible by 11
    If true: throw to monkey 5
    If false: throw to monkey 2

Monkey 4:
  Starting items: 55, 52, 67, 70, 69, 94, 90
  Operation: new = old * old
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 3

Monkey 5:
  Starting items: 69, 85, 89, 91
  Operation: new = old + 7
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 7

Monkey 6:
  Starting items: 75, 53, 73, 52, 75
  Operation: new = old * 7
  Test: divisible by 2
    If true: throw to monkey 0
    If false: throw to monkey 4

Monkey 7:
  Starting items: 94, 60, 79
  Operation: new = old + 2
  Test: divisible by 3
    If true: throw to monkey 1
    If false: throw to monkey 6"""
