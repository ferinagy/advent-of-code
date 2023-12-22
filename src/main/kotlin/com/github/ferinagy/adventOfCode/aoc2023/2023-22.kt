package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import java.util.LinkedList
import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readInputLines(2023, "22-input")
    val test1 = readInputLines(2023, "22-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val mapped = parseAndSettle(input)

    return mapped.count { (_, brick) -> brick.supports.all { 1 < mapped[it]!!.supportedBy.size } }
}

private fun part2(input: List<String>): Int {
    val mapped = parseAndSettle(input)

    return mapped.keys.sumOf { disintegrate(it, mapped) }
}

private fun parseAndSettle(input: List<String>): Map<Int, Brick> {
    var id = 0
    val bricks = input.map { Brick.parse(it, id++) }.sortedBy { it.bottom }

    val settled = mutableListOf<Brick>()
    bricks.forEach { brick ->
        var offset = 0
        while (brick.canDrop(offset + 1, settled)) {
            offset++
        }
        val moved = brick.copyWithOffset(offset)
        val supportedBy = settled.filter { moved.intersects(it, 1) }
        supportedBy.forEach {
            it.supports += moved.id
            moved.supportedBy += it.id
        }
        settled += moved
    }

    return settled.associateBy { it.id }
}

private fun disintegrate(id: Int, bricks: Map<Int, Brick>): Int {
    val queue = LinkedList<Int>()
    queue += id
    val destroyed = mutableSetOf<Int>()
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        val brick = bricks[current]!!
        destroyed += current

        val next = brick.supports.filter {
            val b = bricks[it]!!
            (b.supportedBy - destroyed).isEmpty()
        }
        queue += next
    }

    return destroyed.size - 1
}

private data class Brick(
    val id: Int,
    val xRange: IntRange,
    val yRange: IntRange,
    val zRange: IntRange,
    val supports: MutableSet<Int> = mutableSetOf(),
    val supportedBy: MutableSet<Int> = mutableSetOf()
) {

    val bottom = zRange.first

    fun canDrop(offset: Int, settled: List<Brick>): Boolean {
       return 0 < bottom - offset && settled.none { intersects(it, offset) }
    }

    fun intersects(other: Brick, zOffset: Int): Boolean {
        val zRange = zRange.first - zOffset .. zRange.last - zOffset

        return !(xRange intersect other.xRange).isEmpty() &&
                !(yRange intersect other.yRange).isEmpty() &&
                !(zRange intersect other.zRange).isEmpty()
    }

    fun copyWithOffset(zOffset: Int) = copy(zRange = zRange.first - zOffset .. zRange.last - zOffset)

    companion object {
        fun parse(text: String, id: Int) =
            text.split('~').let { (c1, c2) ->
                val (x1, y1, z1) = c1.split(',').map { it.toInt() }
                val (x2, y2, z2) = c2.split(',').map { it.toInt() }
                Brick(id, x1 .. x2, y1 .. y2, z1 ..z2)
            }
    }
}

private infix fun IntRange.intersect(other: IntRange): IntRange = max(first, other.first) .. min(last, other.last)
