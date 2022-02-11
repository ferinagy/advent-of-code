package com.github.ferinagy.adventOfCode.aoc2016

fun main() {
    println("Part1:")
    println(solve(testInput1, 0))
    println(solve(input, 7))
    println(solve2(7))
    println(solve2(12))

    println()
    println("Part2:")
    println(solve2(12))
}

private fun solve(input: String, value: Int): Long {
    val comp = AssembunnyComputer()
    val instructions = input.lines().map { AssembunnyComputer.Instruction.parse(it) }
    comp.setRegister("a", value.toLong())

    comp.execute(instructions)

    return comp.state.registers["a"]!!
}

private fun solve2(value: Int) = 81 * 94 + factorial(value)

private fun factorial(n: Int): Int = if (n == 1) 1 else n * factorial(n - 1)

private const val testInput1 = """cpy 2 a
tgl a
tgl a
tgl a
cpy 1 a
dec a
dec a"""

private const val input = """cpy a b
dec b
cpy a d
cpy 0 a
cpy b c
inc a
dec c
jnz c -2
dec d
jnz d -5
dec b
cpy b c
cpy c d
dec d
inc c
jnz d -2
tgl c
cpy -16 c
jnz 1 c
cpy 81 c
jnz 94 d
inc a
inc d
jnz d -2
inc c
jnz c -5"""
