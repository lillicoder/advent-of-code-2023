package com.lillicoder.adventofcode2023.day16

import com.lillicoder.adventofcode2023.grids.Direction
import com.lillicoder.adventofcode2023.grids.Grid
import com.lillicoder.adventofcode2023.grids.Node

fun main() {
    val day16 = Day16()
    val grid = Grid.read("input.txt")
    println("The total number of tiles that are energized is ${day16.part1(grid)}.")
    println("The maximum number of tiles that can be energized is ${day16.part2(grid)}.")
}

class Day16 {
    fun part1(grid: Grid<String>) =
        Beam(
            grid.first(),
            Direction.RIGHT,
        ).propagate(
            grid,
        )

    fun part2(grid: Grid<String>) =
        (
            grid.row(0).map { Beam(it, Direction.DOWN) } + // top edge
                grid.column(0).map { Beam(it, Direction.RIGHT) } + // left edge
                grid.column(grid.width - 1).map { Beam(it, Direction.LEFT) } + // right edge
                grid.row(grid.height - 1).map { Beam(it, Direction.UP) } // bottom edge
        ).maxOf { it.propagate(grid) }
}

/**
 * Represents a beam propagating through a mirror maze.
 * @param head Current node for the tip of the beam.
 * @param direction Current direction.
 */
data class Beam(
    val head: Node<String>,
    val direction: Direction,
) {
    /**
     * Propagates this [Beam] through the given [Grid].
     * @param grid Grid to propagate through.
     * @return Number of energized nodes in the grid after propagation.
     */
    fun propagate(grid: Grid<String>): Long {
        val visited = mutableMapOf<Beam, Boolean>()
        propagate(grid, visited)

        return visited.keys.map { it.head }.distinct().count().toLong()
    }

    /**
     * Propagates this [Beam] through the given [Grid], making each node visited in the given map.
     * @param grid Grid to propagate through.
     * @param visited Map to mark visited nodes.
     */
    private fun propagate(
        grid: Grid<String>,
        visited: MutableMap<Beam, Boolean>,
    ) {
        if (visited.contains(this)) return

        // Visit
        visited[this] = true

        // Move the beam through the maze from its current position
        val beams =
            when (head.value) {
                "\\", "/" -> reflect(grid)
                "-", "|" -> split(grid)
                else -> advance(grid)
            }
        beams.forEach { it.propagate(grid, visited) }
    }

    /**
     * Advances this beam to its next node in the given [Grid] for its current direction.
     * @param grid Grid.
     * @return Advanced beam or an empty list if this beam has escaped the grid.
     */
    private fun advance(grid: Grid<String>) =
        grid.adjacent(
            head,
            direction,
        )?.let {
            listOf(Beam(it, direction))
        } ?: emptyList()

    /**
     * Reflects this beam to its next node in the given [Grid] for its current direction.
     * @param grid Grid.
     * @return Reflected beam or an empty list if this beam has escaped the grid.
     */
    private fun reflect(grid: Grid<String>): List<Beam> {
        val reflection =
            when (head.value) {
                "/" -> {
                    when (direction) {
                        Direction.UP -> Direction.RIGHT
                        Direction.DOWN -> Direction.LEFT
                        Direction.LEFT -> Direction.DOWN
                        Direction.RIGHT -> Direction.UP
                    }
                }
                "\\" -> {
                    when (direction) {
                        Direction.UP -> Direction.LEFT
                        Direction.DOWN -> Direction.RIGHT
                        Direction.LEFT -> Direction.UP
                        Direction.RIGHT -> Direction.DOWN
                    }
                }
                else -> direction
            }

        val next = grid.adjacent(head, reflection)
        return next?.let { listOf(Beam(next, reflection)) } ?: emptyList()
    }

    /**
     * Splits this beam to its next nodes in the given [Grid] for its current direction.
     * @param grid Grid.
     * @return Split beams.
     */
    private fun split(grid: Grid<String>): List<Beam> {
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
            true -> advance(grid)
            false -> {
                directions.mapNotNull { direction ->
                    val next = grid.adjacent(head, direction)
                    next?.let { Beam(it, direction) }
                }
            }
        }
    }
}
