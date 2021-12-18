package day12

import readInput

fun main() {

    fun parse(input: List<String>): Graph {
        val nodes = input.map { line ->
            val l = line.trim().split("-")
            Edge(Pair(l[0], l[1]))
        }.toSet()
        val inverted = nodes.map {
            it.converse()
        }
        return Graph(nodes + inverted)
    }

    fun part1(input: Graph): Int {
        return input.traverse().size
    }

    fun part2(input: Graph): Int {
        val s = input.traverseWithLoopback().size
        return s
    }


    val testInput1 = parse(readInput("day12/test10"))
    val testInput2 = parse(readInput("day12/test19"))
    val testInput3 = parse(readInput("day12/test226"))
    check(part1(testInput1) == 10)
    check(part1(testInput2) == 19)
    check(part1(testInput3) == 226)
    check(part2(testInput1) == 36)
    check(part2(testInput2) == 103)
    check(part2(testInput3) == 3509)


    val input = parse(readInput("day12/input"))
    println(part1(input))
//    println(part2(input))
}

data class Graph(val edges: Set<Edge>) {
    private val root = Edge(Pair("root", "start"))
    fun traverse(): List<Set<Edge>> {
        return traverse(current = root, traversed = setOf(), loopback = false)
    }

    fun traverseWithLoopback(): List<Set<Edge>> {
        return traverse(current = root, traversed = setOf(), loopback = true)
    }

    private fun traverse(current: Edge, traversed: Set<Edge>, loopback: Boolean): List<Set<Edge>> {
        val loopbacks = mutableListOf<Boolean>()
        val next = edges
            .filter { edge ->
                val c = canTraverse(current, edge, traversed, loopback)
                loopbacks.add(c.second)
                c.first
            }
        if (current.e.second == "end") {
            return listOf(traversed)
        } else if (next.isEmpty()) {
            return listOf()
        } else if (loopback) {
            return next.zip(loopbacks).flatMap { traverse(it.first, traversed + it.first, !it.second) }
        } else {
            return next.flatMap { traverse(it, traversed + it, false) }
        }
    }

    private fun canTraverse(
        current: Edge,
        dest: Edge,
        traversed: Set<Edge>,
        loopback: Boolean
    ): Pair<Boolean, Boolean> {
        if (dest.e.first == current.e.second) {
            if (isUpperCase(dest.e.second)) {
                return Pair(true, loopback)
            } else if (!loopback) {
                val cond = dest !in traversed &&
                        dest.converse() !in traversed &&
                        dest.e.second != "start" &&
                        dest.e.second !in traversed.map { it.e.first }
                return Pair(cond, loopback)
            } else {
                return Pair(true, false)
            }
        }
        return Pair(false, loopback)
    }

    private fun contains(edge: Edge, edges: Set<Edge>): Boolean {
        return edges.any {
            it.e.second == edge.e.second ||
                    it.e.second == edge.converse().e.second
        }
    }

    private fun isUpperCase(s: String): Boolean {
        return s.all { it.isUpperCase() }
    }
}

data class Edge(val e: Pair<String, String>) {
    fun converse(): Edge {
        return Edge(Pair(e.second, e.first))
    }
}