import kotlin.math.max
import kotlin.math.min

fun part1(): Int {
    val input = readInput("day1")
    val delimiter = "   "

    // Extract each list
    val firstList = input.map { it.split(delimiter).first() }.map { it.toInt() }
    val secondList = input.map { it.split(delimiter).elementAt(1) }.map { it.toInt() }

    // Sort lists
    val sortedFirst = firstList.sorted()
    val sortedSecond = secondList.sorted()

    // Calculate distance between numbers
    val result = sortedFirst.zip(sortedSecond) { first, second ->
        max(first, second) - min(first, second)
    }.sum()

    return result
}

fun part2(): Int {
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

    return similarityScore
}

fun main() {
    part2().println()
}
