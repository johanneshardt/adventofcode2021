package day10

import readInput

fun main() {
    val invalidSyntaxScoring =
        Delimiters.values().map { it.d }.zip(listOf(3, 57, 1197, 25137)).associate { (op, score) ->
            op to score
        }

    val syntaxCompletionScoring = Delimiters.values().map { it.d }.zip(listOf(1, 2, 3, 4)).associate { (op, score) ->
        op to score
    }

    fun parse(input: List<String>): List<List<Char>> {
        return input.map { line ->
            line.trim().toList()
        }
    }

    fun part1(input: List<List<Char>>): Int {
        val stack = ArrayDeque<Char>()
        val incorrect = mutableListOf<Char>()
        var foundError = false
        for (line in input) {
            for (c in line) {
                if (c in Delimiters.opening) {
                    stack.addFirst(c)
                } else if (c in Delimiters.closing) {
                    if (Delimiters.inverse(c)!! != stack.removeFirst() && !foundError) {
                        incorrect.add(c)
                        foundError = true
                    }
                }
            }
            stack.clear()
            foundError = false
        }
        return incorrect.sumOf { invalidSyntaxScoring[Delimiters.matching(it)]!! }
    }

    fun part2(input: List<List<Char>>): Long {
        val scores = mutableListOf<Long>()
        val stack = ArrayDeque<Char>()
        val incorrect = mutableListOf<Char>()
        var foundError = false
        for (line in input) {
            for (c in line) {
                if (c in Delimiters.opening) {
                    stack.addFirst(c)
                } else if (c in Delimiters.closing) {
                    if (Delimiters.inverse(c)!! != stack.removeFirst() && !foundError) {
                        incorrect.add(c)
                        foundError = true
                    }
                }
            }

            if (!foundError) {
                var score = 0.toLong()
                while (stack.size > 0) {
                    val c = stack.removeFirst()
                    score *= 5
                    score += syntaxCompletionScoring[Delimiters.matching(c)]!!
                }
                if (score > 0) {
                    scores.add(score)
                }
            }
            foundError = false
            stack.clear()
        }
        scores.sort()
        return scores[(scores.size - 1) / 2]
    }


    val testInput = parse(readInput("day10/test"))
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957.toLong())

    val input = parse(readInput("day10/input"))
    println(part1(input))
    println(part2(input))
}

data class Delimiter(val opening: Char, val closing: Char) {
    fun matches(c: Char): Boolean {
        return c == opening || c == closing
    }

    fun invertChar(c: Char): Char? {
        return when (c) {
            opening -> {
                closing
            }
            closing -> {
                opening
            }
            else -> {
                null
            }
        }
    }
}

enum class Delimiters(val d: Delimiter) {
    PAREN(Delimiter('(', ')')),
    SQUARE(Delimiter('[', ']')),
    BRACKET(Delimiter('{', '}')),
    ANGLED(Delimiter('<', '>'));

    companion object {
        val opening = values().map { it.d.opening }.toSet()
        val closing = values().map { it.d.closing }.toSet()
        fun inverse(c: Char): Char? {
            return values().map { it.d }.find { it.matches(c) }?.invertChar(c)
        }

        fun matching(c: Char): Delimiter {
            return values().map { it.d }.first { it.matches(c) }
        }
    }
}