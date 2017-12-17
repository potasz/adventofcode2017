package adventofcode2017.potasz

import kotlin.math.abs
import kotlin.math.max

object P11HexEd {

    data class Point(val x: Int, val y: Int) {
        operator fun plus(move: Move): Point = Point(x + move.x, y + move.y)
    }

    enum class Move(val x: Int, val y: Int) {
        N(-1, 1),
        NE(0,1),
        SE(1,0),
        S(1,-1),
        SW(0,-1),
        NW(-1,0)
    }

    fun findCoordinateWithMax(list: List<String>): Pair<Point,Int> =
            list.fold(Pair(Point(0,0), 0)) { pWithMax, move ->
                val nextPoint = pWithMax.first + Move.valueOf(move.toUpperCase())
                val maxDistance = max(distance(nextPoint), pWithMax.second)
                Pair(nextPoint, maxDistance)
            }

    private fun distance(p: Point): Int = max(abs(p.x), abs(p.y))

    @JvmStatic
    fun main(args: Array<String>) {
        val input = readLines("input11.txt")[0].split(",")
        val res = findCoordinateWithMax(input)
        println("${res.first} -> Distance: ${distance(res.first)}, Max distance: ${res.second}")
    }
}
