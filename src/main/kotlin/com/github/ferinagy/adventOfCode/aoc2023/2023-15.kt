package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2023, "15-input")
    val test1 = readInputText(2023, "15-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: String): Int {
    val list = input.split(',')
    return list.sumOf(::hash)
}

private fun part2(input: String): Int {
    val list = input.split(',')

    val boxes = Array(256) { mutableListOf<Pair<String, Int>>() }
    list.forEach { instruction ->
        val label = instruction.takeWhile { it.isLetter() }
        val op = instruction.dropWhile { it.isLetter() }
        when (op[0]) {
            '-' -> {
                val hash = hash(label)
                boxes[hash].removeAll { it.first == label }
            }
            '=' -> {
                val focal = op.drop(1).toInt()
                val hash = hash(label)
                val position = boxes[hash].indexOfFirst { it.first == label }
                if (position == -1) boxes[hash] += label to focal else boxes[hash][position] = label to focal
            }
        }
    }

    return boxes.withIndex().sumOf { (box, lenses) ->
        (box + 1) * lenses.withIndex().sumOf { (it.index + 1) * it.value.second }
    }
}

private fun hash(input: String) = input.fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }
