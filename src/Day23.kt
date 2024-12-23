// AoC 2024 - Day 23: LAN Party
// https://adventofcode.com/2024/day/23

class Day23 : Day(
    dayId = 23,
    expectedResult = listOf(7, 1154, "co,de,ka,ta", "aj,ds,gg,id,im,jx,kq,nj,ql,qr,ua,yh,zn")
) {
    data class Link(val computerA: String, val computerB: String) {
        fun contains(computer: String) = computerA == computer || computerB == computer
        fun other(computer: String) = if (computerA == computer) computerB else computerA

        override fun toString() = "$computerA-$computerB"
    }

    private fun groupsByComputer(computers: List<String>, links: List<Link>) = buildMap {
        for (computer in computers) {
            val linkedComputers = links.filter { it.contains(computer) }.map { it.other(computer) }
            put(computer, linkedComputers)
        }
    }

    private fun areConnected(computerA: String, computerB: String): Boolean =
        groups[computerA]?.contains(computerB) ?: false

    private var groups: Map<String, List<String>> = emptyMap()

    private fun List<String>.processInput(): List<Link> =
        map { link -> link.split("-").let { Link(it[0], it[1]) } }

    override fun part1(input: List<String>): Int {
        val links = input.processInput()
        val computers = links.flatMap { listOf(it.computerA, it.computerB) }.distinct()
        groups = groupsByComputer(computers, links)

        val tGroups = groups.filter { it.key.startsWith("t") }

        val groupsOf3 = buildSet {
            for (tGroup in tGroups) {
                for (link in tGroup.value) {
                    val otherComputers = tGroup.value.minus(link)
                    val threeWayLinks = otherComputers.filter { areConnected(link, it) }.map { it }
                    threeWayLinks.forEach { add(listOf(tGroup.key, link, it).sorted()) }
                }
            }
        }

        return groupsOf3.size
    }

    override fun part2(input: List<String>): Any {
        val links = input.processInput()
        val computers = links.flatMap { listOf(it.computerA, it.computerB) }.distinct()
        groups = groupsByComputer(computers, links)

        val largestGroup = groups.map { (computer, links) ->
            buildSet {
                add(computer)
                for (link in links) if (all { areConnected(link, it) }) add(link)
            }
        }.maxBy { it.size }.sorted()

        return largestGroup.joinToString(",")
    }
}

fun main() = Day23().run()
