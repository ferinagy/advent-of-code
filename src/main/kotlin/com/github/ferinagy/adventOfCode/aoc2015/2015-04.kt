package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.md5toBytes
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText
import com.github.ferinagy.adventOfCode.startsWithZeros

fun main() {
    val input = readInputText(2015, "04-input")
    val test1 = readInputText(2015, "04-test1")

    println("Part1:")
    starsWithZeros(test1, 5).println()
    starsWithZeros(input, 5).println()

    println()
    println("Part2:")
    starsWithZeros(test1, 6).println()
    starsWithZeros(input, 6).println()
}

private fun starsWithZeros(input: String, num: Int): Int {
    var n = 1
    while (true) {
        val hash = (input + n).md5toBytes()

        if (hash.startsWithZeros(num)) return n

        n++
    }
}
