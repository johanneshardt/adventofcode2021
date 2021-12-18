package day09

import readInput

fun main() {

    fun parse(input: List<String>): Heatmap {
        return Heatmap(input.map { line ->
            line.trim().map { it.toString().toInt() }
        })
    }

    fun part1(input: Heatmap): Int {
        return input.getMinimaHeights().sumOf { it + 1 }
    }

    fun part2(input: Heatmap): Int {
        val basinAreas = input.getMinima().map {
            input.exploreBasin(it).area.size
        }.sortedBy { it }.reversed()

        return basinAreas[0] * basinAreas[1] * basinAreas[2]
    }

    val testInput = parse(readInput("day09/test"))
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = parse(readInput("day09/input"))
    println(part1(input))
    println(part2(input))
}

data class Heatmap(val matrix: List<List<Int>>) {
    private fun getHeight(pos: Point): Int {
        val row = pos.y
        val col = pos.x
        return if (row >= 0 && row < matrix.size && col >= 0 && col < matrix.first().size) {
            matrix[row][col]
        } else {
            // If point is out of bounds, since 10 is larger than the largest possible value (9)
            // this makes bounds checking easier
            return 10
        }
    }

    private fun isMinimum(pos: Point): Boolean {
        val cell = matrix[pos.y][pos.x]
        val north = getHeight(pos.step(Direction.NORTH))
        val west = getHeight(pos.step(Direction.SOUTH))
        val east = getHeight(pos.step(Direction.WEST))
        val south = getHeight(pos.step(Direction.EAST))
        return cell < north && cell < west && cell < east && cell < south
    }

    // TODO maybe rewrite?
    fun getMinima(): List<Point> {
        val minima = mutableListOf<Point>()
        for ((i, row) in matrix.withIndex()) {
            for (j in row.indices) {
                if (isMinimum(Point(j, i))) {
                    minima.add(Point(j, i))
                }
            }
        }
        return minima.toList()
    }

    fun getMinimaHeights(): List<Int> {
        return getMinima().map { matrix[it.y][it.x] }
    }

    fun exploreBasin(pos: Point): Basin {
        return exploreBasin(pos, Direction.NORTH) +
                exploreBasin(pos, Direction.SOUTH) +
                exploreBasin(pos, Direction.WEST) +
                exploreBasin(pos, Direction.EAST) + Basin(setOf(pos))
    }

    // recursive helper
    private fun exploreBasin(pos: Point, d: Direction): Basin {
        // Base case if we reach the max value or a border
        return if (getHeight(pos.step(d)) >= 9) {
            Basin(setOf(pos))
        }
        // base case if the slope stops increasing
        else if (getHeight(pos) >= getHeight(pos.step(d))) {
            Basin(setOf(pos))
        }
        // otherwise, we explore in all directions
        else {
            val nextPos = pos.step(d)
            exploreBasin(nextPos, Direction.NORTH) +
                    exploreBasin(nextPos, Direction.SOUTH) +
                    exploreBasin(nextPos, Direction.WEST) +
                    exploreBasin(nextPos, Direction.EAST)
        }
    }
}

data class Point(val x: Int, val y: Int) {
    // Allows method chaining
    fun step(d: Direction): Point {
        return when (d) {
            Direction.NORTH -> this.copy(y = (y - 1))
            Direction.SOUTH -> this.copy(y = (y + 1))
            Direction.WEST -> this.copy(x = (x - 1))
            Direction.EAST -> this.copy(x = (x + 1))
        }
    }
}

data class Basin(val area: Set<Point>) {
    operator fun plus(other: Basin): Basin {
        return Basin(area + other.area)
    }
}

enum class Direction {
    NORTH, SOUTH, WEST, EAST
}
