package day08

import readInput

fun main() {

    fun parse(input: List<String>): List<Pair<Patterns, Patterns>> {
        return input.map { line ->
            val l = line.trim().split(" | ").map { patterns ->
                patterns.split(" ").map { p -> Pattern(p.map { it }.toSet()) }
            }.map {
                Patterns(it)
            }
            Pair(l[0], l[1])
        }
    }


    fun mapToDigits(numbers: Patterns): Map<Pattern, Int> {
        val patterns = numbers.list.toMutableSet()
        val mapping = mutableMapOf<Int, Pattern>()
        val signalStrings = mutableMapOf<String, Set<Char>>()

        fun signal(key: Int): Set<Char> {
            return mapping[key]?.signal ?: throw NoSuchElementException("No element named $key in mapping")
        }

        // Extract patterns for 1,4,7,8 since they can be detected by length
        setOf(2, 4, 3, 7).forEach { len ->
            val match = patterns.first { it.signal.size == len }
            // Remap values from number of signals to the actual, displayed number
            when (len) {
                2 -> mapping += 1 to match
                4 -> mapping += 4 to match
                3 -> mapping += 7 to match
                7 -> mapping += 8 to match
            }
            // Remove the pattern we just found
            patterns -= match
        }

        // Since the input structure is guaranteed, I used !! liberally in this function
        // signalStrings are helper mappings to easier identify some digits
        signalStrings += "a" to (signal(7) subtract signal(1))
        signalStrings += "bd" to (signal(4) subtract signal(1))
        signalStrings += "aeg" to (signal(8) subtract signal(4))
        signalStrings += "eg" to (signalStrings["aeg"]!! subtract signalStrings["a"]!!)

        // Identify each of the remaining digits by creating set differences with either
        // the signalStrings or one of the solved digits
        for (p in patterns) {
            when (p.signal.size) {
                // Identify 0, 6, 9
                6 -> {
                    mapping += if (
                        (p.signal subtract signalStrings["a"]!! subtract signal(4)) != signalStrings["eg"]!!) {
                        9 to p
                    } else if ((p.signal subtract signal(1)).size == 5) {
                        6 to p
                    } else if ((p.signal subtract signal(1)).size == 4) {
                        0 to p
                    } else {
                        throw IllegalStateException("Invalid signal of length 6: $p.signal")
                    }
                }
                // Identify 2, 3, 5
                5 -> {
                    mapping += if ((p.signal subtract signal(4)).size == 3) {
                        2 to p
                    } else if ((p.signal subtract signal(7)).size == 2) {
                        3 to p
                    } else if ((p.signal subtract signalStrings["bd"]!!).size == 3) {
                        5 to p
                    } else {
                        throw IllegalStateException("Invalid signal of length 5: $p.signal")
                    }
                }
                else -> throw IllegalStateException("Invalid signal: $p.signal")
            }
        }

        return mapping.entries.associate { (num, pattern) -> pattern to num }
    }

    fun part1(input: List<Pair<Patterns, Patterns>>): Int {
        return input.sumOf { line ->
            line.second.list.map { it.signal.size }.count { it in setOf(2, 4, 3, 7) }
        }
    }

    fun part2(input: List<Pair<Patterns, Patterns>>): Int {
        return input.sumOf { line ->
            val mapping = mapToDigits(line.first)
            val patterns = line.second.list
            patterns.map { pattern ->
                mapping[pattern]
            }.joinToString("").toInt()
        }
    }

    val testInput = parse(readInput("day08/test"))
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = parse(readInput("day08/input"))
    println(part1(input))
    println(part2(input))
}

data class Pattern(val signal: Set<Char>)

data class Patterns(val list: List<Pattern>)
