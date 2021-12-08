package com.github.ferinagy.adventOfCode

class TspGraph {

    private val edges = mutableMapOf<String, MutableMap<String, Int>>()

    private data class CacheEntry(val node: String, val remaining: Set<String>)

    private val cache = mutableMapOf<CacheEntry, Progress>()

    fun addBidirectionalEdge(from: String, to: String, weight: Int) {
        val e1 = edges.getOrPut(from) { mutableMapOf() }
        val e2 = edges.getOrPut(to) { mutableMapOf() }

        e1[to] = weight
        e2[from] = weight
    }

    fun solveTsp(comparator: Comparator<Int>): Int {
        val nodes = edges.keys

        return nodes.map {
            solveTsp(it, nodes - it, comparator)
        }.minOfWith(comparator) { it.dist }
    }

    private fun solveTsp(node: String, remaining: Set<String>, comparator: Comparator<Int>): Progress {
        val cacheKey = CacheEntry(node, remaining)
        if (cacheKey in cache) return cache[cacheKey]!!

        if (remaining.isEmpty()) {
            return Progress(node, setOf(node), listOf(node), 0).also { cache[cacheKey] = it }
        }

        val subTasks = remaining.map {
            solveTsp(it, remaining - it, comparator)
        }

        val result = subTasks.map {
            val dist = edges[it.end]!![node]!!
            Progress(node, it.visited + node, it.path + node, dist + it.dist)
        }.minWithOrNull { o1, o2 -> comparator.compare(o1.dist, o2.dist) }!!

        return result.also { cache[cacheKey] = it }
    }

    data class Progress(val end: String, val visited: Set<String>, val path: List<String>, val dist: Int)
}