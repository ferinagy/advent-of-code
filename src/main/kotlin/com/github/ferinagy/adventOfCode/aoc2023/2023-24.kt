package com.github.ferinagy.adventOfCode.aoc2023

import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputLines
import java.math.BigInteger

fun main() {
    val input = readInputLines(2023, "24-input")
    val test1 = readInputLines(2023, "24-test1")

    println("Part1:")
    part1(test1, 7.toLong()..27).println()
    part1(input, 200000000000000 .. 400000000000000).println()

    println()
    println("Part2:")
    part2(test1).println()
    part2(input).println()
}

private fun part1(input: List<String>, testArea: LongRange): Long {
    val parsed = input.map { line ->
        val (p, v) = line.split(""" @\s+""".toRegex())
        val position = p.split(""",\s+""".toRegex()).map(String::toBigInteger)
        val velocity = v.split(""",\s+""".toRegex()).map(String::toBigInteger)
        position to velocity
    }

    var result = 0L
    for (i in 0..parsed.size - 2) {
        for (j in i + 1..<parsed.size) {
            val (p1, v1) = parsed[i]
            val (p2, v2) = parsed[j]
            val intersect = intersectsWithin(p1, v1, p2, v2, testArea)

            if (intersect) result++
        }
    }

    return result
}

private fun part2(input: List<String>, print: Boolean = false): BigInteger {
    val parsed = input.map { line ->
        val (p, v) = line.split(""" @\s+""".toRegex())
        val position = p.split(""",\s+""".toRegex()).map(String::toBigInteger)
        val velocity = v.split(""",\s+""".toRegex()).map(String::toBigInteger)
        position to velocity
    }

    val equations = parsed.take(5).flatMap { s ->
        (0..1).map { i ->
            Pair(s.first[i], s.second[i])
        }
    }
    if (print) println("Starting equations")
    val vars = listOf('x', 'y')
    equations.forEachIndexed { index, item ->
        val (p, v) = item
        val variable = vars[index % 2]
        val num = index / 2 + 1
        if (print) println("$p ${v.withSign()} * t${num} = $variable + v$variable * t$num")
    }

    if (print) println()
    if (print) println("Substitution for ts")
    val subs = equations.mapIndexed { index, item ->
        val (p, v) = item
        val qs = listOf(-p, v, (index % 2).toBigInteger(), (index / 2 + 1).toBigInteger())
        val variable = vars[qs[2].toInt()]
        if (print) println("t${qs[3]} = ($variable ${qs[0].withSign()}) / (${qs[1]} - v$variable)")
        qs
    }

    if (print) println()
    if (print) println("Removed ts")
    val e2 = (0..4).map { i ->
        val s1 = subs[i * 2]
        val s2 = subs[i * 2 + 1]
        val qs = listOf(s2[1], -s1[1], s2[0], -s1[0], s1[0] * s2[1] - s2[0] * s1[1])
        if (print) println("x * vy - y * vx = ${qs[0]} * x ${(qs[1]).withSign()} * y ${(qs[2]).withSign()} * vx ${(qs[3]).withSign()} * vy ${(qs[4]).withSign()}")
        qs
    }

    if (print) println()
    if (print) println("Linearize")
    val s1 = e2.first()
    val e3 = e2.drop(1).map { s2 ->
        val qs = listOf(s1[0] - s2[0], s1[1] - s2[1], s1[2] - s2[2], s1[3] - s2[3], s1[4] - s2[4])
        if (print) println("${qs[0]} * x ${(qs[1]).withSign()} * y ${(qs[2]).withSign()} * vx ${(qs[3]).withSign()} * vy ${qs[4].withSign()} = 0")
        qs.toMutableList()
    }.toMutableList()

    val svy = e3.first()
    if (print) println()
    if (print) println("Eliminate vy = (${svy[0]} * x ${(svy[1]).withSign()} * y ${(svy[2]).withSign()} * vx ${svy[4].withSign()}) / ${(-svy[3])}")
    val e4 = e3.drop(1).map { s ->
        val qs = listOf(s[0] * -svy[3] + svy[0] * s[3], s[1] * -svy[3] + svy[1] * s[3], s[2] * -svy[3] + svy[2] * s[3], svy[4] * s[3] + s[4] * -svy[3])
        if (print) println("${qs[0]} * x ${(qs[1]).withSign()} * y ${(qs[2]).withSign()} * vx ${(qs[3]).withSign()} = 0")
        qs
    }

    val svx = e4.first()
    if (print) println()
    if (print) println("Eliminate vx = (${svx[0]} * x ${(svx[1]).withSign()} * y ${(svx[3]).withSign()}) / ${(-svx[2])}")
    val e5 = e4.drop(1).map { s ->
        val qs = listOf(s[0] * -svx[2] + svx[0] * s[2], s[1] * -svx[2] + svx[1] * s[2], svx[3] * s[2] + s[3] * -svx[2])
        if (print) println("${qs[0]} * x ${(qs[1]).withSign()} * y ${(qs[2]).withSign()} = 0")
        qs
    }

    if (print) println()
    val (sy, sx) = e5
    if (print) println("Eliminate y = (${sy[0]} * x ${(sy[2]).withSign()}) / ${-sy[1]}")
    val x = -(sy[2] * sx[1] + sx[2] * -sy[1]) / (sx[0] * -sy[1] + sy[0] * sx[1])
    if (print) println("x = $x")
    val y = (sy[0] * x + sy[2]) / -sy[1]
    if (print) println("y = $y")
    val vx = (svx[0] * x + svx[1] * y + svx[3]) / -svx[2]
    if (print) println("vx = $vx")
    val vy = (svy[0] * x + svy[1] * y + svy[2] * vx + svy[4]) / -svy[3]
    if (print) println("vy = $vy")
    val t1 = (x + subs[0][0]) / (subs[0][1] - vx)
    if (print) println("t1 = $t1")
    val t2 = (x + subs[2][0]) / (subs[2][1] - vx)
    if (print) println("t2 = $t2")
    val vz = (parsed[0].first[2] + parsed[0].second[2] * t1 - parsed[1].first[2] - parsed[1].second[2] * t2) / (t1 - t2)
    if (print) println("vz = $vz")
    val z = parsed[0].first[2] + parsed[0].second[2] * t1 - vz * t1
    if (print) println("z = $z")

    return x + y + z
}

