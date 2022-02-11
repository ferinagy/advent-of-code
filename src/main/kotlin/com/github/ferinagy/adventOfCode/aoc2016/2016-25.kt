package com.github.ferinagy.adventOfCode.aoc2016

fun main() {
    println("Final:")
    println(part1())
}

private fun part1(): Int {
    val comp = AssembunnyComputer()
    val instructions = input.lines().map { AssembunnyComputer.Instruction.parse(it) }.toMutableList()

    var index = 0
    while (true) {
        comp.state = AssembunnyComputer.State()
        comp.setRegister("a", index.toLong())
        val states = mutableMapOf<AssembunnyComputer.State, List<Long>>()
        while (true) {
            comp.step(instructions)
            val withoutOutput = comp.state.copy(output = emptyList())
            val previous = states[withoutOutput]
            if (previous != null && comp.state.output.drop(previous.size).verifyClock()) {
                return index
            }
            states[withoutOutput] = comp.state.output

            if (!comp.state.output.verifyClock()){
                break
            }
        }
        index++
    }
}

private fun List<Long>.verifyClock(): Boolean {
    forEachIndexed { index, l ->
        if (index % 2 == 0 && l != 0L || index % 2 == 1 && l != 1L) return false
    }

    return true
}

private const val input = """cpy a d
cpy 14 c
cpy 182 b
inc d
dec b
jnz b -2
dec c
jnz c -5
cpy d a
jnz 0 0
cpy a b
cpy 0 a
cpy 2 c
jnz b 2
jnz 1 6
dec b
dec c
jnz c -4
inc a
jnz 1 -7
cpy 2 b
jnz c 2
jnz 1 4
dec b
dec c
jnz 1 -4
jnz 0 0
out b
jnz a -19
jnz 1 -21"""

/*
0 cpy a d
1 cpy 14 c
2 cpy 182 b
3 inc d
4 dec b
5 jnz b -2 - jump to 3
6 dec c
7 jnz c -5 - jump to 2
8 cpy d a
9 jnz 0 0
10 cpy a b
11 cpy 0 a
12 cpy 2 c
13 jnz b 2 - jump to 15
14 jnz 1 6 - jump to 20
15 dec b
16 dec c
17 jnz c -4 - jump to 13
18 inc a
19 jnz 1 -7 - jump to 12
20 cpy 2 b
21 jnz c 2 - jump to 23
22 jnz 1 4 - jump to 26
23 dec b
24 dec c
25 jnz 1 -4 - jump to 21
26 jnz 0 0
27 out b
28 jnz a -19 - jump to 9
29 jnz 1 -21 - jump to 8
 */