package day03

import readInput
import kotlin.math.abs

fun main() {

    fun parse(input: List<String>): Set<List<Int>> {
        return input.map { row ->
            row.map {
                Character.getNumericValue(it)
            }.toList()
        }.toSet()
    }

    fun cast(bits: List<Int>): UInt {
        return bits.joinToString("").toUInt(2)
    }

    fun part1(input: Set<List<Int>>, numBits: Int): Int {
        val mostCommon = MutableList(numBits) { 0 }

        for (index in 0 until numBits) {
            if (input.sumOf {
                    it[index]
                } > input.size / 2.0) {
                mostCommon[index] = 1
            }
        }

        val leastCommon = mostCommon.map { abs(it - 1) }
        val gamma = cast(mostCommon.toList())
        val epsilon = cast(leastCommon)
        return (gamma * epsilon).toInt()
    }


    fun part2(input: Set<List<Int>>, numBits: Int): Int {
        return input.size
    }

    val testInput = parse(readInput("day03/test"))
    check(part1(testInput, 5) == 198)

    val input = parse(readInput("day03/input"))
    println(part1(input, 12))
//    println(part2(input))
}

