package com.github.ferinagy.adventOfCode.aoc2016

fun main() {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput1))
    println(part2(input))
}

private fun part1(input: String): Long {
    val comp = MonorailComputer()
    val instructions = input.lines().map { MonorailComputer.Instruction.parse(it) }

    comp.execute(instructions)

    return comp.registers["a"]!!
}

private fun part2(input: String): Long {
    val comp = MonorailComputer()
    val instructions = input.lines().map { MonorailComputer.Instruction.parse(it) }

    comp.registers["c"] = 1
    comp.execute(instructions)

    return comp.registers["a"]!!
}

private class MonorailComputer {
    val registers = mutableMapOf(
        "a" to 0L,
        "b" to 0L,
        "c" to 0L,
        "d" to 0L,
    )

    fun execute(instructions: List<Instruction>) {
        var ip = 0
        while (ip in instructions.indices) {
            when (val instruction = instructions[ip]) {
                is Instruction.CpyReg -> registers[instruction.into] = registers[instruction.register]!!
                is Instruction.CpyVal -> registers[instruction.into] = instruction.value
                is Instruction.Dec -> registers[instruction.register] = registers[instruction.register]!! - 1
                is Instruction.Inc -> registers[instruction.register] = registers[instruction.register]!! + 1
                is Instruction.Jnz -> {
                    if (registers[instruction.register] != 0L) {
                        ip += instruction.offset
                        continue
                    }
                }
            }

            ip++
        }
    }

    sealed class Instruction {
        class CpyVal(val value: Long, val into: String) : Instruction()
        class CpyReg(val register: String, val into: String) : Instruction()
        class Inc(val register: String) : Instruction()
        class Dec(val register: String) : Instruction()
        class Jnz(val register: String, val offset: Int) : Instruction()

        companion object {
            fun parse(input: String): Instruction {
                return when {
                    cpyValRegex.matches(input) -> {
                        val (value, into) = cpyValRegex.matchEntire(input)!!.destructured
                        CpyVal(value.toLong(), into)
                    }
                    cpyRegRegex.matches(input) -> {
                        val (reg, into) = cpyRegRegex.matchEntire(input)!!.destructured
                        CpyReg(reg, into)
                    }
                    incRegex.matches(input) -> Inc(incRegex.matchEntire(input)!!.groupValues[1])
                    decRegex.matches(input) -> Dec(decRegex.matchEntire(input)!!.groupValues[1])
                    jnzRegex.matches(input) -> {
                        val (reg, offset) = jnzRegex.matchEntire(input)!!.destructured
                        Jnz(reg, offset.toInt())
                    }
                    else -> error("Unknown instruction: $input")
                }
            }
        }
    }
}

private val cpyValRegex = """cpy (\d+) (\w)""".toRegex()
private val cpyRegRegex = """cpy (\w) (\w)""".toRegex()
private val incRegex = """inc (\w)""".toRegex()
private val decRegex = """dec (\w)""".toRegex()
private val jnzRegex = """jnz (\w) (-?\d+)""".toRegex()

private const val testInput1 = """cpy 41 a
inc a
inc a
dec a
jnz a 2
dec a"""

private const val input = """cpy 1 a
cpy 1 b
cpy 26 d
jnz c 2
jnz 1 5
cpy 7 c
inc d
dec c
jnz c -2
cpy a c
inc a
dec b
jnz b -2
cpy c b
dec d
jnz d -6
cpy 19 c
cpy 14 d
inc a
dec d
jnz d -2
dec c
jnz c -5"""
