import kotlin.math.max
import kotlin.math.min

fun main() {
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

    result.println()
}
