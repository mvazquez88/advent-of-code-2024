import kotlin.math.abs

// AoC 2024 - Day 21: Keypad Conundrum
// https://adventofcode.com/2024/day/21

class Day21 :
    Day(dayId = 21, expectedResult = listOf(126384, 137870, 154115708116294, 170279148659464)) {

    private val numKeypad = Matrix(4, 3, ' ').apply {
        val topLeft = P2(0, 0)
        listOf('7', '8', '9').forEachIndexed { index, c -> set(topLeft.right(index), c) }
        listOf('4', '5', '6').forEachIndexed { index, c -> set(topLeft.down(1).right(index), c) }
        listOf('1', '2', '3').forEachIndexed { index, c -> set(topLeft.down(2).right(index), c) }
        listOf(' ', '0', 'A').forEachIndexed { index, c -> set(topLeft.down(3).right(index), c) }
    }

    private val dirKeypad = Matrix(2, 3, ' ').apply {
        val topLeft = P2(0, 0)
        listOf(' ', '^', 'A').forEachIndexed { index, c -> set(topLeft.right(index), c) }
        listOf('<', 'v', '>').forEachIndexed { index, c -> set(topLeft.down(1).right(index), c) }
    }

    private val invalidPaths = listOf(
        '0' to "<",
        'A' to "<<",
        '1' to "v",
        '4' to "vv",
        '7' to "vvv",
        '<' to "^",
        '^' to "<",
    )

    private fun String.isValidPath(from: Char) = invalidPaths
        .filter { it.first == from }
        .none { startsWith(it.second) }

    private fun getSequences(pad: Matrix, from: Char, to: Char): List<String> {
        val p1 = pad.find(from)
        val p2 = pad.find(to)

        val dx = p2.x - p1.x
        val dy = p2.y - p1.y

        val horizontal = if (dx > 0) ">" else "<"
        val vertical = if (dy > 0) "v" else "^"

        val sequence = horizontal.repeat(abs(dx)) + vertical.repeat(abs(dy))
        return setOf(sequence, sequence.reversed()).filter { it.isValidPath(from) }.map { it + "A" }
    }

    private fun getCodeComplexity(code: String, numKeypads: Int): Long {
        val cache = Array(26) { HashMap<String, Long>() }

        fun getCodeComplexity(code: String, keypad: Int): Long {
            check(code.endsWith("A"))
            if (keypad == numKeypads) {
                return code.length.toLong()
            }
            cache[keypad][code]?.let { return it }
            val pad = if (keypad == 0) numKeypad else dirKeypad
            var len = 0L

            "A$code".zipWithNext { a, b ->
                val sequences = getSequences(pad, a, b)
                val best = sequences.minOf { getCodeComplexity(it, keypad + 1) }
                len += best
            }
            cache[keypad][code] = len
            return len
        }

        return getCodeComplexity(code, 0) * code.dropLast(1).toInt()
    }

    override fun part1(input: List<String>) = input.sumOf { getCodeComplexity(it, 3) }

    override fun part2(input: List<String>) = input.sumOf { getCodeComplexity(it, 26) }
}


fun main() = Day21().run()
