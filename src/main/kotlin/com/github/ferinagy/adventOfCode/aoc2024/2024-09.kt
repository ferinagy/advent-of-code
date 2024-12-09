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
    part2(input).println()
}

private fun part1(input: String): Long {
    val (files, spaces) = parse(input)

    while (spaces.isNotEmpty()) {
        val space = spaces.removeFirst()
        var remaining = space.second
        var dest = space.first
        while (remaining != 0) {
            if (files.last().first < dest) break

            val file = files.removeLast()
            val moving = minOf(file.second, remaining)
            files.add(0, Triple(dest, moving, file.third))
            dest += moving
            remaining -= moving
            val left = file.second - moving
            if (left != 0) files.add(file.copy(second = left))
        }
    }
    return checkSum(files)
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
