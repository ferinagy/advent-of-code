package com.github.ferinagy.adventOfCode.aoc2016

import com.github.ferinagy.adventOfCode.subSets
import java.util.*
import kotlin.system.measureTimeMillis

fun main() {
    println("Part1:")
    val time1 = measureTimeMillis {
        println(part1(testInput1))
        println(part1(input))
    }
    println("took $time1")

    println()
    println("Part2:")
    val time2 = measureTimeMillis {
        println(part2(testInput1))
        println(part2(input))
    }
    println("took $time2")
}

private fun part1(input: String): Int {
    return searchGraph(
        start = RtgState.parse(input),
        isDone = RtgState::isDone,
        nextSteps = RtgState::nextSteps,
    )
}

private fun part2(input: String): Int {
    val rtgState = RtgState.parse(input)
    val nextInput = rtgState.copy(
        elementCount = rtgState.elementCount + 2,
        floors = rtgState.floors.mapIndexed { index, bitSet ->
            if (index != 0) bitSet else bitSet.also { it.set(rtgState.elementCount * 2, rtgState.elementCount * 2 + 4) }
        }
    )
    return searchGraph(
        start = nextInput,
        isDone = RtgState::isDone,
        nextSteps = RtgState::nextSteps,
    )
}

private data class RtgState(
    val elevator: Int = 0,
    val elementCount: Int,
    val floors: List<BitSet>
) {

    companion object {
        fun parse(input: String): RtgState {
            val elements = regex.findAll(input)
                .map { it.groupValues[1] }
                .distinct()
                .withIndex()
                .map { it.value to it.index }
                .toMap()

            val floors = input.lines().map { line ->
                val bitSet = BitSet(elements.size * 2)
                regex.findAll(line).map { it.destructured }.forEach { (e, t) ->
                    val index = elements[e]!!
                    if (t == " generator") {
                        bitSet.set(index * 2)
                    } else {
                        bitSet.set(index * 2 + 1)
                    }
                }
                bitSet
            }
            return RtgState(floors = floors, elementCount = elements.size)
        }
    }

    override fun toString(): String = buildString {
        floors.asReversed().forEachIndexed { index, floor ->
            append('F').append(floors.size - index).append(' ')
            if (elevator == floors.lastIndex - index) append("E") else append('.')

            append(' ')
            append(floor)

            append("\n")
        }
    }

    fun isDone(): Boolean = floors.dropLast(1).all { it.isEmpty() }

    fun nextSteps(): Set<RtgState> = buildSet {
        val current = floors[elevator]
        val toMove = current.let { it.subSets(1) + it.subSets(2) }.filter { it.isValid() }

        toMove.forEach { moving ->
            val nextCurrent = current.clone().let { it as BitSet }.also { it.andNot(moving) }

            fun move(to: Int) {
                if (to in floors.indices) {
                    val target = floors[to]
                    val nextTarget = target.clone().let { it as BitSet }.also { it.or(moving) }
                    if (nextCurrent.isValid() && nextTarget.isValid()) this += RtgState(
                        elevator = to,
                        elementCount = elementCount,
                        floors = floors.mapIndexed { index, floor ->
                            when (index) {
                                elevator -> nextCurrent
                                to -> nextTarget
                                else -> floor
                            }
                        }
                    )
                }
            }

            move(elevator.inc())
            move(elevator.dec())
        }
    }

    private fun BitSet.isValid(): Boolean {
        var index = nextSetBit(0)
        var hasUnshieldedChip = false
        var hasGenerator = false
        while (index != -1) {
            if (index % 2 == 0) {
                hasGenerator = true
            } else {
                if (!get(index - 1)) hasUnshieldedChip = true
            }
            index = nextSetBit(index + 1)
        }

        return !hasGenerator || !hasUnshieldedChip
    }
}

private val regex = """(\w+)(-compatible microchip| generator)""".toRegex()

private const val testInput1 =
    """The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.
The second floor contains a hydrogen generator.
The third floor contains a lithium generator.
The fourth floor contains nothing relevant."""

private const val input =
    """The first floor contains a thulium generator, a thulium-compatible microchip, a plutonium generator, and a strontium generator.
The second floor contains a plutonium-compatible microchip and a strontium-compatible microchip.
The third floor contains a promethium generator, a promethium-compatible microchip, a ruthenium generator, and a ruthenium-compatible microchip.
The fourth floor contains nothing relevant."""

private fun <T : Any> searchGraph(
    start: T,
    isDone: (T) -> Boolean,
    nextSteps: (T) -> Set<T>,
    computePath: Boolean = false,
    heuristic: (T) -> Int = { 0 }
): Int {
    val dists = mutableMapOf(start to 0)
    val queue = PriorityQueue(compareBy<T> { dists[it]!! + heuristic(it) })
    queue += start

    val paths = mutableMapOf<T, T>()
    while (queue.isNotEmpty()) {
        val current: T = queue.remove()

        val dist = dists[current]!!

        if (isDone(current)) {
            if (computePath) {
                reconstructPath(current, paths).forEach { println(it) }
            }
            return dist
        }

        for (next in nextSteps(current)) {
            val nextDist = dist + 1

            if (dists[next] == null) {
                dists[next] = nextDist

                queue += next

                if (computePath) paths[next] = current
            } else if (nextDist < dists[next]!!) {
                dists[next] = nextDist

                queue -= next
                queue += next

                if (computePath) paths[next] = current
            }
        }
    }

    return -1
}

private fun <T> reconstructPath(end: T, cameFrom: Map<T, T>): List<T> = buildList<T> {
    var current: T? = end
    while (current != null) {
        this += current
        current = cameFrom[current]
    }
}.asReversed()