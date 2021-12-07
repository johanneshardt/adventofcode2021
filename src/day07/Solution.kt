package day07

import readInput
import kotlin.math.abs

fun main() {

    fun parse(input: List<String>): List<Int> {
        return input
            .first()
            .trim()
            .split(",")
            .map { it.toInt() }
    }

    fun minAverageDistance(input: List<Int>, d: DistanceType): Int {
        return (input.minOf { it }..input.maxOf { it })
            .map { guess ->
                input
                    .sumOf {
                        when (d) {
                            DistanceType.NORMAL -> Point(it).distanceTo(Point(guess))
                            DistanceType.SCALING -> Point(it).scalingDistanceTo(Point(guess))
                        }
                    }
            }.minOf { it }
    }

    fun part1(input: List<Int>): Int {
        return minAverageDistance(input, DistanceType.NORMAL)
    }

    fun part2(input: List<Int>): Int {
        return minAverageDistance(input, DistanceType.SCALING)
    }


    val testInput = parse(readInput("day07/test"))
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = parse(readInput("day07/input"))
    println(part1(input))
    println(part2(input))
}

data class Point(var pos: Int) {
    fun distanceTo(other: Point): Int {
        return abs(this.pos - other.pos)
    }

    fun scalingDistanceTo(other: Point): Int {
        // Arithmetic sum
        return (distanceTo(other) * (distanceTo(other) + 1) / 2)
    }
}

enum class DistanceType {
    NORMAL, SCALING
}
