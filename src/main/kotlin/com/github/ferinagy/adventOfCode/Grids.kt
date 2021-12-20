package com.github.ferinagy.adventOfCode

class Grid<T>(val width: Int, val height: Int, private val array: Array<T>) {

    operator fun get(x: Int, y: Int): T = array[x * width + y]
}

class BooleanGrid(val width: Int, val height: Int, initBlock: (Int, Int) -> Boolean): Iterable<Boolean> {

    private val array = BooleanArray(width * height) { initBlock(it / width, it % width) }

    val xRange = 0 until width
    val yRange = 0 until height

    operator fun get(x: Int, y: Int): Boolean = array[x * width + y]
    operator fun set(x: Int, y: Int, value: Boolean) {
        array[x * width + y] = value
    }

    override fun iterator() = array.iterator()
}

fun List<String>.toBooleanGrid(init: (Char) -> Boolean): BooleanGrid {
    val width = first().length
    return BooleanGrid(width, size) { x, y -> init(get(y)[x]) }
}

inline fun <reified T> Grid(width: Int, height: Int, initBlock: (Int, Int) -> T) = Grid(
    width = width,
    height = height,
    array = Array(size = width * height) { initBlock(it / width, it % width) }
)
