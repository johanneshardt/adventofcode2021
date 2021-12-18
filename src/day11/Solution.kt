package day11

import readInput
import kotlin.math.abs

fun main() {

    fun parse(input: List<String>): Simulation {
        return Simulation(
            input.flatMapIndexed { row, line ->
                line.mapIndexed { col, num ->
                    Octopus(col, row, num.toString().toInt())
                }
            }.toSet()
        )
    }

    fun part1(input: Simulation): Int {
        return (0 until 100).sumOf { input.step() }
    }

    fun part2(input: Simulation): Int {
        var step = 1
        while (true) {
            if (input.step() == 100) {
                return step
            }
            step += 1
        }
    }

    // Since the .step() function mutates state, we need multiple Simulation objects
    val testInput = parse(readInput("day11/test"))
    check(part1(testInput.copy()) == 1656)
    check(part2(testInput.copy()) == 195)

    val input = parse(readInput("day11/input"))
    println(part1(input.copy()))
    println(part2(input.copy()))
}

data class Simulation(val octopuses: Set<Octopus>) {
    // Return number of flashes that occurred in the step
    fun step(): Int {
        val seen = mutableSetOf<Octopus>()
        octopuses.map { octopus -> octopus.inc() }
        // TODO .filter() is called two times with the same condition, adding unnecessary overhead
        while (octopuses.filter { it.hasFlashed }.any { it !in seen }) {
            octopuses
                .filter { it.hasFlashed && it !in seen }
                .map {
                    seen.add(it)
                    getNeighbors(it)
                }
                .map { neighbors -> neighbors.map { it.inc() } }
        }
        val numFlashed = octopuses.count { it.hasFlashed }
        octopuses.forEach { it.hasFlashed = false }
        return numFlashed
    }

    private fun getNeighbors(octopus: Octopus): Set<Octopus> {
        return octopuses.filter {
            abs(octopus.x - it.x) <= 1 && abs(octopus.y - it.y) <= 1
        }.toSet() - octopus
    }

    fun copy(): Simulation {
        return Simulation(octopuses.map { it.copy() }.toSet())
    }
}

data class Octopus(val x: Int, val y: Int, var energy: Int, var hasFlashed: Boolean = false) {
    fun inc(): Octopus {
        if (!hasFlashed) {
            energy += 1
            if (energy > 9) {
                hasFlashed = true
                energy = 0
            }
        }
        return this
    }
}
