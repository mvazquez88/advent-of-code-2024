import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

data class P2(val x: Int, val y: Int) {
    fun x(offsetX: Int) = copy(x = x + offsetX)

    fun y(offsetY: Int) = copy(y = y + offsetY)

    fun right(offset: Int) = x(offset)

    fun left(offset: Int) = x(-offset)

    fun up(offset: Int) = y(-offset)

    fun down(offset: Int) = y(offset)

    fun upRight(offset: Int) = up(offset).right(offset)

    fun upLeft(offset: Int) = up(offset).left(offset)

    fun downRight(offset: Int) = down(offset).right(offset)

    fun downLeft(offset: Int) = down(offset).left(offset)

    fun xy(offsetX: Int, offsetY: Int) = x(offsetX).y(offsetY)

    fun upRange(offset: Int) = (0..offset).map { up(it) }

    fun downRange(offset: Int) = (0..offset).map { down(it) }

    fun leftRange(offset: Int) = (0..offset).map { left(it) }

    fun rightRange(offset: Int) = (0..offset).map { right(it) }

    fun upRightRange(offset: Int) = (0..offset).map { up(it).right(it) }

    fun upLeftRange(offset: Int) = (0..offset).map { up(it).left(it) }

    fun downRightRange(offset: Int) = (0..offset).map { down(it).right(it) }

    fun downLeftRange(offset: Int) = (0..offset).map { down(it).left(it) }

    fun move(direction: Facing, by: Int) = when (direction) {
        Facing.Right -> right(by)
        Facing.Down -> down(by)
        Facing.Left -> left(by)
        Facing.Up -> up(by)
    }

    fun neighbors() = listOf(up(1), down(1), left(1), right(1))

    fun neighbors(distance: Int = 1) : List<P2> = buildList {
        for (y in -distance..distance)
            for (x in -distance..distance)
                if (abs(y) + abs(x) <= distance)
                    add(y(y).x(x))
    }

    fun distance(other: P2): Double {
        return sqrt((x - other.x).toDouble().pow(2.0) + (y - other.y).toDouble().pow(2.0))
    }

    fun isValid(matrix: Array<CharArray>) = y in matrix.indices && x in matrix[y].indices
}
