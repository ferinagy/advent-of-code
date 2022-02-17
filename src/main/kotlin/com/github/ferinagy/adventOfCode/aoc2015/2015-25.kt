package com.github.ferinagy.adventOfCode.aoc2015

fun main() {
    println("Final:")
    println(part1(3, 4))
    println(part1(2978, 3083))
}

private fun part1(row: Int, col: Int): Long {
    val sum = col * (col+1) / 2 + row * (row - 1) / 2 + (row - 1) * (col - 1)

    return calculateCode(sum)
}

private fun calculateCode(n: Int): Long {
    var result = 20151125L

    for (i in 1 until n) {
        result *= 252533
        result %= 33554393
    }

    return result
}
