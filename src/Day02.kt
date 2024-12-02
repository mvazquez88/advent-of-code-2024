import kotlin.math.abs

private fun List<Int>.isAllDecreasing() = asSequence().zipWithNext { a, b -> a > b }.all { it }

private fun List<Int>.isAllIncreasing() = asSequence().zipWithNext { a, b -> a < b }.all { it }

private fun List<Int>.maxDistanceBetweenAdjacent() =
    asSequence().zipWithNext { a, b -> abs(b - a) }.max()

private fun List<Int>.minDistanceBetweenAdjacent() =
    asSequence().zipWithNext { a, b -> abs(b - a) }.min()

private fun List<Int>.isSafe() = (isAllIncreasing() || isAllDecreasing())
        && (maxDistanceBetweenAdjacent() <= 3 && minDistanceBetweenAdjacent() >= 1)

private fun List<Int>.isSafeWithLevelDampener(): Boolean {
    if (isSafe()) return true

    forEachIndexed { index, _ ->
        if (filterIndexed { i, _ -> i != index }.isSafe())
            return true
    }
    return false
}

private fun part1(): Int {
    // Read input as a List<List<Int>>
    val input = readInput("day2").map { report ->
        report.split(" ").map { it.toInt() }
    }

    return input.count { it.isSafe() } // Correct answer: 359
}

private fun part2(): Int {
    // Read input as a List<List<Int>>
    val input = readInput("day2").map { report ->
        report.split(" ").map { it.toInt() }
    }

    return input.count { it.isSafeWithLevelDampener() } // Correct answer: 418
}

fun main() {
    println("Part 1 answer is ${part1()}")
    println("Part 2 answer is ${part2()}")
}
