package com.github.ferinagy.adventOfCode.aoc2024

import java.io.File
import java.util.*
import kotlin.time.measureTime

fun main(args: Array<String>) {
    val lines = File(args[0]).readLines()
    val start = Step(Position(1, lines.size - 2))
    val end = Position(lines[0].length - 2, 1)
    val dists = arrayOfNulls<Pair<Int, Array<Step?>>>(0x40000)
    dists[start.value] = 0 to arrayOfNulls(4)

    val queue = CustomQueue(256, compareBy<Step> { dists[it.value]!!.first })
    queue += start

    fun backTrack(step: Step, result: MutableSet<Position>) {
        result += step.position
        dists[step.value]!!.second.forEach {
            if (it != null) backTrack(it, result)
        }
    }

    while (queue.isNotEmpty()) {
        val current = queue.remove()
        val dist = dists[current.value]!!

        if (current.position == end) {
            val result = mutableSetOf<Position>()
            backTrack(current, result)
            println(result.size)
            break
        }

        if (lines[current.position.y + current.direction.y][current.position.x + current.direction.x] != '#') {
            addToQueue(
                Step(position = current.position + current.direction, direction = current.direction),
                dist.first + 1,
                current,
                dists,
                queue
            )
        }
        val cw = current.direction.rotateCw()
        if (lines[current.position.y + cw.y][current.position.x + cw.x] != '#') {
            addToQueue(
                Step(position = current.position + cw, direction = cw),
                dist.first + 1001,
                current,
                dists,
                queue
            )
        }
        val ccw = current.direction.rotateCcw()
        if (lines[current.position.y + ccw.y][current.position.x + ccw.x] != '#') {
            addToQueue(
                Step(position = current.position + ccw, direction = ccw),
                dist.first + 1001,
                current,
                dists,
                queue
            )
        }
    }
}

private inline fun addToQueue(
    next: Step,
    newDist: Int,
    current: Step,
    dists: Array<Pair<Int, Array<Step?>>?>,
    queue: CustomQueue<Step>
) {
    if (dists[next.value] == null || newDist < dists[next.value]!!.first) {
        val newSet = arrayOfNulls<Step?>(4).also { it[current.direction.value] = current }
        dists[next.value] = newDist to newSet
        queue += next
    } else if (newDist == dists[next.value]?.first) {
        dists[next.value]!!.second[current.direction.value] = current
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

private class CustomQueue<T>(initialSize: Int, private val comparator: Comparator<T>) {

    private val queue = arrayOfNulls<Any>(initialSize)
    var size = 0

    operator fun plusAssign(item: T) {
        siftUp(size, item)
        size += 1
    }

    inline fun isNotEmpty() = size != 0

    fun remove(): T {
        val result = queue[0] as T?

        size--
        siftDownUsingComparator()
        queue[size] = null

        return result!!
    }

    private fun siftDownUsingComparator() {
        val x: T = queue[size] as T
        val half = size ushr 1
        var k = 0
        while (k < half) {
            var child = (k shl 1) + 1
            var c = queue[child]
            val right = child + 1
            if (right < size && comparator.compare(c as T, queue[right] as T) > 0) {
                c = queue[right]
                child = right
            }
            if (comparator.compare(x, c as T) <= 0) break

            queue[k] = c
            k = child
        }
        queue[k] = x
    }

    private fun siftUp(k: Int, x: T) {
        var k = k
        while (k > 0) {
            val parent = (k - 1) ushr 1
            val e = queue[parent]
            if (comparator.compare(x, e as T) >= 0) break
            queue[k] = e
            k = parent
        }
        queue[k] = x
    }
}

private inline fun printTime(block: () -> Unit) {
    println(measureTime(block))
}