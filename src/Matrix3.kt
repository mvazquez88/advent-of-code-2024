class Matrix3(val height: Int, val width: Int, val depth: Int, default: Any = '.') {

    constructor(size: Int, default: Any = '.') : this(size, size, size, default)

    private val matrix: Array<Array<Array<Any>>> =
        Array(height) { Array(width) { Array(depth) { default } } }

    fun set(point: P3, value: Any) {
        matrix[point.y][point.x][point.z] = value
    }

    fun get(point: P3): Any = matrix[point.y][point.x][point.z]

    fun isValid(point: P3) = point.x in 0..<width && point.y in 0..<height && point.z in 0..<depth

    fun increment(point: P3) = set(point, at<Int>(point) + 1)
    fun decrement(point: P3) = set(point, at<Int>(point) - 1)

    fun swap(a: P3, b: P3) {
        val (valueA, valueB) = get(a) to get(b)
        set(a, valueB)
        set(b, valueA)
    }

    inline fun <reified T> at(point: P3): T = get(point) as T

    inline fun <reified T> at(x: Int, y: Int, z: Int): T = at<T>(P3(x, y, z))

    inline fun <reified T> find(value: T): P3 {
        for (y in 0..<height) {
            for (x in 0..<width) {
                for (z in 0..<depth) {
                    if (get(P3(x, y, z)) == value)
                        return P3(x, y, z)
                }
            }
        }
        throw IllegalStateException("$value not found")
    }

    /*fun isQ1(point: P2) = point.x < width / 2 && point.y < height / 2
    fun isQ2(point: P2) = point.x > width / 2 && point.y < height / 2
    fun isQ3(point: P2) = point.x < width / 2 && point.y > height / 2
    fun isQ4(point: P2) = point.x > width / 2 && point.y > height / 2*/

    val indices3d: List<P3>
        get() {
            val result = mutableListOf<P3>()
            for (y in matrix.indices)
                for (x in matrix[y].indices)
                    for (z in matrix[y][x].indices)
                        result.add(P3(x, y, z))
            return result
        }

    override fun toString() = buildString {
        for (y in 0..<height) {
            for (x in 0..<width) {
                for (z in 0..<width) {
                    append(matrix[y][x][z])
                }
            }
            appendLine()
        }
    }
}
