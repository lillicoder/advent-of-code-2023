/*
 * Copyright 2026 Scott Weeden-Moody
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this project except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lillicoder.adventofcode2023.day16

import com.lillicoder.adventofcode.kotlin.graphs.SquareLatticeGraph
import com.lillicoder.adventofcode.kotlin.graphs.gridToGraph
import com.lillicoder.adventofcode.kotlin.io.Resources
import com.lillicoder.adventofcode.kotlin.math.Direction
import com.lillicoder.adventofcode.kotlin.math.Vertex

fun main() {
    val day16 = Day16()
    val graph =
        Resources.text(
            "input.txt",
        ) ?: throw IllegalArgumentException("Could not read input from file.")
    println("[Part 1] The total number of tiles that are energized is ${day16.part1(graph)}.")
    println("[Part 2] The maximum number of tiles that can be energized is ${day16.part2(graph)}.")
}

class Day16 {
    fun part1(input: String) =
        input.gridToGraph().let {
            Beam(
                it.first(),
                Direction.RIGHT,
            ).propagate(
                it,
            )
        }

    fun part2(input: String) =
        input.gridToGraph().let { graph ->
            (
                graph.rows().first().map { Beam(it, Direction.DOWN) } + // top edge
                    graph.columns().first().map { Beam(it, Direction.RIGHT) } + // left edge
                    graph.columns().last().map { Beam(it, Direction.LEFT) } + // right edge
                    graph.rows().last().map { Beam(it, Direction.UP) } // bottom edge
            ).maxOf { it.propagate(graph) }
        }
}

/**
 * Represents a beam propagating through a mirror maze.
 * @param head Current [Vertex] for the tip of the beam.
 * @param direction Current [Direction].
 */
private data class Beam(
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

        return visited.keys
            .map { it.head }
            .distinct()
            .count()
            .toLong()
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
