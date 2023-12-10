package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2022, "21-input")
    val testInput1 = readInputLines(2022, "21-test1")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(testInput1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Long {
    return MonkeyMath(input).part1()
}

private fun part2(input: List<String>): Long {
    return MonkeyMath(input).part2()
}

class MonkeyMath(input: List<String>) {

    private val root = run {
        val parsed = input.associate {
            val (monkey, op) = it.split(": ")
            monkey to op
        }
        parse("root", parsed)
    }

    fun part1(): Long {
        return (root.simplify() as Monkey.Number).number
    }

    fun part2(): Long {
        require(root is Monkey.Op)
        val m1 = root.m1.replaceHuman().simplify()
        val m2 = root.m2.replaceHuman().simplify()

        require(m1 is Monkey.Op)
        require(m2 is Monkey.Number)

        return m1.calculateForHuman(m2.number)
    }

    private fun parse(name: String, all: Map<String, String>): Monkey {
        val value = all[name]!!
        val num = value.toLongOrNull()
        if (num != null) return Monkey.Number(name, num)

        val (m1, op, m2) = value.split(' ')
        return Monkey.Op(name, op, parse(m1, all), parse(m2, all))
    }
    private sealed class Monkey {

        abstract fun replaceHuman(): Monkey
        abstract fun simplify(): Monkey

        data class Number(val name: String, val number: Long) : Monkey() {
            override fun replaceHuman() = if (name == "humn") Human else this

            override fun simplify(): Number = this

            override fun calculateForHuman(wanted: Long) = error("can't calculateForHuman on Number")
        }

        data class Op(val name: String, val type: String, val m1: Monkey, val m2: Monkey) : Monkey() {
            override fun replaceHuman(): Monkey = copy(m1 = m1.replaceHuman(), m2 = m2.replaceHuman())

            override fun simplify(): Monkey {
                val s1 = m1.simplify()
                val s2 = m2.simplify()
                return if (s1 is Number && s2 is Number) {
                    when (type) {
                        "+" -> Number(name, s1.number + s2.number)
                        "-" -> Number(name, s1.number - s2.number)
                        "*" -> Number(name, s1.number * s2.number)
                        "/" -> Number(name, s1.number / s2.number)
                        else ->  error("bad op: $type")
                    }
                } else {
                    this.copy(m1 = s1, m2 = s2)
                }
            }

            override fun calculateForHuman(wanted: Long) = when (type) {
                "+" -> {
                    if (m1 is Number) m2.calculateForHuman(wanted - m1.number) else {
                        m1.calculateForHuman(wanted - (m2 as Number).number)
                    }
                }
                "-" -> if (m1 is Number) m2.calculateForHuman(m1.number - wanted) else {
                    m1.calculateForHuman(wanted + (m2 as Number).number)
                }
                "*" -> if (m1 is Number) m2.calculateForHuman(wanted / m1.number) else {
                    m1.calculateForHuman(wanted / (m2 as Number).number)
                }
                "/" -> {
                    if (m1 is Number) m2.calculateForHuman(m1.number / wanted) else {
                        m1.calculateForHuman(wanted * (m2 as Number).number)
                    }
                }
                else ->  error("bad op: $type")
            }
        }

        data object Human : Monkey() {
            override fun replaceHuman() = error("already human")

            override fun simplify() = this

            override fun calculateForHuman(wanted: Long) = wanted
        }

        abstract fun calculateForHuman(wanted: Long): Long
    }
}
