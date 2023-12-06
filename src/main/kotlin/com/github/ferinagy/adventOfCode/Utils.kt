package com.github.ferinagy.adventOfCode

import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInputLines(year: Int, name: String) =
    Path("src/main/kotlin/com/github/ferinagy/adventOfCode/aoc$year/$year-$name.txt").readLines()

fun readInputText(year: Int, name: String) =
    Path("src/main/kotlin/com/github/ferinagy/adventOfCode/aoc$year/$year-$name.txt").readText()

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)