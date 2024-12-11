// AoC 2024 - Day 11: Plutonian Pebbles
// https://adventofcode.com/2024/day/11

class Day11 : Day(
    dayId = 11,
    expectedResult = listOf(55312L, 213625L, 65601038650482L, 252442982856820L)
) {

    private fun List<String>.processInput(): List<Long> = first().split(" ").map { it.toLong() }

    data class Stone(val value: Long, val blinkedTimes: Int = 0) {
        fun blink(): List<Stone> = when {
            value == 0L -> listOf(Stone(1, blinkedTimes + 1))
            "$value".length % 2 == 0 -> split()
            else -> listOf(Stone(value * 2024, blinkedTimes + 1))
        }

        private fun split() = value.toString()
            .chunked("$value".length / 2)
            .map { Stone(it.toLong(), blinkedTimes + 1) }

        override fun toString() = "$value:$blinkedTimes"
    }

    private fun solve(stones: List<Stone>, blinks: Int): Long {
        val cachedResults = hashMapOf<String, Long>()

        fun solve(stone: Stone): Long {
            if (stone.blinkedTimes == blinks) return 1

            cachedResults[stone.toString()]?.let { return it }
            val result = stone.blink().sumOf { solve(it) }
            cachedResults[stone.toString()] = result
            return result

        }
        return stones.sumOf { solve(it) }
    }

    override fun part1(input: List<String>): Number {
        val stones = input.processInput().map { Stone(it) }
        return solve(stones, 25)
    }

    override fun part2(input: List<String>): Number {
        val stones = input.processInput().map { Stone(it) }
        return solve(stones, 75)
    }
}

fun main() = Day11().run()
