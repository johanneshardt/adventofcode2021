fun main() {
    fun part1(input: List<Int>): Int {
        return input.zipWithNext().count { it.second > it.first }
    }

    fun part2(input: List<Int>): Int {
        return input.windowed(3).map { it.sum() }.zipWithNext().count {
            it.second > it.first
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInt("Day01_test")
    check(part1(testInput) == 7)

    val input = readInt("Day01")
    println(part1(input))
    println(part2(input))
}
