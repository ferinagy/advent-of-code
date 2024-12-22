package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.*
import kotlin.math.absoluteValue

fun main() {
    val input = readInputLines(2024, "21-input")
    val test1 = readInputLines(2024, "21-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>) = solve(input, 2)

private fun part2(input: List<String>) = solve(input, 25)

private fun solve(input: List<String>, depth: Int) = input.sumOf { code ->
    val codeOptions = findNumPad(code)
    val shortest = codeOptions.sumOf { sequence ->
        sequence.minOf { calculate(depth, it, mutableMapOf()) }
    }
    shortest * code.dropLast(1).toInt()
}

private fun findNumPad(input: String): List<Set<String>> {
    val grid = listOf(
        "789",
        "456",
        "123",
        " 0A",
    ).toCharGrid()

    var pos = Coord2D(2, 3)
    val result = mutableListOf<Set<String>>()
    input.forEach { next ->
        val nextPos = grid.positionOfFirst { next == it }

        val dirs = getDirections(pos, nextPos).filter { isValid(it, grid, pos) }
        result += dirs.map { it + 'A' }.toSet()
        pos = nextPos
    }

    return result
}

private fun calculate(depth: Int, input: String, cache: MutableMap<Pair<String, Int>, Long>): Long {
    if (depth == 0) return input.length.toLong()

    cache[input to depth]?.let { return it }

    val parts = input.split("(?<=A)".toRegex())
    return parts.sumOf {
        val options = findDirectionPad(it)
        options.minOf { calculate(depth - 1, it, cache) }
    }.also {
        cache[input to depth] = it
    }
}

private fun findDirectionPad(input: String): Set<String> {
    var pos = Coord2D(2, 0)
    val grid = listOf(
        " ^A",
        "<v>",
    ).toCharGrid()

    var result = setOf("")
    input.forEach { next ->
        val nextPos = grid.positionOfFirst { next == it }

        val dirs = getDirections(pos, nextPos).filter { isValid(it, grid, pos) }

        result = result.flatMapTo(mutableSetOf()) { prev -> dirs.map { prev + it + 'A' } }
        pos = nextPos
    }

    return result
}

private fun getDirections(src: Coord2D, dst: Coord2D): Set<String> {
    var result = ""
    repeat((dst.x - src.x).absoluteValue) { result += if (dst.x > src.x) '>' else '<' }
    repeat((dst.y - src.y).absoluteValue) { result += if (dst.y > src.y) 'v' else '^' }

    return permutations(result)
}

private fun isValid(seq: String, grid: CharGrid, start: Coord2D): Boolean {
    var pos = start
    seq.forEach { next ->
        when (next) {
            '<' -> pos = pos.copy(x = pos.x - 1)
            '>' -> pos = pos.copy(x = pos.x + 1)
            '^' -> pos = pos.copy(y = pos.y - 1)
            'v' -> pos = pos.copy(y = pos.y + 1)
        }
        if (grid[pos] == ' ') return false
    }

    return true
}

private fun permutations(input: String): Set<String> {
    if (input.isEmpty()) return setOf("")

    return input.indices.flatMapTo(mutableSetOf()) { index ->
        permutations(input.removeRange(index, index + 1)).map { input[index] + it }
    }
}
