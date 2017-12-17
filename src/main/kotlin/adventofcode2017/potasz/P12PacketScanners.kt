package adventofcode2017.potasz

object P12PacketScanners {
    val sample = listOf(0 to 3, 1 to 2, 4 to 4, 6 to 4)

    fun solve1(layers: List<Pair<Int, Int>>): Int =
            layers.filter { isCaught(it.first, it.second) }.sumBy { it.first * it.second}

    fun solve2(layers: List<Pair<Int, Int>>): Int {
        var delay = 0
        while (layers.any { isCaught(it.first + delay, it.second) }) { delay ++ }
        return delay
    }

    private fun isCaught(time: Int, range: Int): Boolean = time % (2 * (range - 1)) == 0

    @JvmStatic
    fun main(args: Array<String>) {
        println(solve1(sample))
        println(solve2(sample))

        val input = readLines("input13.txt").map { it.split(": ") }.map { Pair(it[0].toInt(), it[1].toInt()) }
        println(solve1(input))
        println(solve2(input))
    }
}
