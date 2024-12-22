// AoC 2024 - Day 22: Monkey Market
// https://adventofcode.com/2024/day/22

class Day22 : Day(dayId = 22, expectedResult = listOf(37327623, 20441185092, 24, 2268)) {

    private fun nextPrice(secret: Int): Int {
        var next = secret.toLong()
        next = (next * 64).mix(next).prune()
        next = (next / 32).mix(next).prune()
        next = (next * 2048).mix(next).prune()
        return next.toInt()
    }

    private fun nextPrice(secret: Int, times: Int): Int {
        var next = secret
        repeat(times) { next = nextPrice(next) }
        return next
    }

    private fun Long.mix(value: Long) = xor(value)
    private fun Long.prune() = this % 16777216
    private fun Int.lastDigit() = this % 10

    private fun predictPriceVariations(startingSecret: Int, times: Int): List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()
        var current = startingSecret
        repeat(times) {
            val next = nextPrice(current)
            val (price, variation) = next.lastDigit() to (next.lastDigit() - current.lastDigit())
            result.add(price to variation)
            current = nextPrice(current)
        }
        return result
    }

    data class Sequence(val variations: List<Int>, val price: Int)

    private fun List<Pair<Int, Int>>.getSequencesWithPrice() = windowed(4)
        .map { window ->
            Sequence(window.map { it.second }, window.last().first)
        }.distinctBy {
            it.variations
        }

    override fun part1(input: List<String>): Number {
        val secrets = input.map { it.toInt() }
        return secrets.sumOf { nextPrice(it, 2000).toLong() }
    }

    override fun part2(input: List<String>): Number {
        val secrets = input.map { it.toInt() }
        val cache = hashMapOf<List<Int>, Int>()

        for (secret in secrets) {
            val priceVariations = predictPriceVariations(secret, 2000)
            val sequences = priceVariations.getSequencesWithPrice()
            for ((sequence, price) in sequences)
                cache[sequence] = cache.getOrDefault(sequence, 0) + price
        }

        return cache.values.max()
    }
}

fun main() = Day22().run()
