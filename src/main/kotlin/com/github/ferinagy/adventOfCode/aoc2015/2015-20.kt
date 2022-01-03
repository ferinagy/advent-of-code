package com.github.ferinagy.adventOfCode.aoc2015

fun main(args: Array<String>) {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput1))
    println(part2(input))
}

private fun part1(input: Int): Int {
    val houses = IntArray(1000000)

    for (elf in 1 .. houses.size) {
        for (house in (elf - 1) .. houses.lastIndex step elf) {
            houses[house] = houses[house] + elf * 10
        }
    }
    val index = houses.indexOfFirst { input <= it }

    return index + 1
}

private fun part2(input: Int): Int {
    val houses = IntArray(1000000)

    for (elf in 1 .. houses.size) {
        for (house in ((elf - 1) .. houses.lastIndex step elf).take(50)) {
            houses[house] = houses[house] + elf * 11
        }
    }
    val index = houses.indexOfFirst { input <= it }

    return index + 1
}

private const val testInput1 = 100

private const val input = 33100000
