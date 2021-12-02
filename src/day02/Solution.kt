package day02

import readInput

fun main() {

    fun parse(input: List<String>): List<Pair<String, Int>> {
        return input.map { it.split(" ") }.map { Pair(it[0], it[1].toInt()) }
    }

    fun simulateCourse(input: List<Pair<String, Int>>, s: Submarine): Int {
        input.forEach {
            when (it.first) {
                "forward" -> s.forward(it.second)
                "up" -> s.up(it.second)
                "down" -> s.down(it.second)
            }
        }
        return s.distance()
    }

    fun part1(input: List<Pair<String, Int>>): Int {
        data class Sub1(
            override var horizontal: Int = 0,
            override var vertical: Int = 0
        ) : Submarine {
            override fun forward(arg: Int) {
                horizontal += arg
            }

            override fun up(arg: Int) {
                vertical -= arg
            }

            override fun down(arg: Int) {
                vertical += arg
            }
        }
        return simulateCourse(input, Sub1())
    }

    fun part2(input: List<Pair<String, Int>>): Int {
        data class Sub2(
            override var horizontal: Int = 0,
            override var vertical: Int = 0,
            var aim: Int = 0
        ) : Submarine {
            override fun forward(arg: Int) {
                horizontal += arg
                vertical += arg * aim
            }

            override fun up(arg: Int) {
                aim -= arg
            }

            override fun down(arg: Int) {
                aim += arg
            }
        }
        return simulateCourse(input, Sub2())
    }

    val testInput = parse(readInput("day02/test"))
    check(part1(testInput) == 150)

    val input = parse(readInput("day02/input"))
    println(part1(input))
    println(part2(input))
}

interface Submarine {
    var horizontal: Int
    var vertical: Int

    fun forward(arg: Int)
    fun up(arg: Int)
    fun down(arg: Int)
    fun distance() = horizontal * vertical
}


