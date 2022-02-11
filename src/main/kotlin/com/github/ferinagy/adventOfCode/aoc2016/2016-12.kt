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
    val comp = AssembunnyComputer()
    val instructions = input.lines().map { AssembunnyComputer.Instruction.parse(it) }

    comp.execute(instructions)

    return comp.registers["a"]!!
}

private fun part2(input: String): Long {
    val comp = AssembunnyComputer()
    val instructions = input.lines().map { AssembunnyComputer.Instruction.parse(it) }

    comp.registers["c"] = 1
    comp.execute(instructions)

    return comp.registers["a"]!!
}

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
