package adventofcode2017.potasz

fun <T> List<T>.combinations(size: Int): Sequence<List<T>> {
    if (this.size <= size) return listOf(this).asSequence()
    if (size == 1) return this.map { listOf(it) }.asSequence()
    return (1 .. this.size - size + 1).asSequence().map { i ->
        this.drop(i).combinations(size - 1).map { listOf(this[i - 1]).plus(it) }
    }.flatMap { it }
}
