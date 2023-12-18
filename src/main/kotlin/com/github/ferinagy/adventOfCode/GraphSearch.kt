package com.github.ferinagy.adventOfCode

import java.util.*

fun <T : Any> searchGraph(
    startingSet: Set<T>,
    isDone: (T) -> Boolean,
    nextSteps: (T) -> Set<Pair<T, Int>>,
    computePath: Boolean = false,
    heuristic: (T) -> Int = { 0 },
    allowDuplicatesInQueue: Boolean = true
): Int {
    val dists = mutableMapOf<T, Int>()
    startingSet.forEach { dists[it] = 0 }
    val queue = PriorityQueue(compareBy<T> { dists[it]!! + heuristic(it) })
    queue += startingSet

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

        for ((next, stepSize) in nextSteps(current)) {
            val nextDist = dist + stepSize

            if (dists[next] == null || nextDist < dists[next]!!) {
                dists[next] = nextDist

                queue += next

                if (computePath) paths[next] = current
            } else if (nextDist < dists[next]!!) {
                dists[next] = nextDist

                if (!allowDuplicatesInQueue) queue -= next
                queue += next

                if (computePath) paths[next] = current
            }
        }
    }

    return -1
}

fun <T : Any> searchGraph(
    start: T,
    isDone: (T) -> Boolean,
    nextSteps: (T) -> Set<Pair<T, Int>>,
    computePath: Boolean = false,
    heuristic: (T) -> Int = { 0 },
    allowDuplicatesInQueue: Boolean = true
): Int = searchGraph(
    startingSet = setOf(start),
    isDone = isDone,
    nextSteps = nextSteps,
    computePath = computePath,
    heuristic = heuristic,
    allowDuplicatesInQueue = allowDuplicatesInQueue
)

fun <T> Set<T>.singleStep() = map { it to 1 }.toSet()

private fun <T> reconstructPath(end: T, cameFrom: Map<T, T>): List<T> = buildList<T> {
    var current: T? = end
    while (current != null) {
        this += current
        current = cameFrom[current]
    }
}.asReversed()
