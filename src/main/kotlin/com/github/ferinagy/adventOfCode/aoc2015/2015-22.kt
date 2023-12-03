package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.searchGraph

fun main() {
    println("Part1:")
    println(part1())

    println()
    println("Part2:")
    println(part2())
}

private fun part1(): Int {
    return playGame(hard = false)
}

private fun part2(): Int {
    return playGame(hard = true)
}

private fun playGame(hard: Boolean) = searchGraph(
    start = GameState(),
    isDone = { it.isWin() },
    nextSteps = { current ->
        if (current.isLoss()) {
            emptySet()
        } else {
            current.next(hard).map { it.newState to it.manaCost }.toSet()
        }
    }
)

private data class GameState(
    val playerHp: Int = 50,
    val playerArmor: Int = 0,
    val playerMana: Int = 500,
    val playerTurn: Boolean = true,
    val bossHp: Int = 58,
    val bossDamage: Int = 9,
    val shieldTimer: Int = 0,
    val poisonTimer: Int = 0,
    val rechargeTimer: Int = 0
)

private fun GameState.next(hard: Boolean): List<GameStep> = buildList {
    val description = StringBuilder()
    description.append("\n")
    if (playerTurn) description.append("-- Player turn --\n") else description.append("-- Boss turn --\n")

    description.append("- Player has ")
        .append(playerHp)
        .append(" hit points, ")
        .append(playerArmor)
        .append(" armor, ")
        .append(playerMana)
        .append(" mana\n")
        .append("- Boss has ")
        .append(bossHp)
        .append(" hit points\n")

    var newShieldTimer = shieldTimer
    var newPoisonTimer = poisonTimer
    var newRechargeTimer = rechargeTimer
    var newBossHp = bossHp
    var newMana = playerMana
    var newPlayerHp = playerHp
    var newPlayerArmor = playerArmor

    if (hard && playerTurn) {
        newPlayerHp--
        description.append("Player loses 1 hit point for hard mode.\n")
        if (newPlayerHp <= 0) {
            this += GameStep(
                newState = copy(
                    playerHp = newPlayerHp,
                    playerMana = newMana,
                    playerTurn = false,
                    playerArmor = newPlayerArmor,
                    bossHp = newBossHp,
                    shieldTimer = newShieldTimer,
                    poisonTimer = newPoisonTimer,
                    rechargeTimer = newRechargeTimer
                ),
                manaCost = 0,
                description = description.toString()
            )
            return@buildList
        }
    }

    if (newShieldTimer != 0) {
        newShieldTimer--
        description.append("Shield's timer is now ").append(newShieldTimer).append(".\n")
        if (newShieldTimer == 0) {
            newPlayerArmor = 0
            description.append("Shield wears off, decreasing armor by 7.\n")
        }
    }

    if (newRechargeTimer != 0) {
        newRechargeTimer--
        newMana += 101
        description.append("Recharge provides 101 mana; its timer is now ").append(newRechargeTimer).append(".\n")

        if (newRechargeTimer == 0) {
            description.append("Recharge wears off.\n")
        }
    }

    if (newPoisonTimer != 0) {
        newPoisonTimer--
        newBossHp -= 3
        if (newBossHp <= 0) {
            description.append("Poison deals 3 damage. This kills the boss, and the player wins.\n")
        } else {
            description.append("Poison deals 3 damage; its timer is now ").append(newPoisonTimer).append(".\n")
        }

        description.append("Poison wears off.\n")
    }

    if (newBossHp <= 0) {
        this += GameStep(
            newState = copy(
                playerHp = newPlayerHp,
                playerMana = newMana,
                playerTurn = !playerTurn,
                playerArmor = newPlayerArmor,
                bossHp = newBossHp,
                shieldTimer = newShieldTimer,
                poisonTimer = newPoisonTimer,
                rechargeTimer = newRechargeTimer
            ),
            manaCost = 0,
            description = description.toString()
        )
        return@buildList
    }

    if (!playerTurn) {
        newPlayerHp -= bossDamage - newPlayerArmor
        if (newPlayerArmor == 0) {
            description.append("Boss attacks for $bossDamage damage!")
        } else {
            description.append("Boss attacks for $bossDamage - $newPlayerArmor = ${bossDamage - newPlayerArmor} damage!")
        }

        if (newPlayerHp == 0) {
            description.append(" This kills the player they loose.\n")
        } else {
            description.append("\n")
        }
        this += GameStep(
            newState = copy(
                playerHp = newPlayerHp,
                playerArmor = newPlayerArmor,
                playerMana = newMana,
                playerTurn = true,
                bossHp = newBossHp,
                shieldTimer = newShieldTimer,
                poisonTimer = newPoisonTimer,
                rechargeTimer = newRechargeTimer
            ),
            manaCost = 0,
            description = description.toString()
        )
    } else {
        if (53 <= newMana) {
            // magic missile
            val newDescription =
                description.toString() + if (newBossHp <= 4) {
                    "Player casts Magic Missile, dealing 4 damage. This kills the boss, and the player wins.\n"
                } else {
                    "Player casts Magic Missile, dealing 4 damage.\n"
                }

            this += GameStep(
                newState = copy(
                    playerHp = newPlayerHp,
                    playerMana = newMana - 53,
                    playerArmor = newPlayerArmor,
                    playerTurn = false,
                    bossHp = newBossHp - 4,
                    shieldTimer = newShieldTimer,
                    poisonTimer = newPoisonTimer,
                    rechargeTimer = newRechargeTimer
                ),
                manaCost = 53,
                description = newDescription
            )
        }
        if (73 <= newMana) {
            // drain
            val newDescription =
                description.toString() + if (newBossHp <= 2) {
                    "Player casts Drain, dealing 2 damage, and healing 2 hit points. This kills the boss, and the player wins.\n"
                } else {
                    "Player casts Drain, dealing 2 damage, and healing 2 hit points.\n"
                }
            this += GameStep(
                newState = copy(
                    playerHp = newPlayerHp + 2,
                    playerMana = newMana - 73,
                    playerArmor = newPlayerArmor,
                    playerTurn = false,
                    bossHp = newBossHp - 2,
                    shieldTimer = newShieldTimer,
                    poisonTimer = newPoisonTimer,
                    rechargeTimer = newRechargeTimer
                ),
                manaCost = 73,
                description = newDescription
            )
        }
        if (113 <= newMana && newShieldTimer == 0) {
            // shield
            this += GameStep(
                newState = copy(
                    playerHp = newPlayerHp,
                    playerMana = newMana - 113,
                    playerArmor = 7,
                    playerTurn = false,
                    bossHp = newBossHp,
                    shieldTimer = 6,
                    poisonTimer = newPoisonTimer,
                    rechargeTimer = newRechargeTimer
                ),
                manaCost = 113,
                description = description.toString() + "Player casts Shield, increasing armor by 7.\n"
            )
        }
        if (173 <= newMana && newPoisonTimer == 0) {
            // poison
            this += GameStep(
                newState = copy(
                    playerHp = newPlayerHp,
                    playerMana = newMana - 173,
                    playerArmor = newPlayerArmor,
                    playerTurn = false,
                    bossHp = newBossHp,
                    shieldTimer = newShieldTimer,
                    poisonTimer = 6,
                    rechargeTimer = newRechargeTimer
                ),
                manaCost = 173,
                description = description.toString() + "Player casts Poison.\n"
            )
        }
        if (229 <= newMana && newRechargeTimer == 0) {
            // recharge
            this += GameStep(
                newState = copy(
                    playerHp = newPlayerHp,
                    playerMana = newMana - 229,
                    playerArmor = newPlayerArmor,
                    playerTurn = false,
                    bossHp = newBossHp,
                    shieldTimer = newShieldTimer,
                    poisonTimer = newPoisonTimer,
                    rechargeTimer = 5
                ),
                manaCost = 229,
                description = description.toString() + "Player casts Recharge.\n"
            )
        }
    }
}

private fun GameState.isLoss() = playerHp <= 0
private fun GameState.isWin() = bossHp <= 0

private data class GameStep(val newState: GameState, val manaCost: Int, val description: String)
