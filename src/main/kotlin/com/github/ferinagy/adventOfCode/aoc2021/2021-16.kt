package com.github.ferinagy.adventOfCode.aoc2021

fun main() {
    println("Part1:")
    println(part1(testInput1))
    println(part1(testInput2))
    println(part1(testInput3))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput4))
    println(part2(input))
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

private fun parse(input: String): String = input.map {
    it.digitToInt(16).toString(2).padStart(4, '0')
}.joinToString(separator = "")

private const val testInput1 = """D2FE28"""
private const val testInput2 = """620080001611562C8802118E34"""
private const val testInput3 = """C0015000016115A2E0802F182340"""
private const val testInput4 = """9C0141080250320F1802104A08"""

private const val input = """E20D4100AA9C0199CA6A3D9D6352294D47B3AC6A4335FBE3FDD251003873657600B46F8DC600AE80273CCD2D5028B6600AF802B2959524B727D8A8CC3CCEEF3497188C017A005466DAA6FDB3A96D5944C014C006865D5A7255D79926F5E69200A164C1A65E26C867DDE7D7E4794FE72F3100C0159A42952A7008A6A5C189BCD456442E4A0A46008580273ADB3AD1224E600ACD37E802200084C1083F1540010E8D105A371802D3B845A0090E4BD59DE0E52FFC659A5EBE99AC2B7004A3ECC7E58814492C4E2918023379DA96006EC0008545B84B1B00010F8E915E1E20087D3D0E577B1C9A4C93DD233E2ECF65265D800031D97C8ACCCDDE74A64BD4CC284E401444B05F802B3711695C65BCC010A004067D2E7C4208A803F23B139B9470D7333B71240050A20042236C6A834600C4568F5048801098B90B626B00155271573008A4C7A71662848821001093CB4A009C77874200FCE6E7391049EB509FE3E910421924D3006C40198BB11E2A8803B1AE2A4431007A15C6E8F26009E002A725A5292D294FED5500C7170038C00E602A8CC00D60259D008B140201DC00C401B05400E201608804D45003C00393600B94400970020C00F6002127128C0129CDC7B4F46C91A0084E7C6648DC000DC89D341B23B8D95C802D09453A0069263D8219DF680E339003032A6F30F126780002CC333005E8035400042635C578A8200DC198890AA46F394B29C4016A4960C70017D99D7E8AF309CC014FCFDFB0FE0DA490A6F9D490010567A3780549539ED49167BA47338FAAC1F3005255AEC01200043A3E46C84E200CC4E895114C011C0054A522592912C9C8FDE10005D8164026C70066C200C4618BD074401E8C90E23ACDFE5642700A6672D73F285644B237E8CCCCB77738A0801A3CFED364B823334C46303496C940"""
