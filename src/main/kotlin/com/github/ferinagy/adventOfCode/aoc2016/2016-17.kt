package com.github.ferinagy.adventOfCode.aoc2016

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.component1
import com.github.ferinagy.adventOfCode.component2
import com.github.ferinagy.adventOfCode.md5toBytes
import com.github.ferinagy.adventOfCode.searchGraph

fun main() {
    println("Part1:")
    println(part1())

    println()
    println("Part2:")
    println(part2("ihgpwlah"))
    println(part2("ulqzkmiv"))
    println(part2(input))
}

private fun part1(): String {
    val start = Coord2D(0, 0) to ""
    var result = "no way"
    searchGraph(
        start = start,
        isDone = { (position, path) ->
            val done = position.isVault()
            if (done) result = path
            done
        },
        nextSteps = { (position, path) -> openDoors(input, path, position) }
    )

    return result
}

private fun part2(input: String): Int {
    val start = Coord2D(0, 0) to ""
    var result = -1
    searchGraph(
        start = start,
        isDone = { (position, path) ->
            val done = position.isVault()
            if (done) result = path.length
            false
        },
        nextSteps = { (position, path) ->
            if (position.isVault()) emptySet() else openDoors(input, path, position)
        }
    )

    return result
}

private fun openDoors(input: String, path: String, position: Coord2D): Set<Pair<Coord2D, String>> {
    val (b1, b2) = (input + path).md5toBytes()
    val (u, d) = b1
    val (l, r) = b2
    val (lp, tp, rp, bp) = position.adjacent(includeDiagonals = false)

    return buildSet {
        if (lp.x in 0..3 && lp.y in 0..3 && 10 < l) this += lp to path + "L"
        if (rp.x in 0..3 && rp.y in 0..3 && 10 < r) this += rp to path + "R"
        if (tp.x in 0..3 && tp.y in 0..3 && 10 < u) this += tp to path + "U"
        if (bp.x in 0..3 && bp.y in 0..3 && 10 < d) this += bp to path + "D"
    }
}

private fun Coord2D.isVault() = x == 3 && y == 3

private const val input = """udskfozm"""
