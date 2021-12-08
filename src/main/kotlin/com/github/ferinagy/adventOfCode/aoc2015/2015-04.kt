import java.security.MessageDigest

fun main(args: Array<String>) {
    println("Part1:")
    println(starsWithZeros(input, 5))

    println()
    println("Part2:")
    println(starsWithZeros(input, 6))
}

private fun starsWithZeros(input: String, num: Int): Int {
    var n = 1
    val prefix = "0".repeat(num)
    while (true) {
        val hash = md5(input + n)

        if (hash.startsWith(prefix)) return n

        n++
    }
}

private fun md5(value: String): String {
    val md = MessageDigest.getInstance("MD5")
    val hash = md.digest(value.toByteArray()).joinToString(separator = "") { "%02x".format(it) }

    return hash
}

private const val testInput1 = """abcdef"""

private const val input = """bgvyzdsv"""
