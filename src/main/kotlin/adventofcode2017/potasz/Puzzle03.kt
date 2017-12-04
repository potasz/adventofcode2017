package adventofcode2017.potasz

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

// http://adventofcode.com/2017/day/3
object Puzzle03 {

    data class Move(val x: Int, val y: Int)

    data class Position(val x: Int, val y: Int) {
        operator fun plus(move: Move) = Position(x + move.x, y + move.y)

        fun distanceFromCenter() = abs(x) + abs(y)

        fun distanceFrom(other: Position) = ((x - other.x).square() + (y - other.y).square())

        fun isWithin(topRight: Position, bottomLeft: Position): Boolean =
                x >= topRight.x && x <= bottomLeft.x && y <= topRight.y && y >= bottomLeft.y

        private fun Int.square() = this * this
    }

    data class Square(val value: Long, val position: Position)

    val moves = arrayOf(Move(1, 0), Move(0, 1), Move(-1, 0), Move(0, -1))

    val input = 289326

    private fun findSquare(v: (Square, Position) -> Long, stopAt: (Square) -> Boolean): Square {
        var moveIndex = 0
        var square = Square(1, Position(0, 0))
        var topRight = square.position
        var bottomLeft = square.position
        while (true) {
            val move = moves[moveIndex++ % moves.size]
            while (square.position.isWithin(topRight, bottomLeft)) {
                val newPosition = square.position + move
                val newValue = v(square, newPosition)
                square = Square(newValue, newPosition)
                if (stopAt(square)) return square
            }
            topRight = Position(min(topRight.x, square.position.x), max(topRight.y, square.position.y))
            bottomLeft = Position(max(bottomLeft.x, square.position.x), min(bottomLeft.y, square.position.y))
        }
    }

    fun solve1(input: Int): Int =
            findSquare({ square, _ ->  square.value + 1 }, { it.value >= input}).position.distanceFromCenter()

    fun solve2(input: Int): Long {
        val squares = mutableListOf<Square>()
        return findSquare({ square, newPosition ->
            squares.add(square)
            squares.filter { it.position.distanceFrom(newPosition) <= 2 }.fold(0L) { acc, s -> acc + s.value }
        }, { it.value > input}).value
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println(listOf(1, 12, 23, 1024, input).map { solve1(it) })
        println(listOf(1, 12, 23, 1024, input).map { solve2(it) })
    }
}
