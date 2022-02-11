package com.github.ferinagy.adventOfCode.aoc2016

class AssembunnyComputer {
    val registers = mutableMapOf(
        "a" to 0L,
        "b" to 0L,
        "c" to 0L,
        "d" to 0L,
    )

    fun execute(originalInstructions: List<Instruction>) {
        val instructions = originalInstructions.toMutableList()
        var ip = 0
        while (ip in instructions.indices) {
            when (val instruction = instructions[ip]) {
                is Instruction.Cpy -> {
                    if (instruction.into is Argument.Register) {
                        registers[instruction.into.value] = instruction.from.get()
                    }
                }
                is Instruction.Dec -> {
                    if (instruction.register is Argument.Register) {
                        registers[instruction.register.value] = registers[instruction.register.value]!! - 1
                    }
                }
                is Instruction.Inc -> {
                    if (instruction.register is Argument.Register) {
                        registers[instruction.register.value] = registers[instruction.register.value]!! + 1
                    }
                }
                is Instruction.Jnz -> {
                    val check = instruction.value.get()
                    if (check != 0L) {
                        ip += instruction.offset.get().toInt()
                        continue
                    }
                }
                is Instruction.Tgl -> {
                    val index = ip + instruction.offset.get().toInt()
                    if (index in instructions.indices) {
                        instructions[index] = instructions[index].toggle()
                    }
                }
            }

            ip++
        }
    }

    private fun Instruction.toggle() = when(this) {
        is Instruction.Cpy -> Instruction.Jnz(from, into)
        is Instruction.Dec -> Instruction.Inc(register)
        is Instruction.Inc -> Instruction.Dec(register)
        is Instruction.Jnz -> Instruction.Cpy(value, offset)
        is Instruction.Tgl -> Instruction.Inc(offset)
    }

    fun Argument.get(): Long = when (this) {
        is Argument.Constant -> value
        is Argument.Register -> registers[value]!!
    }

    sealed class Argument {
        data class Register(val value: String) : Argument()
        data class Constant(val value: Long) : Argument()

        companion object {
            fun parse(value: String): Argument {
                return value.toLongOrNull()?.let { Constant(it) } ?: Register(value)
            }
        }
    }

    sealed class Instruction {
        class Cpy(val from: Argument, val into: Argument) : Instruction()
        class Inc(val register: Argument) : Instruction()
        class Dec(val register: Argument) : Instruction()
        class Jnz(val value: Argument, val offset: Argument) : Instruction()
        class Tgl(val offset: Argument) : Instruction()

        companion object {
            fun parse(input: String): Instruction {
                return when {
                    cpyRegex.matches(input) -> {
                        val (value, into) = cpyRegex.matchEntire(input)!!.destructured
                        Cpy(Argument.parse(value), Argument.parse(into))
                    }
                    incRegex.matches(input) -> Inc(Argument.parse(incRegex.matchEntire(input)!!.groupValues[1]))
                    decRegex.matches(input) -> Dec(Argument.parse(decRegex.matchEntire(input)!!.groupValues[1]))
                    jnzRegex.matches(input) -> {
                        val (reg, offset) = jnzRegex.matchEntire(input)!!.destructured
                        Jnz(Argument.parse(reg), Argument.parse(offset))
                    }
                    tglRegex.matches(input) -> {
                        val (offset) = tglRegex.matchEntire(input)!!.destructured
                        Tgl(Argument.parse(offset))
                    }
                    else -> error("Unknown instruction: $input")
                }
            }
        }
    }
}

private val cpyRegex = """cpy (-?\w+) (-?\w+)""".toRegex()
private val incRegex = """inc (-?\w+)""".toRegex()
private val decRegex = """dec (-?\w+)""".toRegex()
private val jnzRegex = """jnz (-?\w+) (-?\w+)""".toRegex()
private val tglRegex = """tgl (-?\w+)""".toRegex()