package com.github.ferinagy.adventOfCode.aoc2016

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2016, "04-input")
    val test1 = readInputLines(2016, "04-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(input).println()
}

private fun part1(input: List<String>) = input
    .map { parse(it) }.filter { it.isValid() }
    .sumOf { it.sectorId }

private fun part2(input: List<String>): Int {
    val validRooms = input.map { parse(it) }.filter { it.isValid() }
    val map = validRooms.associateBy { it.decryptedName }
    return map["northpole object storage"]!!.sectorId
}

private val roomComparator = compareByDescending<Map.Entry<Char, Int>> { it.value }.thenBy { it.key }

private data class Room(val encryptedName: String, val sectorId: Int, val checkSum: String) {

    fun isValid(): Boolean {
        val letters = encryptedName.filter { it.isLetter() }
        val counts = letters.groupingBy { it }.eachCount()
        val top5 = counts.entries.sortedWith(roomComparator).take(5)
        val sum = top5.map { it.key }.joinToString(separator = "")

        return sum == checkSum
    }

    val decryptedName: String
        get() {
            return encryptedName.map {
                if (it == '-') ' ' else 'a' + (((it - 'a') + sectorId) % 26)
            }.joinToString(separator = "")
        }
}

private fun parse(input: String): Room {
    val (name, sectorId, checksum) = regex.matchEntire(input)!!.destructured
    return Room(name, sectorId.toInt(), checksum)
}

private val regex = """([\w-]+)-(\d+)\[(\w{5})]""".toRegex()
