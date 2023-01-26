package com.github.ferinagy.adventOfCode.aoc2022

import kotlin.math.max

fun main() {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput1))
    println(part2(input))
}

private fun part1(input: String): Int {
    return NotEnoughMinerals(input).solve()
}

private fun part2(input: String): Int {
    return NotEnoughMinerals(input).solve2()
}

private class NotEnoughMinerals(input: String) {

    private val regex = """Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.""".toRegex()

    private val blueprints = input.lines().map {
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

private const val testInput1 =
    """Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian."""

private const val input = """Blueprint 1: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 20 clay. Each geode robot costs 3 ore and 14 obsidian.
Blueprint 2: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 20 clay. Each geode robot costs 2 ore and 20 obsidian.
Blueprint 3: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 16 clay. Each geode robot costs 3 ore and 9 obsidian.
Blueprint 4: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 15 clay. Each geode robot costs 2 ore and 13 obsidian.
Blueprint 5: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 16 clay. Each geode robot costs 3 ore and 13 obsidian.
Blueprint 6: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 14 clay. Each geode robot costs 3 ore and 14 obsidian.
Blueprint 7: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 6 clay. Each geode robot costs 2 ore and 20 obsidian.
Blueprint 8: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 5 clay. Each geode robot costs 4 ore and 8 obsidian.
Blueprint 9: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 19 clay. Each geode robot costs 3 ore and 8 obsidian.
Blueprint 10: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 14 clay. Each geode robot costs 3 ore and 8 obsidian.
Blueprint 11: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 19 clay. Each geode robot costs 4 ore and 13 obsidian.
Blueprint 12: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 20 clay. Each geode robot costs 4 ore and 18 obsidian.
Blueprint 13: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 16 clay. Each geode robot costs 4 ore and 16 obsidian.
Blueprint 14: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 20 clay. Each geode robot costs 2 ore and 16 obsidian.
Blueprint 15: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 11 clay. Each geode robot costs 3 ore and 8 obsidian.
Blueprint 16: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 4 ore and 19 clay. Each geode robot costs 4 ore and 12 obsidian.
Blueprint 17: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 20 clay. Each geode robot costs 3 ore and 15 obsidian.
Blueprint 18: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 15 clay. Each geode robot costs 4 ore and 20 obsidian.
Blueprint 19: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 4 ore and 15 clay. Each geode robot costs 4 ore and 9 obsidian.
Blueprint 20: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 7 clay. Each geode robot costs 2 ore and 9 obsidian.
Blueprint 21: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 3 ore and 19 obsidian.
Blueprint 22: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 17 clay. Each geode robot costs 3 ore and 13 obsidian.
Blueprint 23: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 18 clay. Each geode robot costs 4 ore and 19 obsidian.
Blueprint 24: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 17 clay. Each geode robot costs 2 ore and 13 obsidian.
Blueprint 25: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 15 clay. Each geode robot costs 3 ore and 16 obsidian.
Blueprint 26: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 15 clay. Each geode robot costs 2 ore and 13 obsidian.
Blueprint 27: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 18 clay. Each geode robot costs 4 ore and 9 obsidian.
Blueprint 28: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 7 clay. Each geode robot costs 2 ore and 19 obsidian.
Blueprint 29: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 15 clay. Each geode robot costs 4 ore and 17 obsidian.
Blueprint 30: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 9 clay. Each geode robot costs 4 ore and 16 obsidian."""
