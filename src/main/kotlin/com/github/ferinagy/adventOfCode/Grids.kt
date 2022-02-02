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

    private val array = BooleanArray(width * height) { initBlock(it / width, it % width) }

    override operator fun get(x: Int, y: Int): Boolean = array[x * width + y]
    override operator fun set(x: Int, y: Int, value: Boolean) {
        array[x * width + y] = value
    }

    override fun iterator() = array.iterator()
}

class IntGrid(override val width: Int, override val height: Int, initBlock: (Int, Int) -> Int): Grid<Int> {

    private val array = IntArray(width * height) { initBlock(it / width, it % width) }

    override operator fun get(x: Int, y: Int): Int = array[x * width + y]
    override operator fun set(x: Int, y: Int, value: Int) {
        array[x * width + y] = value
    }

    override fun iterator() = array.iterator()
}

class CharGrid(override val width: Int, override val height: Int, initBlock: (Int, Int) -> Char): Grid<Char> {

    private val array = CharArray(width * height) { initBlock(it / width, it % width) }

    override operator fun get(x: Int, y: Int): Char = array[x * width + y]
    override operator fun set(x: Int, y: Int, value: Char) {
        array[x * width + y] = value
    }

    override fun iterator() = array.iterator()
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
