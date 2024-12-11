import java.util.Collections

// AoC 2024 - Day 9: Disk Fragmenter
// https://adventofcode.com/2024/day/9

private const val FREE: Int = -1

class Day09 : Day(dayId = 9, expectedResult = listOf(1928, 6200294120911, 2858, 6227018762750)) {

    private fun List<String>.processInput(): String =
        first().let { if (it.length % 2 != 0) "${it}0" else it }

    private fun toSpacedDiskMap(input: String): List<Int> = buildList {
        input.chunked(2)
            .map { it[0] to it[1] }
            .forEachIndexed { index, (fileSize, freeSpace) ->
                repeat(fileSize.digitToInt()) { add(index) }
                repeat(freeSpace.digitToInt()) { add(FREE) }
            }
    }

    private fun compactDiskMap(diskMap: List<Int>): List<Int> {
        val fileBlocks = ArrayDeque(diskMap.indicesOf { it != FREE })
        val freeBlocks = ArrayDeque(diskMap.indicesOf { it == FREE })

        val result = diskMap.toMutableList()
        while (freeBlocks.first() < fileBlocks.last()) {
            Collections.swap(result, fileBlocks.removeLast(), freeBlocks.removeFirst())
        }
        return result
    }

    private fun List<Int>.indexOfNextEmptyBlock(size: Int, maxIndex: Int): Int {
        val firstFreeSpace = indexOf(FREE)
        if (firstFreeSpace > maxIndex) return -1

        return subList(firstFreeSpace, maxIndex)
            .windowed(size).indexOfFirst { chunk -> chunk.all { it == FREE } }
            .let { if (it == -1) -1 else it + firstFreeSpace }
    }

    private fun compactDiskMap2(diskMap: List<Int>): List<Int> {
        val partialResult = diskMap.toMutableList()
        val fileIds = diskMap.filter { it != -1 }.distinct().reversed()

        for (fileId in fileIds) {
            val fileIndex = partialResult.indexOf(fileId)
            val fileSize = diskMap.count { it == fileId }

            val nextEmptyBlockIndex = partialResult.indexOfNextEmptyBlock(fileSize, fileIndex)
            if (nextEmptyBlockIndex == -1) continue

            for (i in 0..<fileSize) {
                partialResult[fileIndex + i] = FREE
                partialResult[nextEmptyBlockIndex + i] = fileId
            }
        }

        return partialResult
    }

    private fun calculateChecksum(input: List<Int>) = input
        .mapIndexed { index, value -> if (value != FREE) value.toLong() * index else 0L }
        .sum()

    override fun part1(input: List<String>): Number {
        val disk = input.processInput()
        val spacedDiskMap = toSpacedDiskMap(disk)
        val compactDiskMap = compactDiskMap(spacedDiskMap)
        return calculateChecksum(compactDiskMap)
    }

    override fun part2(input: List<String>): Number {
        val disk = input.processInput()
        val spacedDiskMap = toSpacedDiskMap(disk)
        val compactDiskMap = compactDiskMap2(spacedDiskMap)
        return calculateChecksum(compactDiskMap)
    }
}

fun main() = Day09().run()
