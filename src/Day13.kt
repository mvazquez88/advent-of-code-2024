// AoC 2024 - Day 13: Claw Contraption
// https://adventofcode.com/2024/day/13

data class Button(val x: Int, val y: Int, val cost: Int) {
    companion object {
        fun from(raw: String, cost: Int) = Button(
            x = raw.substringAfter("X+").substringBefore(",").toInt(),
            y = raw.substringAfter("Y+").toInt(),
            cost = cost
        )
    }
}

class Day13 : Day(dayId = 13, expectedResult = listOf(480, 25629, 875318608908, 107487112929999)) {

    private fun List<String>.processInput(): List<Pair<Pair<Button, Button>, P2>> =
        chunked(4).map {
            val prize = P2(it[2].between("X=", ",").toInt(), it[2].substringAfter("Y=").toInt())
            val buttonA = Button.from(it[0], cost = 3)
            val buttonB = Button.from(it[1], cost = 1)
            (buttonA to buttonB) to prize
        }.toList()

    private fun calculateMoves(
        target: P2,
        buttons: Pair<Button, Button>,
        prizeOffset: Long = 0L
    ): Long {
        val (buttonA, buttonB) = buttons
        val (targetX, targetY) = target.x + prizeOffset to target.y + prizeOffset

        val determinant = (buttonA.x * buttonB.y) - (buttonA.y * buttonB.x)

        val countA = (targetX * buttonB.y - targetY * buttonB.x) / determinant
        val countB = (buttonA.x * targetY - buttonA.y * targetX) / determinant

        val offsetX = buttonA.x * countA + buttonB.x * countB
        val offsetY = buttonA.y * countA + buttonB.y * countB

        if (offsetX == targetX && offsetY == targetY) return (countA * 3) + countB
        return 0
    }

    override fun part1(input: List<String>) = input.processInput().sumOf { (buttons, prize) ->
        calculateMoves(prize, buttons)
    }

    override fun part2(input: List<String>) = input.processInput().sumOf { (buttons, prize) ->
        calculateMoves(prize, buttons, prizeOffset = 10000000000000L)
    }
}

fun main() = Day13().run()
