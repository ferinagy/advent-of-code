package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText
import kotlin.math.max
import kotlin.math.min


fun main() {
    val input1 = readInputText(2023, "05-input")
    val test1 = readInputText(2023, "05-test1")

    println("Part1:")
    part1(test1).println()
    part1(input1).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input1).println()
}

private fun part1(input: String): Long {
    val blocks = input.split("\n\n")

    val seeds = blocks[0].substring("seeds: ".length).split(" ").map { it.toLong() }
    val maps = blocks.drop(1).map { it.parseMapping() }

    val locations = seeds.map { seed ->
        maps.fold(seed) { acc: Long, mappings: List<Mapping> -> transform(acc, mappings) }
    }

    return locations.min()
}

private fun part2(input: String): Long {
    val blocks = input.split("\n\n")

    val seedsRanges = blocks[0].substring("seeds: ".length).split(" ").chunked(2).map {
        val start = it[0].toLong()
        val length = it[1].toLong()
        start..< start + length
    }

    val maps = blocks.drop(1).map { it.parseMapping() }

    val ranges = maps.fold(seedsRanges) { ranges, mappings ->
        ranges.flatMap { transform(it, mappings) }.sortedBy { it.first }.consolidate()
    }

    return ranges.minOf { it.first }
}

private fun String.parseMapping() = lines()
    .drop(1)
    .map {
        val values = it.split(" ").map(String::toLong)
        Mapping(values[0], values[1]..<values[1] + values[2])
    }
    .sortedBy { it.sourceRange.first }

private fun transform(value: Long, mappings: List<Mapping>): Long {
    val mapping = mappings.find { value in it.sourceRange } ?: return value

    return mapping.destination + value - mapping.sourceRange.first
}

private fun transform(range: LongRange, mappings: List<Mapping>): List<LongRange> = buildList {
    var mapIndex = mappings.indexOfFirst { !it.sourceRange.intersect(range).isEmpty()  }
    if (mapIndex == -1) {
        this += range
        return@buildList
    }

    var pos = range.first
    while (mapIndex < mappings.size && pos <= range.last) {
        val intersect = (pos ..range.last).intersect(mappings[mapIndex].sourceRange)
        if (!intersect.isEmpty()) {
            if (pos < intersect.first) {
                this += pos ..< intersect.first
            }
            val diff = mappings[mapIndex].destination - mappings[mapIndex].sourceRange.first
            this += intersect.first + diff .. intersect.last + diff

            pos = intersect.last + 1
            mapIndex++
        } else {
            this += pos ..range.last
            pos = range.last + 1
        }
    }
}

private fun LongRange.intersect(other: LongRange) = max(first, other.first) .. min(last, other.last)

private fun List<LongRange>.consolidate() = buildList<LongRange> {
    this@consolidate.forEach { range ->
        val prev = lastOrNull()
        this += if (prev == null || prev.last + 1 != range.first) {
            range
        } else {
            removeLast()
            prev.first..range.last
        }
    }
}

private data class Mapping(val destination: Long, val sourceRange: LongRange)
