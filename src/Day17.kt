import kotlin.math.pow

// AoC 2024 - Day 17: Chronospatial Computer
// https://adventofcode.com/2024/day/17

class Memory(var a: Long, var b: Long, var c: Long) {
    override fun toString() = "A=$a, B=$b, C=$c"
}

class Computer(val program: List<Int>, val memory: Memory) {

    var currentIndex = 0
    val output = mutableListOf<Int>()

    fun restart(memoryA: Long) {
        currentIndex = 0
        output.clear()
        memory.a = memoryA
        memory.b = 0
        memory.c = 0
    }

    fun run(): List<Int> {
        while (currentIndex < program.size) {
            val opCode = program[currentIndex]
            val operand = program[currentIndex + 1]

            val command = Command(opCode, operand.toLong()).parse()
            command.run()

            if (command !is Command.Jnz || memory.a == 0L)
                currentIndex += 2
        }

        return output
    }

    open inner class Command(val opCode: Int, val operand: Long) {
        open fun run() {}

        fun parse(): Command = when (opCode) {
            0 -> Adv(operand)
            1 -> Bxl(operand)
            2 -> Bst(operand)
            3 -> Jnz(operand)
            4 -> Bxc(operand)
            5 -> Out(operand)
            6 -> Bdv(operand)
            7 -> Cdv(operand)
            else -> throw IllegalStateException()
        }

        fun combo(operand: Long): Long = when (operand) {
            0L -> 0
            1L -> 1
            2L -> 2
            3L -> 3
            4L -> memory.a
            5L -> memory.b
            6L -> memory.c
            else -> throw IllegalStateException()
        }

        inner class Adv(operand: Long) : Command(opCode = 0, operand) {
            override fun run() {
                val numerator = memory.a
                val denominator = 2.toDouble().pow(combo(operand).toDouble())
                memory.a = (numerator / denominator).toLong()
            }
        }

        inner class Bxl(operand: Long) : Command(opCode = 1, operand) {
            override fun run() {
                memory.b = memory.b.xor(operand)
            }
        }

        inner class Bst(operand: Long) : Command(opCode = 2, operand) {
            override fun run() {
                memory.b = combo(operand) % 8
            }
        }

        inner class Jnz(operand: Long) : Command(opCode = 3, operand) {
            override fun run() {
                if (memory.a == 0L) return
                currentIndex = operand.toInt()
            }
        }

        inner class Bxc(operand: Long) : Command(opCode = 4, operand) {
            override fun run() {
                memory.b = memory.b.xor(memory.c)
            }
        }

        inner class Out(operand: Long) : Command(opCode = 5, operand) {
            override fun run() {
                output.add((combo(operand) % 8).toInt())
            }
        }

        inner class Bdv(operand: Long) : Command(opCode = 6, operand) {
            override fun run() {
                val numerator = memory.a
                val denominator = 2.toDouble().pow(combo(operand).toDouble())
                memory.b = (numerator / denominator).toLong()
            }
        }

        inner class Cdv(operand: Long) : Command(opCode = 7, operand) {
            override fun run() {
                val numerator = memory.a
                val denominator = 2.toDouble().pow(combo(operand).toDouble())
                memory.c = (numerator / denominator).toLong()
            }
        }
    }
}


class Day17 :
    Day(dayId = 17, expectedResult = listOf(4635635210, 513437217, -1, 216584205979245L)) {

    private fun List<String>.processInput(): Computer {
        val memory = Memory(
            a = get(0).substringAfter("Register A: ").toLong(),
            b = get(1).substringAfter("Register B: ").toLong(),
            c = get(2).substringAfter("Register C: ").toLong()
        )
        val program = get(4).substringAfter("Program: ").split(",").map { it.toInt() }
        return Computer(program, memory)
    }

    override fun part1(input: List<String>): Number {
        val computer = input.processInput()
        computer.run()
        return computer.output.reversed()
            .mapIndexed { index, i -> i * 10.0.pow(index).toLong() }
            .sum()
    }

    override fun part2(input: List<String>): Number {
        val computer = input.processInput()
        val expectedOutput = computer.program
        val expectedOutputSize = computer.program.size

        var memoryA = 0L
        var partialOutputSize = 1

        while (partialOutputSize <= expectedOutputSize) {
            computer.restart(memoryA)

            val output = computer.run()
            if (output == expectedOutput) break

            val (from, to) = expectedOutputSize - partialOutputSize to expectedOutputSize
            val expectedPartialOutput = expectedOutput.subList(from, to)

            if (output == expectedPartialOutput) {
                memoryA *= 8L
                partialOutputSize++
            } else {
                memoryA++
            }
        }

        return memoryA
    }
}

fun main() = Day17().run()
