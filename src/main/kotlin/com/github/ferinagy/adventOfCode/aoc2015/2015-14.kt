package com.github.ferinagy.adventOfCode.aoc2015

import kotlin.math.min

fun main(args: Array<String>) {
    println("Part1:")
    println(part1(input))

    println()
    println("Part2:")
    println(part2(input))
}

private fun part1(input: String): Int {
    val reindeer = input.lines().map { Reindeer.parse(it) }
    val totalTime = 2503

    val dists = reindeer.map {
        var traveled = 0
        var remainingTime = totalTime
        while (0 < remainingTime) {
            traveled += min(remainingTime, it.flyTime) * it.speed

            remainingTime -= it.flyTime + it.restTime
        }


        traveled
    }

    return dists.maxOrNull()!!
}

private fun part2(input: String): Int {
    val reindeer = input.lines().map { Reindeer.parse(it) }
    val totalTime = 2503

    val points = IntArray(reindeer.size)
    for (i in 1..totalTime) {
        reindeer.forEach { it.tick() }

        val dists = reindeer.map { it.dist }
        val max = dists.maxOrNull()

        reindeer.forEachIndexed { index, r ->
            if (r.dist == max) points[index]++
        }
    }

    return points.maxOrNull()!!
}

private class Reindeer(val name: String, val speed: Int, val flyTime: Int, val restTime: Int) {

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
            val (name, speed, flyTime, restTime) = regex.matchEntire(input)!!.destructured
            return Reindeer(name, speed.toInt(), flyTime.toInt(), restTime.toInt())
        }
    }
}

private val regex = """(\w+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds.""".toRegex()

private const val testInput1 = """Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds."""

private const val input = """Dancer can fly 27 km/s for 5 seconds, but then must rest for 132 seconds.
Cupid can fly 22 km/s for 2 seconds, but then must rest for 41 seconds.
Rudolph can fly 11 km/s for 5 seconds, but then must rest for 48 seconds.
Donner can fly 28 km/s for 5 seconds, but then must rest for 134 seconds.
Dasher can fly 4 km/s for 16 seconds, but then must rest for 55 seconds.
Blitzen can fly 14 km/s for 3 seconds, but then must rest for 38 seconds.
Prancer can fly 3 km/s for 21 seconds, but then must rest for 40 seconds.
Comet can fly 18 km/s for 6 seconds, but then must rest for 103 seconds.
Vixen can fly 18 km/s for 5 seconds, but then must rest for 84 seconds."""
