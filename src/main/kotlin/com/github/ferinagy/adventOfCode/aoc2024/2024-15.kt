package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2024, "15-input")
    val test1 = readInputText(2024, "15-test1")
    val test2 = readInputText(2024, "15-test2")

    println("Part1:")
    part1(test1).println()
    part1(test2).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(test2).println()
    part2(input).println()
}

private fun parse(input: String, widthCoeficient: Int): Pair<Warehouse, String> {
    val (area, movesLines) = input.split("\n\n")
    val lines = area.lines()
    val height = lines.size
    val width = lines.first().length
    val walls = mutableSetOf<Coord2D>()
    val crates = mutableSetOf<Coord2D>()
    lateinit var robot: Coord2D
    val moves = movesLines.replace("\n", "")

    for (x in 0..<width) {
        for (y in 0..<height) {
            if (lines[y][x] == '#') walls += Coord2D(x * widthCoeficient, y)
            if (lines[y][x] == 'O') crates += Coord2D(x * widthCoeficient, y)
            if (lines[y][x] == '@') robot = Coord2D(x * widthCoeficient, y)
        }
    }

    return Warehouse(width * widthCoeficient, height, robot, crates, walls) to moves
}

private data class Warehouse(
    val width: Int,
    val height: Int,
    var robot: Coord2D,
    val crates: MutableSet<Coord2D>,
    val walls: Set<Coord2D>,
)

private fun Char.toDir() = when (this) {
    '<' -> Coord2D(-1, 0)
    '>' -> Coord2D(1, 0)
    'v' -> Coord2D(0, 1)
    '^' -> Coord2D(0, -1)
    else -> error("bad dir")
}

private fun part1(input: String) = solve(input, 1)

private fun part2(input: String) = solve(input, 2)

private fun solve(input: String, width: Int): Int {
    val (warehouse, moves) = parse(input, width)

    moves.forEach {
        val dir = it.toDir()
        val toConsider = (-width + 1..0).map { Coord2D(it, 0) + warehouse.robot + dir }
        when {
            toConsider.any { it in warehouse.walls } -> Unit
            toConsider.any { it in warehouse.crates } -> {
                val crate = toConsider.intersect(warehouse.crates).single()
                if (canMoveCrate(crate, dir, warehouse.crates, warehouse.walls, width)) {
                    moveCrates(crate, dir, warehouse.crates, warehouse.walls, width)
                    warehouse.robot += dir
                }
            }
            else -> warehouse.robot += dir
        }
    }

    return warehouse.crates.sumOf { it.x + it.y * 100 }
}

private fun canMoveCrate(crate: Coord2D, dir: Coord2D, crates: Set<Coord2D>, walls: Set<Coord2D>, width: Int): Boolean {
    val offsets = (-width + 1..<width).map { Coord2D(it, 0) }
    val next = if (dir.y == 0) listOf(crate + dir * width) else offsets.map { it + crate + dir }

    if (next.any { it in walls }) return false

    return next.intersect(crates).all { canMoveCrate(it, dir, crates, walls, width) }
}

private fun moveCrates(crate: Coord2D, dir: Coord2D, crates: MutableSet<Coord2D>, walls: Set<Coord2D>, width: Int) {
    val offsets = (-width + 1..<width).map { Coord2D(it, 0) }
    val next = if (dir.y == 0) listOf(crate + dir * width) else offsets.map { it + crate + dir }

    next.intersect(crates).forEach { moveCrates(it, dir, crates, walls, width) }

    crates -= crate
    crates += crate + dir
}
