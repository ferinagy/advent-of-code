package com.github.ferinagy.adventOfCode.aoc2021

fun main() {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput1))
    println(part2(input))
}

private fun part1(input: String): Long {
    return doPairInsertion(input, 10)
}

private fun part2(input: String): Long {
    return doPairInsertion(input, 40)
}

private fun doPairInsertion(input: String, count: Int): Long {
    val (initial, temp) = input.split("\n\n")

    val templates: Map<String, String> = temp.lines().associate {
        val (from, to) = it.split(" -> ")
        from to to + from[1]
    }

    val letterMap = mutableMapOf<Char, Long>()
    val cache = mutableMapOf<Pair<String, Int>, Map<Char, Long>>()
    for (i in 0 until initial.lastIndex) {
        val subLetters = letterMap(initial.substring(i .. i + 1), count, templates, cache)
        letterMap.add(subLetters)
    }
    val first = letterMap[initial.first()]!!
    letterMap[initial.first()] = first + 1

    val sorted = letterMap.map { it.value }.sorted()

    return sorted.last() - sorted.first()
}

private fun MutableMap<Char, Long>.add(other: Map<Char, Long>) {
    other.forEach {
        val count = getOrDefault(it.key, 0)
        put(it.key, count + it.value)
    }
}

private fun letterMap(
    key: String,
    depth: Int,
    templates: Map<String, String>,
    cache: MutableMap<Pair<String, Int>, Map<Char, Long>>
): Map<Char, Long> {
    val existing = cache[key to depth]
    if (existing != null) return existing

    if (depth == 1) {
        return templates[key]!!.toCharArray().letterMap().also { cache[key to depth] = it }
    }

    val result = mutableMapOf<Char, Long>()

    val sub = templates[key]!!
    for (i in 1..sub.lastIndex) {
        val subLetters = letterMap(sub.substring(i - 1..i), depth - 1, templates, cache)
        result.add(subLetters)
    }
    val subLetters = letterMap("${key[0]}${sub[0]}", depth - 1, templates, cache)
    result.add(subLetters)

    return result.also { cache[key to depth] = it }
}

private fun CharArray.letterMap() = groupBy { it }.mapValues { it.value.size.toLong() }

private const val testInput1 = """NNCB

CH -> B
HH -> N
CB -> H
NH -> C
HB -> C
HC -> B
HN -> C
NN -> C
BH -> H
NC -> B
NB -> B
BN -> B
BB -> N
BC -> B
CC -> N
CN -> C"""

private const val input = """PFVKOBSHPSPOOOCOOHBP

FV -> C
CP -> K
FS -> K
VF -> N
HN -> F
FF -> N
SS -> K
VS -> V
BV -> F
HC -> K
BP -> F
OV -> N
BF -> V
VH -> V
PF -> N
FC -> S
CS -> B
FK -> N
VK -> H
FN -> P
SH -> V
CV -> K
HP -> K
HO -> C
NO -> V
CK -> C
VB -> S
OC -> N
NS -> C
NF -> H
SF -> N
NK -> S
NP -> P
OO -> S
NH -> C
BC -> H
KS -> H
PV -> O
KO -> K
OK -> H
OH -> H
BH -> F
NB -> B
FH -> N
HV -> F
BN -> S
ON -> V
CB -> V
CF -> H
FB -> S
KF -> S
PS -> P
OB -> C
NN -> K
KV -> C
BK -> H
SN -> S
NC -> H
PK -> B
PC -> H
KN -> S
VO -> V
FO -> K
CH -> B
PH -> N
SO -> C
KH -> S
HB -> V
HH -> B
BB -> H
SC -> V
HS -> K
SP -> V
KB -> N
VN -> H
HK -> H
KP -> K
OP -> F
CO -> B
VP -> H
OS -> N
OF -> H
KK -> N
CC -> K
BS -> C
VV -> O
CN -> H
PB -> P
BO -> N
SB -> H
FP -> F
SK -> F
PO -> S
KC -> H
VC -> H
NV -> N
HF -> B
PN -> F
SV -> K
PP -> K"""
