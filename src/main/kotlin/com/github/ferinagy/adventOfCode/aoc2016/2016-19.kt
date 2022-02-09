package com.github.ferinagy.adventOfCode.aoc2016

import java.util.*

fun main() {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput1))
    println(part2(input))
}

private fun part1(input: Int): Int {
    val list = LinkedList<Int>()
    repeat(input) {
        list += it + 1
    }
    val iter = list.circularIterator()
    while (list.size != 1) {
        iter.next()
        iter.remove()
        iter.next()
    }

    return list.first
}

private fun part2(input: Int): Int {
    val list = LinkedList<Int>()
    repeat(input) {
        list += it + 1
    }
    val iter = list.circularIterator()
    repeat(input / 2) { iter.next() }
    while (list.size != 1) {
        iter.remove()
        iter.next()
        if (list.size % 2 == 0) iter.next()
    }

    return list.first
}

private fun <T> MutableList<T>.circularIterator(): MutableIterator<T> = object : MutableIterator<T> {

    var iterator = this@circularIterator.iterator().also { it.next() }

    override fun hasNext() = true

    override fun next(): T {
        if (!iterator.hasNext()) iterator = this@circularIterator.iterator()
        return iterator.next()
    }

    override fun remove() {
        iterator.remove()
    }

}

private const val testInput1 = 5

private const val input = 3001330

// 1 2 3 4 5 6 7
// _ _ _ _ _ _ 7
