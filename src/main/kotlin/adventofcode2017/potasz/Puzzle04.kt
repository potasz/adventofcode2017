package adventofcode2017.potasz

object Puzzle04 {


    fun solve1(lines: List<String>): Int = lines.filter {
        val words: List<String> = it.split("\\s+".toRegex())
        words.size == words.toSet().size
    }.count()

    fun solve2(lines: List<String>): Int = lines.filter {
        val words: List<String> = it.split("\\s+".toRegex()).map { it.toList().sorted().joinToString(separator = "") }
        words.size == words.toSet().size
    }.count()

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = javaClass.getResourceAsStream("/input04.txt").bufferedReader().readLines()

        println(solve1(lines))

        println(solve2(lines))
    }

}
