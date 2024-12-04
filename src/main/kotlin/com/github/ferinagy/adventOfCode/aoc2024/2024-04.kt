package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2024, "04-input")
    val test1 = readInputLines(2024, "04-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val dirs = Coord2D(0, 0).adjacent(includeDiagonals = true)
    val word = "XMAS"

    return input.foldIndexed(0) { y, acc1, row ->
        acc1 + row.foldIndexed(0) { x, acc2, _ ->
            acc2 + dirs.count { (dx, dy) ->
                x + dx * word.lastIndex in row.indices && y + dy * word.lastIndex in input.indices
                        && word.indices.all { input[y + dy * it][x + dx * it] == word[it] }
            }
        }
    }
}

private fun part2(input: List<String>): Int = input.foldIndexed(0) { y, acc1, row ->
    acc1 + row.foldIndexed(0) { x, acc2, _ -> acc2 + if (isCross(input, Coord2D(x, y))) 1 else 0 }
}

private fun isCross(input: List<String>, pos: Coord2D): Boolean {
    if (pos.x == 0 || pos.x == input.first().lastIndex || pos.y == 0 || pos.y == input.lastIndex) return false

    val diags = setOf(setOf(Coord2D(-1, -1), Coord2D(1, 1)), setOf(Coord2D(-1, 1), Coord2D(1, -1)))

    return input[pos.y][pos.x] == 'A' && diags.all { diag ->
        diag.any { input[pos.y + it.y][pos.x + it.x] == 'M' && input[pos.y - it.y][pos.x - it.x] == 'S' }
    }
}
