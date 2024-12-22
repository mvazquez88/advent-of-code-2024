import kotlin.time.measureTime

abstract class Day(
    private val dayId: Int,
    private val expectedResult: List<Number>
) {

    abstract fun part1(input: List<String>): Number
    abstract fun part2(input: List<String>): Number

    private val filename = "Day%02d".format(dayId)

    private fun expectedResult(part: Int, isTest: Boolean) = expectedResult[
        when {
            part == 1 && isTest -> 0
            part == 1 && !isTest -> 1
            part == 2 && isTest -> 2
            else -> 3
        }]

    private fun run(part: Int, isTest: Boolean): String {
        val expectedResult = expectedResult(part, isTest)
        if (expectedResult == -1 || expectedResult == -1L) return ""

        val filename = if (isTest) "${filename}_test" else filename
        var result: Number

        val input = readInput(filename)
        val elapsedTime = measureTime {
            result = if (part == 1) part1(input) else part2(input)
        }

        val emoji = if (expectedResult.toLong() == result.toLong()) "✅" else "❌ (expected $expectedResult)"
        val dataSource = if (isTest) "test" else "real"

        return "\n  Result with $dataSource data: $result (${elapsedTime.inWholeMilliseconds}ms) $emoji"
    }

    fun run() {
        val output = buildString {
            append("------ Day $dayId ------")
            appendLine()
            append("Part 1")
            append(run(1, true))
            append(run(1, false))
            appendLine()
            append("Part 2")
            append(run(2, true))
            append(run(2, false))
        }

        output.println()
    }
}
