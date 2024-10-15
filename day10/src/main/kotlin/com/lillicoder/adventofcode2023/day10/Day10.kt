package com.lillicoder.adventofcode2023.day10

import com.lillicoder.adventofcode2023.graphs.SquareLatticeGraph
import com.lillicoder.adventofcode2023.graphs.Vertex
import com.lillicoder.adventofcode2023.graphs.gridToGraph
import com.lillicoder.adventofcode2023.io.Resources
import com.lillicoder.adventofcode2023.math.Direction
import com.lillicoder.adventofcode2023.math.Math
import com.lillicoder.adventofcode2023.math.area

fun main() {
    val day10 = Day10()
    val graph =
        Resources.text("input.txt")?.gridToGraph()
            ?: throw IllegalArgumentException("Could not read input from file.")
    val maze = PipeMaze(graph)
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
data class PipeMaze(private val graph: SquareLatticeGraph<String>) {
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
     * Finds the maximum distance away from the starting [Vertex] in this pipe maze.
     * @return Max distance.
     */
    fun maxDistanceFromStart(): Long {
        val start = start()

        // Using BFS, each iteration of a queue pop will be the next tier of distance
        val queue = ArrayDeque<Vertex<String>>()
        val distance = mutableMapOf<Vertex<String>, Long>().withDefault { 0 }

        queue.add(start)
        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()
            validNeighbors(next).forEach { adjacent ->
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
                Vertex(it.id, it.value)
            }

        val area = vertices.mapNotNull { graph.coordinates(it) }.area() // Total area including boundary vertices
        return Math.area(area, loop.size) // Area without boundary vertices
    }

    /**
     * Find the list of [Vertex] that comprise the pipe loop in this pipe maze.
     * @return Vertices starting with S and ending with a vertex adjacent to S.
     */
    private fun findLoop(): List<Vertex<String>> {
        val loop = mutableListOf<Vertex<String>>()

        val start = start()
        var vertex = start
        do {
            val adjacent = validNeighbors(vertex).toMutableList()
            if (loop.isNotEmpty()) {
                adjacent.remove(loop.last()) // Force forward movement
            }

            loop.add(vertex)
            vertex = adjacent.first()
        } while (vertex != start)

        return loop
    }

    /**
     * Gets all valid neighbors for the given [Vertex]. A
     * @param vertex Vertex.
     * @return Valid neighbors.
     */
    private fun validNeighbors(vertex: Vertex<String>) =
        graph.neighbors(vertex).filter {
            val validSymbols =
                when (graph.direction(vertex, it)) {
                    Direction.LEFT -> validLefts
                    Direction.UP -> validTops
                    Direction.DOWN -> validBottoms
                    Direction.RIGHT -> validRights
                    else -> emptyMap()
                }
            validSymbols[vertex.value]?.contains(it.value) ?: false
        }

    /**
     * Gets the start [Vertex] for this maze.
     * @return Start vertex.
     * @throws IllegalArgumentException Thrown if this maze has no start vertex.
     */
    private fun start() =
        graph.find {
            it.value == "S"
        } ?: throw IllegalArgumentException("Pipe maze has no starting vertex.")
}
