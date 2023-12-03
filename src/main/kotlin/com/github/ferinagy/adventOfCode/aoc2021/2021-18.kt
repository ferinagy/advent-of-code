package com.github.ferinagy.adventOfCode.aoc2021

import kotlin.math.max

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
    val snailNumbers = input.lines().map { parse(it).first }
    val result = snailNumbers.reduce { acc, snailNumber -> acc + snailNumber }

    return result.magnitude()
}

private fun part2(input: String): Long {
    val lines = input.lines()

    var max = 0L
    for (i in lines.indices) {
        for (j in lines.indices) {
            if (i == j) continue

            val sum = (parse(lines[i]).first + parse(lines[j]).first).magnitude()
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

private const val testInput1 = """[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
[[[5,[2,8]],4],[5,[[9,9],0]]]
[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
[[[[5,4],[7,7]],8],[[8,3],8]]
[[9,3],[[9,9],[6,[4,9]]]]
[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]"""

private const val input = """[9,[[8,3],[6,9]]]
[[[0,[5,0]],[9,[1,0]]],[[8,0],[6,[3,3]]]]
[[5,[[4,1],[3,3]]],[[7,5],[[1,5],9]]]
[8,3]
[[[[0,5],3],2],[2,[6,0]]]
[[[0,8],[7,5]],6]
[[8,[9,[7,6]]],2]
[[[[2,3],9],[0,0]],[8,[[8,2],6]]]
[[[[8,7],[4,9]],[[0,1],9]],[[[2,1],[9,5]],2]]
[[5,[6,0]],[[1,[6,5]],[[3,4],2]]]
[[6,[5,6]],[6,5]]
[[[[6,0],0],3],[[[7,8],6],[[2,5],[8,8]]]]
[[[[1,4],[3,4]],[[1,3],7]],6]
[[[[9,7],[3,9]],2],9]
[[8,[1,7]],[[[9,4],3],5]]
[[[[9,9],[6,1]],5],[[2,6],[7,0]]]
[4,[[0,5],2]]
[[[1,8],7],[[5,[7,1]],[[2,6],8]]]
[[[3,[8,5]],9],[[6,1],[[8,1],3]]]
[[9,[[6,3],5]],[[[5,3],9],[[5,0],7]]]
[[[[3,4],[2,3]],[6,[2,1]]],[2,[7,1]]]
[[1,[9,[4,8]]],[4,9]]
[[0,[[5,0],0]],[[[6,6],[1,4]],8]]
[[[[0,9],[1,4]],[[3,4],3]],[1,[[7,7],[3,5]]]]
[8,[4,[[2,5],8]]]
[[7,6],[1,9]]
[[[1,[6,0]],1],[[[5,8],4],1]]
[[[6,[1,1]],[[3,0],[9,7]]],[[[2,3],[0,4]],[4,5]]]
[[3,9],[4,[6,1]]]
[[[5,2],[[4,1],2]],[[9,[9,2]],[5,[6,6]]]]
[[[2,5],[3,[5,5]]],[[8,[6,1]],[[1,3],[1,4]]]]
[[5,[3,2]],[[1,0],[[1,6],[0,3]]]]
[[[3,[3,0]],[[4,8],9]],[[[6,0],3],[1,[5,2]]]]
[[6,[8,1]],1]
[[[[8,6],4],[[2,0],[1,3]]],[8,7]]
[[[1,7],1],[[2,5],[[5,1],6]]]
[8,9]
[[6,[6,7]],[[[1,8],6],[6,[5,4]]]]
[[[[9,2],[2,4]],[[4,9],[5,0]]],[[[0,4],9],[[0,7],[6,2]]]]
[[0,4],0]
[[[[6,8],[8,9]],0],[[0,3],[[7,0],7]]]
[[6,[[9,6],6]],[[7,[5,4]],[7,6]]]
[[[7,7],[[6,8],[7,3]]],[[7,[9,8]],[[2,2],1]]]
[[[8,5],[[8,2],[7,4]]],[[9,[3,3]],[[5,1],[1,9]]]]
[2,6]
[[[3,[4,4]],[[5,4],[0,0]]],[[1,6],[1,[1,0]]]]
[[[8,9],[[0,1],[3,0]]],[[[1,8],1],[6,6]]]
[[[[9,2],[1,5]],7],[[8,2],3]]
[[[[0,5],1],[[8,1],[2,8]]],[[3,[8,4]],[[4,2],[0,9]]]]
[[[[2,8],[4,2]],9],[3,[3,[8,0]]]]
[[[2,[3,8]],[[6,8],1]],[[5,4],0]]
[[3,[7,9]],[[3,[8,6]],[2,1]]]
[[[3,6],[[4,4],[1,7]]],[4,0]]
[[7,[0,[7,6]]],[[[1,8],4],[4,[7,8]]]]
[[[[9,4],[2,9]],[[1,8],[1,4]]],[3,[0,8]]]
[[[7,2],[[0,7],[8,8]]],[3,[[5,9],3]]]
[[[[9,9],[3,1]],2],[[[2,3],1],[[8,9],2]]]
[[[[9,6],7],2],[[[0,8],7],[[6,9],2]]]
[[[[0,0],[7,7]],0],[[3,9],[0,[9,5]]]]
[[[1,[3,0]],[8,9]],[2,[[5,7],5]]]
[[[6,[7,2]],[9,0]],[6,[1,[2,7]]]]
[[[7,[0,2]],[1,[8,6]]],[[[8,9],5],[[1,5],[9,3]]]]
[[[[6,1],[2,0]],[6,8]],[8,[8,4]]]
[[[7,[2,8]],1],[[[9,4],[7,9]],[[7,6],[5,7]]]]
[[[8,3],[[4,1],9]],[[[6,7],1],[6,7]]]
[[[[2,2],4],[[0,3],[9,7]]],[[[9,0],7],0]]
[[[[8,7],[3,7]],[[1,9],0]],[[[4,8],7],[[2,0],1]]]
[[2,9],[8,1]]
[[[[6,4],[8,0]],[2,2]],[9,[5,[9,4]]]]
[[[[4,4],[7,3]],[3,0]],[[[4,5],5],[1,3]]]
[[[[2,0],5],[7,[2,1]]],[1,[[3,5],[9,5]]]]
[1,[[4,[3,6]],[[1,1],[2,3]]]]
[6,[3,[[1,6],5]]]
[[[9,5],[7,[8,3]]],[[9,8],[[6,6],5]]]
[[[[8,8],3],[7,[3,3]]],[5,[9,[2,5]]]]
[9,[[[1,0],6],[3,[3,3]]]]
[[[[4,0],[6,5]],5],[7,[8,1]]]
[7,[[[3,8],4],[[2,8],8]]]
[[[[1,0],[4,2]],[0,1]],[5,[[9,9],[6,9]]]]
[[[[1,8],[9,8]],[3,[4,2]]],0]
[[[[1,2],4],7],[[2,6],[7,3]]]
[3,[[0,[0,1]],[[3,0],[2,0]]]]
[[[8,[5,1]],[[4,8],2]],[3,7]]
[[9,[[1,3],[1,7]]],[[9,2],7]]
[[[[5,1],[2,6]],[[6,8],[7,9]]],[[[2,4],[2,0]],[6,0]]]
[[8,[4,[7,3]]],[6,[7,[2,5]]]]
[[8,[1,8]],4]
[[[2,[5,6]],[[3,0],[2,6]]],[[[2,7],6],[[6,3],0]]]
[[7,[8,6]],[[[0,0],4],[7,9]]]
[[3,[[2,7],1]],[[5,[3,4]],1]]
[[4,[[7,7],5]],[[[9,4],[6,0]],6]]
[[[8,5],[[1,0],[0,9]]],[[4,[3,2]],[1,3]]]
[1,[6,1]]
[[8,[9,[3,9]]],[[0,6],[0,[6,6]]]]
[8,[[[8,9],[5,1]],[[7,9],5]]]
[[[[4,8],0],[1,3]],[5,[1,0]]]
[[4,1],[[[7,3],4],[[0,4],[5,8]]]]
[[[[6,9],3],7],[[[5,3],[0,1]],[[7,7],[4,5]]]]
[[6,[8,5]],[[[7,8],[5,7]],[3,[7,8]]]]
[[[[1,3],2],[8,[9,5]]],[[3,[9,2]],[[9,0],[4,8]]]]"""
