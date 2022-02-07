package com.github.ferinagy.adventOfCode.aoc2016

import com.github.ferinagy.adventOfCode.subSets
import java.util.*

fun main() {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput1))
    println(part2(input))
}

private fun part1(input: RtgState): Int {
    return searchGraph(start = input, isDone = RtgState::isDone, nextSteps = RtgState::nextSteps, computePath = true)
}

private fun part2(input: RtgState): Int {
    val nextInput = input.copy(
        floors = input.floors.mapIndexed { index, floor ->
            if (index == 0) {
                floor + listOf(
                    RtgState.Component.Chip("El"),
                    RtgState.Component.Generator("El"),
                    RtgState.Component.Chip("Di"),
                    RtgState.Component.Generator("Di"),
                )
            } else floor
        }
    )
    return searchGraph(
        start = nextInput,
        isDone = RtgState::isDone,
        nextSteps = RtgState::nextSteps,
        computePath = true
    )
}

private data class RtgState(val elevator: Int = 0, val floors: List<Set<Component>>) {

    override fun toString(): String = buildString {
        floors.asReversed().forEachIndexed { index, floor ->
            append('F').append(floors.size - index).append(' ')
            if (elevator == floors.lastIndex - index) append("E") else append('.')

            append(' ')
            append(floor)

            append("\n")
        }
    }

    sealed class Component {
        abstract val type: String

        data class Chip(override val type: String) : Component() {
            override fun toString() = "$type chip"
        }

        data class Generator(override val type: String) : Component() {
            override fun toString() = "$type generator"
        }
    }

    fun isDone(): Boolean = floors.dropLast(1).all { it.isEmpty() }

    fun nextSteps(): Set<RtgState> = buildSet {
        val current = floors[elevator]
        val toMove = current.let { it.subSets(1) + it.subSets(2) }.filter { it.isValid() }

        toMove.forEach {
            val nextCurrent = current - it

            fun move(to: Int) {
                if (to in floors.indices) {
                    val target = floors[to]
                    val nextTarget = target + it
                    if (nextCurrent.isValid() && nextTarget.isValid()) this += RtgState(
                        elevator = to,
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

    private fun Set<Component>.isValid() = none { it is Component.Chip } ||
            none { it is Component.Generator } ||
            all { it !is Component.Chip || Component.Generator(it.type) in this }
}

private val testInput1 = RtgState(
    floors = listOf(
        setOf(RtgState.Component.Chip("H"), RtgState.Component.Chip("L")),
        setOf(RtgState.Component.Generator("H")),
        setOf(RtgState.Component.Generator("L")),
        setOf(),
    )
)
//The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.
//The second floor contains a hydrogen generator.
//The third floor contains a lithium generator.
//The fourth floor contains nothing relevant.

private val input = RtgState(
    floors = listOf(
        setOf(
            RtgState.Component.Chip("Tm"),
            RtgState.Component.Generator("Tm"),
            RtgState.Component.Generator("Pu"),
            RtgState.Component.Generator("Sr")
        ),
        setOf(RtgState.Component.Chip("Pu"), RtgState.Component.Chip("Sr")),
        setOf(
            RtgState.Component.Generator("Pm"),
            RtgState.Component.Chip("Pm"),
            RtgState.Component.Generator("Ru"),
            RtgState.Component.Chip("Ru")
        ),
        setOf(),
    )
)
//The first floor contains a thulium generator, a thulium-compatible microchip, a plutonium generator, and a strontium generator.
//The second floor contains a plutonium-compatible microchip and a strontium-compatible microchip.
//The third floor contains a promethium generator, a promethium-compatible microchip, a ruthenium generator, and a ruthenium-compatible microchip.
//The fourth floor contains nothing relevant.

private fun <T : Any> searchGraph(
    start: T,
    isDone: (T) -> Boolean,
    nextSteps: (T) -> Set<T>,
    computePath: Boolean = false
): Int {
    val dists = mutableMapOf(start to 0)
    val queue = PriorityQueue<T>(compareBy { dists[it]!! })
    queue += start

    val paths = mutableMapOf(start to listOf(start))
    while (queue.isNotEmpty()) {
        val current: T = queue.first()

        val dist = dists[current]!!
        queue -= current

        if (isDone(current)) {
            paths[current]?.forEach {
                println(it)
            }
            return dist
        }

        for (next in nextSteps(current)) {
            val nextDist = dist + 1

            if (dists[next] == null) {
                dists[next] = nextDist
                queue += next
                if (computePath) paths[next] = paths[current]!! + next
            } else if (nextDist < dists[next]!!) {
                dists[next] = nextDist
                queue -= next
                queue += next
                if (computePath) paths[next] = paths[current]!! + next
            }
        }
    }

    return -1
}