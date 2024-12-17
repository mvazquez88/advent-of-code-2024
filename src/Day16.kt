import java.util.PriorityQueue

// AoC 2024 - Day 16: Reindeer Maze
// https://adventofcode.com/2024/day/16

class Day16 : Day(dayId = 16, expectedResult = listOf(11048, 134588, 64, 631)) {

    companion object {
        private const val REINDEER: Char = 'S'
        private const val END: Char = 'E'
        private const val EMPTY: Char = '.'
    }

    data class Reindeer(val position: Point, val facing: Facing = Facing.Right) {
        fun turnLeft() = copy(facing = facing.previous())
        fun turnRight() = copy(facing = facing.next())
        fun move() = copy(position = facing.move(position))
    }

    data class Node(val reindeer: Reindeer, val path: List<Reindeer>, val score: Int)

    class Maze(val size: Int) {
        private val matrix = Matrix(size)
        private fun findReindeer() = Reindeer(matrix.find(REINDEER))
        private fun at(point: Point): Char = matrix.at(point) as Char

        fun update(point: Point, value: Char) = matrix.set(point, value)

        fun solve(): Pair<Int, Int> {
            val startReindeer = findReindeer()
            val startingNode = Node(startReindeer, path = listOf(startReindeer), score = 0)

            val queue = PriorityQueue<Node>(compareBy { it.score })
            queue.add(startingNode)

            val nodesScore = mutableMapOf<Reindeer, Int>()
            val endNodes = mutableSetOf<Node>()

            while (queue.isNotEmpty()) {
                val currentNode = queue.poll()
                val (reindeer, path, score) = currentNode

                if (at(reindeer.position) == END) {
                    endNodes.add(currentNode)
                    continue
                }

                listOf(
                    reindeer.move() to 1,
                    reindeer.turnLeft().move() to 1001,
                    reindeer.turnRight().move() to 1001
                ).filter {
                    (at(it.first.position) == EMPTY || at(it.first.position) == END)
                }.map { (updatedReindeer, movementScore) ->
                    Node(updatedReindeer, path + updatedReindeer, score + movementScore)
                }.filter {
                    it.score <= nodesScore.getOrDefault(it.reindeer, Int.MAX_VALUE)
                }.forEach {
                    nodesScore[it.reindeer] = it.score
                    queue.offer(it)
                }
            }

            val minimumScore = endNodes.minOf { it.score }
            val seats = endNodes.filter { it.score == minimumScore }
                .flatMap { it.path }
                .map { it.position }
                .toSet()

            return minimumScore to seats.size
        }

        override fun toString() = matrix.toString()
    }

    private fun List<String>.processInput(): Maze {
        val maze = Maze(size)
        for (y in indices)
            for (x in this[y].indices) {
                maze.update(Point(x, y), this[y][x])
            }
        return maze
    }

    override fun part1(input: List<String>) = input.processInput().solve().first

    override fun part2(input: List<String>) = input.processInput().solve().second
}

fun main() = Day16().run()
