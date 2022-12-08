package com.github.ferinagy.adventOfCode.aoc2017

class KnotHasher(size: Int = 256) {
    val array = IntArray(size) { it }

    private val temp = IntArray(size)

    private var position = 0
    private var skip = 0

    fun round(length: Int) {
        val rest = length.coerceAtMost(array.size - position)
        val overflow = length - rest
        array.copyInto(temp, destinationOffset = 0, startIndex = position, endIndex = position + rest)
        array.copyInto(temp, destinationOffset = rest, startIndex = 0, endIndex = overflow)

        temp.reverse(0, length)

        temp.copyInto(array, destinationOffset = position, startIndex = 0, endIndex = rest)
        temp.copyInto(array, destinationOffset = 0, startIndex = rest, endIndex = length)

        position += length + skip
        position %= array.size
        skip++
    }

    fun hash(input: String): String {
        val lengths = input.map { it.code } + listOf(17, 31, 73, 47, 23)

        repeat(64) {
            lengths.forEach { length ->
                round(length)
            }
        }

        return array.toList().windowed(16, 16)
            .map { window -> window.reduce { acc, i -> acc xor i } }
            .map { "%02x".format(it) }
            .joinToString(separator = "")
    }

}