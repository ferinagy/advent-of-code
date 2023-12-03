package com.github.ferinagy.adventOfCode

private fun extendedGcd(a: Int, b: Int): Int {
    var r = a to b
    var s = 1 to 0
    var t = 0 to 1

    while (r.second != 0) {
        val q = r.first / r.second
        r = r.second to r.first - q * r.second
        s = s.second to s.first - q * s.second
        t = t.second to t.first - q * t.second
    }

    println("Bézout coefficients: ${s.first}, ${t.first}")
    println("quotients by the gcd: ${s.second}, ${t.second}")
    return r.first
}
