package day06

import readInput

fun main() {

    fun parse(input: List<String>): Set<FishState> {
        return input
            .flatMap { line ->
                line
                    .trim()
                    .split(",")
                    .map { it.toInt() }
                    .groupingBy { it }
                    .eachCount()
                    .map { FishState(it.key, it.value) }
            }
            .toSet()
    }

    fun part1and2(input: Set<FishState>, days: Int): Long {
        val count: MutableList<Long> = MutableList(9) { 0 }
        input.forEach { count[it.age] = it.count.toLong() }
        for (i in 1..days) {
            var bornFish: Long = 0
            var resetFish: Long = 0
            for ((index, numFish) in count.withIndex()) {
                if (index == 0) {
                    bornFish = numFish
                    resetFish = numFish
                    count[0] = 0
                } else {
                    count[index - 1] += count[index]
                    count[index] = 0
                }
            }
            count[6] += resetFish
            count[8] += bornFish
        }
        return count.sum()
    }

    val testInput = parse(readInput("day06/test"))
    check(part1and2(testInput, 18) == 26.toLong())

    val input = parse(readInput("day06/input"))
    println(part1and2(input, 80))
    println(part1and2(input, 256))
}

data class FishState(val age: Int, val count: Int)


