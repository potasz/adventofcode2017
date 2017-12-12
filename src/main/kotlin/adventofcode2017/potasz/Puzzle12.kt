package adventofcode2017.potasz

object Puzzle12 {

    data class Program(val id: Int, val peers: MutableSet<Int> = mutableSetOf())

    fun solve(lines: List<String>): List<Set<Int>> {
        val programs = mutableMapOf<Int, Program>()
        lines.map { it.split(" <-> ") }
                .map { Pair(it[0].toInt(), it[1].split(", ").map{ it.toInt() }) }
                .forEach { pair ->
                    val p = programs.getOrPut(pair.first) { Program(pair.first) }
                    pair.second
                            .map { programs.getOrPut(it, {Program(it)}).apply { this.peers.add(p.id) } }
                            .forEach { p.peers.add(it.id) }
                }

        return findGroups(programs.toMap())
    }

    private fun findGroups(programs: Map<Int, Program>): List<Set<Int>> {
        val groups = mutableListOf<Set<Int>>()
        val foundPrograms = mutableSetOf<Int>()
        var root = programs.keys.minus(foundPrograms).sorted().firstOrNull()
        while (root != null) {
            val group = mutableSetOf<Int>()
            findPeersFrom(root, programs, group)
            groups.add(group)
            foundPrograms.addAll(group)
            root = programs.keys.minus(foundPrograms).sorted().firstOrNull()
        }

        return groups.toList()
    }

    private fun findPeersFrom(root: Int, programs: Map<Int, Program>, group: MutableSet<Int>) {
        programs[root]!!.peers
                .filter { it !in group }
                .forEach {
                    group.add(it)
                    findPeersFrom(it, programs, group)
                }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val input = readLines("input12.txt")
        val groups = solve(input)
        println("Group size containing 0: ${groups[0].size}")
        println("Number of groups: ${groups.size}")
    }
}
