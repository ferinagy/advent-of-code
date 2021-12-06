fun main(args: Array<String>) {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput1))
    println(part2(input))
}

private fun part1(input: String): String {
    var pwd = input
    while (!meetsPolicy1(pwd)) pwd = pwd.increment()

    return pwd
}

private fun part2(input: String): String {
    var pwd = part1(input).increment()
    while (!meetsPolicy1(pwd)) pwd = pwd.increment()

    return pwd
}

private fun meetsPolicy1(pwd: String): Boolean {
    if ('i' in pwd || 'o' in pwd || 'l' in pwd) return false

    val hasSequence = pwd.windowed(3)
        .map { it.toCharArray() }
        .any { (a, b, c) -> a.inc() == b && b.inc() == c }

    if (!hasSequence) return false

    val hasPairs = pwd.windowed(2)
        .mapIndexed { index, s -> s to index }
        .filter { it.first[0] == it.first[1] }
        .let { pairs ->
            when {
                pairs.size < 2 -> false
                pairs.size == 2 -> pairs[1].second - pairs[0].second >= 2
                else -> true
            }
        }

    if (!hasPairs) return false

    return true
}

private fun String.increment(): String {
    if (isEmpty()) return "a"

    return if (last() != 'z') {
        substring(0, lastIndex) + last().inc()
    } else {
        substring(0, lastIndex).increment() + 'a'
    }
}

private const val testInput1 = """abcdefgh"""

private const val input = """hxbxwxba"""
