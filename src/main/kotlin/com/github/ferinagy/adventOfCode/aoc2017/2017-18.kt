package com.github.ferinagy.adventOfCode.aoc2017

fun main() {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput2))
    println(part2(input))
}

private fun part1(input: String): Long {
    val p = Program(input)

    var sound = -1L
    p.onSend = { sound = it }
    var result = -1L
    p.preReceive = {
        if (it != 0L) {
            result = sound
        }
        p.queue += 0
    }

    while (p.isRunning) {
        p.step()
        if (result != -1L) {
            return result
        }
    }

    error("No rcv?")
}

private fun part2(input: String): Int {
    val p0 = Program(input).apply { setRegister("p", 0) }
    val p1 = Program(input).apply { setRegister("p", 1) }

    var count = 0

    p0.onSend = { p1.queue += it }
    p1.onSend = {
        count++
        p0.queue += it
    }

    while (p0.isRunning || p1.isRunning) {
        p0.step()
        p1.step()
    }

    return count
}

private class Program(input: String) {

    private val commands = input.lines()
    private var position = 0

    private val registers = mutableMapOf<String, Long>()

    val queue = mutableListOf<Long>()

    var onSend: (Long) -> Unit = {}

    var isRunning = true
        private set

    var preReceive: ((current: Long) -> Unit)? = null
    private fun String.value() = toLongOrNull() ?: registers.getOrDefault(this, 0)

    fun setRegister(name: String, value: Long) {
        registers[name] = value
    }

    fun step() {
        if (position !in commands.indices) {
            isRunning = false
            return
        }

        val parts = commands[position].split(' ')
        when (parts.first()) {
            "snd" -> onSend(parts[1].value())
            "set" -> registers[parts[1]] = parts[2].value()
            "add" -> registers[parts[1]] = parts[1].value() + parts[2].value()
            "mul" -> registers[parts[1]] = parts[1].value() * parts[2].value()
            "mod" -> registers[parts[1]] = parts[1].value() % parts[2].value()
            "rcv" -> {
                preReceive?.invoke(parts[1].value())
                if (queue.isNotEmpty()) {
                    registers[parts[1]] = queue.removeFirst()
                    isRunning = true
                } else {
                    isRunning = false
                    return
                }
            }
            "jgz" -> if (parts[1].value() > 0) {
                position += parts[2].value().toInt()
                return
            }
        }
        position++
    }
}

private const val testInput1 = """set a 1
add a 2
mul a a
mod a 5
snd a
set a 0
rcv a
jgz a -1
set a 1
jgz a -2"""

private const val testInput2 = """snd 1
snd 2
snd p
rcv a
rcv b
rcv c
rcv d"""

private const val input = """set i 31
set a 1
mul p 17
jgz p p
mul a 2
add i -1
jgz i -2
add a -1
set i 127
set p 464
mul p 8505
mod p a
mul p 129749
add p 12345
mod p a
set b p
mod b 10000
snd b
add i -1
jgz i -9
jgz a 3
rcv b
jgz b -1
set f 0
set i 126
rcv a
rcv b
set p a
mul p -1
add p b
jgz p 4
snd a
set a b
jgz 1 3
snd b
set f 1
add i -1
jgz i -11
snd a
jgz f -16
jgz a -19"""
