class Matrix(val height: Int, val width: Int, default: Any = '.') {

    constructor(size: Int, default: Any = '.') : this(size, size, default)

    private val matrix: Array<Array<Any>> = Array(height) { Array(width) { default } }

    fun set(point: P2, value: Any) {
        matrix[point.y][point.x] = value
    }

    fun get(point: P2): Any = matrix[point.y][point.x]

    fun isValid(point: P2) = point.x in 0..<width && point.y in 0..<height

    fun increment(point: P2) = set(point, at<Int>(point) + 1)
    fun decrement(point: P2) = set(point, at<Int>(point) - 1)

    fun swap(a: P2, b: P2) {
        val (valueA, valueB) = get(a) to get(b)
        set(a, valueB)
        set(b, valueA)
    }

    inline fun <reified T> at(point: P2): T = get(point) as T

    inline fun <reified T> at(x: Int, y: Int): T = at<T>(P2(x, y))

    inline fun <reified T> find(value: T): P2 {
        for (y in 0..<height) {
            for (x in 0..<width) {
                if (get(P2(x, y)) == value)
                    return P2(x, y)
            }
        }
        throw IllegalStateException("$value not found")
    }

    fun isQ1(point: P2) = point.x < width / 2 && point.y < height / 2
    fun isQ2(point: P2) = point.x > width / 2 && point.y < height / 2
    fun isQ3(point: P2) = point.x < width / 2 && point.y > height / 2
    fun isQ4(point: P2) = point.x > width / 2 && point.y > height / 2

    val indices2d: List<P2>
        get() {
            val result = mutableListOf<P2>()
            for (y in matrix.indices)
                for (x in matrix[y].indices)
                    result.add(P2(x, y))
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
