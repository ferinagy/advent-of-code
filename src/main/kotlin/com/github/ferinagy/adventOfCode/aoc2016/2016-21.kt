package com.github.ferinagy.adventOfCode.aoc2016

fun main() {
    println("Part1:")
    println(part1(testInput1, "abcde"))
    println(part1(input, "abcdefgh"))

    println()
    println("Part2:")
    println(part2(testInput1, "decab"))
    println(part2(input, "fbgdceah"))
}

private fun part1(input: String, start: String): String {
    val instructions = input.lines().map { ScrambleInstruction.parse(it) }
    val pass = start.toCharArray()
    val temp = CharArray(pass.size)
    instructions.forEach {
        pass.copyInto(temp)
        it.execute(pass, temp)
        temp.copyInto(pass)
    }

    return pass.joinToString(separator = "")
}

private fun part2(input: String, end: String): String {
    val instructions = input.lines().map { ScrambleInstruction.parse(it) }.reversed()
    var pass = listOf(end.toCharArray())
    instructions.forEach { instruction ->
        pass = pass.flatMap { instruction.reverse(it) }
    }

    return if (pass.size == 1) pass.first().joinToString(separator = "") else {
        pass.joinToString { it.joinToString(separator = "") }
    }
}

private sealed class ScrambleInstruction {

    abstract fun execute(source: CharArray, dest: CharArray)
    abstract fun reverse(source: CharArray): List<CharArray>

    data class SwapPosition(val x: Int, val y: Int) : ScrambleInstruction() {
        override fun execute(source: CharArray, dest: CharArray) {
            dest[x] = source[y]
            dest[y] = source[x]
        }

        override fun reverse(source: CharArray): List<CharArray> {
            val dest = source.copyOf()
            execute(source, dest)
            return listOf(dest)
        }
    }

    data class SwapLetter(val x: Char, val y: Char) : ScrambleInstruction() {
        override fun execute(source: CharArray, dest: CharArray) {
            for (i in dest.indices) {
                if (source[i] == x) dest[i] = y
                if (source[i] == y) dest[i] = x
            }
        }

        override fun reverse(source: CharArray): List<CharArray> {
            val dest = source.copyOf()
            execute(source, dest)
            return listOf(dest)
        }
    }

    data class Rotate(val left: Boolean, val steps: Int) : ScrambleInstruction() {
        override fun execute(source: CharArray, dest: CharArray) {
            val offset = if (left) (source.size - steps) % source.size else steps % source.size
            rotate(source, dest, offset)
        }

        override fun reverse(source: CharArray): List<CharArray> {
            val dest = source.copyOf()
            val offset = if (!left) (source.size - steps) % source.size else steps % source.size
            rotate(source, dest, offset)
            return listOf(dest)
        }

        private fun rotate(source: CharArray, dest: CharArray, offset: Int) {
            source.copyInto(dest, offset, 0, source.size - offset)
            source.copyInto(dest, 0, source.size - offset, source.size)
        }
    }

    data class RotatePosition(val letter: Char) : ScrambleInstruction() {
        override fun execute(source: CharArray, dest: CharArray) {
            val index = source.indexOf(letter)
            val offset = computeOffset(index, source.size)
            source.copyInto(dest, offset, 0, source.size - offset)
            source.copyInto(dest, 0, source.size - offset, source.size)
        }

        override fun reverse(source: CharArray): List<CharArray> {
            val index = source.indexOf(letter)
            val originals = source.indices.filter { (it + computeOffset(it, source.size)) % source.size == index }

            return originals.map {
                val dest = source.copyOf()
                val offset = (it - index).mod(dest.size)
                source.copyInto(dest, offset, 0, source.size - offset)
                source.copyInto(dest, 0, source.size - offset, source.size)
            }
        }

        private fun computeOffset(index: Int, size: Int): Int {
            val additional = if (4 <= index) 2 else 1
            return (additional + index) % size
        }
    }

    data class Reverse(val x: Int, val y: Int) : ScrambleInstruction() {
        override fun execute(source: CharArray, dest: CharArray) {
            repeat(y - x + 1) { i ->
                dest[x + i] = source[y - i]
            }
        }

        override fun reverse(source: CharArray): List<CharArray> {
            val dest = source.copyOf()
            execute(source, dest)
            return listOf(dest)
        }
    }

    data class Move(val x: Int, val y: Int) : ScrambleInstruction() {
        override fun execute(source: CharArray, dest: CharArray) {
            source.copyInto(dest, x, x + 1, source.size)
            dest.copyInto(dest, y + 1, y, dest.size - 1)
            dest[y] = source[x]
        }

        override fun reverse(source: CharArray): List<CharArray> {
            val dest = source.copyOf()
            Move(y, x).execute(source, dest)
            return listOf(dest)
        }
    }

