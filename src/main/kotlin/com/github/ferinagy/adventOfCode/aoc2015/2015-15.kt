package com.github.ferinagy.adventOfCode.aoc2015

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2015, "15-input")
    val test1 = readInputLines(2015, "15-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Long {
    val ingredients = input.map { Ingredient.parse(it) }
    val total = 100

    val combinations = combinations(ingredients.size, total)

    return combinations.maxOf { combo ->
        val zip = combo.zip(ingredients)
        val cap = zip.sumOf { (count, ingredient) -> count * ingredient.capacity }.coerceAtLeast(0)
        val dur = zip.sumOf { (count, ingredient) -> count * ingredient.durability }.coerceAtLeast(0)
        val fla = zip.sumOf { (count, ingredient) -> count * ingredient.flavor }.coerceAtLeast(0)
        val tex = zip.sumOf { (count, ingredient) -> count * ingredient.texture }.coerceAtLeast(0)

        cap.toLong() * dur * fla * tex
    }
}

private fun part2(input: List<String>): Long {
    val ingredients = input.map { Ingredient.parse(it) }
    val total = 100
    val targetCalories = 500

    val combinations = combinations(ingredients.size, total)

    return combinations.maxOf { combo ->
        val zip = combo.zip(ingredients)
        val cal = zip.sumOf { (count, ingredient) -> count * ingredient.calories }
        if (cal != targetCalories) return@maxOf 0L

        val cap = zip.sumOf { (count, ingredient) -> count * ingredient.capacity }.coerceAtLeast(0)
        val dur = zip.sumOf { (count, ingredient) -> count * ingredient.durability }.coerceAtLeast(0)
        val fla = zip.sumOf { (count, ingredient) -> count * ingredient.flavor }.coerceAtLeast(0)
        val tex = zip.sumOf { (count, ingredient) -> count * ingredient.texture }.coerceAtLeast(0)

        cap.toLong() * dur * fla * tex
    }
}

private fun combinations(size: Int, total: Int): List<List<Int>> {
    if (size == 1) return listOf(listOf(total))

    val result = mutableListOf<List<Int>>()
    for (i in 0..total) {
        val subResult = combinations(size - 1, total - i)
        subResult.forEach { combo ->
            result += combo + i
        }
    }

    return result
}

private data class Ingredient(
    val name: String,
    val capacity: Int,
    val durability: Int,
    val flavor: Int,
    val texture: Int,
    val calories: Int
) {
    companion object {
        fun parse(input: String): Ingredient {
            val (name, cap, dur, fla, tex, cal) = regex.matchEntire(input)!!.destructured
            return Ingredient(name, cap.toInt(), dur.toInt(), fla.toInt(), tex.toInt(), cal.toInt())
        }
    }
}

private val regex =
    """(\w+): capacity (-?\d+), durability (-?\d+), flavor (-?\d+), texture (-?\d+), calories (-?\d+)""".toRegex()
