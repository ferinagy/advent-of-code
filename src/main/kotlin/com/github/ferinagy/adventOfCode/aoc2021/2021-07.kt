package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText
import kotlin.math.abs

fun main() {
    val input = readInputText(2021, "07-input")
    val test1 = readInputText(2021, "07-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: String): Int {
    return calculateCrabPosition(input, fuelCaclulation = { it })
}

private fun part2(input: String): Int {
    val fuelTable = Array(2000) { 0 }
    for (i in 1..fuelTable.lastIndex) {
        fuelTable[i] = fuelTable[i - 1] + i
    }

    return calculateCrabPosition(input, fuelCaclulation = { fuelTable[it] })
}

private fun calculateCrabPosition(input: String, fuelCaclulation: (Int) -> Int): Int {
    val list = input.split(",").map { it.toInt() }

    val min = list.minOrNull()!!
    val max = list.maxOrNull()!!

    return (min..max).map { position ->
        list.sumOf { fuelCaclulation(abs(it - position)) }
    }.minOrNull()!!
}
