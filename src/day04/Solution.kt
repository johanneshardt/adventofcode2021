package day04

import readInput

fun main() {

    // Maybe refactor
    fun parse(input: List<String>, dimension: Int): Game {
        val numberDraw = input.first().split(",").map { it.toInt() }
        val boards = mutableListOf<Board>()
        val currentBoard = mutableSetOf<Square>()
        var currentRow = 0
        for (line in input.drop(1)) {
            if (line.isBlank()) {
                if (currentRow >= dimension) {
                    // Convert to immutable set
                    boards.add(Board(currentBoard.toSet()))
                    currentBoard.clear()
                }
                currentRow = 0
            } else {
                line.trim().split("\\s+".toRegex()).map { it.toInt() }.forEachIndexed { col, element ->
                    currentBoard.add(Square(element, currentRow, col))
                }
                currentRow += 1
            }
        }
        // If there is no blank line after the last board
        boards.add(Board(currentBoard.toSet()))
        return Game(numberDraw, boards.toList())
    }

    fun part1(g: Game): Int {
        for (num in g.numberDraw) {
            for (board in g.boards) {
                if (num in board.numbers) {
                    board.mark(num)
                }
                if (board.bingo()) {
                    return (board.calculateScore(num))
                }
            }
        }
        throw IllegalStateException("No valid boards")
    }

    fun part2(g: Game): Int {
        val winningTurns = mutableListOf<Pair<Board, Int>>()
        val winningBoards = mutableSetOf<Board>()
        for (num in g.numberDraw) {
            for (board in g.boards) {
                // Avoid editing board that already won, as that messes up the equality checking for the mutable set
                if (num in board.numbers && board !in winningBoards) {
                    board.mark(num)
                }
                if (board.bingo() && board !in winningBoards) {
                    winningTurns.add(Pair(board, num))
                    winningBoards.add(board)
                }
            }
        }
        if (winningBoards.size > 0) {
            val t = winningTurns.last()
            return t.first.calculateScore(t.second)
        } else {
            throw IllegalStateException("No valid boards")
        }
    }


    val testInput = parse(readInput("day04/test"), 5)
    check(part1(testInput) == 4512)

    val input = parse(readInput("day04/input"), 5)
    println(part1(input))
    println(part2(input))
}

data class Square(
    val number: Int,
    val row: Int,
    val col: Int,
    var marked: Boolean = false
)

data class Board(private val squares: Set<Square>) {
    // Cache available numbers for better performance
    val numbers = squares.map { it.number }.toSet()

    // Pretty slow, maybe improve?
    fun mark(num: Int) {
        squares.forEach {
            if (it.number == num) {
                it.marked = true
                return  // Return here since there aren't duplicate values in bingo
            }
        }
    }

    fun bingo(): Boolean {
        val completeRows = squares.groupBy { it.row }.map { it.value }.any { row -> row.all { it.marked } }
        val completeCols = squares.groupBy { it.col }.map { it.value }.any { col -> col.all { it.marked } }

        return completeRows or completeCols
    }

    fun calculateScore(num: Int): Int {
        return squares.filter { !it.marked }.sumOf { it.number } * num
    }
}

data class Game(val numberDraw: List<Int>, val boards: List<Board>)


