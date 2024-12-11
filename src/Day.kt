import kotlin.time.measureTime

abstract class Day(
    private val dayId: Int,
    private val expectedResult: List<Number>
) {

    abstract fun part1(input: List<String>): Number
    abstract fun part2(input: List<String>): Number

    private val filename = "Day%02d".format(dayId)

    private fun run(part: Int, isTest: Boolean): String {
        val filename = if (isTest) "${filename}_test" else filename
        var result: Number

        val input = readInput(filename)
        val elapsedTime = measureTime {
            result = if (part == 1) part1(input) else part2(input)
        }

        val expectedResult = when {
            part == 1 && isTest -> 0
            part == 1 && !isTest -> 1
            part == 2 && isTest -> 2
            else -> 3
        }.let { expectedResult[it] }

        val emoji = if (expectedResult == result) "✅" else "❌ (expected $expectedResult)"
        val dataSource = if (isTest) "test" else "real"

        return "  Result with $dataSource data: $result (${elapsedTime.inWholeMilliseconds}ms) $emoji"
    }

    fun run() {
        val output = buildString {
            append("------ Day $dayId ------")
            appendLine()
            append("Part 1")
            appendLine()
            append(run(1, true))
            appendLine()
            append(run(1, false))
            appendLine()
            append("Part 2")
            appendLine()
            append(run(2, true))
            appendLine()
            append(run(2, false))
        }

        output.println()
    }
}
