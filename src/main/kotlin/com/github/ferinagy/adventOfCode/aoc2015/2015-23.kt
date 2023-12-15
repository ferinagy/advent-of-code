package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2015, "23-input")
    val test1 = readInputLines(2015, "23-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    return solve(input, 0)
}

private fun solve(input: List<String>, a: Int): Int {
    var comp = Computer(a = a)
    val instructions = input.map { Instruction.parse(it) }
    while (comp.ip in instructions.indices) {
        comp = comp.doInstruction(instructions[comp.ip])
    }

    return comp.b
}

private fun part2(input: List<String>): Int {
    return solve(input, 1)
}

private data class Computer(val a: Int = 0, val b: Int = 0, val ip: Int = 0)

private fun Computer.doInstruction(instruction: Instruction): Computer {
    return when (instruction) {
        is Instruction.Half -> {
            if (instruction.register == "a") copy(a = a / 2, ip = ip + 1) else copy(b = b / 2, ip = ip + 1)
        }
        is Instruction.Inc -> {
            if (instruction.register == "a") copy(a = a + 1, ip = ip + 1) else copy(b = b + 1, ip = ip + 1)
        }
        is Instruction.Triple -> {
            if (instruction.register == "a") copy(a = a * 3, ip = ip + 1) else copy(b = b * 3, ip = ip + 1)
        }
        is Instruction.Jump -> {
            copy(ip = ip + instruction.offset)
        }
        is Instruction.Jie -> {
            if (instruction.register == "a") {
                if (a % 2 == 0) {
                    copy(ip = ip + instruction.offset)
                } else {
                    copy(ip = ip + 1)
                }
            } else {
                if (b % 2 == 0) {
                    copy(ip = ip + instruction.offset)
                } else {
                    copy(ip = ip + 1)
                }
            }
        }
        is Instruction.Jio -> {
            if (instruction.register == "a") {
                if (a == 1) {
                    copy(ip = ip + instruction.offset)
                } else {
                    copy(ip = ip + 1)
                }
            } else {
                if (b == 1) {
                    copy(ip = ip + instruction.offset)
                } else {
                    copy(ip = ip + 1)
                }
            }
        }
    }
}

private sealed class Instruction {
    data class Inc(val register: String) : Instruction()
    data class Half(val register: String) : Instruction()
    data class Triple(val register: String) : Instruction()
    data class Jump(val offset: Int) : Instruction()
    data class Jie(val register: String, val offset: Int) : Instruction()
    data class Jio(val register: String, val offset: Int) : Instruction()

    companion object {
        fun parse(line: String) = when {
            line.startsWith("inc") -> Inc(line.substring(4))
            line.startsWith("hlf") -> Half(line.substring(4))
            line.startsWith("tpl") -> Triple(line.substring(4))
            line.startsWith("jmp") -> Jump(line.substring(4).toInt())
            line.startsWith("jie") -> Jie(line.substring(4..4), line.substring(7).toInt())
            line.startsWith("jio") -> Jio(line.substring(4..4), line.substring(7).toInt())
            else -> error("Unknown: '$line'")
        }
    }
}
