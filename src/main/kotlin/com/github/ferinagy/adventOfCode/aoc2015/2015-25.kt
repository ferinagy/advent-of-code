package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val (row, col) = readInputText(2015, "25-input").let(regex::matchEntire)!!.destructured

    println("Final:")
    part1(3, 4).println()
    part1(row.toInt(), col.toInt()).println()
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

private val regex = """To continue, please consult the code grid in the manual.  Enter the code at row (\d+), column (\d+).""".toRegex()
