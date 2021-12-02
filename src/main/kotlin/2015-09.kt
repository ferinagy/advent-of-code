fun main(args: Array<String>) {
    println("Part1:")
    println(part1(input))

    println()
    println("Part2:")
    println(part2(input))
}

private fun part1(input: String): Int {
    val graph = readGraph(input)
    return graph.solveTsp { d1, d2 -> d1 - d2 }
}

private fun part2(input: String): Int {
    val graph = readGraph(input)
    return graph.solveTsp { d1, d2 -> d2 - d1 }
}

private fun readGraph(input: String): TspGraph {
    val graph = TspGraph()
    val regex = """(\w+) to (\w+) = (\d+)""".toRegex()

    input.lines().forEach {
        val (from, to, dist) = regex.matchEntire(it)!!.destructured
        graph.addBidirectionalEdge(from, to, dist.toInt())
    }

    return graph
}

private class TspGraph {

    private val edges = mutableMapOf<String, MutableMap<String, Int>>()

    fun addBidirectionalEdge(from: String, to: String, weight: Int) {
        val e1 = edges.getOrPut(from) { mutableMapOf() }
        val e2 = edges.getOrPut(to) { mutableMapOf() }

        e1[to] = weight
        e2[from] = weight
    }

    fun solveTsp(comparator: Comparator<Int>): Int {
        val nodes = edges.keys

        return nodes.map {
            solveTsp(it, nodes - it, comparator)
        }.minOfWith(comparator) { it.dist }
    }

    private fun solveTsp(node: String, remaining: Set<String>, comparator: Comparator<Int>): Progress {
        if (remaining.isEmpty()) {
            return Progress(node, setOf(node), listOf(node), 0)
        }

        val subTasks = remaining.map {
            solveTsp(it, remaining - it, comparator)
        }

        return subTasks.map {
            val dist = edges[it.end]!![node]!!
            Progress(node, it.visited + node, it.path + node, dist + it.dist)
        }.minWithOrNull { o1, o2 -> comparator.compare(o1.dist, o2.dist) }!!
    }

    data class Progress(val end: String, val visited: Set<String>, val path: List<String>, val dist: Int)
}

private const val testInput1 = """London to Dublin = 464
London to Belfast = 518
Dublin to Belfast = 141"""

private const val input = """AlphaCentauri to Snowdin = 66
AlphaCentauri to Tambi = 28
AlphaCentauri to Faerun = 60
AlphaCentauri to Norrath = 34
AlphaCentauri to Straylight = 34
AlphaCentauri to Tristram = 3
AlphaCentauri to Arbre = 108
Snowdin to Tambi = 22
Snowdin to Faerun = 12
Snowdin to Norrath = 91
Snowdin to Straylight = 121
Snowdin to Tristram = 111
Snowdin to Arbre = 71
Tambi to Faerun = 39
Tambi to Norrath = 113
Tambi to Straylight = 130
Tambi to Tristram = 35
Tambi to Arbre = 40
Faerun to Norrath = 63
Faerun to Straylight = 21
Faerun to Tristram = 57
Faerun to Arbre = 83
Norrath to Straylight = 9
Norrath to Tristram = 50
Norrath to Arbre = 60
Straylight to Tristram = 27
Straylight to Arbre = 81
Tristram to Arbre = 90"""
