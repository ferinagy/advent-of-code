package com.github.ferinagy.adventOfCode

import java.util.*

fun <T : Any> searchGraph(
    start: T,
    isDone: (T) -> Boolean,
    nextSteps: (T) -> Set<T>,
    computePath: Boolean = false,
    heuristic: (T) -> Int = { 0 }
): Int {
    val dists = mutableMapOf(start to 0)
    val queue = PriorityQueue(compareBy<T> { dists[it]!! + heuristic(it) })
    queue += start

    val paths = mutableMapOf<T, T>()
    while (queue.isNotEmpty()) {
        val current: T = queue.remove()

        val dist = dists[current]!!

        if (isDone(current)) {
            if (computePath) {
                reconstructPath(current, paths).forEach { println(it) }
            }
            return dist
        }

        for (next in nextSteps(current)) {
            val nextDist = dist + 1

            if (dists[next] == null) {
                dists[next] = nextDist

                queue += next

                if (computePath) paths[next] = current
            } else if (nextDist < dists[next]!!) {
                dists[next] = nextDist

                queue -= next
                queue += next

                if (computePath) paths[next] = current
            }
        }
    }

    return -1
}

private fun <T> reconstructPath(end: T, cameFrom: Map<T, T>): List<T> = buildList<T> {
    var current: T? = end
    while (current != null) {
        this += current
        current = cameFrom[current]
    }
}.asReversed()
