import kotlin.math.pow
import kotlin.streams.asStream

// AoC 2024 - Day 07: Bridge Repair
// https://adventofcode.com/2024/day/07

class Day07b : Day(
    dayId = 7,
    expectedResult = listOf(3749L, 303766880536L, 11387L, 337041851384440L)
) {

    private fun List<String>.processInput(): List<Pair<Long, List<Long>>> =
        map { it.substringBefore(":") to it.substringAfter(":").trim().split(" ") }
            .map { (expected, values) -> expected.toLong() to values.map { it.toLong() } }

    private fun isSolvable(values: List<Long>, expected: Long, operators: List<Operator>): Boolean {
        val operationsCount = values.size - 1
        val permutationsCount = operators.size.toDouble().pow(operationsCount).toInt()

        val permutations = lazyPermutations(permutationsCount, operationsCount, operators.size)

        return permutations.asStream()
            .parallel()
            .filter { solve(values, it) == expected }
            .findAny().isPresent
    }

    private fun lazyPermutations(count: Int, length: Int, variants: Int): Sequence<List<Operator>> {
        var permutationIndex = 0
        return generateSequence {
            if (permutationIndex == count) return@generateSequence null

            permutationIndex++.toPaddedString(length, variants)
                .map { Operator.entries[it.digitToInt()] }
        }
    }

    private fun solve(values: List<Long>, operators: List<Operator>) =
        values.reduceIndexed { index, acc, value -> operators[index - 1].apply(acc, value) }

    override fun part1(input: List<String>): Number {
        val operators = listOf(Operator.SUM, Operator.MULTIPLY)
        return input.processInput()
            .filter { (expected, values) -> isSolvable(values, expected, operators) }
            .sumOf { (expected, _) -> expected }
    }

    override fun part2(input: List<String>): Number {
        val operators = listOf(Operator.SUM, Operator.MULTIPLY, Operator.CONCAT)
        return input.processInput()
            .filter { (expected, values) -> isSolvable(values, expected, operators) }
            .sumOf { (expected, _) -> expected }
    }
}

fun main() = Day07b().run()
