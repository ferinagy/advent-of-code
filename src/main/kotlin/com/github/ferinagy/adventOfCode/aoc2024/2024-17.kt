package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2024, "17-input")
    val test1 = readInputLines(2024, "17-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(input).println()
}

private fun part1(input: List<String>): String {
    val a = input[0].substring(12).toLong()
    val p = input[4].substring(9).split(',').map { it.toInt() }

    return runProgram(a, p).joinToString(separator = ",")
}

private fun part2(input: List<String>): Long {
    val p = input[4].substring(9).split(',').map { it.toInt() }

    return search(p, 0L)!!
}

private fun search(program: List<Int>, acc: Long): Long? {
    if (program.isEmpty()) return acc

    val candidates = (0L..7L).filter { b1 ->
        val b2 = b1 xor 5
        val c = ((acc shl 3) or b1) shr b2.toInt()
        val b3 = b2 xor c
        val b4 = b3 xor 6
        (b4 % 8).toInt() == program.last()
    }

    val rest = program.dropLast(1)
    return candidates.firstNotNullOfOrNull { search(rest, acc shl 3 or it) }
}

private fun runProgram(initialA: Long, program: List<Int>): List<Int> {
    var a = initialA
    var b = 0L
    var c = 0L
    fun combo(n: Int): Long = when (n) {
        in 0..3 -> n.toLong()
        4 -> a
        5 -> b
        6 -> c
        else -> error("bad combo")
    }

    fun pow2(n: Long): Int = 1 shl n.toInt()

    var ip = 0
    val output = mutableListOf<Int>()
    while (ip in program.indices) {
        val op = program[ip]
        when (op) {
            0 -> a /= pow2(combo(program[ip + 1]))
            1 -> b = b xor program[ip + 1].toLong()
            2 -> b = combo(program[ip + 1]) % 8
            3 -> if (a != 0L) {
                ip = program[ip + 1]
                continue
            }

            4 -> b = b xor c
            5 -> output += (combo(program[ip + 1]) % 8).toInt()
            6 -> b = a / pow2(combo(program[ip + 1]))
            7 -> c = a / pow2(combo(program[ip + 1]))
        }
        ip += 2
    }
    return output
}
