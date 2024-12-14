package com.github.ferinagy.adventOfCode.aoc2024

import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2024, "14-input")
    val test1 = readInputLines(2024, "14-test1")

    println("Part1:")
    part1(test1, 11, 7).println()
    part1(input, 101, 103).println()

    println()
    println("Part2:")
    part2(input, 101, 103).println()
}

private fun part1(input: List<String>, width: Int, height: Int) = parse(input)
    .map { (pos, dir) -> move(pos, dir, width, height, 100) }
    .filter { it.x != width / 2 && it.y != height / 2 }
    .partition { it.x < width / 2 }
    .toList()
    .flatMap { it.partition { it.y < height / 2 }.toList() }
    .fold(1) { acc, next -> acc * next.size }

private fun move(
    pos: Coord2D,
    dir: Coord2D,
    width: Int,
    height: Int,
    count: Int,
) = Coord2D((pos.x + (dir * count).x).mod(width), (pos.y + (dir * count).y).mod(height))

private fun part2(input: List<String>, width: Int, height: Int): Int {
    var robots = parse(input)

    var count = 0
    while (true) {
        robots = robots.map { (pos, dir) -> move(pos, dir, width, height, 1) to dir }
        count++
        if (robots.size == robots.distinctBy { it.first }.size) {
            return count
        }
    }
}

private fun parse(input: List<String>): List<Pair<Coord2D, Coord2D>> {
    fun parsePair(line: String) = line.drop(2).split(',').let { (x, y) -> Coord2D(x.toInt(), y.toInt()) }

    return input.map { line ->
        val (pos, dir) = line.split(' ')
        parsePair(pos) to parsePair(dir)
    }

}