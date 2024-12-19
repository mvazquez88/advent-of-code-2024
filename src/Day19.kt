// AoC 2024 - Day 19: Linen Layout
// https://adventofcode.com/2024/day/19

class Day19 : Day(dayId = 19, expectedResult = listOf(6, 365, 16, 730121486795169)) {

    private fun countCombinations(towel: String, patterns: List<String>): Long {
        val countByIndex = Array(towel.length + 1) { index -> if (index == 0) 1L else 0L }

        for (index in towel.indices) {
            val substring = towel.substring(index)

            patterns.filter { pattern ->
                substring.startsWith(pattern)
            }.forEach { pattern ->
                countByIndex[index + pattern.length] += countByIndex[index]
            }
        }

        return countByIndex.last()
    }

    private fun List<String>.processInput(): Pair<List<String>, List<String>> {
        return this[0].split(",").map { it.trim() } to subList(2, this.size)
    }

    override fun part1(input: List<String>): Number {
        val (patterns, towels) = input.processInput()
        return towels.count { countCombinations(it, patterns) > 0 }.toLong()
    }

    override fun part2(input: List<String>): Number {
        val (patterns, towels) = input.processInput()
        return towels.sumOf { countCombinations(it, patterns) }
    }
}

fun main() = Day19().run()
