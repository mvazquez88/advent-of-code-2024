import kotlin.math.max
import kotlin.math.min

private fun part1(): Int {
    val input = readInput("day1")

    // Extract each list
    val delimiter = "   "
    val firstList = input.map { it.split(delimiter).first() }.map { it.toInt() }
    val secondList = input.map { it.split(delimiter).elementAt(1) }.map { it.toInt() }

    // Sort lists
    val sortedFirst = firstList.sorted()
    val sortedSecond = secondList.sorted()

    // Calculate distance between numbers
    val result = sortedFirst.zip(sortedSecond) { first, second ->
        max(first, second) - min(first, second)
    }.sum()

    return result // Correct answer: 2264607
}

private fun part2(): Int {
    val input = readInput("day1")
    val delimiter = "   "

    // Extract each list
    val firstList = input.map { it.split(delimiter).first() }.map { it.toInt() }
    val secondList = input.map { it.split(delimiter).elementAt(1) }.map { it.toInt() }

    var similarityScore = 0
    firstList.forEach { locationId ->
        val count = secondList.count { it == locationId }
        similarityScore += locationId * count
    }

    return similarityScore // Correct answer: 19457120
}

fun main() {
    println("Part 1 answer is ${part1()}")
    println("Part 2 answer is ${part2()}")
}
