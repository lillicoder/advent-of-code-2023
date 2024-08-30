package com.lillicoder.adventofcode2023.day10

import com.lillicoder.adventofcode2023.grids.Direction
import com.lillicoder.adventofcode2023.grids.Grid
import com.lillicoder.adventofcode2023.grids.GridParser
import com.lillicoder.adventofcode2023.grids.Node
import com.lillicoder.adventofcode2023.math.Math
import com.lillicoder.adventofcode2023.math.Vertex

fun main() {
    val day10 = Day10()
    val maze = PipeMaze(GridParser().parseFile("input.txt").first())
    println("The max distance for the loop in the pipe maze is ${day10.part1(maze)}.")
    println("The area of enclosures spaces is ${day10.part2(maze)}.")
}

class Day10 {
    fun part1(maze: PipeMaze) = maze.maxDistanceFromStart()

    fun part2(maze: PipeMaze) = maze.enclosedArea()
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
     * Finds the maximum distance away from the starting node in this pipe maze.
     * @return Max distance.
     */
    fun maxDistanceFromStart(): Long {
        val start = start()

        // Using BFS, each iteration of a queue pop will be the next tier of distance
        val queue = ArrayDeque<Node<String>>()
        val distance = mutableMapOf<Node<String>, Long>().withDefault { 0 }

        queue.add(start)
        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()
            adjacent(next).forEach { adjacent ->
                if (!distance.contains(adjacent)) {
                    distance[adjacent] = distance.getValue(next) + 1
                    queue.add(adjacent)
                }
            }
        }

        return distance.values.max()
    }

    /**
     * Gets the area of the enclosed space in this pipe maze.
     * @return Enclosed area.
     */
    fun enclosedArea(): Long {
        val loop = findLoop()
        val vertices =
            loop.filter {
                when (it.value) {
                    "F", "7", "L", "J", "S" -> true
                    else -> false
                }
            }.map {
                Vertex(it.x, it.y)
            }

        val area: Long = Math.area(vertices) // Total area including boundary nodes
        return Math.area(area, loop.size) // Area without boundary nodes
    }

    /**
     * Gets all valid adjacent [Node] for the given node.
     * @param node Node.
     * @return Valid adjacent nodes.
     */
    private fun adjacent(node: Node<String>) =
        // Only some kinds of values are legitimate adjacent nodes for the purposes of finding
        // distances, paths, enclosures, so filter the output from the underlying grid
        grid.adjacent(node) { adjacent, direction ->
            val validSymbols =
                when (direction) {
                    Direction.LEFT -> validLefts
                    Direction.UP -> validTops
                    Direction.DOWN -> validBottoms
                    Direction.RIGHT -> validRights
                    else -> mapOf()
                }
            validSymbols[node.value]?.contains(adjacent.value) ?: false
        }

    /**
     * Find the list of [Node] that comprise the pipe loop in this pipe maze.
     * @return Nodes starting with S and ending with a node adjacent to S.
     */
    private fun findLoop(): List<Node<String>> {
        val loop = mutableListOf<Node<String>>()

        val start = start()
        var node = start
        do {
            val adjacent = adjacent(node).toMutableList()
            if (loop.isNotEmpty()) {
                adjacent.remove(loop.last()) // Force forward movement
            }

            loop.add(node)
            node = adjacent.first()
        } while (node != start)

        return loop
    }

    /**
     * Gets the start node for this maze.
     * @return Start node.
     * @throws IllegalArgumentException Thrown if this maze has no start node.
     */
    private fun start() = grid.find { it == "S" } ?: throw IllegalArgumentException("Pipe maze has no starting node.")
}
