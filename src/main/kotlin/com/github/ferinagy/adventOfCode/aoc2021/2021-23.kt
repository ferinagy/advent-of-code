package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2021, "23-input")
    val test1 = readInputLines(2021, "23-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val initial = Burrow.parse(input)

    return sort(initial)
}

private fun part2(input: List<String>): Int {
    val initial = Burrow.parse(input)
    val toAdd = listOf("DD", "CB", "BA", "AC")
    val newRooms = initial.rooms.mapIndexed { i, room ->
        room[0] + toAdd[i] + room[1]
    }
    val new = initial.copy(rooms = newRooms)
    return sort(new)
}

private fun sort(initial: Burrow): Int {
    val energyMap = mutableMapOf(initial to 0)
    val set = mutableSetOf(initial)
    val visited = mutableSetOf<Burrow>()

    while (set.isNotEmpty()) {
        val burrow = set.minByOrNull { energyMap[it]!! }!!
        visited += burrow
        set -= burrow

        val subEnergy = energyMap[burrow]!!

        if (burrow.isOrganized()) return subEnergy

        burrow.possibleMoves().forEach { (next, energy) ->
            val newEnergy = energy + subEnergy
            val previous = energyMap[next]
            if (previous == null || newEnergy < previous) {
                energyMap[next] = newEnergy
            }
            if (next !in visited) {
                set += next
            }
        }
    }

    return -1
}

private data class Burrow(val hallway: String, val rooms: List<String>) {
    companion object {
        fun parse(input: List<String>): Burrow {
            return input.let { lines ->
                val rooms = roomPositions.map { "${lines[2][it+1]}${lines[3][it+1]}" }
                Burrow(hallway = lines[1].substring(1..11), rooms = rooms)
            }
        }
    }
}

private val roomPositions = listOf(2, 4, 6, 8)
private val energyCosts = listOf(1, 10, 100, 1000)

private fun Burrow.isOrganized(): Boolean {
    return rooms.withIndex().all { (index, room) ->
        room.all { it == 'A' + index }
    }
}

private fun Burrow.possibleMoves(): List<Pair<Burrow, Int>> = buildList {
    val inHallway = hallway.mapIndexedNotNull { index, c ->
        if (c != '.') c to index else null
    }
    inHallway.forEach { (type, index) ->
        val roomIndex = type - 'A'

        val range = range(index, roomPositions[roomIndex])
        val placeInRoom = rooms[roomIndex].freeRoomIndex(type)
        val isFree = hallway.substring(range).withIndex().all { it.value == '.' || it.index == index - range.first }

        if (isFree && placeInRoom != -1) {
            val steps = (range.last - range.first) + (placeInRoom + 1)
            val newHallway = hallway.replaceAt('.', index)
            val newRooms = rooms.mapIndexed { i, r -> if (i != roomIndex) r else r.replaceAt(type, placeInRoom) }
            this += Burrow(newHallway, newRooms) to steps * energyCosts[type - 'A']
        }
    }

    rooms.forEachIndexed { roomIndex, room ->
        val positionInRoom = room.firstOccupiedIndex()
        if (positionInRoom == -1) return@forEachIndexed

        val type = room[positionInRoom]
        if (room.substring(positionInRoom).all { it == 'A' + roomIndex }) return@forEachIndexed

        val roomPosition = roomPositions[roomIndex]
        val left = hallway.substring(0, roomPosition).indexOfLast { it != '.' }
        val right = hallway.substring(roomPosition + 1)
            .indexOfFirst { it != '.' }
            .let { if (it == -1) hallway.length else it + roomPosition + 1 }
        for (i in left + 1 until roomPosition) {
            if (i in roomPositions) continue

            val steps = roomPosition - i + positionInRoom + 1
            val newHallway = hallway.replaceAt(type, i)
            val newRooms = rooms.mapIndexed { ir, r -> if (ir != roomIndex) r else r.replaceAt('.', positionInRoom) }
            this += Burrow(newHallway, newRooms) to steps * energyCosts[type - 'A']
        }

        for (i in roomPosition + 1 until right) {
            if (i in roomPositions) continue

            val steps = i - roomPosition + positionInRoom + 1
            val newHallway = hallway.replaceAt(type, i)
            val newRooms = rooms.mapIndexed { ir, r -> if (ir != roomIndex) r else r.replaceAt('.', positionInRoom) }
            this += Burrow(newHallway, newRooms) to steps * energyCosts[type - 'A']
        }
    }
}

private fun String.freeRoomIndex(type: Char): Int {
    var candidate = lastIndex
    while (0 <= candidate && this[candidate] == type) {
        candidate--
    }

    if (candidate != -1 && this[candidate] != '.') return -1

    return candidate
}

private fun String.firstOccupiedIndex() = indexOfFirst { it != '.' }

private fun String.replaceAt(type: Char, index: Int): String {
    return substring(0, index) + type + substring(index + 1)
}

private fun range(a: Int, b: Int): IntRange = if (a <= b) a .. b else b .. a
