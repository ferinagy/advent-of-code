package com.github.ferinagy.adventOfCode.aoc2021

import java.util.*

fun main(args: Array<String>) {
    println("Part1:")
    println(part1(input))

    println()
    println("Part2:")
    println(part2(input))
}

private fun part1(input: String): Long {
    val opposites = getOpposites(input)

    val result = Array(14) { 0 }
    opposites.forEach { (index1, val1, index2, val2) ->
        if (val1 < val2) {
            result[index1] = 9
            result[index2] = 9 - (val2 - val1)
        } else {
            result[index2] = 9
            result[index1] = 9 - (val1 - val2)
        }
    }

    return result.joinToString(separator = "").toLong()
}

private fun part2(input: String): Long {
    val opposites = getOpposites(input)

    val result = Array(14) { 0 }
    opposites.forEach { (index1, val1, index2, val2) ->
        if (val1 < val2) {
            result[index1] = 1 + (val2 - val1)
            result[index2] = 1
        } else {
            result[index2] = 1 + (val1 - val2)
            result[index1] = 1
        }
    }

    return result.joinToString(separator = "").toLong()
}

private fun getOpposites(input: String): MutableList<Opposites> {
    val ops = input.lines().windowed(18, step = 18).map {
        val willAdd = it[4].endsWith('1')
        val previousDiff = it[5].split(' ')[2].toInt()
        val thisDiff = it[15].split(' ')[2].toInt()

        InputOp(willAdd, if (willAdd) thisDiff else -previousDiff)
    }

    val stack = LinkedList<IndexedValue<InputOp>>()
    val opposites = mutableListOf<Opposites>()
    ops.withIndex().forEach {
        if (it.value.add) {
            stack.addFirst(it)
        } else {
            val other = stack.removeFirst()
            opposites += Opposites(it.index, it.value.value, other.index, other.value.value)
        }
    }
    return opposites
}

private data class InputOp(val add: Boolean, val value: Int)

private data class Opposites(val index1: Int, val diff1: Int, val index2: Int, val diff2: Int)

private const val input = """inp w
mul x 0
add x z
mod x 26
div z 1
add x 14
eql x w
eql x 0
mul y 0
add y 25
mul y x
add y 1
mul z y
mul y 0
add y w
add y 12
mul y x
add z y
inp w
mul x 0
add x z
mod x 26
div z 1
add x 11
eql x w
eql x 0
mul y 0
add y 25
mul y x
add y 1
mul z y
mul y 0
add y w
add y 8
mul y x
add z y
inp w
mul x 0
add x z
mod x 26
div z 1
add x 11
eql x w
eql x 0
mul y 0
add y 25
mul y x
add y 1
mul z y
mul y 0
add y w
add y 7
mul y x
add z y
inp w
mul x 0
add x z
mod x 26
div z 1
add x 14
eql x w
eql x 0
mul y 0
add y 25
mul y x
add y 1
mul z y
mul y 0
add y w
add y 4
mul y x
add z y
inp w
mul x 0
add x z
mod x 26
div z 26
add x -11
eql x w
eql x 0
mul y 0
add y 25
mul y x
add y 1
mul z y
mul y 0
add y w
add y 4
mul y x
add z y
inp w
mul x 0
add x z
mod x 26
div z 1
add x 12
eql x w
eql x 0
mul y 0
add y 25
mul y x
add y 1
mul z y
mul y 0
add y w
add y 1
mul y x
add z y
inp w
mul x 0
add x z
mod x 26
div z 26
add x -1
eql x w
eql x 0
mul y 0
add y 25
mul y x
add y 1
mul z y
mul y 0
add y w
add y 10
mul y x
add z y
inp w
mul x 0
add x z
mod x 26
div z 1
add x 10
eql x w
eql x 0
mul y 0
add y 25
mul y x
add y 1
mul z y
mul y 0
add y w
add y 8
mul y x
add z y
inp w
mul x 0
add x z
mod x 26
div z 26
add x -3
eql x w
eql x 0
mul y 0
add y 25
mul y x
add y 1
mul z y
mul y 0
add y w
add y 12
mul y x
add z y
inp w
mul x 0
add x z
mod x 26
div z 26
add x -4
eql x w
eql x 0
mul y 0
add y 25
mul y x
add y 1
mul z y
mul y 0
add y w
add y 10
mul y x
add z y
inp w
mul x 0
add x z
mod x 26
div z 26
add x -13
eql x w
eql x 0
mul y 0
add y 25
mul y x
add y 1
mul z y
mul y 0
add y w
add y 15
mul y x
add z y
inp w
mul x 0
add x z
mod x 26
div z 26
add x -8
eql x w
eql x 0
mul y 0
add y 25
mul y x
add y 1
mul z y
mul y 0
add y w
add y 4
mul y x
add z y
inp w
mul x 0
add x z
mod x 26
div z 1
add x 13
eql x w
eql x 0
mul y 0
add y 25
mul y x
add y 1
mul z y
mul y 0
add y w
add y 10
mul y x
add z y
inp w
mul x 0
add x z
mod x 26
div z 26
add x -11
eql x w
eql x 0
mul y 0
add y 25
mul y x
add y 1
mul z y
mul y 0
add y w
add y 9
mul y x
add z y"""
