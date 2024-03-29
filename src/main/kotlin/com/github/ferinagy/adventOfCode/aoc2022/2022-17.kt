package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.BooleanGrid
import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import com.github.ferinagy.adventOfCode.readInputText
import com.github.ferinagy.adventOfCode.toBooleanGrid
import kotlin.math.max

fun main() {
    val input = readInputText(2022, "17-input")
    val testInput1 = readInputText(2022, "17-test1")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(testInput1).println()
    part2(input).println()
}
private fun part1(input: String) = solve(input, 2022)

private fun part2(input: String) = solve(input, 1000000000000)

private class PyroclasticFlow(private val jets: String) {

    private val blocks = blocksTypes.split("\n\n").map { it.lines().toBooleanGrid { it == '#' } }

    private var type = 0
    private var jet = 0
    private var top = 0

    private var counter = 0L
    private val existing = mutableSetOf<Coord2D>()

    private val cache = mutableMapOf<State, Pair<Long, Int>>()

    private var offset = 0L

    private data class State(val blockType: Int, val jetIndex: Int, val tops: List<Int>)

    fun getTop() = top + offset

    fun dropBlocks(target: Long) {
        while (counter < target) {
            counter++

            val block = blocks[type]
            var position = Coord2D(2, top + 3 + block.height)

            val state = createState(position, type, jet)
            checkRepeatedState(state, target)

            while (true) {
                position = moveHorizontally(position, block)

                jet++
                jet %= jets.length

                val newPos = position.copy(y = position.y - 1)
                if (canFit(block, newPos)) {
                    position = newPos
                } else {
                    break
                }
            }

            addToExisting(block, position)

            top = max(position.y, top)

            type++
            type %= blocks.size
        }
    }

    private fun addToExisting(block: BooleanGrid, position: Coord2D) {
        for (x in block.xRange) {
            for (y in block.yRange) {
                if (block[x, y]) existing += Coord2D(x + position.x, position.y - y)
            }
        }
    }

    private fun moveHorizontally(position: Coord2D, block: BooleanGrid): Coord2D {
        if (jets[jet] == '<') {
            val newPos = position.copy(x = position.x - 1)
            if (canFit(block, newPos)) {
                return newPos
            }
        } else {
            val newPos = position.copy(x = position.x + 1)
            if (canFit(block, newPos)) {
                return newPos
            }
        }
        return position
    }

    private fun checkRepeatedState(state: State, target: Long) {
        if (offset == 0L && state in cache) {
            val (prevCounter, prevTop) = cache[state]!!

            val topDiff = top - prevTop
            val counterDiff = counter - prevCounter

            val r = (target - counter) / counterDiff

            counter += r * counterDiff
            offset = topDiff * r
        } else {
            cache[state] = counter to top
        }
    }

    private fun createState(position: Coord2D, type: Int, jet: Int): State {
        val tops = (0..6).map { x ->
            generateSequence(0) { it + 1 }.first {
                position.y - it < 0 || Coord2D(x, position.y - it) in existing
            }
        }
        return State(type, jet, tops)
    }

    private fun canFit(block: BooleanGrid, position: Coord2D): Boolean {
        for (x in block.xRange) {
            for (y in block.yRange) {
                if (position.x + x !in 0..6 || position.y - y <= 0 || (block[x, y] && Coord2D(position.x + x, position.y - y) in existing)) return false
            }
        }

        return true
    }
}

private fun solve(input: String, target: Long): Long {
    val flow = PyroclasticFlow(input)
    flow.dropBlocks(target)

    return flow.getTop()
}

private const val blocksTypes = """####

.#.
###
.#.

..#
..#
###

#
#
#
#

##
##"""
