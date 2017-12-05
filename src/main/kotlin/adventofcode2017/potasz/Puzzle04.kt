package adventofcode2017.potasz

object Puzzle04 {


    fun solve1(lines: List<List<String>>): Int = lines.filter { it.size == it.toSet().size }.count()

    fun solve2(lines: List<List<String>>): Int = lines
            .map { it.map { it.toList().sorted() } }
            .filter { it.size == it.toSet().size }
            .count()

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = javaClass.getResourceAsStream("/input04.txt")
                .bufferedReader()
                .readLines()
                .map { it.split("\\s+".toRegex()) }

        println(solve1(lines))

        println(solve2(lines))
    }

}
