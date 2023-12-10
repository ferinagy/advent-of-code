package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import kotlin.math.max

fun main() {
    val input = readInputLines(2022, "19-input")
    val testInput1 = readInputLines(2022, "19-test1")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(testInput1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    return NotEnoughMinerals(input).solve()
}

private fun part2(input: List<String>): Int {
    return NotEnoughMinerals(input).solve2()
}

private class NotEnoughMinerals(input: List<String>) {

    private val regex = """Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.""".toRegex()

    private val blueprints = input.map {
        val match = regex.matchEntire(it)!!
        Blueprint(
            match.groupValues[1].toInt(),
            match.groupValues[2].toInt(),
            match.groupValues[3].toInt(),
            match.groupValues[4].toInt(),
            match.groupValues[5].toInt(),
            match.groupValues[6].toInt(),
            match.groupValues[7].toInt(),
        )
    }
    private data class Blueprint(
        val id: Int,
        val oreRobotOre: Int,
        val clayRobotOre: Int,
        val obsidianRobotOre: Int,
        val obsidianRobotClay: Int,
        val geodeRobotOre: Int,
        val geodeRobotObsidian: Int
    )

    private data class State(
        val time: Int,
        val ore: Int = 0,
        val clay: Int = 0,
        val obsidian: Int = 0,
        val geode: Int = 0,
        val oreRobot: Int = 1,
        val clayRobot: Int = 0,
        val obsidianRobot: Int = 0,
        val geodeRobot: Int = 0,
    )

    fun solve(): Int {
        val geodes = blueprints.map {
            val dfs = Dfs(24, it)
            val quality = dfs.solve(State(24))
            quality * it.id
        }

        return geodes.sum()
    }

    fun solve2(): Int {
        val geodes = blueprints.take(3).map {
            val dfs = Dfs(32, it)
            val quality = dfs.solve(State(32))
            quality
        }

        return geodes.reduce { acc, i -> acc * i }
    }

    private class Dfs(private val time: Int, private val blueprint: Blueprint) {

        private val cache = mutableMapOf<State, Int>()
        private var bestSoFar = 0

        private val seq = (1..32).scan(0) { a, b -> a + b }

        fun solve(state: State = State(time)): Int {
            if (state in cache) {
                return cache[state]!!
            }

            if (state.time == 0) {
                cache[state] = state.geode
                bestSoFar = bestSoFar.coerceAtLeast(state.geode)
                return state.geode
            }

            var best = state.geodeRobot * state.time + state.geode
            val potential = state.geode + seq[state.time] + state.time * state.geodeRobot
            if (potential < bestSoFar) {
                cache[state] = 0
                return 0
            }

            best = buildGeodeRobot(state).coerceAtLeast(best)
            best = buildObsidianRobot(state).coerceAtLeast(best)
            best = buildClayRobot(state).coerceAtLeast(best)
            best = buildOreRobot(state).coerceAtLeast(best)

            cache[state] = best
            return best
        }

        private fun buildGeodeRobot(state: State): Int {
            if (state.oreRobot != 0 && state.obsidianRobot != 0) {
                val t = geodeRobotTime(state)
                if (t < state.time) {
                    return solve(
                        state = state.copy(
                            time = state.time - t,
                            ore = state.ore + state.oreRobot * t - blueprint.geodeRobotOre,
                            clay = state.clay + state.clayRobot * t,
                            obsidian = state.obsidian + state.obsidianRobot * t - blueprint.geodeRobotObsidian,
                            geode = state.geode + state.geodeRobot * t,
                            geodeRobot = state.geodeRobot + 1
                        )
                    )
                }
            }
            return 0
        }

        private fun buildObsidianRobot(state: State): Int {
            if (state.oreRobot != 0 && state.clayRobot != 0) {
                val maxObsidianNeeded = blueprint.geodeRobotObsidian * state.time
                val t = obsidianRobotTime(state)
                if (t < state.time && state.obsidianRobot * state.time + state.obsidian < maxObsidianNeeded) {
                    return solve(
                        state = state.copy(
                            time = state.time - t,
                            ore = state.ore + state.oreRobot * t - blueprint.obsidianRobotOre,
                            clay = state.clay + state.clayRobot * t - blueprint.obsidianRobotClay,
                            obsidian = state.obsidian + state.obsidianRobot * t,
                            geode = state.geode + state.geodeRobot * t,
                            obsidianRobot = state.obsidianRobot + 1
                        )
                    )
                }
            }
            return 0
        }

        private fun buildClayRobot(state: State): Int {
            if (state.oreRobot != 0) {
                val maxClayNeeded = blueprint.obsidianRobotClay * state.time
                val t = if (blueprint.clayRobotOre > state.ore) {
                    ((blueprint.clayRobotOre - state.ore - 1) / state.oreRobot) + 1
                } else 0
                if (t < state.time && state.clayRobot * state.time + state.clay < maxClayNeeded) {
                    return solve(
                        state = state.copy(
                            time = state.time - t - 1,
                            ore = state.ore + state.oreRobot * (t + 1) - blueprint.clayRobotOre,
                            clay = state.clay + state.clayRobot * (t + 1),
                            obsidian = state.obsidian + state.obsidianRobot * (t + 1),
                            geode = state.geode + state.geodeRobot * (t + 1),
                            clayRobot = state.clayRobot + 1
                        )
                    )
                }
            }
            return 0
        }

        private fun buildOreRobot(state: State): Int {
            if (state.oreRobot != 0) {
                val maxOreNeeded = maxOf(
                    blueprint.oreRobotOre,
                    blueprint.clayRobotOre,
                    blueprint.obsidianRobotOre,
                    blueprint.geodeRobotOre
                ) * state.time
                val t = if (blueprint.oreRobotOre > state.ore) {
                    ((blueprint.oreRobotOre - state.ore - 1) / state.oreRobot) + 1
                } else 0
                if (t < state.time && state.oreRobot * state.time + state.ore < maxOreNeeded) {
                    return solve(
                        state = state.copy(
                            time = state.time - t - 1,
                            ore = state.ore + state.oreRobot * (t + 1) - blueprint.oreRobotOre,
                            clay = state.clay + state.clayRobot * (t + 1),
                            obsidian = state.obsidian + state.obsidianRobot * (t + 1),
                            geode = state.geode + state.geodeRobot * (t + 1),
                            oreRobot = state.oreRobot + 1
                        )
                    )
                }
            }
            return 0
        }

        private fun geodeRobotTime(state: State): Int {
            val timeToGetOre = if (blueprint.geodeRobotOre > state.ore) {
                ((blueprint.geodeRobotOre - state.ore - 1) / state.oreRobot) + 1
            } else 0
            val timeToGetObsidian = if (blueprint.geodeRobotObsidian > state.obsidian) {
                ((blueprint.geodeRobotObsidian - state.obsidian - 1) / state.obsidianRobot) + 1
            } else 0
            return max(timeToGetOre, timeToGetObsidian) + 1
        }

        private fun obsidianRobotTime(state: State): Int {
            val timeToGetOre = if (blueprint.obsidianRobotOre > state.ore) {
                ((blueprint.obsidianRobotOre - state.ore - 1) / state.oreRobot) + 1
            } else 0
            val timeToGetClay = if (blueprint.obsidianRobotClay > state.clay) {
                ((blueprint.obsidianRobotClay - state.clay - 1) / state.clayRobot) + 1
            } else {
                0
            }
            return max(timeToGetOre, timeToGetClay) + 1
        }
    }
}
