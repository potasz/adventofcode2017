package adventofcode2017.potasz

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream
import java.io.InputStream

object Puzzle09 {

    val samples = listOf("{}", "{{},{}}", "{{{}}}", "{{{},{},{{}}}}", "{<{},{},{{}}>}", "{<a>,<a>,<a>,<a>}",
            "{{<a>},{<a>},{<a>},{<a>}}", "{{<!>},{<!>},{<!>},{<a>}}", "{{<!!>},{<!!>},{<!!>},{<!!>}}",
            "{{<a!>},{<a!>},{<a!>},{<ab>}}")

    fun groupsAndGarbage(input: InputStream, value: Int): Pair<Int, Int> {
        var nextByte = input.read()
        var groupCount = 0
        var garbage = false
        var skipNext = false
        var garbageCounter = 0
        while (nextByte != -1) {
            val char = nextByte.toChar()
            if (!skipNext && char == '!') {
                skipNext = true
            } else if (skipNext) {
                skipNext = false
            } else if (garbage) {
                if (char == '>') {
                    garbage = false
                } else {
                    garbageCounter++
                }
            } else when (char) {
                '<' -> garbage = true
                '{' -> {
                    val res = groupsAndGarbage(input, value + 1)
                    groupCount += value + 1 + res.first
                    garbageCounter += res.second
                }
                '}' -> return groupCount to garbageCounter
            }
            nextByte = input.read()
        }
        return groupCount to garbageCounter
    }

    @JvmStatic
    fun main(args: Array<String>) {
        samples.forEach { sample ->
            val bis = ByteInputStream(sample.toByteArray(), sample.length)
            println(groupsAndGarbage(bis, 0))
        }

        val input = javaClass.getResourceAsStream("/input09.txt")
        println(groupsAndGarbage(input, 0))
    }
}
