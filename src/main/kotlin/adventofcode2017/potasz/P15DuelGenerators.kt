package adventofcode2017.potasz

import kotlin.coroutines.experimental.buildIterator

object P15DuelGenerators {

    val A = 16807L
    val B = 48271L
    val DIV = 2147483647L
    val MASK_LOW_16 = 0xFFFFL
    val MASK_MOD_4 = 0x0003L
    val MASK_MOD_8 = 0x0007L

    fun f(x: Long, mul: Long): Long = (x * mul) % DIV

    fun fA(x: Long) = f(x, A)
    fun fB(x: Long) = f(x, B)

    fun solve1Loop(initA: Long, initB: Long): Int {
        var a = initA
        var b = initB
        var count = 0
        repeat (40_000_000) {
            a = fA(a)
            b = fB(b)
            if (a and MASK_LOW_16 == b and MASK_LOW_16) count++
        }
        return count
    }

    fun solve2Loop(initA: Long, initB: Long): Int {
        var a = initA
        var b = initB
        var count = 0
        repeat (5_000_000) {
            if (a and MASK_LOW_16 == b and MASK_LOW_16) count++
            do {
                a = fA(a)
            } while (a and MASK_MOD_4 != 0L)
            do {
                b = fB(b)
            } while (b and MASK_MOD_8 != 0L)
        }
        return count
    }

    fun iterator(init: Long, f: (Long) -> Long, modMask: Long = 0L) = buildIterator {
        var x = init
        yield(x)
        while (true) {
            do {
                x = f(x)
            } while (x and modMask != 0L)
            yield(x)
        }
    }

    fun compare(iterA: Iterator<Long>, iterB: Iterator<Long>, repeat: Int): Int {
        var count = 0
        repeat(repeat) {
            if (iterA.next() and MASK_LOW_16 == iterB.next() and MASK_LOW_16) count++
        }

        return count
    }

    fun solve1Iter(initA: Long, initB: Long) =
            compare(iterator(initA, this::fA), iterator(initB, this::fB), 40_000_000)

    fun solve2Iter(initA: Long, initB: Long) =
            compare(iterator(initA, this::fA, MASK_MOD_4), iterator(initB, this::fB, MASK_MOD_8), 5_000_000)

    @JvmStatic
    fun main(args: Array<String>) {
        println(solve1Loop(1092455L, 430625591L))
        println(solve2Loop(1092455L, 430625591L))
        println(solve1Loop(516L, 190L))
        println(solve2Loop(516L, 190L))
    }
}
