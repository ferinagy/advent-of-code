package com.github.ferinagy.adventOfCode.aoc2024

import java.io.File
import java.util.*
import kotlin.time.measureTime

fun main(args: Array<String>) {
//    printTime {
        val lines = File(args[0]).readLines()
        val start = Step(Position(1, lines.size - 2))
        val end = Position(lines[0].length - 2, 1)
        val prev = IntArray(0x100000)
        val dists = IntArray(0x40000)

        val queue = CustomQueue(256, compareBy { dists[it] })
        queue += start

        fun backTrack(step: Step, result: MutableSet<Position>) {
            result += step.position
            repeat(4) {
                val x = prev[step.value * 4 + it]
                if (x != 0) backTrack(Step(x), result)
            }
        }

        while (queue.isNotEmpty()) {
            val current = queue.remove()
            val dist = dists[current.value]

            if (current.position == end) {
                val result = mutableSetOf<Position>()
                backTrack(current, result)
                println(result.size)
                break
            }

            val straight = current.position + current.direction
            if (lines[straight.y][straight.x] != '#') {
                addToQueue(
                    Step(position = straight, direction = current.direction),
                    dist + 1,
                    current,
                    prev,
                    dists,
                    queue
                )
            }
            val cw = current.direction.rotateCw()
            val right = current.position + cw
            if (lines[right.y][right.x] != '#') {
                addToQueue(
                    Step(position = right, direction = cw),
                    dist + 1001,
                    current,
                    prev,
                    dists,
                    queue
                )
            }
            val ccw = current.direction.rotateCcw()
            val left = current.position + ccw
            if (lines[left.y][left.x] != '#') {
                addToQueue(
                    Step(position = left, direction = ccw),
                    dist + 1001,
                    current,
                    prev,
                    dists,
                    queue
                )
            }
        }
//    }
}

private inline fun addToQueue(
    next: Step,
    newDist: Int,
    current: Step,
    prev: IntArray,
    dist: IntArray,
    queue: CustomQueue
) {
    if (dist[next.value] == 0 || newDist < dist[next.value]) {
        repeat(4) { prev[next.value * 4 + it] = 0 }
        prev[next.value * 4 + current.direction.value] = current.value
        dist[next.value] = newDist

        queue += next
    } else if (newDist == dist[next.value]) {
        prev[next.value * 4 + current.direction.value] = current.value
    }
}

@JvmInline
private value class Position(val value: Int) {
    constructor(x: Int, y: Int) : this(x.shl(8) or y)

    inline val x: Int
        get() = value.shr(8)

    inline val y: Int
        get() = value and 0xFF
}

@JvmInline
private value class Direction(val value: Int) {

    inline val x: Int
        get() = (value and 0b1) * (value.and(0b10) - 1)

    inline val y: Int
        get() = (value.inv() and 0b1) * (value.and(0b10) - 1)
}

private inline fun Direction.rotateCw() = Direction(value.inc().mod(4))
private inline fun Direction.rotateCcw() = Direction(value.dec().mod(4))

private inline operator fun Position.plus(dir: Direction) = Position(x = x + dir.x, y = y + dir.y)

@JvmInline
private value class Step(val value: Int) {
    constructor(
        position: Position,
        direction: Direction = Direction(3)
    ) : this(position.value.shl(2) or direction.value)

    inline val position: Position
        get() = Position(value.shr(2))

    inline val direction: Direction
        get() = Direction((value and 0b11))
}

private class CustomQueue(initialSize: Int, private val comparator: Comparator<Int>) {

    private val queue = IntArray(initialSize)
    var size = 0

    operator fun plusAssign(item: Step) {
        siftUp(size, item.value)
        size += 1
    }

    inline fun isNotEmpty() = size != 0

    fun remove(): Step {
        val result = Step(queue[0])

        size--
        siftDownUsingComparator()

        return result
    }

    private fun siftDownUsingComparator() {
        val x = queue[size]
        val half = size ushr 1
        var k = 0
        while (k < half) {
            var child = (k shl 1) + 1
            var c = queue[child]
            val right = child + 1
            if (right < size && comparator.compare(c, queue[right]) > 0) {
                c = queue[right]
                child = right
            }
            if (comparator.compare(x, c) <= 0) break

            queue[k] = c
            k = child
        }
        queue[k] = x
    }

    private fun siftUp(k: Int, x: Int) {
        var k = k
        while (k > 0) {
            val parent = (k - 1) ushr 1
            val e = queue[parent]
            if (comparator.compare(x, e) >= 0) break
            queue[k] = e
            k = parent
        }
        queue[k] = x
    }
}

private inline fun printTime(block: () -> Unit) {
    println(measureTime(block))
}