package adventofcode2017.potasz

object Puzzle06 {

    val sample = arrayListOf(0, 2, 7, 0)

    val input = arrayListOf(2, 8, 8, 5, 4, 2, 3, 1, 5, 5, 1, 2, 15, 13, 5, 14)

    fun reallocate(input: List<Int>): List<Int> {
        val output = input.toIntArray()
        var (position, units) = output.foldIndexed(Pair(0, 0)) { index, acc, i -> if (i > acc.second) Pair(index, i) else acc }
        output[position] -= units
        for(i in 0 until units) {
            output[++position % output.size] += 1
        }
        return output.toList()
    }

    // https://en.wikipedia.org/wiki/Cycle_detection
    fun <T> floyd(f: (T) -> T, x0: T): Pair<Int, Int> {
        // find first repetition
        var tortoise = f(x0)
        var hare = f(f(x0))
        while (tortoise != hare) {
            tortoise = f(tortoise)
            hare = f(f(hare))
        }

        // find first occurrence of the repeated value (mu)
        var mu = 0
        tortoise = x0
        while (tortoise != hare) {
            tortoise = f(tortoise)
            hare = f(hare)
            mu++
        }

        // find the length after the value is repeated
        var lam = 1
        hare = f(tortoise)
        while (tortoise != hare) {
            hare = f(hare)
            lam++
        }

        return lam to mu
    }

    @JvmStatic
    fun main(args: Array<String>) {
        listOf(sample, input). forEach {
            val (lambda, mu) = floyd(this::reallocate, it)
            println("First occurrence after $mu steps. Repeated after $lambda steps. Found repetition after ${mu + lambda} steps.")
        }
    }
}
