package adventofcode2017.potasz

import java.util.stream.Collectors
import java.util.stream.IntStream

object P14DiskDefrag {

    fun String.toBinary(): BooleanArray = this
            .map { it.toString().toInt(16) }
            .flatMap { num -> listOf(8, 4, 2, 1).map { num and it == it } }
            .toBooleanArray()

    fun bitMatrix(input: String): Array<BooleanArray> = IntStream.range(0, 128)
            .parallel()
            .mapToObj { "$input-$it" }
            .map { P10KnotHash.knotHash(it).toBinary() }
            .collect(Collectors.toList())
            .toTypedArray()

    fun solve1(input: String): Int = bitMatrix(input).sumBy { it.count { it } }

    fun solve2(input: String): Int {
        val bitMatrix = bitMatrix(input)
        return findGroups(bitMatrix)
    }

    fun findGroups(bitMatrix: Array<BooleanArray>): Int {
        var nextGroup = 1
        val groups = Array(bitMatrix.size, { IntArray(bitMatrix.size) })
        for (i in 0 until bitMatrix.size) {
            for (j in 0 until bitMatrix[i].size) {
                if (bitMatrix[i][j] && groups[i][j] == 0) {
                    groups[i][j] = nextGroup
                    findAdjacent(i,j, bitMatrix, groups, nextGroup++)
                }
            }
        }

        return nextGroup - 1
    }

    private fun findAdjacent(i: Int, j: Int, bitMatrix: Array<BooleanArray>, groups: Array<IntArray>, groupLabel: Int) {
        withNeighboursOf(i, j, bitMatrix.size) { k, l ->
            if (groups[k][l] != groupLabel && bitMatrix[k][l]) {
                groups[k][l] = groupLabel
                findAdjacent(k,l, bitMatrix, groups, groupLabel)
            }
        }
    }

    private fun withNeighboursOf(i: Int, j: Int, size: Int, block: (Int, Int) -> Unit) {
        listOf(Pair(i - 1,j), Pair(i, j - 1), Pair(i + 1, j), Pair(i, j + 1))
                .filter { it.first in 0..(size - 1) && it.second in 0..(size - 1) }
                .forEach { block(it.first, it.second) }
    }

    @JvmStatic
    fun main(args: Array<String>) {

        val sample = "flqrgnkx"
        println(solve1(sample))
        println(solve2(sample))

        val input = "ffayrhll"
        println(solve1(input))
        println(solve2(input))
    }
}
