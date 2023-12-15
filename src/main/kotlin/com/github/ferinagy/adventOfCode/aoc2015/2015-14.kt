package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import kotlin.math.min

fun main() {
    val input = readInputLines(2015, "14-input")
    val test1 = readInputLines(2015, "14-test1")

    println("Part1:")
    part1(test1, 1000).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1, 1000).println()
    part2(input).println()
}

private fun part1(input: List<String>, totalTime: Int = 2503): Int {
    val reindeer = input.map { Reindeer.parse(it) }

    return reindeer.maxOf {
        var traveled = 0
        var remainingTime = totalTime
        while (0 < remainingTime) {
            traveled += min(remainingTime, it.flyTime) * it.speed

            remainingTime -= it.flyTime + it.restTime
        }

        traveled
    }
}

private fun part2(input: List<String>, totalTime: Int = 2503): Int {
    val reindeer = input.map { Reindeer.parse(it) }

    val points = IntArray(reindeer.size)
    for (i in 1..totalTime) {
        reindeer.forEach { it.tick() }

        val max = reindeer.maxOfOrNull { it.dist }

        reindeer.forEachIndexed { index, r ->
            if (r.dist == max) points[index]++
        }
    }

    return points.maxOrNull()!!
}

private class Reindeer(val speed: Int, val flyTime: Int, val restTime: Int) {

    private var isFlying: Boolean = true
    private var remainingFlightTime: Int = flyTime
    private var remainingRestTime: Int = restTime

    var dist = 0
        private set

    fun tick(): Int {
        if (isFlying) {
            dist += speed
            remainingFlightTime--

            if (remainingFlightTime == 0) {
                isFlying = false
                remainingFlightTime = flyTime
            }
        } else {
            remainingRestTime--
            if (remainingRestTime == 0) {
                isFlying = true
                remainingRestTime = restTime
            }
        }

        return dist
    }

    companion object {
        fun parse(input: String): Reindeer {
            val (speed, flyTime, restTime) = regex.matchEntire(input)!!.destructured
            return Reindeer(speed.toInt(), flyTime.toInt(), restTime.toInt())
        }
    }
}

private val regex = """\w+ can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds.""".toRegex()
