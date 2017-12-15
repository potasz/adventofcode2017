package adventofcode2017.potasz

import kotlin.math.abs

object Puzzle07 {

    data class Disc(val name: String, var weight: Int = 0, var totalWeight: Int = 0, var children: List<Disc> = emptyList(),
                    var parent: Disc? = null) {
        override fun toString(): String {
            return "Disc($name, w: $weight, tw: $totalWeight, p: ${parent?.name} -> [${children.map { it.name }.joinToString()}])"
        }
    }

    fun countWeights(node: Disc): Int {
        node.totalWeight = node.weight + node.children.sumBy { countWeights(it) }
        return node.totalWeight
    }

    fun traverse(node: Disc, level: Int, maxLevel: Int, f: (Disc, Int) -> Unit) {
        if (level <= maxLevel) {
            f(node, level)
            node.children.forEach { traverse(it, level + 1, maxLevel, f) }
        }
    }

    fun findUnbalanced(node: Disc) {
        val avg = node.children.sumBy { it.totalWeight } / node.children.size.toDouble()
        val unBalanced = node.children.maxBy { abs(it.totalWeight - avg) }
        if (unBalanced != null && unBalanced.totalWeight != avg.toInt()) {
            findUnbalanced(unBalanced)
        } else {
            val sibling = node.parent!!.children.find { it.name != node.name }
            val diff = node.totalWeight - sibling!!.totalWeight
            println("Unbalanced: $node")
            println("  Sibling total weight: ${sibling.totalWeight}. New weight: ${node.weight - diff}")
        }
    }

    fun findRoot(lines: List<String>): Disc {
        val discMap = mutableMapOf<String, Disc>()
        lines.forEach { line ->
            val m = pattern.matchEntire(line) ?: throw RuntimeException("Cannot parse line: $line")
            with (m.destructured) {
                val name = component1()
                val weight = component2().toInt()
                val children = if (component3().isEmpty()) mutableListOf() else component3().split(", ")
                val disc = discMap.getOrPut(name, { Disc(name) })
                disc.weight = weight
                val childList = children.map { discMap.getOrPut(it, { Disc(it) }).apply { this.parent = disc } }
                disc.children = childList
            }
        }

        return discMap.values.find { it.parent == null } ?: throw RuntimeException("Cannot find root")
    }

    val pattern = """([a-z]+)\s+\((\d+)\)[->\s]*([a-z,\s]*)""".toRegex()

    @JvmStatic
    fun main(args: Array<String>) {
        val sample = readLines("sample07.txt")
        val input = readLines("input07.txt")

        listOf(sample, input).forEach {
            val root = findRoot(it)
            countWeights(root)
            traverse(root, 0, 1) { disc, i -> println("  " * i + disc) }
            findUnbalanced(root)
            println()
        }
    }
}
