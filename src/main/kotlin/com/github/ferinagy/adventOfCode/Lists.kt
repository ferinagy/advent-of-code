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