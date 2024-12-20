import kotlin.math.abs

// AoC 2024 - Day 20: Race Condition
// https://adventofcode.com/2024/day/20

class Day20 : Day(dayId = 20, expectedResult = listOf(44, 1307, 285, 986545)) {

    class Maze(val size: Int) {
        private val matrix = Matrix(size)
        private val distancesFromStart = Array(size) { IntArray(size) { Int.MAX_VALUE } }
        private val distancesFromEnd = Array(size) { IntArray(size) { Int.MAX_VALUE } }

        fun set(location: P2, value: Char) = matrix.set(location, value)

        private fun isWall(location: P2) = matrix.get(location) == '#'

        private fun distanceFromStart(to: P2) = distancesFromStart[to.y][to.x]
        private fun distanceToEnd(to: P2) = distancesFromEnd[to.y][to.x]

        private fun calculateDistances(from: P2, distances: Array<IntArray>) {
            val queue = ArrayDeque<P2>()
            queue.add(from)
            distances[from.y][from.x] = 0

            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                val distance = distances[current.y][current.x] + 1

                current.neighbors().filter {
                    matrix.isValid(it) && !isWall(it) && distances[it.y][it.x] == Int.MAX_VALUE
                }.forEach {
                    distances[it.y][it.x] = distance
                    queue.add(it)
                }
            }
        }

        fun solve(savedTimeThreshold: Int, cheatWindow: Int): Int {
            val start = matrix.find('S')
            val end = matrix.find('E')

            calculateDistances(start, distancesFromStart)
            calculateDistances(end, distancesFromEnd)
            val timeWithoutCheating = distanceToEnd(start)

            return matrix.indices2d.flatMap {
                it.neighbors(cheatWindow).filter { neighbor ->
                    matrix.isValid(neighbor)
                }.map { neighbor ->
                    it to neighbor
                }
            }.filter { (start, end) ->
                distanceFromStart(start) != Int.MAX_VALUE && distanceToEnd(end) != Int.MAX_VALUE
            }.map { (start, end) ->
                val jump = abs(end.y - start.y) + abs(end.x - start.x)
                val newTime = distanceFromStart(start) + distanceToEnd(end) + jump
                timeWithoutCheating - newTime
            }.count {
                it >= savedTimeThreshold
            }
        }
    }

    private fun List<String>.processInput(): Maze {
        val maze = Maze(size)
        for (y in indices)
            for (x in this[y].indices) {
                maze.set(P2(x, y), this[y][x])
            }
        return maze
    }

    override fun part1(input: List<String>): Number {
        val maze = input.processInput()

        val isTest = maze.size == 15
        val minSaveTime = if (isTest) 1 else 100

        return maze.solve(minSaveTime, cheatWindow = 2)
    }

    override fun part2(input: List<String>): Number {
        val maze = input.processInput()

        val isTest = maze.size == 15
        val minSaveTime = if (isTest) 50 else 100

        return maze.solve(minSaveTime, cheatWindow = 20)
    }
}

fun main() = Day20().run()
