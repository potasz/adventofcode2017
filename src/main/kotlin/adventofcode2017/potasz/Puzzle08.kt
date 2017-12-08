package adventofcode2017.potasz

import kotlin.math.max

sealed class Expr<out T>(val reg: String, val number: Int, val op: (Int) -> T) {
    fun eval(context: MutableMap<String, Int>): T {
        return op(context.getOrDefault(reg, 0))
    }

    override fun toString(): String {
        return "${this.javaClass.simpleName}($reg, $number)"
    }

    companion object {
        fun operation(reg: String, token: String, number: Int): Expr<Int> = when (token) {
            "inc" -> Inc(reg, number)
            "dec" -> Dec(reg, number)
            else -> throw RuntimeException("Unknown operation: $token")
        }

        fun condition(reg: String, token: String, number: Int): Expr<Boolean> = when (token) {
            "==" -> Eq(reg, number)
            "!=" -> Neq(reg, number)
            "<" -> Lt(reg, number)
            "<=" -> Lte(reg, number)
            ">" -> Gt(reg, number)
            ">=" -> Gte(reg, number)
            else -> throw RuntimeException("Unknown condition: $token")
        }
    }
}

class Inc(reg: String, number: Int): Expr<Int>(reg, number, { it + number })
class Dec(reg: String, number: Int): Expr<Int>(reg, number, { it - number })

class Eq(reg: String, number: Int): Expr<Boolean>(reg, number, { it == number })
class Neq(reg: String, number: Int): Expr<Boolean>(reg, number, { it != number})
class Lt(reg: String, number: Int): Expr<Boolean>(reg, number, { it < number})
class Lte(reg: String, number: Int): Expr<Boolean>(reg, number, { it <= number })
class Gt(reg: String, number: Int): Expr<Boolean>(reg, number, { it > number})
class Gte(reg: String, number: Int): Expr<Boolean>(reg, number, { it >= number})

object Puzzle08 {

    // c inc -20 if c == 10
    val PATTERN = """(\w+)\s(inc|dec)\s([-0-9]+)\sif\s(\w+)\s([!=<>]+)\s([-0-9]+)""".toRegex()

    data class Instruction(val op: Expr<Int>, val cond: Expr<Boolean>)

    fun parseLine(line: String): Instruction {
        val tokens = PATTERN.matchEntire(line)?.groupValues ?: throw RuntimeException("Cannot parse line: $line")
        return Instruction(Expr.operation(tokens[1], tokens[2], tokens[3].toInt()),
                Expr.condition(tokens[4], tokens[5], tokens[6].toInt()))
    }

    fun solve(lines: List<String>): Pair<Int, Int> {
        val context = mutableMapOf<String, Int>()
        var max = Int.MIN_VALUE
        lines.map { parseLine(it) }.forEach {
            if (it.cond.eval(context)) {
                val result = it.op.eval(context)
                context[it.op.reg] = result
                max = max(max, result)
            }
        }

        return (context.entries.maxBy { it.component2() }?.component2() ?: 0) to max
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val sample = readLines("sample08.txt")
        val sampleSolution = solve(sample)
        println("Sample -> max register value: ${sampleSolution.first}, highest value: ${sampleSolution.second}")

        val input = readLines("input08.txt")
        val solution = solve(input)
        println("Input -> max register value: ${solution.first}, highest value: ${solution.second}")
    }
}
