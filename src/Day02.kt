import kotlin.math.abs

// AoC 2024 - Day 02: Red-Nosed Reports
// https://adventofcode.com/2024/day/2

class Day02 : Day(dayId = 2, expectedResult = listOf(2, 359, 4, 418)) {

    private fun List<String>.processInput(): List<List<Int>> = map { report ->
        report.split(" ").map { it.toInt() }
    }

    private fun List<Int>.distanceBetweenAdjacent() = zipWithNext { a, b -> abs(b - a) }

    private fun List<Int>.isSafe(): Boolean {
        val (min, max) = distanceBetweenAdjacent().let { it.min() to it.max() }
        return (isAllIncreasing() || isAllDecreasing()) && max <= 3 && min >= 1
    }

    private fun List<Int>.isSafeWithLevelDampener(): Boolean {
        if (isSafe()) return true

        for (i in indices)
            if (minusIndex(i).isSafe()) return true

        return false
    }

    override fun part1(input: List<String>): Number {
        return input.processInput().count { it.isSafe() }
    }

    override fun part2(input: List<String>): Number {
        return input.processInput().count { it.isSafeWithLevelDampener() }
    }
}

fun main() = Day02().run()
