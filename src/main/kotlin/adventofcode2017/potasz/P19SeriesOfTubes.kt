package adventofcode2017.potasz

object P19SeriesOfTubes {

    data class Position(val x: Int, val y: Int) {
        operator fun plus(dir: Direction) = Position(x + dir.x, y + dir.y)
        fun isOut(length: Int, height: Int) = x < 0 || x >= length || y < 0 || y >= height
        fun isValid(diagram: Array<CharArray>) =
                !isOut(diagram[0].size, diagram.size) && diagram[y][x] != ' '
    }
    data class Direction(val x: Int, val y: Int)
    data class Move(val ahead: Direction, val left: Direction, val right: Direction)

    val DOWN = Move(Direction(0, 1), Direction(1, 0), Direction(-1, 0))
    val RIGHT = Move(Direction(1, 0), Direction(0, -1), Direction(0, 1))
    val UP = Move(Direction(0, -1), Direction(-1, 0), Direction(1, 0))
    val LEFT = Move(Direction(-1, 0), Direction(0, 1), Direction(0, -1))

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = readLines("input19.txt")
        val length = lines.maxBy { it.length }?.length ?: 0
        val diagram = lines.map { it.padEnd(length) }.map { it.toCharArray() }.toTypedArray()
        val start = Position(1,0)
        var dir = DOWN
        var pos = start + dir.ahead
        var steps = 1
        while(pos.isValid(diagram)) {
            val char = diagram[pos.y][pos.x]
            if (char.isLetter()) print(char)
            else if (char == '+') {
                val ahead = if((pos + dir.left).isValid(diagram)) dir.left else dir.right
                dir = listOf(DOWN, RIGHT, UP, LEFT).find { it.ahead == ahead } ?: throw RuntimeException("No move found: $ahead")
            }
            steps++
            pos += dir.ahead
        }
        println()
        println(steps)
    }
}
