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
                    .map { FishState(age = it.key, count = it.value) }
            }
            .toSet()
    }

    fun simulateFish(input: Set<FishState>, days: Int): Long {
        val fishCount: MutableList<Long> = MutableList(9) { 0 }
        input.forEach { fishCount[it.age] = it.count.toLong() }
        repeat(days) {
            val newFish = fishCount.first()
            fishCount.indices.drop(1).forEach {
                fishCount[it - 1] = fishCount[it]
                fishCount[it] = 0
            }
            fishCount[6] += newFish
            fishCount[8] += newFish
        }
        return fishCount.sum()
    }

    fun part1(input: Set<FishState>, days: Int = 80): Long {
        return simulateFish(input, days)
    }

    fun part2(input: Set<FishState>, days: Int = 256): Long {
        return simulateFish(input, days)
    }


    val testInput = parse(readInput("day06/test"))
    check(part1(testInput, 18) == 26.toLong())

    val input = parse(readInput("day06/input"))
    println(part1(input))
    println(part2(input))
}

data class FishState(val age: Int, val count: Int)


