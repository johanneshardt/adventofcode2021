package day02

import readInput

fun main() {
    fun simulateCourse(input: List<String>, s: Submarine): Int {
        input.map {
            it.split(" ")
        }.forEach {
            val arg = it[1].toInt()
            when (it[0]) {
                "forward" -> s.forward(arg)
                "up" -> s.up(arg)
                "down" -> s.down(arg)
            }
        }
        return s.distance()
    }

    fun part1(input: List<String>): Int {
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

    fun part2(input: List<String>): Int {
        data class Sub2(
            override var horizontal: Int = 0,
            override var vertical: Int = 0, var aim: Int = 0
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


    val testInput = readInput("day02/test")
    check(part1(testInput) == 150)

    val input = readInput("day02/input")
    println(part1(input))
    println(part2(input))
}

interface Submarine {
    var horizontal: Int
    val vertical: Int

    fun forward(arg: Int)
    fun up(arg: Int)
    fun down(arg: Int)
    fun distance() = horizontal * vertical
}


