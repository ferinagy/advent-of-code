package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import kotlin.math.max

fun main() {
    val input = readInputLines(2021, "18-input")
    val test1 = readInputLines(2021, "18-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Long {
    val snailNumbers = input.map { parse(it).first }
    val result = snailNumbers.reduce { acc, snailNumber -> acc + snailNumber }

    return result.magnitude()
}

private fun part2(input: List<String>): Long {
    var max = 0L
    for (i in input.indices) {
        for (j in input.indices) {
            if (i == j) continue

            val sum = (parse(input[i]).first + parse(input[j]).first).magnitude()
            max = max(max, sum)
        }
    }

    return max
}

private sealed class SnailNumber {

    var parent: Pair? = null

    abstract fun magnitude(): Long

    data class Number(var value: Int) : SnailNumber() {
        override fun magnitude(): Long {
            return value.toLong()
        }

        override fun toString(): String {
            return value.toString()
        }
    }

    data class Pair(var left: SnailNumber, var right: SnailNumber) : SnailNumber() {

        override fun toString() = "[$left,$right]"

        fun reduce(): Pair {
            while (true) {
                if (explode() || split()) continue

                return this
            }
        }

        fun explode(): Boolean {
            val toExplode = deeperThanFour() ?: return false

            leftNeighbor(toExplode)?.let {
                it.value += (toExplode.left as Number).value
            }

            rightNeighbor(toExplode)?.let {
                it.value += (toExplode.right as Number).value
            }

            val parent = toExplode.parent!!
            if (parent.left === toExplode) {
                parent.left = Number(0).also { it.parent = parent }
            } else {
                parent.right = Number(0).also { it.parent = parent }
            }

            return true
        }

        fun split(): Boolean {
            val toSplit = findChildToSplit() ?: return false

            val leftValue = toSplit.value / 2
            val rightValue = toSplit.value - leftValue
            val newLeft = Number(leftValue)
            val newRight = Number(rightValue)
            val newPair = Pair(newLeft, newRight).also {
                newLeft.parent = it
                newRight.parent = it
            }

            val parent = toSplit.parent!!
            if (parent.left === toSplit) {
                parent.left = newPair.also { it.parent = parent }
            } else {
                parent.right = newPair.also { it.parent = parent }
            }

            return true
        }

        override fun magnitude() = 3 * left.magnitude() + 2* right.magnitude()
    }
}

private fun SnailNumber.Pair.deeperThanFour(depth: Int = 0): SnailNumber.Pair? {
    if (depth == 4) return this

    val left = left
    if (left is SnailNumber.Pair) {
        val leftResult = left.deeperThanFour(depth + 1)
        if (leftResult != null) return leftResult
    }
    val right = right
    if (right is SnailNumber.Pair) {
        val rightResult = right.deeperThanFour(depth + 1)
        if (rightResult != null) return rightResult
    }

    return null
}

private fun SnailNumber.Pair.findChildToSplit(): SnailNumber.Number? {
    val left = left
    if (left is SnailNumber.Number) {
        if (10 <= left.value) return left
    } else {
        val leftSub = (left as SnailNumber.Pair).findChildToSplit()
        if (leftSub != null) return leftSub
    }

    val right = right
    if (right is SnailNumber.Number) {
        if (10 <= right.value) return right
    } else {
        val rightSub = (right as SnailNumber.Pair).findChildToSplit()
        if (rightSub != null) return rightSub
    }
    return null
}

private fun leftNeighbor(current: SnailNumber): SnailNumber.Number? {
    val parent = current.parent ?: return null

    val isRight = parent.right === current
    if (isRight) return rightMostNumber(parent.left)

    return leftNeighbor(parent)
}

private fun rightMostNumber(current: SnailNumber): SnailNumber.Number {
    if (current is SnailNumber.Number) return current

    return rightMostNumber((current as SnailNumber.Pair).right)
}

private fun rightNeighbor(current: SnailNumber): SnailNumber.Number? {
    val parent = current.parent ?: return null

    val isleft = parent.left === current
    if (isleft) return leftMostNumber(parent.right)

    return rightNeighbor(parent)
}

private fun leftMostNumber(current: SnailNumber): SnailNumber.Number {
    if (current is SnailNumber.Number) return current

    return leftMostNumber((current as SnailNumber.Pair).left)
}

private operator fun SnailNumber.plus(other: SnailNumber): SnailNumber {
    val pair = SnailNumber.Pair(this, other)
    this.parent = pair
    other.parent = pair
    return pair.reduce()
}

private fun parse(input: String, start: Int = 0) : Pair<SnailNumber, Int> {
    return if (input[start] == '[') {
        val (left, middle) = parse(input, start + 1)
        require(input[middle] == ',')
        val (right, end) = parse(input, middle + 1)
        require(input[end] == ']')

        SnailNumber.Pair(left, right).also {
            left.parent = it
            right.parent = it
        } to end + 1
    } else {
        SnailNumber.Number(input[start].digitToInt()) to start + 1
    }
}
