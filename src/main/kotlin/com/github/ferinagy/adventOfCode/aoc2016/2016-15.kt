package com.github.ferinagy.adventOfCode.aoc2016

fun main() {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2())
}

private fun part1(input: String): Int {
    val disks = input.lines().map {
        val (number, positions, initial) =  regex.matchEntire(it)!!.destructured
        Disk(number.toInt(), positions.toInt(), initial.toInt())
    }

    var time = 0
    while (true) {
        if (disks.all { (it.initial + time + it.number) % it.positions == 0 }) return time

        time++
    }
}

private fun part2(): Int {
    val disks = input.lines().map {
        val (number, positions, initial) =  regex.matchEntire(it)!!.destructured
        Disk(number.toInt(), positions.toInt(), initial.toInt())
    } + Disk(7, 11, 0)

    var time = 0
    while (true) {
        if (disks.all { (it.initial + time + it.number) % it.positions == 0 }) return time

        time++
    }
}

private data class Disk(val number: Int, val positions: Int, val initial: Int)

private val regex = """Disc #(\d) has (\d+) positions; at time=0, it is at position (\d+).""".toRegex()

private const val testInput1 = """Disc #1 has 5 positions; at time=0, it is at position 4.
Disc #2 has 2 positions; at time=0, it is at position 1."""

private const val input = """Disc #1 has 7 positions; at time=0, it is at position 0.
Disc #2 has 13 positions; at time=0, it is at position 0.
Disc #3 has 3 positions; at time=0, it is at position 2.
Disc #4 has 5 positions; at time=0, it is at position 2.
Disc #5 has 17 positions; at time=0, it is at position 0.
Disc #6 has 19 positions; at time=0, it is at position 7."""
