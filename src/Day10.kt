// AoC 2024 - Day 10: Hoof It
// https://adventofcode.com/2024/day/10

class Day10 : Day(dayId = 10, expectedResult = listOf(36, 822, 81, 1801)) {

    private fun List<String>.processInput(): List<List<Int>> =
        map { it.toCharArray().map { height -> height.digitToInt() } }

    private fun findStartingPoints(map: List<List<Int>>): List<Point> = buildList {
        for (y in map.indices)
            for (x in map[y].indices)
                if (map[y][x] == 0) add(Point(x, y))
    }

    private fun calculateTrails(map: List<List<Int>>, start: Point): List<Point> {
        val currentHeight = map[start.y][start.x]
        if (currentHeight == 9) return listOf(start)

        val moves = listOf(
            start.xy(0, -1),// North
            start.xy(1, 0), // East
            start.xy(0, 1), // West
            start.xy(-1, 0) // South
        )

        return moves
            .filter { it.x in map.indices && it.y in map.indices }
            .filter { map[it.y][it.x] == currentHeight + 1 }
            .flatMap { calculateTrails(map, it) }
    }

    override fun part1(input: List<String>): Number = input.processInput().let { map ->
        return findStartingPoints(map).sumOf { calculateTrails(map, it).distinct().count() }
    }

    override fun part2(input: List<String>): Number = input.processInput().let { map ->
        return findStartingPoints(map).sumOf { calculateTrails(map, it).count() }
    }
}

fun main() = Day10().run()
