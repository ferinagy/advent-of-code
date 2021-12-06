fun main(args: Array<String>) {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput1))
    println(part2(input))
}

private fun part1(input: String): Long {
    return simulateLanternFish(input, 80)
}

private fun part2(input: String): Long {
    return simulateLanternFish(input, 256)
}

private fun simulateLanternFish(input: String, generations: Int): Long {
    val array = Array(9) { 0L }
    input.split(",")
        .map { it.toInt() }
        .forEach { array[it]++ }

    repeat(generations) {
        val born = array[0]
        for (i in 1 .. array.lastIndex) {
            array[i-1] = array[i]
        }
        array[array.lastIndex] = 0
        array[6] += born
        array[8] += born
    }

    return array.sum()
}

private fun simulateLanternFish2(input: String, generationd: Int): Int {
    val list = input.split(",")
        .map { it.toInt() }
        .toMutableList()
    repeat(generationd) {
        var born = 0
        list.forEachIndexed { index, i ->
            if (i == 0) {
                born++
                list[index] = 6
            } else {
                list[index] = i-1
            }
        }

        repeat(born) { list += 8 }
    }

    return list.size
}

private const val testInput1 = """3,4,3,1,2"""

private const val input = """3,4,1,1,5,1,3,1,1,3,5,1,1,5,3,2,4,2,2,2,1,1,1,1,5,1,1,1,1,1,3,1,1,5,4,1,1,1,4,1,1,1,1,2,3,2,5,1,5,1,2,1,1,1,4,1,1,1,1,3,1,1,3,1,1,1,1,1,1,2,3,4,2,1,3,1,1,2,1,1,2,1,5,2,1,1,1,1,1,1,4,1,1,1,1,5,1,4,1,1,1,3,3,1,3,1,3,1,4,1,1,1,1,1,4,5,1,1,3,2,2,5,5,4,3,1,2,1,1,1,4,1,3,4,1,1,1,1,2,1,1,3,2,1,1,1,1,1,4,1,1,1,4,4,5,2,1,1,1,1,1,2,4,2,1,1,1,2,1,1,2,1,5,1,5,2,5,5,1,1,3,1,4,1,1,1,1,1,1,1,4,1,1,4,1,1,1,1,1,2,1,2,1,1,1,5,1,1,3,5,1,1,5,5,3,5,3,4,1,1,1,3,1,1,3,1,1,1,1,1,1,5,1,3,1,5,1,1,4,1,3,1,1,1,2,1,1,1,2,1,5,1,1,1,1,4,1,3,2,3,4,1,3,5,3,4,1,4,4,4,1,3,2,4,1,4,1,1,2,1,3,1,5,5,1,5,1,1,1,5,2,1,2,3,1,4,3,3,4,3"""
