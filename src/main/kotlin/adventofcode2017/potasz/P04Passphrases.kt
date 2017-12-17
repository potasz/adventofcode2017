package adventofcode2017.potasz

object P04Passphrases {


    fun solve1(lines: List<List<String>>): Int = lines.filter {
        it.combinations(2).find { it[0] == it[1] } == null
    }.count()

    fun solve2(lines: List<List<String>>): Int = lines.filter {
        it.combinations(2).map { it.map { it.toList().sorted() } }.find { it[0] == it[1] } == null
    }.count()

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
