class Matrix(val height: Int, val width: Int, default: Any = '.') {

    constructor(size: Int, default: Any = '.') : this(size, size, default)

    private val matrix: Array<Array<Any>> = Array(height) { Array(width) { default } }

    fun set(point: Point, value: Any) {
        matrix[point.y][point.x] = value
    }

    fun get(point: Point): Any = matrix[point.y][point.x]

    fun increment(point: Point) = set(point, at<Int>(point) + 1)
    fun decrement(point: Point) = set(point, at<Int>(point) - 1)

    fun swap(a: Point, b: Point) {
        val (valueA, valueB) = get(a) to get(b)
        set(a, valueB)
        set(b, valueA)
    }

    inline fun <reified T> at(point: Point): T = get(point) as T

    inline fun <reified T> at(x: Int, y: Int): T = at<T>(Point(x, y))

    inline fun <reified T> find(value: T): Point {
        for (y in 0..<height) {
            for (x in 0..<width) {
                if (get(Point(x, y)) == value)
                    return Point(x, y)
            }
        }
        throw IllegalStateException("$value not found")
    }

    fun isQ1(point: Point) = point.x < width / 2 && point.y < height / 2
    fun isQ2(point: Point) = point.x > width / 2 && point.y < height / 2
    fun isQ3(point: Point) = point.x < width / 2 && point.y > height / 2
    fun isQ4(point: Point) = point.x > width / 2 && point.y > height / 2

    val indices2d: List<Point>
        get() {
            val result = mutableListOf<Point>()
            for (y in matrix.indices)
                for (x in matrix[y].indices)
                    result.add(Point(x, y))
            return result
        }

    override fun toString() = buildString {
        for (y in 0..<height) {
            for (x in 0..<width) {
                append(matrix[y][x])
            }
            appendLine()
        }
    }
}
