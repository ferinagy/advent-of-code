package com.github.ferinagy.adventOfCode

fun extendedEuclidean(a: Long, b: Long): Triple<Long, Long, Long> {
    var r = a to b
    var s = 1L to 0L
    var t = 0L to 1L

    while (r.second != 0L) {
        val q = r.first / r.second
        r = r.second to r.first - q * r.second
        s = s.second to s.first - q * s.second
        t = t.second to t.first - q * t.second
    }

    return Triple(r.first, s.first, t.first)
}

fun gcd(a: Long, b: Long): Long {
    return extendedEuclidean(a, b).first
}

fun gcd(a: Int, b: Int): Int = gcd(a.toLong(), b.toLong()).toInt()


fun lcm(a: Long, b: Long): Long {
    return a / gcd(a, b) * b
}
fun lcm(a: Int, b: Int): Int {
    return a / gcd(a, b) * b
}
