package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.CharGrid
import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.contains
import com.github.ferinagy.adventOfCode.get
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText
import kotlin.math.abs

fun main() {
    val input = readInputText(2022, "22-input")
    val testInput1 = readInputText(2022, "22-test1")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(testInput1, 4).println()
    part2(input, 50).println()
}

private fun part1(input: String): Int {
    return solve(input, 0, ::nextPosition2d)
}
private fun part2(input: String, size: Int): Int {
    return solve(input, size, ::nextPosition3d)
}

private fun solve(
    input: String,
    size: Int,
    newPosition: (Coord2D, Coord2D, CharGrid, Int) -> Pair<Coord2D, Coord2D>
): Int {
    val (map, path) = input.split("\n\n")
    val lines = map.lines()
    val width = lines.maxOf { it.length }
    val grid = CharGrid(width, lines.size) { x, y -> lines[y].getOrElse(x) { ' ' } }

    val instructions = path.split("(?=[RL])".toRegex()).mapIndexed { index, s -> if (index == 0) "R$s" else s }

    var position = Coord2D(x = grid.xRange.first { grid[it, 0] != ' ' }, y = 0)
    var direction = Coord2D(x = 0, y = -1)
    instructions.forEach { instruction ->
        direction = if (instruction.first() == 'R') {
            direction.rotate(1, 1)
        } else {
            direction.rotate(3, 1)
        }
        val steps = instruction.drop(1).toInt()

        for (i in 1..steps) {
            val (newPosition, newDirection) = newPosition(position, direction, grid, size)

            if (grid[newPosition] == '#') break
            position = newPosition
            direction = newDirection
        }
    }

    return result(position, direction)
}

private fun nextPosition2d(position: Coord2D, direction: Coord2D, grid: CharGrid, size: Int): Pair<Coord2D, Coord2D> {
    var newPosition = (position + direction).wrapIn(grid)
    while (grid[newPosition] == ' ') newPosition = (newPosition + direction).wrapIn(grid)
    return newPosition to direction
}

private fun nextPosition3d(position: Coord2D, direction: Coord2D, grid: CharGrid, size: Int): Pair<Coord2D, Coord2D> {
    var newPosition = position + direction
    var newDirection = direction
    if (newPosition !in grid || grid[newPosition] == ' ') {
        data class Config(val rotate: Int, val final: Coord2D, val path: List<Coord2D>)

        val onFace = Coord2D(x = position.x % size, y = position.y % size)
        val right = direction.rotate(1, 1)

        val configs = listOf(
            Config(1, Coord2D(0, 1), listOf(Coord2D(1, 0), Coord2D(1, 1))),
            Config(3, Coord2D(0, 1), listOf(Coord2D(-1, 0), Coord2D(-1, 1))),
            Config(2, Coord2D(-2, 2), listOf(Coord2D(-1, 0), Coord2D(-2, 0), Coord2D(-2, 1))),
            Config(2, Coord2D(2, 0), listOf(Coord2D(0, -1), Coord2D(1, -1), Coord2D(2, -1))),
            Config(1, Coord2D(-2, -3), listOf(Coord2D(0, -1), Coord2D(0, -2), Coord2D(-1, -2), Coord2D(-1, -3))),
            Config(
                0,
                Coord2D(-2, -4),
                listOf(Coord2D(0, -1), Coord2D(-1, -1), Coord2D(-1, -2), Coord2D(-1, -3), Coord2D(-2, -3))
            ),
            Config(
                0,
                Coord2D(-2, -4),
                listOf(Coord2D(-1, 0), Coord2D(-1, -1), Coord2D(-1, -2), Coord2D(-2, -2), Coord2D(-2, -3))
            ),
            Config(3, Coord2D(4, -1), listOf(Coord2D(1, 0), Coord2D(1, -1), Coord2D(2, -1), Coord2D(3, -1))),
        )

        val config = configs.single { (_, _, path) ->
            path.all {
                val check = position + (direction * size * it.y) + (right * size * it.x)
                check in grid && grid[check] != ' '
            }
        }

        val newDir = direction.rotate(config.rotate, 1)
        val emptyNeighbor = position + (direction * size * config.final.y) + (right * size * config.final.x)
        val pair = emptyNeighbor - onFace + onFace.rotate(config.rotate, size) + newDir to newDir

        newPosition = pair.first
        newDirection = pair.second
    }
    return Pair(newPosition, newDirection)
}

private fun result(position: Coord2D, direction: Coord2D): Int {
    val dir = abs(direction.x) * (1 - direction.x) + abs(direction.y) * (2 - direction.y)
    return (position.y + 1) * 1000 + (position.x + 1) * 4 + dir
}

private fun Coord2D.wrapIn(grid: CharGrid) =
    if (grid.width <= x || y <= grid.height) Coord2D(x.mod(grid.width), y.mod(grid.height)) else this

private fun Coord2D.rotate(times: Int, size: Int): Coord2D =
    if (times == 0) this else Coord2D(x = size - 1 - y, y = x).rotate(times - 1, size)
