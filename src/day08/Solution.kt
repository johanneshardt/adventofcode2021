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
        val mapping = mutableMapOf<Int, Pattern>()
        val patterns = numbers.patterns.toMutableSet()
        val signalStrings = mutableMapOf<String, Set<Char>>()
        setOf(2, 4, 3, 7).map { len ->
            val match = patterns.first { it.signal.size == len }
            patterns -= match   // TODO change this somehow
            len to match
        }.forEach {
            // Remap values from number of signals to the actual, displayed number
            when (it.first) {
                2 -> mapping += 1 to it.second
                4 -> mapping += 4 to it.second
                3 -> mapping += 7 to it.second
                7 -> mapping += 8 to it.second
            }
        }

        // Since the input structure is guaranteed, I used !! liberally in this function
        signalStrings += "a" to (mapping[7]!!.signal subtract mapping[1]!!.signal)
        signalStrings += "bd" to (mapping[4]!!.signal subtract mapping[1]!!.signal)
        signalStrings += "aeg" to (mapping[8]!!.signal subtract mapping[4]!!.signal)
        signalStrings += "eg" to (signalStrings["aeg"]!! subtract signalStrings["a"]!!)


        for (p in patterns) {
            when (p.signal.size) {
                // 0, 6, 9
                6 -> {
                    mapping += if (
                        (p.signal subtract signalStrings["a"]!! subtract mapping[4]!!.signal) != signalStrings["eg"]!!) {
                        9 to p
                    } else if ((p.signal subtract mapping[1]!!.signal).size == 5) {
                        6 to p
                    } else if ((p.signal subtract mapping[1]!!.signal).size == 4) {
                        0 to p
                    } else {
                        throw IllegalStateException("Invalid input")
                    }
                }
                // 2, 3, 5
                5 -> {
                    mapping += if ((p.signal subtract mapping[4]!!.signal).size == 3) {
                        2 to p
                    } else if ((p.signal subtract mapping[7]!!.signal).size == 2) {
                        3 to p
                    } else if ((p.signal subtract signalStrings["bd"]!!).size == 3) {
                        5 to p
                    } else {
                        throw IllegalStateException("Invalid input")
                    }

                }
                else -> throw IllegalStateException("Invalid input")
            }
        }

        return mapping.entries.associate { (num, pattern) -> pattern to num }
    }

    fun part1(input: List<Pair<Patterns, Patterns>>): Int {
        return input.sumOf { line ->
            line.second.patterns.map { it.signal.size }.count { it in setOf(2, 4, 3, 7) }
        }
    }

    fun part2(input: List<Pair<Patterns, Patterns>>): Int {
        val v = input.sumOf { line ->
            val mapping = mapToDigits(line.first)
            val output = line.second.patterns
            val mapped = output.map { pattern ->
                mapping[pattern]
            }
            mapped.joinToString("").toInt()
        }
        return v
    }


    val testInput = parse(readInput("day08/test"))
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = parse(readInput("day08/input"))
    println(part1(input))
    println(part2(input))
}

data class Pattern(val signal: Set<Char>)

data class Patterns(val patterns: List<Pattern>)
