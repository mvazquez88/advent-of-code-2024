import java.util.Collections

// AoC 2024 - Day 05: Print Queue
// https://adventofcode.com/2024/day/05

class Day05 : Day(dayId = 5, expectedResult = listOf(143, 4662, 123, 5900)) {

    private fun List<String>.extractRules(): List<Pair<Int, Int>> = subList(0, indexOf(""))
        .map { it.split("|") }
        .map { it[0].toInt() to it[1].toInt() }

    private fun List<String>.extractUpdates(): List<List<Int>> = subList(indexOf("") + 1, size)
        .map { update -> update.split(",").map { it.toInt() } }

    private fun List<Int>.isValid(rules: List<Pair<Int, Int>>) = indices.all { index ->
        rules.filter { it.first == this[index] }
            .none { subList(0, index).contains(it.second) }
    }

    private fun List<Int>.applyRules(allRules: List<Pair<Int, Int>>): List<Int> {
        val result = this.toMutableList()
        var index = 0

        while (index < result.size) {
            val rules = allRules.filter { it.first == result[index] }
            val previous = result.subList(0, index)

            rules.filter { previous.contains(it.second) }
                .map { result.indexOf(it.first) to result.indexOf(it.second) }
                .onEach { Collections.swap(result, it.first, it.second) }
                .onEach { index = 0 }

            index++
        }

        return result
    }

    override fun part1(input: List<String>): Number {
        return input.extractUpdates()
            .filter { it.isValid(input.extractRules()) }
            .sumOf { it[it.size / 2] }
    }

    override fun part2(input: List<String>): Number {
        val rules = input.extractRules()

        return input.extractUpdates()
            .filter { !it.isValid(rules) }
            .map { it.applyRules(rules) }
            .sumOf { it[it.size / 2] }
    }
}

fun main() = Day05().run()