private fun intersectsWithin(
    p1: List<BigInteger>,
    v1: List<BigInteger>,
    p2: List<BigInteger>,
    v2: List<BigInteger>,
    range: LongRange
): Boolean {
    val (vx1, vy1) = v1
    val (vx2, vy2) = v2

    val (x1, y1) = p1
    val (x3, y3) = p2

    val x2 = x1 + vx1
    val y2 = y1 + vy1

    val x4 = x3 + vx2
    val y4 = y3 + vy2

    val d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4)

    if (d == BigInteger.ZERO) {
        return false
    }

    val x = (x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)
    val y = (x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)

    val extendedRange = if (BigInteger.ZERO < d) range.first.toBigInteger() * d .. range.last.toBigInteger() * d else range.last.toBigInteger() * d .. range.first.toBigInteger() * d

    val inRange = x in extendedRange && y in extendedRange
    val futureA = if (BigInteger.ZERO < d) BigInteger.ZERO <= (x - x1 * d) * vx1 else BigInteger.ZERO > (x - x1 * d) * vx1
    val futureB = if (BigInteger.ZERO < d) BigInteger.ZERO <= (x - x3 * d) * vx2 else BigInteger.ZERO > (x - x3 * d) * vx2
    val inFuture = futureA && futureB

    return inRange && inFuture
}

private fun BigInteger.withSign() = if (BigInteger.ZERO <= this) "+ $this" else "- ${this.abs()}"
