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

private fun part2(input: List<String>): BigInteger {
    println(input.size)

    val parsed = input.map { line ->
        val (p, v) = line.split(""" @\s+""".toRegex())
        val position = p.split(""",\s+""".toRegex()).map(String::toBigInteger)
        val velocity = v.split(""",\s+""".toRegex()).map(String::toBigInteger)
        position to velocity
    }

    // / x1 + vx1 * t1 = x + vx * t1
    val equations = parsed.take(3).flatMap { s ->
        (0..2).map { i ->
            Pair(s.first[i], s.second[i])
        }
    }
    println("Starting equations")
    val vars = listOf('x', 'y', 'z')
    equations.forEachIndexed { index, item ->
        val (p, v) = item
        val variable = vars[index % 3]
        val num = index / 3 + 1
        println("$p ${v.withSign()} * t${num} = $variable + v$variable * t$num")
    }

    // px = p1x + v1x * t1 - vx * t1
    val subs = equations.take(3)
    println()
    println("Substitutions for x, y, z")
    subs.forEachIndexed { index, item ->
        val (p, v) = item
        val variable = vars[index % 3]
        println("$variable = $p ${v.withSign()} * t1 - v$variable * t1")
    }

    println()
    println("Substituted x, y, z from first 3")
    val e2 = equations.drop(3).mapIndexed { index, item ->
        val (p, v) = item
        val qs = listOf(p - subs[index % 3].first, v, subs[index % 3].second)
        val num = index / 3 + 2
        val variable = vars[index % 3]
        println("${qs[0]} ${qs[1].withSign()} * t${num} = ${qs[2]} * t1 - v$variable * t1 + v$variable * t$num")
        qs
    }

    println()
    println("Substitutions for vx, vy, vz")
    val subs2 = e2.mapIndexed { index, qs ->
        val num = index / 3 + 2
        listOf(qs[2], -qs[1], -qs[0], num.toBigInteger())
    }
    subs2.forEachIndexed { index, qs ->
        val variable = vars[index % 3]
        println("v$variable = (${qs[0]} * t1 ${qs[1].withSign()} * t${qs[3]} ${qs[2].withSign()}) / (t1 - t${qs[3]})")
    }

    println()
    println("After 2nd sub")
    val e3 = subs2.take(3).zip(subs2.drop(3)).mapIndexed { index, (left, right) ->
        require(left[0] == right[0])
        val qs = listOf(
            left[1] + right[0] /* t1 * t2 */,
            -left[0]-right[1] /* t1 * t3 */,
            -left[1] + right[1] /* t2 * t3 */,
            left[2] - right[2] /* t1 */,
            right[2] /* t2 */,
            -left[2] /* t3 */,
        )
        println("${qs[0]} * t1 * t2 ${qs[1].withSign()} * t1 * t3 ${qs[2].withSign()} * t2 * t3 ${qs[3].withSign()} * t1 ${qs[4].withSign()} * t2 ${qs[5].withSign()} * t3 = 0")
        qs
    }
    println()
    println("Sub for t1 - not safe for division by 0")
    val subs3 = e3.mapIndexed { index, qs ->
        val s = listOf(-qs[2], -qs[4], -qs[5], qs[0], qs[1], qs[3])
        println("t1 = (${s[0]} * t2 * t3 ${s[1].withSign()} * t2 ${s[2].withSign()} * t3) / (${s[3]} * t2 ${s[4].withSign()} * t3 ${s[5].withSign()})")
        s
    }

    println()
    println("After subbing t1")
    val s1 = subs3.first()
    val e4 = subs3.drop(1).map { qs ->
        val next = listOf(
            s1[0] * qs[3] - qs[0] * s1[3] /* t2 * t2 * t3 */,
            s1[0] * qs[4] - qs[0] * s1[4] /* t2 * t3 * t3 */,
            s1[1] * qs[3] - qs[1] * s1[3] /* t2 * t2 */,
            s1[2] * qs[3] + s1[1] * qs[4] + s1[0] * qs[5] - qs[2] * s1[3] - qs[1] * s1[4] - qs[0] * s1[5] /* t2 * t3 */,
            s1[2] * qs[4] - qs[2] * s1[4] /* t3 * t3 */,
            s1[1] * qs[5] - qs[1] * s1[5] /* t2 */,
            s1[2] * qs[5] - qs[2] * s1[5] /* t3 */,
        )
        println("${next[0]} * t2 * t2 * t3 ${(next[1]).withSign()} * t2 * t3 * t3 ${(next[2]).withSign()} * t2 * t2 ${(next[3]).withSign()} * t2 * t3 ${(next[4]).withSign()} * t3 * t3 ${(next[5]).withSign()} * t2 ${(next[6]).withSign()} * t3 = 0")
        next
    }

    println()
    println("Quadratics:")
    val e5 = e4.map { qs ->
        val d = getDiscriminant(listOf(
            qs[1] * qs[1],
            qs[3] * qs[1] + qs[1] * qs[3] + qs[0] * qs[4] * (-4).toBigInteger(),
            qs[5] * qs[1] + qs[3] * qs[3] + qs[1] * qs[5] + qs[2] * qs[4] * (-4).toBigInteger() + qs[0] * qs[6] * (-4).toBigInteger(),
            qs[5] * qs[3] + qs[3] * qs[5] + qs[2] * qs[6] * (-4).toBigInteger(),
            qs[5] * qs[5]
        ))

        val newQs = listOf(-qs[1] + d[0], -qs[3] + d[1], -qs[5] + d[2], -qs[1] - d[0], -qs[3] - d[1], -qs[5] - d[2], qs[0] * 2.toBigInteger(), qs[2] * 2.toBigInteger())
        println("t2 = (${newQs[0]} * t3^2 ${(newQs[1]).withSign()} * t3 ${(newQs[2]).withSign()}) / (${newQs[6]} * t3 ${newQs[7].withSign()})")
        println("t2 = (${(newQs[3])} * t3^2 ${(newQs[4]).withSign()} * t3 ${(newQs[5]).withSign()}) / (${newQs[6]} * t3 ${newQs[7].withSign()})")
        listOf(
            listOf(newQs[0], newQs[1], newQs[2], newQs[6], newQs[7]),
            listOf(newQs[3], newQs[4], newQs[5], newQs[6], newQs[7]),
        )
    }

    println()
    println("Final:")
    for (left in e5[0]) {
        for (right in e5[1]) {
            val qs = listOf(
                left[0] * right[3] - right[0] * left[3],
                left[1] * right[3] + left[0] * right[4] - right[1] * left[3] - right[0] * left[4],
                left[2] * right[3] + left[1] * right[4] - right[2] * left[3] - right[1] * left[4],
                left[2] * right[4] - right[2] * left[4],
            )
            println("${qs[0]} * t3^3 ${(qs[1]).withSign()} * t3^2 ${(qs[2]).withSign()} * t3 ${(qs[3]).withSign()} = 0")
            if (qs[0] == BigInteger.ZERO && qs.any { it != BigInteger.ZERO }) {
                val results = if (qs[1] == BigInteger.ZERO) {
                    val t3 = qs[3] / -qs[2]
                    listOf(t3)
                } else {
                    val d = (qs[2] * qs[2] - 4.toBigInteger() * qs[1] * qs[3]).sqrt()
                    val x1 = (-qs[2] + d) / (2.toBigInteger() * qs[1])
                    val x2 = (-qs[2] - d) / (2.toBigInteger() * qs[1])
                    listOf(x1, x2)
                }
                results.forEach { t3 ->
                    val t2 = (left[0] * t3 * t3 + left[1] * t3 + left[2]) / (left[3] * t3 + left[4])
                    val subT1 = subs3.first()
                    val t1 = (subT1[0] * t2 * t3 + subT1[1] * t2 + subT1[2] * t3) / (subT1[3] * t2 + subT1[4] * t3 + subT1[5])
                    val subVx = subs2[0]
                    val vx = (subVx[0] * t1 + subVx[1] * t2 + subVx[2]) / (t1 - t2)
                    val subVy = subs2[1]
                    val vy = (subVy[0] * t1 + subVy[1] * t2 + subVy[2]) / (t1 - t2)
                    val subVz = subs2[2]
                    val vz = (subVz[0] * t1 + subVz[1] * t2 + subVz[2]) / (t1 - t2)
                    val x = subs[0].first + subs[0].second * t1 - vx * t1
                    val y = subs[1].first + subs[1].second * t1 - vy * t1
                    val z = subs[2].first + subs[2].second * t1 - vz * t1

                    val valid = listOf(
                        x + t1 * vx == equations[0].first + t1 * equations[0].second,
                        y + t1 * vy == equations[1].first + t1 * equations[1].second,
                        z + t1 * vz == equations[2].first + t1 * equations[2].second,
                        x + t2 * vx == equations[3].first + t2 * equations[3].second,
                        y + t2 * vy == equations[4].first + t2 * equations[4].second,
                        z + t2 * vz == equations[5].first + t2 * equations[5].second,
                        x + t3 * vx == equations[6].first + t3 * equations[6].second,
                        y + t3 * vy == equations[7].first + t3 * equations[7].second,
                        z + t3 * vz == equations[8].first + t3 * equations[8].second,
                    )
                    if (valid.all { it }) {
                        println("t1 = $t1")
                        println("t2 = $t2")
                        println("t3 = $t3")
                        println("vx = $vx")
                        println("vy = $vy")
                        println("vz = $vz")
                        println("x = $x")
                        println("y = $y")
                        println("z = $z")

                        return x + y + z
                    }
                }
            }
        }
    }


    return BigInteger.ZERO
}

private fun getDiscriminant(qs: List<BigInteger>): List<BigInteger> {
    val a = qs[0].sqrt().abs()
    val b = (qs[1] / (a * BigInteger.TWO)).abs()
    val c = qs[4].sqrt().abs()

    val one = BigInteger.ONE
    val signs = listOf(-one, one)
    for (sa in signs) {
        for (sb in signs) {
            for (sc in signs) {
                val pa = sa * a
                val pb = sb * b
                val pc = sc * c
                if (qs[1] == BigInteger.TWO * pa * pb && qs[2] == (BigInteger.TWO * pa * pc + pb * pb) && qs[3] == BigInteger.TWO * pb * pc) {
                    return listOf(pa, pb, pc)
                }
            }
        }
    }

    return emptyList()
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
