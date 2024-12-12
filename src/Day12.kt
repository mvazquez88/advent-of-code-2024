// AoC 2024 - Day 12: Garden Groups
// https://adventofcode.com/2024/day/12

enum class Direction(
    val offset: (Point) -> Point,
    val next: (Point) -> Point,
    val previous: (Point) -> Point,
) {
    North({ it.up(1) }, { it.right(1) }, { it.left(1) }),
    South({ it.down(1) }, { it.left(1) }, { it.right(1) }),
    East({ it.right(1) }, { it.up(1) }, { it.down(1) }),
    West({ it.left(1) }, { it.down(1) }, { it.up(1) }),
}

data class Wall(val facing: Direction, val location: Point) {
    fun next(): Wall = copy(location = facing.next(location))
    fun previous(): Wall = copy(location = facing.previous(location))
}

data class Plot(val plantType: Char, val location: Point) {
    private fun neighbor(direction: Direction, map: Array<CharArray>): Plot? =
        direction.offset(location).let {
            return if (it.isValid(map)) Plot(map.at(it), it) else null
        }

    private fun neighbors(map: Array<CharArray>): List<Plot> =
        Direction.entries.mapNotNull { neighbor(it, map) }

    fun neighborsSameType(map: Array<CharArray>): List<Plot> =
        neighbors(map).filter { it.plantType == plantType }

    fun perimeter(map: Array<CharArray>) = Direction.entries.size - neighborsSameType(map).size

    fun explode(map: Array<CharArray>): List<Wall> = Direction.entries
        .map { Wall(it, it.offset(location)) }
        .filter { !it.location.isValid(map) || map.at(it.location) != plantType }
}

data class Region(val plots: Set<Plot>) {
    val area: Int = plots.size

    fun perimeter(map: Array<CharArray>) = plots.sumOf { it.perimeter(map) }

    fun sides(map: Array<CharArray>): Int {
        val fenceCoordinates = plots.flatMap { it.explode(map) }

        val nonConsecutiveCoordinates = mutableListOf<Wall>()
        val remainingCoordinates = fenceCoordinates.toMutableList()

        while (remainingCoordinates.isNotEmpty()) {
            val coordinate = remainingCoordinates.first()
            nonConsecutiveCoordinates.add(coordinate)
            remainingCoordinates.removeConsecutives(coordinate)
        }

        return nonConsecutiveCoordinates.size
    }

    private fun MutableList<Wall>.removeConsecutives(target: Wall) {
        remove(target)
        var (previous, next) = target.previous() to target.next()
        while (contains(previous)) remove(previous).also { previous = previous.previous() }
        while (contains(next)) remove(next).also { next = next.next() }
    }
}

class Day12 : Day(dayId = 12, expectedResult = listOf(1930, 1486324, 1206, 898684)) {
    private fun List<String>.processInput(): Array<CharArray> =
        map { it.toCharArray() }.toTypedArray()

    private fun Array<CharArray>.plots(): Set<Plot> = indices2d.map { Plot(at(it), it) }.toSet()

    private fun getRegions(map: Array<CharArray>, plots: Set<Plot>): List<Region> {
        val plottedLand = mutableSetOf<Plot>()
        val regions = mutableListOf<Region>()

        do {
            val unplottedLand = plots.minus(plottedLand)
            val groupedPlots = getRegion(unplottedLand.first(), map)
            plottedLand.addAll(groupedPlots)
            regions.add(Region(groupedPlots))

        } while (plottedLand.size != plots.size)

        return regions
    }

    private fun getRegion(plot: Plot, map: Array<CharArray>): Set<Plot> {
        val accumulated = mutableSetOf(plot)

        fun getArea(plot: Plot) {
            accumulated.add(plot)

            val neighborsSameType = plot.neighborsSameType(map).minus(accumulated)
            if (neighborsSameType.isEmpty()) return

            neighborsSameType.forEach { getArea(it) }
        }

        getArea(plot)
        return accumulated
    }

    override fun part1(input: List<String>): Number {
        val map = input.processInput()
        val plots = map.plots()

        return getRegions(map, plots).sumOf { it.area * it.perimeter(map) }
    }

    override fun part2(input: List<String>): Number {
        val map = input.processInput()
        val plots = map.plots()

        return getRegions(map, plots).sumOf { it.area * it.sides(map) }
    }
}

fun main() = Day12().run()
