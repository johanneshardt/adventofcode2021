package day03

import readInput
import kotlin.math.abs

fun main() {

    fun parse(input: List<String>): List<List<Int>> {
        return input.map { row ->
            row.map {
                Character.getNumericValue(it)
            }.toList()
        }
    }

    fun cast(bits: List<Int>): UInt {
        return bits.joinToString("").toUInt(2)
    }

    fun digits(bytes: List<List<Int>>): List<List<Int>> {
        return (0 until bytes.first().size)
            .map { digit ->
                bytes.map {
                    it[digit]
                }
            }
    }

    fun digitsAt(bytes: List<List<Int>>, pos: Int): List<Int> = digits(bytes)[pos]


    // Returns the occurrence of a digit (value at position in UInt)
    fun criteria(bits: List<Int>, d: Int): Occurence {
        val freq = bits.count { it == d }
        return if (freq > bits.size * 0.5) {
            Occurence.MOSTCOMMON
        } else if (freq < bits.size * 0.5) {
            Occurence.LEASTCOMMON
        } else {
            Occurence.EQUALLYCOMMON
        }
    }

    fun mostCommon(bits: List<Int>): Int? {
        return bits.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key
    }


    fun part1(input: List<List<Int>>): Int {
        val digits = digits(input)
        val mostCommon = digits.map {
            mostCommon(it)!!
        }
        val leastCommon = mostCommon.map { abs(it - 1) }
        val gamma = cast(mostCommon)
        val epsilon = cast(leastCommon)
        return (gamma * epsilon).toInt()
    }


    fun part2(input: List<List<Int>>, numBits: Int): Int {
        var oxygen = input
        var digit = 0
        while (oxygen.size > 1 && digit < numBits) {
            oxygen = oxygen.filter { bytes ->
                when (criteria(digitsAt(oxygen, digit), bytes[digit])) {
                    Occurence.MOSTCOMMON -> true
                    Occurence.EQUALLYCOMMON -> bytes[digit] == 1
                    Occurence.LEASTCOMMON -> false
                }
            }
            digit += 1
        }

        var co2 = input
        digit = 0
        while (co2.size > 1 && digit < numBits) {
            co2 = co2.filter { bytes ->
                when (criteria(digitsAt(co2, digit), bytes[digit])) {
                    Occurence.MOSTCOMMON -> false
                    Occurence.EQUALLYCOMMON -> bytes[digit] == 0
                    Occurence.LEASTCOMMON -> true
                }
            }
            digit += 1
        }
        if (oxygen.size > 1 || co2.size > 1) {
            throw IllegalStateException("No life support rating found.")
        }
        return (cast(oxygen.first()) * cast(co2.first())).toInt()
    }

    val testInput = parse(readInput("day03/test"))
    check(part1(testInput) == 198)

    val input = parse(readInput("day03/input"))
    println(part1(input))
    println(part2(input, 12))
}

enum class Occurence {
    MOSTCOMMON, LEASTCOMMON, EQUALLYCOMMON
}

data class Digit(val pos: Int, val value: Int)

