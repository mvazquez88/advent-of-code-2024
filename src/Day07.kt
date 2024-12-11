// AoC 2024 - Day 07: Bridge Repair
// https://adventofcode.com/2024/day/07

enum class Operator {
    SUM,
    MULTIPLY,
    CONCAT;

    fun apply(a: Long, b: Long): Long = when (this) {
        SUM -> a + b
        MULTIPLY -> a * b
        CONCAT -> "$a$b".toLong()
    }
}

class Day07 : Day(
    dayId = 7,
    expectedResult = listOf(3749L, 303766880536L, 11387L, 337041851384440L)
) {

    private fun List<String>.processInput(): List<Pair<Long, List<Long>>> =
        map { it.substringBefore(":") to it.substringAfter(":").trim().split(" ") }
            .map { (expected, values) -> expected.toLong() to values.map { it.toLong() } }

    private fun isSolvable(values: List<Long>, expected: Long, operators: List<Operator>): Boolean {
        fun isSolvable(accumulated: Long, index: Int): Boolean {
            if (accumulated > expected) return false
            if (index == values.size) return expected == accumulated

            return operators.any { isSolvable(it.apply(accumulated, values[index]), index + 1) }
        }
        return isSolvable(values.first(), 1)
    }


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

fun main() = Day07().run()
