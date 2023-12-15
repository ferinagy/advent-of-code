package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText

fun main() {
    val input = readInputText(2021, "16-input")
    val test1 = readInputText(2021, "16-test1")
    val test2 = readInputText(2021, "16-test2")
    val test3 = readInputText(2021, "16-test3")
    val test4 = readInputText(2021, "16-test4")

    println("Part1:")
    part1(test1).println()
    part1(test2).println()
    part1(test3).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test4).println()
    part2(input).println()
}

private fun part1(input: String): Int {
    val parser = PacketParser(input)
    return parser.parse().versionSum()
}

private fun part2(input: String): Long {
    val parser = PacketParser(input)
    return parser.parse().calculate()
}

private class PacketParser(input: String) {

    private val binaryRep = input
        .map { it.digitToInt(16).toString(2).padStart(4, '0') }
        .joinToString(separator = "")

    fun parse() = parsePacket().first

    private fun parsePacket(start: Int = 0): Pair<Packet, Int> {
        val version = binaryRep.substring(start, start + 3).toInt(2)
        val typeId = binaryRep.substring(start + 3, start + 6).toInt(2)

        return if (typeId == 4) {
            val (value, end) = readLiteralValue(start + 6)
            Literal(version, value) to end
        } else {
            val (subPackets, end) = readSubPackets(start + 6)
            Operator(version, typeId, subPackets) to end
        }
    }

    private fun readLiteralValue(start: Int): Pair<Long, Int> {
        var position = start
        var group = binaryRep.substring(position, position + 5)
        position += 5

        return buildString {
            append(group.substring(1))
            while (group.first() == '1') {
                group = binaryRep.substring(position, position + 5)
                append(group.substring(1))
                position += 5
            }
        }.toLong(2) to position
    }

    private fun readSubPackets(start: Int): Pair<List<Packet>, Int> {
        return if (binaryRep[start] == '0') {
            readSubPacketsByLength(start + 1)
        } else {
            readSubPacketsByCount(start + 1)
        }
    }

    private fun readSubPacketsByLength(start: Int): Pair<List<Packet>, Int> {
        var position = start
        var remainingLength = binaryRep.substring(position, position + 15).toInt(2)
        position += 15

        return buildList {
            while (remainingLength != 0) {
                val (subPacket, rest) = parsePacket(position)
                val read = rest - position
                remainingLength -= read
                position += read
                this += subPacket
            }
        } to position
    }

    private fun readSubPacketsByCount(start: Int): Pair<List<Packet>, Int> {
        var position = start
        val subPacketCount = binaryRep.substring(position, position + 11).toInt(2)
        position += 11

        return buildList {
            repeat(subPacketCount) {
                val (subPacket, rest) = parsePacket(position)
                val read = rest - position
                position += read
                this += subPacket
            }
        } to position
    }
}

private sealed class Packet {
    abstract fun versionSum(): Int

    abstract fun calculate(): Long
}

private class Literal(val version: Int, val value: Long) : Packet() {
    override fun versionSum(): Int = version

    override fun calculate(): Long = value

}

private class Operator(val version: Int, val typeId: Int, val subPackets: List<Packet>) : Packet() {
    override fun versionSum(): Int {
        return version + subPackets.sumOf { it.versionSum() }
    }

    override fun calculate(): Long {
        return when (typeId) {
            0 -> subPackets.sumOf { it.calculate() }
            1 -> subPackets.fold(1) { acc, item -> acc * item.calculate() }
            2 -> subPackets.minOf { it.calculate() }
            3 -> subPackets.maxOf { it.calculate() }
            5 -> {
                val first = subPackets[0].calculate()
                val second = subPackets[1].calculate()
                if (first > second) 1 else 0
            }
            6 -> {
                val first = subPackets[0].calculate()
                val second = subPackets[1].calculate()
                if (first < second) 1 else 0
            }
            7 -> {
                val first = subPackets[0].calculate()
                val second = subPackets[1].calculate()
                if (first == second) 1 else 0
            }
            else -> {
                error("wrong type id: $typeId")
            }
        }
    }

}
