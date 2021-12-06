package day05

import readInput
import kotlin.math.max
import kotlin.math.min

fun main() {

    fun parse(input: List<String>): Set<Segment> {
        return input
            .map { it.split(" -> ") }
            .map { points ->
                val segment = points
                    .map { point ->
                        val xy = point
                            .split(",")
                            .map { it.toInt() }
                        Point(xy[0], xy[1])
                    }
                Segment(segment[0], segment[1])
            }.toSet()
    }

    fun gridSize(lines: Set<Segment>): Pair<Int, Int> {
        val points = lines.map { listOf(it.first, it.second) }.flatten()
        val xsize = points.maxByOrNull { it.x }!!.x
        val ysize = points.maxByOrNull { it.y }!!.y
        return Pair(xsize + 1, ysize + 1)
    }

    fun lineFill(seg: Segment, matrix: MutableList<MutableList<Int>>, f: Fill) {
        val (start, end) = seg
        val xmin = min(start.x, end.x)
        val xmax = max(start.x, end.x)
        val ymin = min(start.y, end.y)
        val ymax = max(start.y, end.y)

        if (f == Fill.STRAIGHT) {
            for (y in ymin..ymax) {
                for (x in xmin..xmax) {
                    matrix[y][x] += 1
                }
            }
        } else if (f == Fill.DIAGONAL) {
            // Could be ymax - ymin as well, diagonal lines are always 45 degrees
            val length = xmax - xmin
            val (first, second) = setOf(seg.first, seg.second).sortedBy { it.y }
            if (first.x < second.x) {
                for (i in 0..length) {
                    matrix[ymin + i][xmin + i] += 1
                }
            } else if (first.x > second.x) {
                for (i in 0..length) {
                    matrix[ymin + i][xmax - i] += 1
                }
            }
        }
    }

    fun part1(input: Set<Segment>): Int {
        val dimensions = gridSize(input)
        val matrix = MutableList(dimensions.second) { MutableList(dimensions.first) { 0 } }
        input.filter { seg ->
            val start = seg.first
            val end = seg.second
            (start.x == end.x) or (start.y == end.y)
        }.forEach { lineFill(it, matrix, Fill.STRAIGHT) }
        return matrix.flatten().count { it >= 2 }
    }

    fun part2(input: Set<Segment>): Int {
        val dimensions = gridSize(input)
        val matrix = MutableList(dimensions.second) { MutableList(dimensions.first) { 0 } }
        input.forEach { seg ->
            val start = seg.first
            val end = seg.second
            if ((start.x == end.x) or (start.y == end.y)) {
                lineFill(seg, matrix, Fill.STRAIGHT)
            } else {
                lineFill(seg, matrix, Fill.DIAGONAL)
            }
        }
        return matrix.flatten().count { it >= 2 }
    }


    val testInput = parse(readInput("day05/test"))
    check(part1(testInput) == 5)

    val input = parse(readInput("day05/input"))
    println(part1(input))
    check(part2(testInput) == 12)
    println(part2(input))
}

data class Point(val x: Int, val y: Int)
data class Segment(val first: Point, val second: Point)
enum class Fill {
    STRAIGHT, DIAGONAL
}


