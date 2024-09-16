package com.lillicoder.adventofcode2023.day14

import com.lillicoder.adventofcode2023.grids.Direction
import com.lillicoder.adventofcode2023.grids.Grid
import com.lillicoder.adventofcode2023.grids.GridParser
import com.lillicoder.adventofcode2023.grids.Node
import com.lillicoder.adventofcode2023.io.splitMap
import com.lillicoder.adventofcode2023.io.splitNotEmpty

fun main() {
    val day14 = Day14()
    val grid = GridParser().parseFile("input.txt").first()
    println("The total load for a single tilt to the north is ${day14.part1(grid)}.")
    println("The total load for a 1000000000 cycles is ${day14.part2(grid)}.")
}

class Day14 {
    fun part1(grid: Grid<String>) =
        grid.tiltedLoad(
            listOf(
                Direction.UP,
            ),
            1,
        )

    fun part2(grid: Grid<String>) =
        grid.tiltedLoad(
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
 * Gets the load for this [Grid] after tilting it in each
 * given [Direction] for the given number of cycles.
 * @param order Directions to tilt.
 * @param cycles Number of cycles to tilt.
 * @return Tilted load.
 */
private fun Grid<String>.tiltedLoad(
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
 * Gets the load for the this [Grid].
 * @return Load.
 */
private fun Grid<String>.load() =
    countNodesByRow {
        it.value == "O"
    }.mapIndexed { index, count ->
        // Load of a row = number of rocks * distance from edge
        count * (height - index)
    }.sum().toLong()

/**
 * Tilts this [Grid] in each of the given [Direction].
 * @param directions Directions to tilt.
 * @return Tilted grid.
 */
private fun Grid<String>.tilt(directions: List<Direction>) =
    directions.fold(this) { grid, direction ->
        grid.tilt(direction)
    }

/**
 * Tilts this [Grid] in the given [Direction].
 * @param direction Direction to tilt.
 * @return Tilted grid.
 */
private fun Grid<String>.tilt(direction: Direction): Grid<String> {
    val comparator = TiltComparator(direction)
    val tilted =
        when (direction) {
            Direction.LEFT, Direction.RIGHT -> {
                mapRows {
                    it.tilt(comparator)
                }
            }
            Direction.DOWN, Direction.UP -> {
                mapColumns {
                    it.tilt(comparator)
                }.columnsToRows() // Convert columns to rows before making grid to ensure proper order
            }
        }

    return Grid.create(tilted)
}

/**
 * Tilts this row or column of [Node] based on the given [Comparator].
 * @param comparator Comparator to determine node order.
 * @return Tilted row or column as a string.
 */
private fun List<Node<String>>.tilt(comparator: Comparator<String>) =
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
 * [Comparator] of node values based on a given tilt [Direction].
 */
private class TiltComparator(private val direction: Direction) : Comparator<String> {
    override fun compare(
        node: String,
        other: String,
    ): Int {
        if (node == other) return 0
        return when (node) {
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
