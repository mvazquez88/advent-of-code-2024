import kotlin.math.max
import kotlin.math.min

// AoC 2024 - Day 01: Historian Hysteria
// https://adventofcode.com/2024/day/1

class Day01 : Day(dayId = 1, expectedResult = listOf(11, 2264607, 31, 19457120)) {

    private fun List<String>.processInput(): Pair<List<Int>, List<Int>> {
        val delimiter = "   "

        val firstList = map { it.split(delimiter)[0] }.map { it.toInt() }
        val secondList = map { it.split(delimiter)[1] }.map { it.toInt() }

        return firstList to secondList
    }

    override fun part1(input: List<String>): Number {
        val (listA, listB) = input.processInput()

        val sortedA = listA.sorted()
        val sortedB = listB.sorted()

        return sortedA
            .zip(sortedB) { first, second -> max(first, second) - min(first, second) }
            .sum()
    }

    override fun part2(input: List<String>): Number {
        val (listA, listB) = input.processInput()

        return listA.sumOf { first -> first * listB.count { second -> first == second } }
    }
}

fun main() = Day01().run()
