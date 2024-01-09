package com.lillicoder.adventofcode2023.day10

import kotlin.math.abs

fun main() {
    val grid = GridParser().parse("input.txt")
    println("Grid: \n$grid")

    val pathfinder = Pathfinder()
    val distance = pathfinder.findLoopMaxDistance(grid)
    println("The max distance for the loop in the grid is $distance.")

    val area = pathfinder.findEnclosureArea(grid)
    println("The area of enclosures spaces is $area.")
}

/**
 * Represents a node in a grid.
 */
data class Node(
    val x: Int,
    val y: Int,
    val value: String
)

/**
 * Represents an arbitrary grid of nodes.
 */
data class Grid(
    private val tiles: List<List<Node>>
) {

    private val validTops = mapOf(
        "|" to listOf("|", "7", "F", "S"),
        "-" to listOf(""),
        "L" to listOf("|", "7", "F", "S"),
        "J" to listOf("|", "7", "F", "S"),
        "7" to listOf(""),
        "F" to listOf(""),
        "S" to listOf("|", "7", "F", "S")
    )

    private val validBottoms = mapOf(
        "|" to listOf("|", "L", "J", "S"),
        "-" to listOf(""),
        "L" to listOf(""),
        "J" to listOf(""),
        "7" to listOf("|", "L", "J", "S"),
        "F" to listOf("|", "L", "J", "S"),
        "S" to listOf("|", "L", "J", "S")
    )

    private val validLefts = mapOf(
        "|" to listOf(""),
        "-" to listOf("-", "F", "L", "S"),
        "L" to listOf(""),
        "J" to listOf("-", "F", "L", "S"),
        "7" to listOf("-", "F", "L", "S"),
        "F" to listOf(""),
        "S" to listOf("-", "F", "L", "S")
    )

    private val validRights = mapOf(
        "|" to listOf(""),
        "-" to listOf("-", "7", "J", "S"),
        "L" to listOf("-", "7", "J", "S"),
        "J" to listOf(""),
        "7" to listOf(""),
        "F" to listOf("-", "7", "J", "S"),
        "S" to listOf("-", "7", "J", "S")
    )

    /**
     * Gets all adjacent [Node]s for the given node.
     * @param node Node to get neighbors of.
     * @return Adjacent nodes.
     */
    fun adjacent(node: Node): List<Node> {
        val nodes = mutableListOf<Node>()
        if (node.y > 0) {
            val top = tiles[node.y - 1][node.x]
            val isValid = validTops[node.value]?.contains(top.value) ?: false
            if (isValid) nodes.add(top)
        }

        if (node.y < tiles.size - 1) {
            val bottom = tiles[node.y + 1][node.x]
            val isValid = validBottoms[node.value]?.contains(bottom.value) ?: false
            if (isValid) nodes.add(bottom)
        }

        if (node.x > 0) {
            val left = tiles[node.y][node.x - 1]
            val isValid = validLefts[node.value]?.contains(left.value) ?: false
            if (isValid) nodes.add(left)
        }

        if (node.x < tiles[0].size - 1) {
            val right = tiles[node.y][node.x + 1]
            val isValid = validRights[node.value]?.contains(right.value) ?: false
            if (isValid) nodes.add(right)
        }

        return nodes
    }

    /**
     * Gets the position of the tile matching the given predicate.
     * @param predicate Condition to check.
     * @return Tile position or null if no such tile found.
     */
    fun positionOf(predicate: (String) -> Boolean): Node? {
        tiles.forEach { row ->
            row.forEach { column ->
                if (predicate.invoke(column.value)) return column
            }
        }

        return null
    }

    override fun toString(): String = tiles.joinToString("\n") {
        it.joinToString("") {
            node -> node.value.toString()
        }
    }
}

class GridParser {

    /**
     * Parses the file with the given filename into a [Grid].
     * @param filename Filename.
     * @return Grid.
     */
    fun parse(filename: String): Grid {
        val grid: MutableList<MutableList<Node>> = mutableListOf()
        var y = 0
        javaClass.classLoader.getResourceAsStream(filename)!!.reader().forEachLine { line ->
            val row = mutableListOf<Node>()
            line.split("").filter { it.isNotEmpty() }.forEachIndexed { x, value ->
                val node = Node(x, y, value)
                row.add(node)
            }

            grid.add(row)
            y++
        }

        return Grid(grid)
    }
}

class Pathfinder {

    /**
     * Gets the area of all enclosed spaces in the given [Grid].
     * @param grid Grid to search.
     * @return Sum of enclosed areas.
     */
    fun findEnclosureArea(grid: Grid): Double {
        val loop = findLoop(grid)
        val vertices = loop.filter {
            when (it.value) {
                "F", "7", "L", "J", "S" -> true
                else -> false
            }
        }

        // Shoelace formula
        // area = 1/2 * sum of all cross products of vertex pairs (including first and last as they form an edge)
        var shoelace = 0.0
        vertices.windowed(2, 1).forEach { pair ->
            shoelace += cross(pair[0].x, pair[0].y, pair[1].x, pair[1].y)
        }
        shoelace += cross(vertices.last().x, vertices.last().y, vertices.first().x, vertices.first().y)
        shoelace = abs(shoelace) / 2L

        // Pick's theorem
        // interior area = area - (loopSize / 2) + 1
        return shoelace - (loop.size / 2) + 1
    }

    /**
     * Finds the maximum distance away from the starting node in
     * the pipe loop for the given [Grid].
     * @param grid Grid to search.
     * @return Max distance.
     */
    fun findLoopMaxDistance(grid: Grid): Long {
        val start = grid.positionOf { it == "S" }!!

        // Using BFS, each iteration of a queue pop will be the next tier of distance
        val queue = ArrayDeque<Node>()
        val distance = mutableMapOf<Node, Long>().withDefault { 0 }

        queue.add(start)
        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()
            grid.adjacent(next).forEach { adjacent ->
                if (!distance.contains(adjacent)) {
                    distance[adjacent] = distance.getValue(next) + 1
                    queue.add(adjacent)
                }
            }
        }

        return distance.values.max()
    }

    /**
     * Gets the cross product of the given points.
     * @param x1 First X-coordinate.
     * @param y1 First y-coordinate.
     * @param x2 Second x-coordinate.
     * @param y2 Second y-coordinate.
     * @return Cross product.
     */
    private fun cross(x1: Int, y1: Int, x2: Int, y2: Int): Long {
        return ((x1 * y2) - (x2 * y1)).toLong()
    }

    /**
     * Find the list of [Node] that comprise the pipe loop in the given [Grid].
     * @param grid Grid to find the loop in.
     * @return Pipe loop nodes starting with S and ending with a node adjacent to S.
     */
    private fun findLoop(grid:Grid): List<Node> {
        val loop = mutableListOf<Node>()

        val start = grid.positionOf { it == "S" }!!
        var node = start
        do {
            val adjacent = grid.adjacent(node).toMutableList()
            if (loop.isNotEmpty()) {
                adjacent.remove(loop.last()) // Force forward movement
            }

            loop.add(node)
            node = adjacent.first()
        } while (node != start)

        return loop
    }
}
