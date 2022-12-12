package com.github.ferinagy.adventOfCode

import java.util.BitSet

interface Grid<T>: Iterable<T> {

    val width: Int
    val height: Int

    val xRange: IntRange
        get() = 0 until width
    val yRange: IntRange
        get() = 0 until height

    operator fun get(x: Int, y: Int): T
    operator fun set(x: Int, y: Int, value: T)
}

class BooleanGrid(override val width: Int, override val height: Int, initBlock: (Int, Int) -> Boolean): Grid<Boolean> {

    constructor(width: Int, height: Int, default: Boolean): this(width, height, { _, _  -> default })

    private val bitSet = BitSet(width * height).apply {
        repeat(width * height) {
            set(it, initBlock(it % width, it / width))
        }
    }

    override operator fun get(x: Int, y: Int): Boolean = bitSet[x + y * width]
    override operator fun set(x: Int, y: Int, value: Boolean) {
        bitSet[x + y * width] = value
    }

    override fun iterator() = object : Iterator<Boolean> {
        var current = 0

        override fun hasNext(): Boolean {
            return current < width * height
        }

        override fun next(): Boolean {
            return bitSet[current].also { current++ }
        }

    }

    fun count() = bitSet.cardinality()

    override fun toString(): String = buildString {
        append('┌')
        repeat(width) { append('─') }
        append('┐')
        append('\n')
        for (y in 0 until height) {
            append('│')
            for (x in 0 until width) {
                if (get(x, y)) append('#') else append('.')
            }
            append('│')
            append('\n')
        }
        append('└')
        repeat(width) { append('─') }
        append('┘')
    }

    fun copyRow(row: Int): BooleanArray = BooleanArray(width) { get(it, row) }

    fun copyColumn(col: Int): BooleanArray = BooleanArray(height) { get(col, it) }

    fun mapIndexed(transform: (Coord2D, Boolean) -> Boolean) = BooleanGrid(width, height) { x, y -> transform(Coord2D(x, y), get(x, y)) }
}

class IntGrid(override val width: Int, override val height: Int, initBlock: (Int, Int) -> Int): Grid<Int> {

    private val array = IntArray(width * height) { initBlock(it % width, it / width) }

    constructor(width: Int, height: Int, default: Int): this(width, height, { _, _ -> default})

    override operator fun get(x: Int, y: Int): Int = array[x + y * width]
    override operator fun set(x: Int, y: Int, value: Int) {
        array[x + y * width] = value
    }

    override fun iterator() = array.iterator()

    override fun toString(): String = buildString {
        val max = array.maxOrNull()!!
        val size = max.toString().length + 1
        val format = "%${size}d"

        for (y in 0 until height) {
            for (x in 0 until width) { append(format.format(get(x, y))) }
            append('\n')
        }
    }
}

class CharGrid(override val width: Int, override val height: Int, initBlock: (Int, Int) -> Char): Grid<Char> {

    private val array = CharArray(width * height) { initBlock(it % width, it / width) }

    override operator fun get(x: Int, y: Int): Char = array[x + y * width]
    override operator fun set(x: Int, y: Int, value: Char) {
        array[x + y * width] = value
    }

    override fun iterator() = array.iterator()

    override fun toString(): String = buildString {
        append('┌')
        repeat(width) { append('─') }
        append('┐')
        append('\n')
        for (y in 0 until height) {
            append('│')
            for (x in 0 until width) { append(get(x, y)) }
            append('│')
            append('\n')
        }
        append('└')
        repeat(width) { append('─') }
        append('┘')
    }
}

fun List<String>.toBooleanGrid(init: (Char) -> Boolean): BooleanGrid {
    val width = first().length
    return BooleanGrid(width, size) { x, y -> init(get(y)[x]) }
}

fun List<String>.toCharGrid(): CharGrid {
    val width = first().length
    return CharGrid(width, size) { x, y -> get(y)[x] }
}

fun List<List<Boolean>>.toBooleanGrid(): BooleanGrid {
    val width = first().size
    return BooleanGrid(width, size) { x, y -> get(y)[x] }
}

fun List<List<Int>>.toIntGrid(): IntGrid {
    val width = first().size
    return IntGrid(width, size) { x, y -> get(y)[x] }
}

operator fun <T> Grid<T>.get(position: Coord2D): T = get(position.x, position.y)
operator fun <T> Grid<T>.set(position: Coord2D, value: T) = set(position.x, position.y, value)
operator fun <T> Grid<T>.contains(position: Coord2D) = position.x in xRange && position.y in yRange
