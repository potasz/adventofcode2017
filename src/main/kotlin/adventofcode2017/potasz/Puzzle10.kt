package adventofcode2017.potasz

object Puzzle10 {
    val SUFFIX = listOf(17, 31, 73, 47, 23)

    val sampleLengths = arrayListOf(3, 4, 1, 5)
    val inputLengths1 = arrayListOf(165,1,255,31,87,52,24,113,0,91,148,254,158,2,73,153)
    val input = "165,1,255,31,87,52,24,113,0,91,148,254,158,2,73,153"

    private fun sparseHash(lengths: List<Int>, size: Int = 256, rounds: Int = 64): List<Int> {
        val numbers = IntArray(size) { it }
        var pos = 0
        var skipSize = 0
        for (round in 0 until rounds) {
            for (length in lengths) {
                val subList = IntArray(length) { numbers[(pos + it) % size] }
                (pos until pos + length).forEach { numbers[it % size] = subList[length - 1 - (it - pos)] }
                pos = (pos + length + skipSize++) % size
            }
        }
        return numbers.toList()
    }

    private fun denseHash(sparseHash: List<Int>, blockSize: Int = 16): List<Int> {
        if (sparseHash.size % blockSize != 0) throw IllegalArgumentException("Hash size need to be divisible by block size!")
        return sparseHash.chunked(blockSize) { it.fold(-1) { acc, i -> if (acc == -1) i else acc xor i } }
    }

    fun List<Int>.toHexString(): String = this.map { it.toString(16).padStart(2, '0') }.joinToString("")

    fun knotHash(input: String): String = denseHash(sparseHash(input.toCharArray().map {it.toInt()}.plus(SUFFIX))).toHexString()

    @JvmStatic
    fun main(args: Array<String>) {
        val sampleHash1 = sparseHash(sampleLengths, 5, 1)
        println(sampleHash1[0] * sampleHash1[1])
        val inputHash1 = sparseHash(inputLengths1, rounds = 1)
        println(inputHash1[0] * inputHash1[1])

        println(knotHash(input))
    }
}
