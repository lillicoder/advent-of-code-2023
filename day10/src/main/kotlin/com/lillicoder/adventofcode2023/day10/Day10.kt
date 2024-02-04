package com.lillicoder.adventofcode2023.day10

import com.lillicoder.adventofcode2023.grids.Direction
import com.lillicoder.adventofcode2023.grids.Grid
import com.lillicoder.adventofcode2023.grids.GridParser
import com.lillicoder.adventofcode2023.grids.Node
import kotlin.math.abs

fun main() {
    val day10 = Day10()
    val maze = PipeMaze(GridParser().parse("input.txt") { it })
    println("The max distance for the loop in the pipe maze is ${day10.part1(maze)}.")
    println("The area of enclosures spaces is ${day10.part2(maze)}.")
}

class Day10 {
    fun part1(maze: PipeMaze) = Pathfinder().findLoopMaxDistance(maze)

    fun part2(maze: PipeMaze) = Pathfinder().findEnclosureArea(maze)
}

/**
 * Represents an arbitrary grid of pipes. It's not really a maze but whatever.
 */
data class PipeMaze(private val grid: Grid<String>) {
    private val validTops =
        mapOf(
            "|" to listOf("|", "7", "F", "S"),
            "-" to listOf(""),
            "L" to listOf("|", "7", "F", "S"),
            "J" to listOf("|", "7", "F", "S"),
            "7" to listOf(""),
            "F" to listOf(""),
            "S" to listOf("|", "7", "F", "S"),
        )

    private val validBottoms =
        mapOf(
            "|" to listOf("|", "L", "J", "S"),
            "-" to listOf(""),
            "L" to listOf(""),
            "J" to listOf(""),
            "7" to listOf("|", "L", "J", "S"),
            "F" to listOf("|", "L", "J", "S"),
            "S" to listOf("|", "L", "J", "S"),
        )

    private val validLefts =
        mapOf(
            "|" to listOf(""),
            "-" to listOf("-", "F", "L", "S"),
            "L" to listOf(""),
            "J" to listOf("-", "F", "L", "S"),
            "7" to listOf("-", "F", "L", "S"),
            "F" to listOf(""),
            "S" to listOf("-", "F", "L", "S"),
        )

    private val validRights =
        mapOf(
            "|" to listOf(""),
            "-" to listOf("-", "7", "J", "S"),
            "L" to listOf("-", "7", "J", "S"),
            "J" to listOf(""),
            "7" to listOf(""),
            "F" to listOf("-", "7", "J", "S"),
            "S" to listOf("-", "7", "J", "S"),
        )

    /**
     * Gets all valid adjacent [Node] for the given node.
     * @param node Node.
     * @return Valid adjacent nodes.
     */
    fun adjacent(node: Node<String>) =
        // Only some kinds of values are legitimate adjacent nodes for the purposes of finding
        // distances, paths, enclosures, so filter the output from the underlying grid
        grid.adjacent(node) { adjacent, direction ->
            val validSymbols =
                when (direction) {
                    Direction.LEFT -> validLefts
                    Direction.UP -> validTops
                    Direction.DOWN -> validBottoms
                    Direction.RIGHT -> validRights
                    else -> null
                }
            validSymbols?.get(node.value)?.contains(adjacent.value) ?: false
        }

    /**
     * Finds the first [Node] that satisfies the given predicate.
     * @param predicate Predicate to check.
     * @return Found node or null if no node satisfied the predicate.
     */
    fun find(predicate: (String) -> Boolean): Node<String>? = grid.find(predicate)
}

class Pathfinder {
    /**
     * Gets the area of all enclosed spaces in the given [PipeMaze].
     * @param maze Maze to search.
     * @return Sum of enclosed areas.
     */
    fun findEnclosureArea(maze: PipeMaze): Double {
        val loop = findLoop(maze)
        val vertices =
            loop.filter {
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
     * the pipe loop for the given [PipeMaze].
     * @param maze Maze to search.
     * @return Max distance.
     */
    fun findLoopMaxDistance(maze: PipeMaze): Long {
        val start = maze.find { it == "S" }!!

        // Using BFS, each iteration of a queue pop will be the next tier of distance
        val queue = ArrayDeque<Node<String>>()
        val distance = mutableMapOf<Node<String>, Long>().withDefault { 0 }

        queue.add(start)
        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()
            maze.adjacent(next).forEach { adjacent ->
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
    private fun cross(
        x1: Long,
        y1: Long,
        x2: Long,
        y2: Long,
    ) = (x1 * y2) - (x2 * y1)

    /**
     * Find the list of [Node] that comprise the pipe loop in the given [PipeMaze].
     * @param maze Maze to find the loop in.
     * @return Pipe loop nodes starting with S and ending with a node adjacent to S.
     */
    private fun findLoop(maze: PipeMaze): List<Node<String>> {
        val loop = mutableListOf<Node<String>>()

        val start = maze.find { it == "S" }!!
        var node = start
        do {
            val adjacent = maze.adjacent(node).toMutableList()
            if (loop.isNotEmpty()) {
                adjacent.remove(loop.last()) // Force forward movement
            }

            loop.add(node)
            node = adjacent.first()
        } while (node != start)

        return loop
    }
}
