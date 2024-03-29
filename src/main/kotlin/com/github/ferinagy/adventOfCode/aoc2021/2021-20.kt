package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.BooleanGrid
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText
import com.github.ferinagy.adventOfCode.toBooleanGrid

private typealias Image = BooleanGrid

fun main() {
    val input = readInputText(2021, "20-input")
    val test1 = readInputText(2021, "20-test1")
    val test2 = readInputText(2021, "20-test1")

    println("Part1:")
    part1(test1).println()
    part1(test2).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: String): Int {
    return enhance(input, 2)
}

private fun part2(input: String): Int {
    return enhance(input, 50)
}

private fun enhance(input: String, n: Int, print: Boolean = false): Int {
    val (algo, imagePart) = input.split("\n\n")
    require(algo.length == 512)

    var image = imagePart.lines().toBooleanGrid { it == '#' }
    if (print) {
        println(image.toImageString('.'))
    }

    val isBlinking = algo.first() == '#' && algo.last() == '.'
    repeat(n) {
        image = image.enhance(algo, it + 1)
        if (print) {
            println(image.toImageString(if (isBlinking && it % 2 == 0) '#' else '.'))
        }
    }

    return image.litPixels()
}

private fun Image.litPixels(): Int {
    return count { it }
}

private fun Image.toImageString(border: Char): String {
    return buildString {
        append(border.toString().repeat(width + 2))
        append('\n')
        for (y in 0 until height) {
            append(border)
            for (x in 0 until width) {
                if (get(x, y)) append('#') else append('.')
            }
            append(border)
            append('\n')
        }
        append(border.toString().repeat(width + 2))
        append('\n')
    }
}

private fun Image.enhance(algo: String, iteration: Int): Image {
    val isBlinking = algo.first() == '#' && algo.last() == '.'
    val defaultPixel = isBlinking && iteration % 2 == 0
    val newImage = BooleanGrid(width + 2, height + 2) { x, y ->
        isLight(x - 1, y - 1, algo, defaultPixel)
    }

    return newImage
}

private fun Image.isLight(x: Int, y: Int, algo: String, defaultPixel: Boolean): Boolean {
    var index = 0
    for (dy in -1..1) {
        for (dx in -1..1) {
            index = index shl 1
            val value = if (x + dx in xRange && y + dy in yRange) get(x + dx, y + dy) else defaultPixel
            if (value) index++
        }
    }
    require(index < 512) { "Bad index $index for $this" }
    return algo[index] == '#'
}
