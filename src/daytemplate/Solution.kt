package daytemplate

import readInput

fun main() {

    fun parse(input: List<String>): List<String> {
        return input
    }

    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }


    val testInput = parse(readInput("dayxx/test"))
    check(part1(testInput) == 1)

    val input = parse(readInput("dayxx/input"))
    println(part1(input))
    println(part2(input))
}
