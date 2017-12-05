package adventofcode2017.potasz

object Puzzle05 {

    val input: () -> IntArray = {
        javaClass.getResourceAsStream("/input05.txt")
                .bufferedReader()
                .lines()
                .mapToInt { it.toInt() }
                .toArray()
    }

    val sample: () -> IntArray = { listOf("0", "3", "0", "1", "-3").stream().mapToInt { it.toInt() }.toArray() }

    private fun doSolve(input: IntArray, rule: (Int) -> Int): Int {
        var pointer = 0
        var counter = 0
        while (pointer < input.size) {
            val jump = input[pointer]
            input[pointer] = rule(jump)
            pointer += jump
            counter++
        }
        return counter
    }

    fun solve1(input: IntArray) = doSolve(input, { it + 1 })

    fun solve2(input: IntArray) = doSolve(input, { if (it >= 3) it - 1 else it + 1 })

    @JvmStatic
    fun main(args: Array<String>) {

        println(solve1(sample()))
        println(solve1(input()))

        println(solve2(sample()))
        println(solve2(input()))
    }
}
