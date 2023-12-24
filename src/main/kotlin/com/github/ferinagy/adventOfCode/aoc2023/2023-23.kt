package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.CharGrid
import com.github.ferinagy.adventOfCode.Coord2D
import com.github.ferinagy.adventOfCode.contains
import com.github.ferinagy.adventOfCode.get
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import com.github.ferinagy.adventOfCode.toCharGrid

fun main() {
    val input = readInputLines(2023, "23-input")
    val test1 = readInputLines(2023, "23-test1")

    println("Part1:")
    part1(test1).println()
    part1(input).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    val start = Coord2D(input.first().indexOf('.'), 0)
    val end = Coord2D(input.last().indexOf('.'), input.lastIndex)

    val nodes = parseNodes(input)

    return longestDist(start, end, nodes.keys, nodes)!!
}

private fun part2(input: List<String>): Int {
    val start = Coord2D(input.first().indexOf('.'), 0)
    val end = Coord2D(input.last().indexOf('.'), input.lastIndex)

    val nodes = parseNodes(input)
    nodes.forEach { (node, connections) ->
        connections.forEach { (other, dist) ->
            val otherConnections = nodes[other]!!
            if (node !in otherConnections) {
                otherConnections[node] = dist
            }
        }
    }

    return longestDist(start, end, nodes.keys, nodes)!!
}

private fun longestDist(start: Coord2D, end: Coord2D, subset: Set<Coord2D>, nodes: Map<Coord2D, Map<Coord2D, Int>>): Int? {
    if (start == end) return 0

    val next = nodes[start]!!.filter { it.key in subset }.mapNotNull { (connection, steps) ->
        longestDist(connection, end, subset - start, nodes)?.let { it + steps }
    }

    return next.maxOrNull()
}

private fun parseNodes(input: List<String>): MutableMap<Coord2D, MutableMap<Coord2D, Int>> {
    val grid = input.toCharGrid()

    val nodes = mutableMapOf<Coord2D, MutableMap<Coord2D, Int>>()
    for (x in grid.xRange) {
        for (y in grid.yRange) {
            if (grid[x, y] != '#') {
                val coord = Coord2D(x, y)
                val neighbors = coord.adjacent(false).filter { it in grid && grid[it] != '#' }
                if (neighbors.size != 2) {
                    nodes[coord] = mutableMapOf()
                }
            }
        }
    }



    nodes.forEach { (node, connections) ->
        connections += dfs(node, grid, nodes)
    }
    return nodes
}

private fun dfs(
    current: Coord2D,
    grid: CharGrid,
    nodes: MutableMap<Coord2D, MutableMap<Coord2D, Int>>,
    steps: Int = 0,
    isStart: Boolean = true,
    visited: MutableSet<Coord2D> = mutableSetOf()
): List<Pair<Coord2D, Int>> {
    visited += current

    val connections = current.adjacent(false).filter { it in grid && grid[it] != '#' && it !in visited }
    if (isStart) {
        return connections.flatMap {
            dfs(it, grid, nodes, steps + 1, false, visited)
        }
    } else {
        if (connections.size != 1) {
            return listOf(current to steps)
        } else {
            val single = connections.single()
            if (grid[single] != '.'  && slopes[grid[single]]!! != single - current) return emptyList()

            return dfs(single, grid, nodes, steps + 1, false, visited)
        }
    }
}

private fun addEdge(
    src: Coord2D,
    dest: Coord2D,
    steps: Int,
    oneWay: Boolean?,
    nodes: MutableMap<Coord2D, MutableMap<Coord2D, Int>>
) {
    when (oneWay) {
        true -> {
            nodes[src]!![dest] = steps
        }

        false -> {
            nodes[dest]!![src] = steps
        }

        null -> {
            nodes[src]!![dest] = steps
            nodes[dest]!![src] = steps
        }
    }
}

private val slopes = mapOf(
    '>' to Coord2D(1, 0),
    '<' to Coord2D(-1, 0),
    'v' to Coord2D(0, 1),
    '^' to Coord2D(0, -1),
)
