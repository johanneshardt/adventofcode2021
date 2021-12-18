package day13

import readInput

fun main() {

    fun parse(input: List<String>): Pair<Dots, Folds> {
        val dots = input
            .takeWhile { it.trim().isNotEmpty() }
            .map { line ->
                val p = line.split(",").map { it.toInt() }
                Dot(x = p[0], y = p[1])
            }
            .toMutableSet()
        val folds = input
            .dropWhile { it.trim().isNotEmpty() }.drop(1)
            .map { line ->
                val f = line.removePrefix("fold along").trim().split("=")
                when (f[0]) {
                    "x" -> Fold(f[1].toInt(), FoldKind.HORIZONTAL)
                    "y" -> Fold(f[1].toInt(), FoldKind.VERTICAL)
                    else -> throw IllegalArgumentException("Invalid input file")
                }
            }
        return Pair(Dots(dots), Folds(folds))
    }

    fun part1(input: Pair<Dots, Folds>): Int {
        input.second.f[0].execute(input.first)
        return input.first.matrix.count()
    }

    fun part2(input: Pair<Dots, Folds>): String {
        input.second.execute(input.first)
        return input.first.toString()
    }


    val testInput = parse(readInput("day13/test"))
    check(part1(testInput) == 17)

    val input = parse(readInput("day13/input"))
    println(part1(input))
    println(part2(input))
}

interface Folding {
    fun execute(dots: Dots)
}

data class Dot(val x: Int, val y: Int)
data class Dots(val matrix: MutableSet<Dot>) {
    override fun toString(): String {
        val repr = StringBuilder()
        val width = matrix.maxOf { it.x }
        val height = matrix.maxOf { it.y }

        // TODO refactor
        for (row in 0..height) {
            val line = StringBuilder()
            for (col in 0..width) {
                if (Dot(col, row) in matrix) {
                    line.append("⬛")
                } else {
                    line.append("⬜")
                }
            }
            repr.appendLine(line.toString())
        }
        return repr.toString()
    }
}

data class Fold(val line: Int, val kind: FoldKind) : Folding {
    override fun execute(dots: Dots) {
        when (kind) {
            FoldKind.HORIZONTAL -> {
                dots.matrix
                    .filter { it.x > line }
                    .forEach {
                        dots.matrix.remove(it)
                        dots.matrix.add(it.copy(x = line - (it.x - line)))
                    }
            }

            FoldKind.VERTICAL -> {
                dots.matrix
                    .filter { it.y > line }
                    .forEach {
                        dots.matrix.remove(it)
                        dots.matrix.add(it.copy(y = line - (it.y - line)))
                    }
            }
        }
    }
}

data class Folds(val f: List<Fold>) : Folding {
    override fun execute(dots: Dots) {
        f.forEach { it.execute(dots) }
    }
}

enum class FoldKind() {
    HORIZONTAL, VERTICAL;
}



