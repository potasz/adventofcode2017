package adventofcode2017.potasz

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream
import java.io.InputStream

object Puzzle09 {

    val samples = listOf("{}", "{{},{}}", "{{{}}}", "{{{},{},{{}}}}", "{<{},{},{{}}>}", "{<a>,<a>,<a>,<a>}",
            "{{<a>},{<a>},{<a>},{<a>}}", "{{<!>},{<!>},{<!>},{<a>}}", "{{<!!>},{<!!>},{<!!>},{<!!>}}",
            "{{<a!>},{<a!>},{<a!>},{<ab>}}")

    fun scoreAndGarbage(stream: InputStream): Pair<Int, Int> {
        var score = 0
        var depth = 0
        var garbage = false
        var skipNext = false
        var garbageCounter = 0
        var char = stream.read().toChar()
        while (char != (-1).toChar()) {
            if (!skipNext && char == '!') skipNext = true
            else if (skipNext) skipNext = false
            else if (garbage && char == '>') garbage = false
            else if (garbage) garbageCounter++
            else when (char) {
                '<' -> garbage = true
                '{' -> score += depth++ + 1
                '}' -> depth--
            }
            char = stream.read().toChar()
        }
        return score to garbageCounter
    }

    @JvmStatic
    fun main(args: Array<String>) {
        samples.forEach { sample ->
            val bis = ByteInputStream(sample.toByteArray(), sample.length)
            println(scoreAndGarbage(bis))
        }

        val input = javaClass.getResourceAsStream("/input09.txt")
        println(scoreAndGarbage(input))
    }
}
