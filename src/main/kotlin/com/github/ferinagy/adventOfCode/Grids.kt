package com.github.ferinagy.adventOfCode

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

    private val array = BooleanArray(width * height) { initBlock(it % width, it / width) }

    override operator fun get(x: Int, y: Int): Boolean = array[x + y * width]
    override operator fun set(x: Int, y: Int, value: Boolean) {
        array[x + y * width] = value
    }

    override fun iterator() = array.iterator()

    fun count() = array.count { it }

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

    fun copyRow(row: Int): BooleanArray = array.copyOfRange(row * width, row.inc() * width)

    fun copyColumn(col: Int): BooleanArray = BooleanArray(height) { get(col, it) }
}

class IntGrid(override val width: Int, override val height: Int, initBlock: (Int, Int) -> Int): Grid<Int> {

    private val array = IntArray(width * height) { initBlock(it % width, it / width) }

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

fun List<List<Int>>.toIntGrid(): IntGrid {
    val width = first().size
    return IntGrid(width, size) { x, y -> get(y)[x] }
}

operator fun <T> Grid<T>.get(position: Coord2D): T = get(position.x, position.y)
