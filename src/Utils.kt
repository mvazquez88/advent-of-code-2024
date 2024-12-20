import java.math.BigInteger
import java.security.MessageDigest
import java.util.Collections
import kotlin.io.path.Path
import kotlin.io.path.readText


/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("input/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun String.between(before: String, after: String): String =
    substringAfter(before).substringBefore(after)

fun Iterable<Int>.isAllDecreasing(): Boolean = asSequence().zipWithNext { a, b -> a > b }.all { it }

fun Iterable<Int>.isAllIncreasing(): Boolean = asSequence().zipWithNext { a, b -> a < b }.all { it }

fun <T> Iterable<T>.minusIndex(indexToRemove: Int) =
    filterIndexedTo(ArrayList()) { index, _ -> index != indexToRemove }

fun Array<CharArray>.at(point: P2): Char = this[point.y][point.x]

val Array<CharArray>.indices2d: List<P2>
    get() {
        val result = mutableListOf<P2>()
        for (y in indices)
            for (x in this@indices2d[y].indices)
                result.add(P2(x, y))
        return result
    }

fun Int.toPaddedBinary(padding: Int): String = toPaddedString(padding, 2)

fun Int.toPaddedString(padding: Int, radix: Int): String =
    toUInt().toString(radix).padStart(padding, '0')

fun <T> List<T>.permutations(): List<List<T>> {
    fun permutationsRecursive(input: List<T>, index: Int, answers: MutableList<List<T>>) {
        if (index == input.lastIndex) answers.add(input.toList())
        for (i in index..input.lastIndex) {
            Collections.swap(input, index, i)
            permutationsRecursive(input, index + 1, answers)
            Collections.swap(input, i, index)
        }
    }

    val solutions = mutableListOf<List<T>>()
    permutationsRecursive(this, 0, solutions)
    return solutions
}

fun <T> List<T>.combinations(chunked: Int): List<List<T>> {
    val result = mutableListOf<List<T>>()

    fun combinations(arr: List<T>, len: Int, startPosition: Int, partialResult: MutableList<T>) {
        if (len == 0) {
            result.add(partialResult.toList())
            return
        }
        for (i in startPosition..(arr.size - len)) {
            partialResult[partialResult.size - len] = arr[i]
            combinations(arr, len - 1, i + 1, partialResult)
        }
    }

    combinations(this, chunked, 0, MutableList(chunked) { this.first() })
    return result
}

inline fun <T> List<T>.indicesOf(predicate: (T) -> Boolean) = mapIndexedNotNull { index, i ->
    if (predicate(i)) index else null
}
