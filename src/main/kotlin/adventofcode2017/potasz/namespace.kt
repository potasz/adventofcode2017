package adventofcode2017.potasz

fun <T> List<T>.combinations(size: Int): Sequence<List<T>> {
    if (this.size <= size) return listOf(this).asSequence()
    if (size == 1) return this.asSequence().map { listOf(it) }
    return (1 .. this.size - size + 1).asSequence().map { i ->
        this.drop(i).combinations(size - 1).map { listOf(this[i - 1]).plus(it) }
    }.flatMap { it }
}

fun readLines(name: String) = object {}.javaClass.getResourceAsStream("/$name").bufferedReader().readLines()

operator fun String.times(i: Int): String {
    val sb = StringBuffer()
    (0 until i).fold(sb) { acc, _ -> acc.append(this) }
    return sb.toString()
}
