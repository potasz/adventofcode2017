package adventofcode2017.potasz

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.LongAdder
import kotlin.concurrent.thread

sealed class Instruction {
    companion object {
        fun from(line: String): Instruction {
            val reg = line[4]
            val value = Value(line.drop(6))
            return when (line.take(3)) {
                "snd" -> SND(Value(line.drop(4)))
                "set" -> SET(reg, value)
                "add" -> ADD(reg, value)
                "mul" -> MUL(reg, value)
                "mod" -> MOD(reg, value)
                "rcv" -> RCV(reg)
                "jgz" -> JGZ(Value(line[4].toString()), value)
                else -> throw IllegalArgumentException("Unknown instruction: $line")
            }
        }
    }
}

data class Value(val value: String) : Instruction() {
    fun eval(ctx: MutableMap<Char, Long>): Long = if (value[0].isLetter()) ctx.getReg(value[0]) else value.toLong()
}
data class SND(val value: Value) : Instruction()
data class SET(val reg: Char, val value: Value) : Instruction()
data class ADD(val reg: Char, val value: Value) : Instruction()
data class MUL(val reg: Char, val value: Value) : Instruction()
data class MOD(val reg: Char, val value: Value) : Instruction()
data class RCV(val reg: Char) : Instruction()
data class JGZ(val cond: Value, val value: Value) : Instruction()

fun MutableMap<Char,Long>.getReg(reg: Char) = this.getOrPut(reg, { 0L })

object P18Duet {

    class SoundCard {
        val sounds = mutableMapOf<Char, Long>()

        fun play(reg: Char, value: Long) = sounds.put(reg, value)
        fun recover(reg: Char) = sounds[reg]

        override fun toString(): String {
            return "SoundCard($sounds)"
        }
    }

    fun execute1(intructions: List<Instruction>, ctx: MutableMap<Char, Long>, sdcard: SoundCard)  {
        var ip = 0
        while (ip < intructions.size) {
            val instr = intructions[ip++]
            when (instr) {
                is SND -> sdcard.play(instr.value.value[0], instr.value.eval(ctx))
                is SET -> ctx[instr.reg] = instr.value.eval(ctx)
                is ADD -> ctx[instr.reg] = ctx.getReg(instr.reg) + instr.value.eval(ctx)
                is MUL -> ctx[instr.reg] = ctx.getReg(instr.reg) * instr.value.eval(ctx)
                is MOD -> ctx[instr.reg] = ctx.getReg(instr.reg) % instr.value.eval(ctx)
                is RCV -> {
                    if (ctx.getReg(instr.reg) != 0L) {
                        val s = sdcard.recover(instr.reg)
                        if (s != null) {
                            println("Recovered sound $s for reg ${instr.reg}")
                            return
                        }
                    }
                }
                is JGZ -> if (instr.cond.eval(ctx) > 0) ip += instr.value.eval(ctx).toInt() - 1
            }
        }
    }

    class Messages {
        private val QUEUE_SIZE = 1000_000
        private val queues = ConcurrentHashMap<Int, BlockingQueue<Long>>()
        private val sendCounters = mutableMapOf<Int, LongAdder>()

        fun send(to: Int, value: Long) {
            sendCounters.getOrPut(to, { LongAdder() }).increment()
            queues.computeIfAbsent(to, { ArrayBlockingQueue(QUEUE_SIZE) }).add(value)
        }

        fun receive(id: Int): Long {
            return queues.computeIfAbsent(id, { ArrayBlockingQueue(QUEUE_SIZE) }).poll(5, TimeUnit.SECONDS)
                    ?: throw TimeoutException("Deadlock in program $id. Program $id sent ${sendCounters[id]?.sum()} messages")
        }
    }

    fun execute2(id: Int, peerId: Int, intructions: List<Instruction>, ctx: MutableMap<Char, Long>, messagesBus: Messages)  {
        try {
            var ip = 0
            ctx['p'] = id.toLong()
            while (ip < intructions.size) {
                val instr = intructions[ip++]
                when (instr) {
                    is SND -> messagesBus.send(peerId, instr.value.eval(ctx))
                    is SET -> ctx[instr.reg] = instr.value.eval(ctx)
                    is ADD -> ctx[instr.reg] = ctx.getReg(instr.reg) + instr.value.eval(ctx)
                    is MUL -> ctx[instr.reg] = ctx.getReg(instr.reg) * instr.value.eval(ctx)
                    is MOD -> ctx[instr.reg] = ctx.getReg(instr.reg) % instr.value.eval(ctx)
                    is RCV -> ctx[instr.reg] = messagesBus.receive(id)
                    is JGZ -> if (instr.cond.eval(ctx) > 0) ip += instr.value.eval(ctx).toInt() - 1
                }
            }
        } catch (ex: TimeoutException) {
            println(ex.message)
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        execute1(readLines("sample18.txt").map { Instruction.from(it) }, mutableMapOf(), SoundCard())

        val instructions = readLines("input18.txt").map { Instruction.from(it) }
        execute1(instructions, mutableMapOf(), SoundCard())

        val messageBus = Messages()
        (0..1).forEach { id ->
            thread {
                execute2(id, (id + 1) % 2, instructions, mutableMapOf(), messageBus)
            }
        }
    }
}
