// AoC 2024 - Day 05: Print Queue
// https://adventofcode.com/2024/day/05

private const val BLOCKER = '#'
private const val VISITED = 'X'
private const val NOT_VISITED = '.'

enum class GuardType(val id: Char, val move: (Point) -> Point) {
    LookingUp('^', { it.up(1) }),
    LookingRight('>', { it.right(1) }),
    LookingDown('v', { it.down(1) }),
    LookingLeft('<', { it.left(1) });

    fun next(): GuardType = entries[(ordinal + 1) % entries.size]

    companion object {
        fun fromChar(char: Char): GuardType? = entries.firstOrNull { it.id == char }
    }
}

data class Guard(
    val type: GuardType,
    val position: Point,
) {
    fun rotateRight() = copy(type = type.next())
    fun move() = copy(position = type.move(position))
}

private fun Char.toGuard() = GuardType.fromChar(this)

class Day06 : Day(dayId = 6, expectedResult = listOf(41, 5551, 6, 1939)) {

    private fun generatePossibleObstaclePositions(matrix: Array<CharArray>) =
        matrix.indices2d.filter { matrix.at(it) == '.' }

    private fun List<String>.processInput(): Array<CharArray> =
        map { it.toCharArray() }.toTypedArray()

    private fun findGuard(map: Array<CharArray>): Guard = map.indices2d
        .firstNotNullOf { position ->
            map.at(position).toGuard()?.let { guard -> guard to position }
        }
        .let { Guard(it.first, it.second) }

    private fun Array<CharArray>.cloneWithObstacle(obstacle: Point) = map { it.clone() }
        .toTypedArray()
        .apply { this[obstacle.y][obstacle.x] = BLOCKER }

    private fun patrolWithObstacles(
        input: Array<CharArray>,
        guard: Guard,
        obstacles: List<Point>
    ) = obstacles
        .map { input.cloneWithObstacle(it) }
        .parallelStream()
        .filter { isTheGuardLooping(it, guard) }
        .count()
        .toInt()

    private fun calculatePatrolRouteLength(map: Array<CharArray>, startingGuard: Guard): Int {
        var guard = startingGuard
        map[guard.position.y][guard.position.x] = VISITED

        var count = 1
        var nextMove = startingGuard.move().position

        while (nextMove.isValid(map)) {
            when (map.at(nextMove)) {
                BLOCKER -> guard = guard.rotateRight()
                VISITED -> guard = guard.move()
                NOT_VISITED -> guard = guard.move()
                    .also { map[nextMove.y][nextMove.x] = VISITED }
                    .also { count++ }
            }

            nextMove = guard.move().position
        }

        return count
    }

    private fun isTheGuardLooping(map: Array<CharArray>, startingGuard: Guard): Boolean {
        var guard = startingGuard
        map[guard.position.y][guard.position.x] = VISITED

        var count = 1
        var repeatedMoves = 0
        var nextMove = startingGuard.move().position

        while (nextMove.isValid(map)) {
            when (map.at(nextMove)) {
                BLOCKER -> guard = guard.rotateRight()
                VISITED -> guard = guard.move().also { repeatedMoves++ }
                NOT_VISITED -> guard = guard.move()
                    .also { map[nextMove.y][nextMove.x] = VISITED }
                    .also { count++ }
                    .also { repeatedMoves = 0 }
            }

            nextMove = guard.move().position

            if (repeatedMoves >= count)
                return true // Loop found
        }

        return false
    }

    override fun part1(input: List<String>): Number {
        val map = input.processInput()
        return calculatePatrolRouteLength(map, findGuard(map))
    }

    override fun part2(input: List<String>): Number {
        val map = input.processInput()
        val obstacles = generatePossibleObstaclePositions(map)
        return patrolWithObstacles(map, findGuard(map), obstacles)
    }
}

fun main() = Day06().run()
