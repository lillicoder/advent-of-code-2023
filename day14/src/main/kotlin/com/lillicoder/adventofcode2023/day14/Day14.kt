package com.lillicoder.adventofcode2023.day14

import com.lillicoder.adventofcode2023.graphs.Graph
import com.lillicoder.adventofcode2023.graphs.SquareLatticeGraph
import com.lillicoder.adventofcode2023.graphs.Vertex
import com.lillicoder.adventofcode2023.graphs.gridToGraph
import com.lillicoder.adventofcode2023.io.Resources
import com.lillicoder.adventofcode2023.io.splitMap
import com.lillicoder.adventofcode2023.io.splitNotEmpty
import com.lillicoder.adventofcode2023.math.Direction

fun main() {
    val day14 = Day14()
    val graph =
        Resources.text(
            "input.txt",
        )?.gridToGraph() ?: throw IllegalArgumentException("Could not read input from file.")
    println("The total load for a single tilt to the north is ${day14.part1(graph)}.")
    println("The total load for a 1000000000 cycles is ${day14.part2(graph)}.")
}

class Day14 {
    fun part1(graph: SquareLatticeGraph<String>) =
        graph.tiltedLoad(
            listOf(
                Direction.UP,
            ),
            1,
        )

    fun part2(graph: SquareLatticeGraph<String>) =
        graph.tiltedLoad(
            listOf(
                Direction.UP,
                Direction.LEFT,
                Direction.DOWN,
                Direction.RIGHT,
            ),
            1_000_000_000,
        )
}

/**
 * Gets the load for this [Graph] after tilting it in each
 * given [Direction] for the given number of cycles.
 * @param order Directions to tilt.
 * @param cycles Number of cycles to tilt.
 * @return Tilted load.
 */
private fun SquareLatticeGraph<String>.tiltedLoad(
    order: List<Direction>,
    cycles: Int,
): Long {
    val cache = mutableMapOf<String, Int>()

    var tilted = this
    repeat(cycles) { cycle ->
        val key = tilted.toString()
        if (key in cache) {
            val distance = cycle - cache[key]!!
            val remaining = (cycles - cycle) % distance
            repeat(remaining) { tilted = tilted.tilt(order) }

            return tilted.load()
        }

        cache[key] = cycle
        tilted = tilted.tilt(order)
    }

    return tilted.load()
}

/**
 * Gets the load for the this [Graph].
 * @return Load.
 */
private fun SquareLatticeGraph<String>.load() =
    rows().map { row ->
        row.count { it.value == "O" }
    }.mapIndexed { index, count ->
        // Load of a row = number of rocks * distance from edge
        count * (height - index)
    }.sum().toLong()

/**
 * Tilts this [Graph] in each of the given [Direction].
 * @param directions Directions to tilt.
 * @return Tilted graph.
 */
private fun SquareLatticeGraph<String>.tilt(directions: List<Direction>) =
    directions.fold(this) { graph, direction ->
        graph.tilt(direction)
    }

/**
 * Tilts this [Graph] in the given [Direction].
 * @param direction Direction to tilt.
 * @return Tilted graph.
 */
private fun SquareLatticeGraph<String>.tilt(direction: Direction): SquareLatticeGraph<String> {
    val comparator = TiltComparator(direction)
    val tilted =
        when (direction) {
            Direction.LEFT, Direction.RIGHT -> {
                rows().map {
                    it.tilt(comparator)
                }
            }
            Direction.DOWN, Direction.UP -> {
                columns().map {
                    it.tilt(comparator)
                }.columnsToRows() // Convert columns to rows before making graph to ensure proper order
            }
            else -> emptyList()
        }

    return tilted.joinToString(System.lineSeparator()).gridToGraph()
}

/**
 * Tilts this row or column of [Vertex] based on the given [Comparator].
 * @param comparator Comparator to determine vertex order.
 * @return Tilted row or column as a string.
 */
private fun List<Vertex<String>>.tilt(comparator: Comparator<String>) =
    joinToString("") {
        it.value
    }.splitMap("#") { // Split by cubes, sort each chunk to roll the spheres, and put the cubes back
        it.splitNotEmpty(
            "",
        ).sortedWith(
            comparator,
        ).joinToString(
            "",
        )
    }.joinToString("#")

/**
 * Converts a list of strings representing columns to a list of strings representing
 * those columns as rows.
 *
 * E.g.
 *
 * [0]  [1] [2]
 * 1    4   8
 * 2    6   9
 * 3    7   10
 *
 * becomes
 *
 * [0]  1   4   8
 * [1]  2   6   9
 * [2]  3   7   10
 */
private fun List<String>.columnsToRows() =
    IntRange(
        0,
        get(0).length - 1,
    ).map {
        fold("") { accumulator, column ->
            accumulator + column[it]
        }
    }

/**
 * [Comparator] of vertex values based on a given tilt [Direction].
 */
private class TiltComparator(private val direction: Direction) : Comparator<String> {
    override fun compare(
        vertex: String,
        other: String,
    ): Int {
        if (vertex == other) return 0
        return when (vertex) {
            "O" -> {
                when (direction) {
                    Direction.UP, Direction.LEFT -> -1
                    else -> 1
                }
            }
            "." -> {
                when (direction) {
                    Direction.UP, Direction.LEFT -> 1
                    else -> -1
                }
            }
            else -> 0
        }
    }
}
