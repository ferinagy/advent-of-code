package com.github.ferinagy.adventOfCode.aoc2022

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines

fun main() {
    val input = readInputLines(2022, "07-input")
    val testInput1 = readInputLines(2022, "07-test1")

    println("Part1:")
    part1(testInput1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(testInput1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Long {
    val root = exploreFiles(input)

    val sizes = root.calculateDirSizes()

    return sizes.filter { it <= 100000 }.sum()
}

private fun part2(input: List<String>): Long {
    val root = exploreFiles(input)

    val sizes = root.calculateDirSizes()
    val total = 70000000
    val min = 30000000
    val free = total - root.size
    val toDelete = min - free

    return sizes.filter { it >= toDelete }.minOrNull()!!
}

private fun exploreFiles(input: List<String>): Node.Dir {
    val root = Node.Dir("/", null)
    var current: Node.Dir = root

    input.forEach { line ->
        val parts = line.split(' ')
        when {
            parts.first() == "$" -> {
                when (parts[1]) {
                    "cd" -> {
                        current = when {
                            parts[2] == ".." -> current.parent!!
                            parts[2] == "/" -> root
                            else -> current.content[parts[2]] as Node.Dir
                        }
                    }
                    "ls" -> Unit
                    else -> error("unexpected '${parts[1]}")
                }
            }

            parts.first().toIntOrNull() != null -> {
                val next = Node.File(parts[1], current, parts[0].toLong())
                current.content[next.name] = next
            }

            parts.first() == "dir" -> {
                if (current.content[parts[1]] == null) {
                    current.content[parts[1]] = Node.Dir(parts[1], current)
                }
            }
        }
    }
    return root
}

private sealed class Node {

    abstract val size: Long
    data class Dir(val name: String, val parent: Dir?, val content: MutableMap<String, Node> = mutableMapOf(), override var size: Long = -1): Node()

    data class File(val name: String, val parent: Dir?, override val size: Long): Node()
}

private fun Node.calculateDirSizes(): List<Long> = when (this) {
    is Node.File -> emptyList()
    is Node.Dir -> {
        val sizes = mutableListOf<Long>()
        size = content.values.sumOf {
            sizes += it.calculateDirSizes()
            it.size
        }
        sizes += size
        sizes
    }
}

private fun Node.print(indent: Int = 0): Unit = when (this) {
    is Node.File -> println(" ".repeat(indent) + "- $name (file, size=$size)")
    is Node.Dir -> {
        println(" ".repeat(indent) + "- $name (dir: $size)")
        content.values.forEach { node: Node ->
            node.print(indent + 2)
        }
    }
}

