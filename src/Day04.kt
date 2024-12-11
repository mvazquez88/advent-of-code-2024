// AoC 2024 - Day 04: Ceres Search
// https://adventofcode.com/2024/day/04

class Day04 : Day(dayId = 4, expectedResult = listOf(18, 2530, 9, 1921)) {

    private fun isCrossMas(input: Array<CharArray>, center: Point): Boolean {
        if (center.y !in 1..<input.size - 1 || center.x !in 1..<input[center.x].size - 1)
            return false

        val firstDiagonal = input.at(center.upLeft(1)) + "A" + input.at(center.downRight(1))
        val secondDiagonal = input.at(center.upRight(1)) + "A" + input.at(center.downLeft(1))

        val matcher = "MAS|SAM".toRegex()
        return firstDiagonal.matches(matcher) && secondDiagonal.matches(matcher)
    }

    private fun isXmas(input: Array<CharArray>, chain: List<Point>): Boolean {
        if (chain.any { !it.isValid(input) }) return false

        return chain.map { input.at(it) }.toCharArray().concatToString() == "XMAS"
    }

    private fun countXmas(input: Array<CharArray>, start: Point): Int {
        val length = 3
        val combinations = listOf(
            start.upRange(length),
            start.downRange(length),
            start.rightRange(length),
            start.leftRange(length),
            start.upLeftRange(length),
            start.upRightRange(length),
            start.downLeftRange(length),
            start.downRightRange(length),
        )
        return combinations.count { isXmas(input, it) }
    }

    private fun List<String>.processInput(): Array<CharArray> =
        map { it.toCharArray() }.toTypedArray()

    override fun part1(input: List<String>): Number = input.processInput().let { matrix ->
        return matrix.indices2d
            .filter { matrix.at(it) == 'X' }
            .sumOf { countXmas(matrix, it) }
    }

    override fun part2(input: List<String>): Number = input.processInput().let { matrix ->
        return matrix.indices2d
            .filter { matrix.at(it) == 'A' }
            .count { isCrossMas(matrix, it) }
    }
}

fun main() = Day04().run()
