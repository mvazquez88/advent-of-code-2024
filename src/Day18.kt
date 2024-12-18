import java.util.PriorityQueue

// AoC 2024 - Day 18: RAM Run
// https://adventofcode.com/2024/day/18

class Day18 : Day(dayId = 18, expectedResult = listOf(22, 280, 61, 2856)) {

    companion object {
        private const val BLOCKER = '#'
        private const val EMPTY = '.'
    }

    class Map(val size: Int, fallingBytes: List<Point>) {

        private val matrix = Matrix(size).apply {
            for (byte in fallingBytes)
                set(byte, BLOCKER)
        }

        data class MazeRunner(val position: Point, val facing: Facing = Facing.Right) {
            fun turnLeft() = copy(facing = facing.previous())
            fun turnRight() = copy(facing = facing.next())
            fun move() = copy(position = facing.move(position))
        }

        data class Node(val location: MazeRunner, val path: List<MazeRunner>, val score: Int)

        fun solve(): Int {
            val startLocation = MazeRunner(Point(0, 0))
            val endLocation = Point(size - 1, size - 1)
            val startingNode = Node(startLocation, path = listOf(startLocation), score = 0)

            val queue = PriorityQueue<Node>(compareBy { it.score })
            queue.add(startingNode)

            val nodesScore = mutableMapOf<MazeRunner, Int>()
            val endNodes = mutableSetOf<Node>()
            val visited = mutableSetOf<MazeRunner>()

            while (queue.isNotEmpty()) {
                val currentNode = queue.poll()
                val (runner, path, score) = currentNode

                if (runner.position == endLocation) {
                    endNodes.add(currentNode)
                    continue
                }

                listOf(
                    runner.move(),
                    runner.turnLeft().move(),
                    runner.turnRight().move(),
                ).filter {
                    matrix.isValid(it.position) && it !in visited
                }.filter {
                    matrix.get(it.position) == EMPTY || it.position == endLocation
                }.map {
                    Node(it, path + it, score + 1)
                }.filter {
                    it.score <= nodesScore.getOrDefault(it.location, Int.MAX_VALUE)
                }.forEach {
                    visited.add(it.location)
                    nodesScore[it.location] = it.score
                    queue.offer(it)
                }
            }

            return if (endNodes.isEmpty()) -1 else endNodes.minOf { it.score }
        }

        override fun toString() = matrix.toString()
    }

    private fun List<String>.processInput(): List<Point> = map { it.split(',') }
        .map { Point(it[0].toInt(), it[1].toInt()) }

    override fun part1(input: List<String>): Number {
        val fallingBytes = input.processInput()
        val isTest = fallingBytes.size == 25

        val mapSize = if (isTest) 7 else 71
        val bytesCount = if (isTest) 12 else 1024

        return Map(mapSize, fallingBytes.take(bytesCount)).solve()
    }

    override fun part2(input: List<String>): Number {
        val fallingBytes = input.processInput()
        val isTest = fallingBytes.size == 25

        val mapSize = if (isTest) 7 else 71

        var (start, end) = 0 to fallingBytes.size

        while (start < end) {
            val nextByte = start + ((end - start) / 2)
            val isBlocker = Map(mapSize, fallingBytes.take(nextByte)).solve() == -1

            if (isBlocker)
                end = nextByte - 1
            else
                start = nextByte + 1
        }

        val blockerByte = fallingBytes[start - 1]
        return "${blockerByte.x}${blockerByte.y}".toInt()
    }
}

fun main() = Day18().run()
