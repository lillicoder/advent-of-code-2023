package com.lillicoder.adventofcode2023.day16

import com.lillicoder.adventofcode2023.graphs.SquareLatticeGraph
import com.lillicoder.adventofcode2023.graphs.Vertex
import com.lillicoder.adventofcode2023.graphs.gridToGraph
import com.lillicoder.adventofcode2023.io.Resources
import com.lillicoder.adventofcode2023.math.Direction

fun main() {
    val day16 = Day16()
    val graph =
        Resources.text(
            "input.txt",
        )?.gridToGraph() ?: throw IllegalArgumentException("Could not read input from file.")
    println("The total number of tiles that are energized is ${day16.part1(graph)}.")
    println("The maximum number of tiles that can be energized is ${day16.part2(graph)}.")
}

class Day16 {
    fun part1(graph: SquareLatticeGraph<String>) =
        Beam(
            graph.first(),
            Direction.RIGHT,
        ).propagate(
            graph,
        )

    fun part2(graph: SquareLatticeGraph<String>) =
        (
            graph.rows().first().map { Beam(it, Direction.DOWN) } + // top edge
                graph.columns().first().map { Beam(it, Direction.RIGHT) } + // left edge
                graph.columns().last().map { Beam(it, Direction.LEFT) } + // right edge
                graph.rows().last().map { Beam(it, Direction.UP) } // bottom edge
        ).maxOf { it.propagate(graph) }
}

/**
 * Represents a beam propagating through a mirror maze.
 * @param head Current [Vertex] for the tip of the beam.
 * @param direction Current [Direction].
 */
data class Beam(
    val head: Vertex<String>,
    val direction: Direction,
) {
    /**
     * Propagates this [Beam] through the given [SquareLatticeGraph].
     * @param graph Graph to propagate through.
     * @return Number of energized vertices in the graph after propagation.
     */
    fun propagate(graph: SquareLatticeGraph<String>): Long {
        val visited = mutableMapOf<Beam, Boolean>()
        propagate(graph, visited)

        return visited.keys.map { it.head }.distinct().count().toLong()
    }

    /**
     * Propagates this [Beam] through the given [SquareLatticeGraph],
     * marking each vertex visited in the given map.
     * @param graph Graph to propagate through.
     * @param visited Map to mark visited vertices.
     */
    private fun propagate(
        graph: SquareLatticeGraph<String>,
        visited: MutableMap<Beam, Boolean>,
    ) {
        if (visited.contains(this)) return

        // Visit
        visited[this] = true

        // Move the beam through the maze from its current position
        val beams =
            when (head.value) {
                "\\", "/" -> reflect(graph)
                "-", "|" -> split(graph)
                else -> advance(graph)
            }
        beams.forEach { it.propagate(graph, visited) }
    }

    /**
     * Advances this beam to its next vertex in the given [SquareLatticeGraph] for its current direction.
     * @param graph Graph.
     * @return Advanced beam or an empty list if this beam has escaped the graph.
     */
    private fun advance(graph: SquareLatticeGraph<String>) =
        when (val neighbor = graph.neighbor(head, direction)) {
            null -> emptyList()
            else -> listOf(Beam(neighbor, direction))
        }

    /**
     * Reflects this beam to its next vertex in the given
     * [SquareLatticeGraph] for its current direction.
     * @param graph Graph.
     * @return Reflected beam or an empty list if this beam has escaped the graph.
     */
    private fun reflect(graph: SquareLatticeGraph<String>): List<Beam> {
        val reflection =
            when (head.value) {
                "/" -> {
                    when (direction) {
                        Direction.UP -> Direction.RIGHT
                        Direction.DOWN -> Direction.LEFT
                        Direction.LEFT -> Direction.DOWN
                        Direction.RIGHT -> Direction.UP
                        else -> direction
                    }
                }
                "\\" -> {
                    when (direction) {
                        Direction.UP -> Direction.LEFT
                        Direction.DOWN -> Direction.RIGHT
                        Direction.LEFT -> Direction.UP
                        Direction.RIGHT -> Direction.DOWN
                        else -> direction
                    }
                }
                else -> direction
            }

        val next = graph.neighbor(head, reflection)
        return next?.let { listOf(Beam(next, reflection)) } ?: emptyList()
    }

    /**
     * Splits this beam to its next vertices in the given
     * [SquareLatticeGraph] for its current direction.
     * @param graph Graph.
     * @return Split beams.
     */
    private fun split(graph: SquareLatticeGraph<String>): List<Beam> {
        val directions =
            when (head.value) {
                "-" -> {
                    when (direction) {
                        Direction.UP, Direction.DOWN -> listOf(Direction.LEFT, Direction.RIGHT)
                        else -> emptyList()
                    }
                }
                "|" -> {
                    when (direction) {
                        Direction.LEFT, Direction.RIGHT -> listOf(Direction.UP, Direction.DOWN)
                        else -> emptyList()
                    }
                }
                else -> emptyList()
            }

        return when (directions.isEmpty()) {
            true -> advance(graph)
            false -> {
                directions.mapNotNull { direction ->
                    val next = graph.neighbor(head, direction)
                    next?.let { Beam(it, direction) }
                }
            }
        }
    }
}
