package com.github.ferinagy.adventOfCode.aoc2021

import com.github.ferinagy.adventOfCode.Coord3D
import com.github.ferinagy.adventOfCode.println
import com.github.ferinagy.adventOfCode.readInputText
import com.github.ferinagy.adventOfCode.transpose
import kotlin.math.abs

fun main() {
    val input = readInputText(2021, "19-input")
    val testInput1 = readInputText(2021, "19-test1")

    println("Test part 1 and 2:")
    val (test1, test2) = solve(testInput1)
    test1.println()
    test2.println()

    println()
    println("Real part 1 and 2:")
    val (real1, real2) = solve(input)
    real1.println()
    real2.println()
}

private fun solve(input: String): Pair<Int, Int> {
    val rawScanners = parse(input)
    val scanners = rawScanners.map { (name, allBeacons) ->
        val beacons = allBeacons.map { current ->
            allBeacons.filter { it != current }
                .map { current.relativePosition(it) }
                .map { it.possiblePositions() }
                .transpose()
                .map { it.toSet() }
                .let { UnplacedBeacon(current.possiblePositions(), it) }
        }

        UnplacedScanner(name, beacons)
    }

    val fixed = mutableListOf(scanners.first().place(Coord3D(0, 0, 0), 0))
    val toPlace = scanners.drop(1).toMutableList()

    outer@ while (toPlace.isNotEmpty()) {
        val current = toPlace.removeFirst()

        for (currentBeacon in current.beacons.drop(11)) {
            for (fixedScanner in fixed) {
                for (fixedBeacon in fixedScanner.beacons.drop(11)) {
                    val possibleScanners = currentBeacon.getPossibleScanners(fixedBeacon)
                    if (possibleScanners.isEmpty()) continue

                    val (position, rotation) = possibleScanners.single()

                    val placedScanner = current.place(position, rotation)
                    val verified = verifyScanner(placedScanner, fixed)
                    if (verified) {
                        fixed.add(placedScanner)
                        continue@outer
                    }
                }
            }
        }

        toPlace.add(current)
    }

    val beacons = fixed.flatMap { it.beacons }.map { it.position }.toSet()

    val dist = fixed.maxOf { s1 ->
        fixed.maxOf { s2 ->
            s1.position.relativePosition(s2.position).manhattanDist
        }
    }

    return beacons.size to dist
}

private fun PlacedScanner.isInRange(beacon: PlacedBeacon) = abs(position.x - beacon.position.x) <= 1000 &&
        abs(position.y - beacon.position.y) <= 1000 &&
        abs(position.z - beacon.position.z) <= 1000

private fun verifyScanner(scanner: PlacedScanner, placedScanners: List<PlacedScanner>): Boolean {
    return placedScanners.all { otherScanner ->
        val othersInRange = otherScanner.beacons.filter { scanner.isInRange(it) }.map { it.position }.toSet()
        val inRangeOfOther = scanner.beacons.filter { otherScanner.isInRange(it) }.map { it.position }.toSet()

        (othersInRange - inRangeOfOther).isEmpty() && (inRangeOfOther - othersInRange).isEmpty()
    }
}

private fun parse(input: String): List<Pair<String, List<Coord3D>>> {
    val scanners = input.split("\n\n").map { scanner ->
        val lines = scanner.lines()
        lines.first() to lines.drop(1).map { Coord3D.parse(it) }
    }
    return scanners
}

private data class PlacedScanner(val name: String, val position: Coord3D, val beacons: List<PlacedBeacon>)
private data class UnplacedScanner(val name: String, val beacons: List<UnplacedBeacon>)

private fun UnplacedScanner.place(position: Coord3D, rotation: Int): PlacedScanner {
    val placedBeacons = beacons.map {
        val relative = it.positions[rotation]
        val final = position + relative
        PlacedBeacon(final, it.relativePositions[rotation])
    }
    return PlacedScanner(name, position, placedBeacons)
}

private data class UnplacedBeacon(val positions: List<Coord3D>, val relativePositions: List<Set<Coord3D>>)

private fun UnplacedBeacon.getPossibleScanners(other: PlacedBeacon): List<Pair<Coord3D, Int>> {
    return relativePositions.mapIndexedNotNull { rotation, beacon ->
        val common = other.relativePositions.intersect(beacon).size

        if (common < 11) null else {
            positions[rotation].relativePosition(other.position) to rotation
        }
    }
}

private data class PlacedBeacon(val position: Coord3D, val relativePositions: Set<Coord3D>)

fun Coord3D.relativePosition(other: Coord3D) = Coord3D(other.x - x, other.y - y, other.z - z)

fun Coord3D.possiblePositions(): List<Coord3D> {
    val original = this
    return listOf(
        original.copy(x = x, y = y, z = z),
        original.copy(x = x, y = z, z = -y),
        original.copy(x = x, y = -y, z = -z),
        original.copy(x = x, y = -z, z = y),

        original.copy(x = -x, y = -z, z = -y),
        original.copy(x = -x, y = -y, z = z),
        original.copy(x = -x, y = z, z = y),
        original.copy(x = -x, y = y, z = -z),

        original.copy(x = y, y = x, z = -z),
        original.copy(x = y, y = -z, z = -x),
        original.copy(x = y, y = -x, z = z),
        original.copy(x = y, y = z, z = x),

        original.copy(x = -y, y = z, z = -x),
        original.copy(x = -y, y = -x, z = -z),
        original.copy(x = -y, y = -z, z = x),
        original.copy(x = -y, y = x, z = z),

        original.copy(x = z, y = -y, z = x),
        original.copy(x = z, y = x, z = y),
        original.copy(x = z, y = y, z = -x),
        original.copy(x = z, y = -x, z = -y),

        original.copy(x = -z, y = -x, z = y),
        original.copy(x = -z, y = y, z = x),
        original.copy(x = -z, y = x, z = -y),
        original.copy(x = -z, y = -y, z = -x),
    )
}
