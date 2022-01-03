package com.github.ferinagy.adventOfCode.aoc2015

fun main(args: Array<String>) {
    println("Part1:")
    println(part1())

    println()
    println("Part2:")
    println(part2())
}

private fun part1(): Int {
    return playGame(hard = false, print = false)
}

private fun part2(): Int {
    return playGame(hard = true, print = false)
}

private fun playGame(hard: Boolean, print: Boolean): Int {
    val state = GameState()
    val manaSpent = mutableMapOf(state to GameProgress(0, ""))
    val queue = mutableSetOf(state)
    val visited = mutableSetOf<GameState>()

    while (queue.isNotEmpty()) {
        val current = queue.minByOrNull { manaSpent[it]!!.manaSpent }!!
        queue -= current

        val (mana, description) = manaSpent[current]!!
        if (current.isWin()) {
            if (print) println(description)
            return mana
        }
        visited += current

        current.next(hard, print).forEach { (newState, newMana, newDesc) ->
            if (newState in visited || newState.isLoss()) return@forEach

            if (newState !in manaSpent || mana + newMana < manaSpent[newState]!!.manaSpent) {
                manaSpent[newState] = GameProgress(mana + newMana, if (print) description + newDesc else "")
            }

            queue += newState
        }
    }

    return -1
}

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

@OptIn(ExperimentalStdlibApi::class)
private fun GameState.next(hard: Boolean, print: Boolean): List<GameStep> = buildList {
    val description = StringBuilder()
    if (print) {
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
    }

    var newShieldTimer = shieldTimer
    var newPoisonTimer = poisonTimer
    var newRechargeTimer = rechargeTimer
    var newBossHp = bossHp
    var newMana = playerMana
    var newPlayerHp = playerHp
    var newPlayerArmor = playerArmor

    if (hard && playerTurn) {
        newPlayerHp--
        if (print) { description.append("Player loses 1 hit point for hard mode.\n") }
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
        if (print) { description.append("Shield's timer is now ").append(newShieldTimer).append(".\n") }
        if (newShieldTimer == 0) {
            newPlayerArmor = 0
            if (print) { description.append("Shield wears off, decreasing armor by 7.\n") }
        }
    }

    if (newRechargeTimer != 0) {
        newRechargeTimer--
        newMana += 101
        if (print) {
            description.append("Recharge provides 101 mana; its timer is now ").append(newRechargeTimer).append(".\n")
        }

        if (newRechargeTimer == 0) {
            if (print) {
                description.append("Recharge wears off.\n")
            }
        }
    }

    if (newPoisonTimer != 0) {
        newPoisonTimer--
        newBossHp -= 3
        if (print) {
            if (newBossHp <= 0) {
                description.append("Poison deals 3 damage. This kills the boss, and the player wins.\n")
            } else {
                description.append("Poison deals 3 damage; its timer is now ").append(newPoisonTimer).append(".\n")
            }
        }

        if (print && newPoisonTimer == 0) {
            description.append("Poison wears off.\n")
        }
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
        if (print) {
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
            val newDescription = if (print) {
                description.toString() + if (newBossHp <= 4) {
                    "Player casts Magic Missile, dealing 4 damage. This kills the boss, and the player wins.\n"
                } else {
                    "Player casts Magic Missile, dealing 4 damage.\n"
                }
            } else ""

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
            val newDescription = if (print) {
                description.toString() + if (newBossHp <= 2) {
                    "Player casts Drain, dealing 2 damage, and healing 2 hit points. This kills the boss, and the player wins.\n"
                } else {
                    "Player casts Drain, dealing 2 damage, and healing 2 hit points.\n"
                }
            } else ""
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
                description = if (print) { description.toString() + "Player casts Shield, increasing armor by 7.\n" } else ""
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
                description = if (print) { description.toString() + "Player casts Poison.\n" } else ""
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
                description = if (print) { description.toString() + "Player casts Recharge.\n" } else ""
            )
        }
    }
}

private fun GameState.isLoss() = playerHp <= 0
private fun GameState.isWin() = bossHp <= 0

private data class GameStep(val newState: GameState, val manaCost: Int, val description: String)
private data class GameProgress(val manaSpent: Int, val description: String)
