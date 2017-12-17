package adventofcode2017.potasz

object P17Splinlock {

    fun solve(jump: Int): Int {
        val buffer = mutableListOf(0)
        var pos = 0
        for (i in 1..2017) {
            pos = (pos + jump) % buffer.size + 1
            buffer.add(pos, i)
        }
        return buffer[pos + 1]
    }

    fun solve2(jump: Int): Int {
        var second = 0
        var pos = 0
        for (i in 1..50_000_000) {
            pos = (pos + jump) % i + 1
            if (pos == 1) second = i
        }
        return second
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println(solve(344))
        println(solve2(344))
    }
}
