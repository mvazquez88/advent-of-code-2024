// AoC 2024 - Day 15: Warehouse Woes
// https://adventofcode.com/2024/day/15

private const val ROBOT: Char = '@'
private const val WALL: Char = '#'
private const val EMPTY: Char = '.'

private const val BOX: Char = 'O'
private const val BOX_LEFT: Char = '['
private const val BOX_RIGHT: Char = ']'

private sealed class Item(val location: P2) {

    abstract fun canMove(direction: Facing, matrix: Matrix): Boolean
    open fun move(direction: Facing, matrix: Matrix) {}

    class Empty(location: P2) : Item(location) {
        override fun canMove(direction: Facing, matrix: Matrix) = true
    }

    class Wall(location: P2) : Item(location) {
        override fun canMove(direction: Facing, matrix: Matrix) = false
    }

    class Simple(location: P2) : Item(location) {
        override fun canMove(direction: Facing, matrix: Matrix): Boolean {
            return from(direction.move(location), matrix).canMove(direction, matrix)
        }

        override fun move(direction: Facing, matrix: Matrix) {
            from(direction.move(location), matrix).move(direction, matrix)
            matrix.swap(location, direction.move(location))
        }
    }

    class Double(private val left: P2, private val right: P2) : Item(left) {
        override fun canMove(direction: Facing, matrix: Matrix): Boolean {
            return if (direction.isHorizontal) {
                val coordinate = if (direction == Facing.Right) right else left
                from(direction.move(coordinate), matrix).canMove(direction, matrix)
            } else
                listOf(direction.move(location), direction.move(location).right(1))
                    .map { from(it, matrix) }
                    .all { it.canMove(direction, matrix) }
        }

        override fun move(direction: Facing, matrix: Matrix) {
            if (direction.isHorizontal) {
                val coordinate = if (direction == Facing.Right) right else left
                from(direction.move(coordinate), matrix).move(direction, matrix)

                if (direction == Facing.Right) {
                    matrix.swap(right, right.right(1))
                    matrix.swap(left, left.right(1))
                } else {
                    matrix.swap(left, left.left(1))
                    matrix.swap(right, right.left(1))
                }
            } else {
                val (targetA, targetB) = direction.move(left) to direction.move(right)
                from(targetA, matrix).move(direction, matrix)
                from(targetB, matrix).move(direction, matrix)
                matrix.swap(targetA, left)
                matrix.swap(targetB, right)
            }
        }
    }

    companion object {
        private fun from(char: Char, location: P2): Item = when (char) {
            ROBOT -> Simple(location)
            BOX -> Simple(location)
            BOX_LEFT -> Double(location, location.right(1))
            BOX_RIGHT -> Double(location.left(1), location)
            EMPTY -> Empty(location)
            WALL -> Wall(location.left(1))
            else -> throw IllegalStateException()
        }

        fun from(location: P2, matrix: Matrix): Item = from(matrix.at<Char>(location), location)
    }
}

private class Warehouse(height: Int, width: Int) {
    private val matrix = Matrix(height, width)
    fun update(point: P2, value: Char) = matrix.set(point, value)

    fun tryMove(direction: Facing) {
        val robot = Item.from(matrix.find(ROBOT), matrix)

        if (robot.canMove(direction, matrix))
            robot.move(direction, matrix)
    }

    fun gps(): Int = matrix.indices2d
        .filter { matrix.get(it) == BOX || matrix.get(it) == BOX_LEFT }
        .sumOf { (it.y * 100) + it.x }

    override fun toString() = matrix.toString()
}

class Day15 : Day(dayId = 15, expectedResult = listOf(10092, 1568399, 9021, 1575877)) {

    private fun List<String>.processInput(expand: Boolean = false): Pair<Warehouse, List<Facing>> {
        val splitIndex = indexOf("")
        val map = subList(0, splitIndex).map {
            if (!expand) it
            else it.replace("#", "##")
                .replace(".", "..")
                .replace("O", "[]")
                .replace("@", "@.")
        }

        val warehouse = Warehouse(map.size, map[0].length)

        for (y in map.indices)
            for (x in map[y].indices)
                warehouse.update(P2(x, y), map[y][x])

        val moves = subList(splitIndex, size)
            .joinToString()
            .replace(",", "")
            .trim()
            .mapNotNull { Facing.fromChar(it) }

        return warehouse to moves
    }

    override fun part1(input: List<String>): Number {
        val (warehouse, moves) = input.processInput()
        moves.forEach { warehouse.tryMove(it) }
        return warehouse.gps()
    }

    override fun part2(input: List<String>): Number {
        val (warehouse, moves) = input.processInput(expand = true)
        moves.forEach { warehouse.tryMove(it) }
        return warehouse.gps()
    }
}

fun main() = Day15().run()
