package adventofcode2017.potasz

import kotlin.system.measureTimeMillis

sealed class Move
data class Spin(val num: Int) : Move()
data class Exchange(val posA: Int, val posB: Int) : Move()
data class Partner(val nameA: Char, val nameB: Char): Move()

object Puzzle16 {

    fun solve1(input: String, moves: List<Move>, repeat: Int = 1): String {
        val dancers = input.toCharArray()
        repeat(repeat) {
            moves.forEach { move ->
                when (move) {
                    is Spin -> dancers.shift(move.num)
                    is Exchange -> dancers.swap(move.posA, move.posB)
                    is Partner -> dancers.swap(dancers.indexOf(move.nameA), dancers.indexOf(move.nameB))
                }
            }
        }
        return dancers.str()
    }

    fun CharArray.swap(x: Int, y: Int) {
        this[x] = this[y].also { this[y] = this[x] }
    }

    fun CharArray.shift(num: Int) {
        val n = num % this.size
        if (n == 0) return
        val buffer = copyOf()
        System.arraycopy(buffer, size - n, this, 0, n)
        System.arraycopy(buffer, 0, this, n, size - n)
    }

    fun CharArray.str(): String = this.joinToString("") { it.toString() }

    private fun String.toMove(): Move = when(this.first()) {
        's' -> Spin(this.drop(1).toInt())
        'x' -> {
            val parts = this.drop(1).split("/")
            Exchange(parts[0].toInt(), parts[1].toInt())
        }
        'p' -> Partner(this[1], this[3])
        else -> throw IllegalArgumentException("Unknown this: $this")
    }

    val dancers = CharArray(16) { (it + 'a'.toInt()).toChar() }.str()
    val moves = readLines("input16.txt")[0].split(",").map { it.toMove() }

    @JvmStatic
    fun main(args: Array<String>) {
        val time = measureTimeMillis {
            // Part1
            println(solve1(dancers, moves))
            // Part2
            val floyd = Puzzle06.floyd({x -> solve1(x, moves)}, dancers)
            val remainder = 1000_000_000 % floyd.first
            println(solve1(dancers, moves, repeat = remainder))
        }
        println(time)
    }
}
