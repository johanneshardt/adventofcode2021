package day01

import readInt

fun main() {
    fun part1(input: List<Int>): Int {
        return input.zipWithNext().count { it.second > it.first }
    }

    fun part2(input: List<Int>): Int {
        return input.windowed(3).map { it.sum() }.zipWithNext().count {
            it.second > it.first
        }
    }

    val testInput = readInt("day01/test")
    check(part1(testInput) == 7)

    val input = readInt("day01/input")
    println(part1(input))
    println(part2(input))
}
