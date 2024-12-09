package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2024, "09-input")
    val test1 = readInputText(2024, "09-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: String): Long {
    val (files, _) = parse(input)

    val array = IntArray(input.sumOf { it.digitToInt() }) { -1 }
    files.forEach { (start, length, id) -> repeat(length) { array[start + it] = id } }

    var left = 0
    var right = array.lastIndex
    while (left <= right) {
        while (array[left] != -1) left++
        while (array[right] == -1) right--
        if (right < left) break
        array[left] = array[right]
        array[right] = -1
    }

    return array.mapIndexed { i, c -> if (c != -1) c.toLong() * i else 0 }.sum()
}

private fun part2(input: String): Long {
    val (files, spaces) = parse(input)

    files.indices.reversed().forEach { fileIndex ->
        val freeSpaceIndex = spaces.indexOfFirst { (pos,size) -> files[fileIndex].second <= size && pos < files[fileIndex].first }
        if (freeSpaceIndex != -1) {
            files[fileIndex] = files[fileIndex].copy(first = spaces[freeSpaceIndex].first)
            spaces[freeSpaceIndex] = spaces[freeSpaceIndex].first + files[fileIndex].second to spaces[freeSpaceIndex].second - files[fileIndex].second
        }
    }

    return checkSum(files)
}

private fun checkSum(files: MutableList<Triple<Int, Int, Int>>) =
    files.sumOf { (pos, len, id) -> (pos..<pos + len).sumOf { it * id.toLong() } }

private fun parse(input: String): Pair<MutableList<Triple<Int, Int, Int>>, MutableList<Pair<Int, Int>>> {
    var index = 0
    val files = mutableListOf<Triple<Int, Int, Int>>()
    val spaces = mutableListOf<Pair<Int, Int>>()
    input.map { it.digitToInt() }.windowed(2, 2, true).forEachIndexed { id, window ->
        val fileLength = window[0]
        files += Triple(index, fileLength, id)
        index += fileLength
        window.getOrNull(1)?.let {
            spaces += index to it
            index += it
        }
    }
    return files to spaces
}
