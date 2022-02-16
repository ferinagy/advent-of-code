package com.github.ferinagy.adventOfCode.aoc2015

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
    val ingredients = input.lines().map { Ingredient.parse(it) }
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

private fun part2(input: String): Long {
    val ingredients = input.lines().map { Ingredient.parse(it) }
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

private const val testInput1 = """Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3"""

private const val input = """Frosting: capacity 4, durability -2, flavor 0, texture 0, calories 5
Candy: capacity 0, durability 5, flavor -1, texture 0, calories 8
Butterscotch: capacity -1, durability 0, flavor 5, texture 0, calories 6
Sugar: capacity 0, durability 0, flavor -2, texture 2, calories 1"""
