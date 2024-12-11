// AoC 2024 - Day 03: Mull It Over
// https://adventofcode.com/2024/day/3

class Day03 : Day(dayId = 3, expectedResult = listOf(161, 161085926, 48, 82045421)) {

    private val regexPart1 = "mul\\((\\d{1,3}),(\\d{1,3})\\)".toRegex()
    private val regexPart2 = "mul\\((\\d{1,3}),(\\d{1,3})\\)|(do\\(\\))|(don't\\(\\))".toRegex()

    private fun List<String>.processInput(): String = joinToString()

    private fun MatchResult.isMultiply(): Boolean = groupValues[0].startsWith("mul(")
    private fun MatchResult.isDo(): Boolean = groupValues[0] == "do()"
    private fun MatchResult.isDont(): Boolean = groupValues[0] == "don't()"

    private fun MatchResult.multiply() = groupValues[1].toInt() * groupValues[2].toInt()

    override fun part1(input: List<String>): Number {
        return regexPart1.findAll(input.processInput()).sumOf { it.multiply() }
    }

    override fun part2(input: List<String>): Number {
        val matches = regexPart2.findAll(input.processInput())

        var enabled = true
        var sum = 0

        for (match in matches) {
            if (match.isDo()) enabled = true
            if (match.isDont()) enabled = false
            if (match.isMultiply() && enabled) sum += match.multiply()
        }

        return sum
    }
}

fun main() = Day03().run()
