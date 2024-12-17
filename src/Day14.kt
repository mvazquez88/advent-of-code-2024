// AoC 2024 - Day 14: Restroom Redoubt
// https://adventofcode.com/2024/day/14

data class Robot(val initial: Point, val velocity: Point) {
    fun calculatePosition(seconds: Int, maxY: Int, maxX: Int): Point {
        var current = initial

        for (s in 0..<seconds) {
            val newX = Math.floorMod(current.x + velocity.x, maxX)
            val newY = Math.floorMod(current.y + velocity.y, maxY)
            current = Point(newX, newY)
        }

        return current
    }
}

class Map(private val height: Int, private val width: Int) {
    private val matrix = Matrix(height, width, default = 0)

    fun addRobots(robots: List<Robot>, seconds: Int) = robots.map {
        it.calculatePosition(seconds, height, width)
    }.forEach { matrix.increment(it) }

    fun addRobotsWithNoOverlapping(robots: List<Robot>, seconds: Int): Boolean {
        for (robot in robots) {
            val position = robot.calculatePosition(seconds, height, width)
            if (matrix.at<Int>(position) == 1) return false
            matrix.increment(position)
        }
        return true
    }

    fun calculateRobotsPerQuadrant(): Int = with(matrix.indices2d) {
        val q1 = filter { matrix.isQ1(it) }.sumOf { matrix.at<Int>(it) }
        val q2 = filter { matrix.isQ2(it) }.sumOf { matrix.at<Int>(it) }
        val q3 = filter { matrix.isQ3(it) }.sumOf { matrix.at<Int>(it) }
        val q4 = filter { matrix.isQ4(it) }.sumOf { matrix.at<Int>(it) }
        return q1 * q2 * q3 * q4
    }
}

class Day14 : Day(dayId = 14, expectedResult = listOf(12, 218295000, -1, 6870)) {

    private val testMapSize = 7 to 11
    private val realMapSize = 103 to 101

    private fun List<String>.processInput(): List<Robot> = flatMap { it.split(" ") }
        .chunked(2)
        .map {
            val l = Point(it[0].between("p=", ",").toInt(), it[0].substringAfter(",").toInt())
            val v = Point(it[1].between("v=", ",").toInt(), it[1].substringAfter(",").toInt())
            Robot(l, v)
        }

    override fun part1(input: List<String>): Number {
        val robots = input.processInput()
        val isTest = robots.size == 12
        val (height, width) = if (isTest) testMapSize else realMapSize
        val map = Map(height, width)
        map.addRobots(robots, seconds = 100)
        return map.calculateRobotsPerQuadrant()
    }

    override fun part2(input: List<String>): Number {
        val robots = input.processInput()
        val (height, width) = realMapSize
        for (s in 0..height * width) {
            val map = Map(height, width)
            if (map.addRobotsWithNoOverlapping(robots, s))
                return s
        }
        return 0
    }
}

fun main() = Day14().run()
