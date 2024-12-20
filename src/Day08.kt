// AoC 2024 - Day 07: Resonant Collinearity
// https://adventofcode.com/2024/day/07

class Day08 : Day(dayId = 8, expectedResult = listOf(14, 276, 34, 991)) {

    private fun Array<CharArray>.extractDistinctAntennas(): List<Char> =
        flatMap { it.toList() }.distinct().minus('.')

    private fun getAntennaPositions(input: Array<CharArray>, antenna: Char): List<P2> =
        input.indices2d.filter { input.at(it) == antenna }

    private fun getAntennaPairs(antennaPositions: List<P2>): List<List<P2>> =
        antennaPositions.combinations(2)

    private fun getDistanceBetweenAntennas(antennaA: P2, antennaB: P2): P2 =
        antennaB.x(-antennaA.x).y(-antennaA.y)

    private fun getAntinodes(
        antennaA: P2,
        antennaB: P2,
        map: Array<CharArray>,
        maxStep: Int = Int.MAX_VALUE,
        includeSource: Boolean = false
    ): List<P2> {
        val (offsetX, offsetY) = getDistanceBetweenAntennas(antennaA, antennaB)
        return buildList {
            var step = if (includeSource) 0 else 1
            do {
                val newNodes = listOf(
                    antennaA.x(-offsetX * step).y(-offsetY * step),
                    antennaB.x(offsetX * step).y(offsetY * step)
                ).filter { it.isValid(map) }

                if (newNodes.isEmpty()) break else addAll(newNodes)
                step++
            } while (step < maxStep)
        }
    }

    private fun List<String>.processInput(): Array<CharArray> =
        map { it.toCharArray() }.toTypedArray()

    override fun part1(input: List<String>): Number {
        val map = input.processInput()
        return map.extractDistinctAntennas()
            .map { getAntennaPositions(map, it) }
            .flatMap { getAntennaPairs(it) }
            .flatMap { getAntinodes(it[0], it[1], map, maxStep = 1) }
            .distinct()
            .size
    }

    override fun part2(input: List<String>): Number {
        val map = input.processInput()
        return map.extractDistinctAntennas()
            .map { getAntennaPositions(map, it) }
            .flatMap { getAntennaPairs(it) }
            .flatMap { getAntinodes(it[0], it[1], map, includeSource = true) }
            .distinct()
            .size
    }
}

fun main() = Day08().run()
