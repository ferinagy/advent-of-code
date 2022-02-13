package com.github.ferinagy.adventOfCode.aoc2016

import java.io.File

fun main() {
    val input = File("src", "com.github.ferinagy.adventOfCode.aoc2015.com.github.ferinagy.adventOfCode.aoc2015.com.github.ferinagy.adventOfCode.aoc2015.input-2016-07.txt").readText()

    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput1))
    println(part2(input))
}

private fun part1(input: String) = input.lines().count { it.supportsV7Tls() }

private fun part2(input: String) = input.lines().count { it.supportsV7Ssl() }

private fun String.supportsV7Tls(): Boolean {
    val (hyper, normal) = split('[')
        .mapIndexed { index, s -> if (index == 0) listOf("", s) else s.split(']') }
        .fold(emptyList<String>() to emptyList<String>()) { (l1, l2), (a1, a2) -> l1 + a1 to l2 + a2 }

    return normal.any { it.hasAbba() } && hyper.none { it.hasAbba() }
}

private fun String.supportsV7Ssl(): Boolean {
    val (hyper, normal) = split('[')
        .mapIndexed { index, s -> if (index == 0) listOf("", s) else s.split(']') }
        .fold(emptyList<String>() to emptyList<String>()) { (l1, l2), (a1, a2) -> l1 + a1 to l2 + a2 }

    val abas = normal.flatMap { it.getAbas() }

    return abas.any { aba -> hyper.any { aba.flipAba() in it } }
}

private fun String.hasAbba(): Boolean = (0..length - 4).any { isAbba(it) }

private fun String.isAbba(start: Int): Boolean =
    get(start) != get(start + 1) && get(start + 1) == get(start + 2) && get(start) == get(start + 3)

private fun String.getAbas(): List<String> = (0..length - 3).filter { isAba(it) }.map { substring(it, it + 3) }

private fun String.isAba(start: Int): Boolean =
    get(start) != get(start + 1) && get(start) == get(start + 2)

private fun String.flipAba() = get(1).toString() + get(0) + get(1)

private const val testInput1 = """abba[mnop]qrst
abcd[bddb]xyyx
aaaa[qwer]tyui
ioxxoj[asdfgh]zxcvbn"""
