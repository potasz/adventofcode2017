package adventofcode2017.potasz

import kotlin.math.abs

object P20Particles {

    data class Coord(var x: Long, var y: Long, var z: Long) {
        fun update(other: Coord) {
            x += other.x
            y += other.y
            z += other.z
        }
    }

    data class Particle(val id: Int, val p: Coord, val v: Coord, val a: Coord) {
        fun update() {
            v.update(a)
            p.update(v)
        }

        fun distance(): Long = abs(p.x) + abs(p.y) + abs(p.z)
    }

    val pattern = """\w=<([-\d]+),([-\d]+),([-\d]+)>.*""".toRegex()

    private fun parseInput(sample: List<String>): List<Particle> {
        return sample.mapIndexed { index, line ->
            val (p, v, a) = line.split(", ")
                    .map { pattern.matchEntire(it) }
                    .filterNotNull()
                    .map { Coord(it.groupValues[1].toLong(), it.groupValues[2].toLong(), it.groupValues[3].toLong()) }
            Particle(index, p, v, a)
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val input = parseInput(readLines("input20.txt"))
        repeat(1000) {
            input.parallelStream().forEach { it.update() }
        }
        println(input.minBy { it.distance() })

        var input2 = parseInput(readLines("input20.txt"))
        repeat(1000) {
            input2.parallelStream().forEach { it.update() }
            input2 = input2.groupBy { it.p }.filter { it.value.size == 1 }.map { it.value[0] }
        }
        println(input2.size)
    }

}
