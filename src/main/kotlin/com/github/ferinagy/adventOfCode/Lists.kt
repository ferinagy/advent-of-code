package com.github.ferinagy.adventOfCode

fun <T> List<List<T>>.transpose(): List<List<T>> {
    if (isEmpty()) return emptyList()

    val innerSize = size
    val outerSize = first().size

    return List(outerSize) { outer ->
        List(innerSize) { inner ->
            this[inner][outer]
        }
    }
}

fun <T> Set<T>.subSets(): Set<Set<T>> {
    if (isEmpty()) return setOf(emptySet())

    val first = first()

    val sub = (this - first).subSets()

    return sub + sub.map { it + first }
}

fun <T> Set<T>.subSets(size: Int): Set<Set<T>> {
    if (size == 0) return setOf(emptySet())

    return flatMap {
        val set: Set<Set<T>> = (this - it).subSets(size - 1)
        set.map { s -> s + it }
    }.toSet()
}