    companion object {
        fun parse(text: String): ScrambleInstruction = when {
            swapPositionRegex.matches(text) -> {
                val (x, y) = swapPositionRegex.matchEntire(text)!!.destructured
                SwapPosition(x.toInt(), y.toInt())
            }
            swapLetterRegex.matches(text) -> {
                val (x, y) = swapLetterRegex.matchEntire(text)!!.destructured
                SwapLetter(x.single(), y.single())
            }
            reverseRegex.matches(text) -> {
                val (x, y) = reverseRegex.matchEntire(text)!!.destructured
                Reverse(x.toInt(), y.toInt())
            }
            rotateRegex.matches(text) -> {
                val (dir, steps) = rotateRegex.matchEntire(text)!!.destructured
                Rotate(dir == "left", steps.toInt())
            }
            moveRegex.matches(text) -> {
                val (x, y) = moveRegex.matchEntire(text)!!.destructured
                Move(x.toInt(), y.toInt())
            }
            rotatePositionRegex.matches(text) -> {
                val (x) = rotatePositionRegex.matchEntire(text)!!.destructured
                RotatePosition(x.single())
            }
            else -> error("invalid instruction: $text")
        }
    }
}

private val swapPositionRegex = """swap position (\d+) with position (\d+)""".toRegex()
private val swapLetterRegex = """swap letter (.) with letter (.)""".toRegex()
private val reverseRegex = """reverse positions (\d+) through (\d+)""".toRegex()
private val rotateRegex = """rotate (\w+) (\d+) steps?""".toRegex()
private val moveRegex = """move position (\d+) to position (\d+)""".toRegex()
private val rotatePositionRegex = """rotate based on position of letter (.)""".toRegex()

private const val testInput1 = """swap position 4 with position 0
swap letter d with letter b
reverse positions 0 through 4
rotate left 1 step
move position 1 to position 4
move position 3 to position 0
rotate based on position of letter b
rotate based on position of letter d"""

private const val input = """reverse positions 1 through 6
rotate based on position of letter a
swap position 4 with position 1
reverse positions 1 through 5
move position 5 to position 7
swap position 4 with position 0
swap position 4 with position 6
rotate based on position of letter a
swap position 0 with position 2
move position 5 to position 2
move position 7 to position 1
swap letter d with letter c
swap position 5 with position 3
reverse positions 3 through 7
rotate based on position of letter d
swap position 7 with position 5
rotate based on position of letter f
swap position 4 with position 1
swap position 3 with position 6
reverse positions 4 through 7
rotate based on position of letter c
move position 0 to position 5
swap position 7 with position 4
rotate based on position of letter f
reverse positions 1 through 3
move position 5 to position 3
rotate based on position of letter g
reverse positions 2 through 5
rotate right 0 steps
rotate left 0 steps
swap letter f with letter b
rotate based on position of letter h
move position 1 to position 3
reverse positions 3 through 6
rotate based on position of letter h
swap position 4 with position 3
swap letter b with letter h
swap letter a with letter h
reverse positions 1 through 6
swap position 3 with position 6
swap letter e with letter d
swap letter e with letter h
swap position 1 with position 5
rotate based on position of letter a
reverse positions 4 through 5
swap position 0 with position 4
reverse positions 0 through 3
move position 7 to position 2
swap letter e with letter c
swap position 3 with position 4
rotate left 3 steps
rotate left 7 steps
rotate based on position of letter e
reverse positions 5 through 6
move position 1 to position 5
move position 1 to position 2
rotate left 1 step
move position 7 to position 6
rotate left 0 steps
reverse positions 5 through 6
reverse positions 3 through 7
swap letter d with letter e
rotate right 3 steps
swap position 2 with position 1
swap position 5 with position 7
swap letter h with letter d
swap letter c with letter d
rotate based on position of letter d
swap letter d with letter g
reverse positions 0 through 1
rotate right 0 steps
swap position 2 with position 3
rotate left 4 steps
rotate left 5 steps
move position 7 to position 0
rotate right 1 step
swap letter g with letter f
rotate based on position of letter a
rotate based on position of letter b
swap letter g with letter e
rotate right 4 steps
rotate based on position of letter h
reverse positions 3 through 5
swap letter h with letter e
swap letter g with letter a
rotate based on position of letter c
reverse positions 0 through 4
rotate based on position of letter e
reverse positions 4 through 7
rotate left 4 steps
swap position 0 with position 6
reverse positions 1 through 6
rotate left 2 steps
swap position 5 with position 3
swap letter b with letter d
swap letter b with letter d
rotate based on position of letter d
rotate based on position of letter c
rotate based on position of letter h
move position 4 to position 7"""
