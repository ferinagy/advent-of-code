package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2015, "21-input")

    println("Part1:")
    part1(input).println()

    println()
    println("Part2:")
    part2(input).println()
}

private fun part1(input: String) = solve(input, true, naturalOrder<Loadout>().reversed())

private fun part2(input: String) = solve(input, false, naturalOrder())

private fun solve(input: String, winning: Boolean, comparator: Comparator<Loadout>): Int {
    val (hp, dmg, arm) = input.lines().map { it.split(": ") }.map { it[1].toInt() }

    val boss = Warrior(hp, dmg, arm)
    val player = Warrior(100, 0, 0)

    val results = simulateFights(player, boss)

    val best = results.filter { it.second == winning }.map { it.first }.maxWithOrNull(comparator)!!

    return best.cost
}

private fun simulateFights(player: Warrior, boss: Warrior): MutableList<Pair<Loadout, Boolean>> {
    val results = mutableListOf<Pair<Loadout, Boolean>>()
    weapons.forEach { weapon ->
        armors.forEach { armor ->
            for (i in 0 until rings.lastIndex) {
                for (j in i + 1..rings.lastIndex) {
                    val loadout = Loadout(weapon, armor, rings[i], rings[j])
                    results += loadout to player.withLoadout(loadout).fight(boss)
                }
            }
        }
    }
    return results
}

private data class Warrior(val hp: Int, val damage: Int, val armor: Int)
private data class Equipment(val name: String, val cost: Int, val damage: Int, val armor: Int)
private data class Loadout(
    val weapon: Equipment,
    val armor: Equipment,
    val ring1: Equipment,
    val ring2: Equipment
) : Comparable<Loadout> {

    val cost: Int
        get() = weapon.cost + armor.cost + ring1.cost + ring2.cost

    override fun compareTo(other: Loadout) = cost - other.cost
}

private fun Warrior.fight(other: Warrior): Boolean {
    var hp1 = hp
    var hp2 = other.hp

    while (true) {
        hp2 -= (damage - other.armor).coerceAtLeast(1)
        if (hp2 <= 0) return true
        hp1 -= (other.damage - armor).coerceAtLeast(1)
        if (hp1 <= 0) return false
    }
}

private fun Warrior.withLoadout(loadout: Loadout): Warrior = copy(
    damage = damage + loadout.weapon.damage + loadout.ring1.damage + loadout.ring2.damage,
    armor = armor + loadout.armor.armor + loadout.ring1.armor + loadout.ring2.armor
)

private val weapons = listOf(
    Equipment("Dagger", 8, 4, 0),
    Equipment("Shortsword", 10, 5, 0),
    Equipment("Warhammer", 25, 6, 0),
    Equipment("Longsword", 40, 7, 0),
    Equipment("Greataxe", 74, 8, 0),
)

private val armors = listOf(
    Equipment("Naked", 0, 0, 0),
    Equipment("Leather", 13, 0, 1),
    Equipment("Chainmail", 31, 0, 2),
    Equipment("Splintmail", 53, 0, 3),
    Equipment("Bandedmail", 75, 0, 4),
    Equipment("Platemail", 102, 0, 5),
)

private val rings = listOf(
    Equipment("Unequipped 1", 0, 0, 0),
    Equipment("Unequipped 2", 0, 0, 0),
    Equipment("Damage +1", 25, 1, 0),
    Equipment("Damage +2", 50, 2, 0),
    Equipment("Damage +3", 100, 3, 0),
    Equipment("Defense +1", 20, 0, 1),
    Equipment("Defense +2", 40, 0, 2),
    Equipment("Defense +3", 80, 0, 3),
)
