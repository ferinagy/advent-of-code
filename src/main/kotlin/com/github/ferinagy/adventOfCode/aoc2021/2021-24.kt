package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import java.util.*

fun main() {
    val input = readInputLines(2021, "24-input")

    println("Part1:")
    part1(input).println()

    println()
    println("Part2:")
    part2(input).println()
}

private fun part1(input: List<String>): Long {
    val opposites = getOpposites(input)

    val result = Array(14) { 0 }
    opposites.forEach { (index1, val1, index2, val2) ->
        if (val1 < val2) {
            result[index1] = 9
            result[index2] = 9 - (val2 - val1)
        } else {
            result[index2] = 9
            result[index1] = 9 - (val1 - val2)
        }
    }

    return result.joinToString(separator = "").toLong()
}

private fun part2(input: List<String>): Long {
    val opposites = getOpposites(input)

    val result = Array(14) { 0 }
    opposites.forEach { (index1, val1, index2, val2) ->
        if (val1 < val2) {
            result[index1] = 1 + (val2 - val1)
            result[index2] = 1
        } else {
            result[index2] = 1 + (val1 - val2)
            result[index1] = 1
        }
    }

    return result.joinToString(separator = "").toLong()
}

private fun getOpposites(input: List<String>): MutableList<Opposites> {
    val ops = input.windowed(18, step = 18).map {
        val willAdd = it[4].endsWith('1')
        val previousDiff = it[5].split(' ')[2].toInt()
        val thisDiff = it[15].split(' ')[2].toInt()

        InputOp(willAdd, if (willAdd) thisDiff else -previousDiff)
    }

    val stack = LinkedList<IndexedValue<InputOp>>()
    val opposites = mutableListOf<Opposites>()
    ops.withIndex().forEach {
        if (it.value.add) {
            stack.addFirst(it)
        } else {
            val other = stack.removeFirst()
            opposites += Opposites(it.index, it.value.value, other.index, other.value.value)
        }
    }
    return opposites
}

private data class InputOp(val add: Boolean, val value: Int)

private data class Opposites(val index1: Int, val diff1: Int, val index2: Int, val diff2: Int)
