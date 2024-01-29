package com.lillicoder.adventofcode2023.day14

import com.lillicoder.adventofcode2023.grids.Grid
import com.lillicoder.adventofcode2023.grids.GridParser
import com.lillicoder.adventofcode2023.grids.Node

fun main() {
    // Part 1
    val grid = GridParser().parse("input.txt") { it }
    val load = load(grid, 1, mutableListOf(Tilter.Direction.NORTH))
    println("The total load for a single tilt to the north is $load.")

    // Part 2
    val cycledLoad = load(grid)
    println("The total load for a 1000000000 cycles is $cycledLoad.")
}

/**
 * Finds the load for the given [Grid].
 * @param grid Grid to evaluate.
 * @return Load.
 */
fun load(
    grid: Grid<String>,
    cycles: Int = 1000000000,
    order: List<Tilter.Direction> = mutableListOf(
        Tilter.Direction.NORTH,
        Tilter.Direction.WEST,
        Tilter.Direction.SOUTH,
        Tilter.Direction.EAST
    )
): Long {
    val cache = mutableMapOf<String, Int>()
    val tilter = Tilter()
    val calculator = LoadCalculator()

    var tilted = grid
    repeat(cycles) { cycle ->
        val key = tilted.toString()
        if (key in cache) {
            val distance = cycle - cache[key]!!
            val remaining = (cycles - cycle) % distance
            repeat(remaining) { tilted = tilter.tiltSequence(tilted, order) }

            return calculator.load(tilted)
        }

        cache[key] = cycle
        tilted = tilter.tiltSequence(tilted, order)
    }

    return calculator.load(tilted)
}

class LoadCalculator {

    /**
     * Calculates the load for the given [Grid].
     * @param grid Grid to evaluate.
     * @return Load.
     */
    fun load(grid: Grid<String>): Long {
        var load = 0L
        grid.forEachRowIndexed{ index, row ->
            val spheres = row.count { it.value == "O" }
            load += spheres * (grid.height - index)
        }

        return load
    }
}

class Tilter {

    enum class Direction {
        EAST,
        NORTH,
        SOUTH,
        WEST
    }

    /**
     * Tilts the given [Grid] with the given sequence of [Direction].
     * @param grid Grid to tilt.
     * @param directions Directions to tilt.
     * @return Tilted grid.
     */
    fun tiltSequence(grid: Grid<String>, directions: List<Direction>): Grid<String> {
        var tilted = grid
        directions.forEach { tilted = tilt(tilted, it) }

        return tilted
    }

    /**
     * Tilts the given [Grid] in the given [Direction].
     * @param grid Grid to tilt.
     * @param direction Direction to tilt.
     * @return Tilted grid..
     */
    private fun tilt(grid: Grid<String>, direction: Direction): Grid<String> {
        return when (direction) {
            Direction.EAST, Direction.WEST -> tiltRows(grid, direction)
            Direction.NORTH, Direction.SOUTH -> tiltColumns(grid, direction)
        }
    }

    /**
     * Tilts the given [Grid]'s columns in the given [Direction].
     * @param grid Grid to tilt.
     * @param direction Direction to tilt.
     * @return Tilted grid.
     */
    private fun tiltColumns(grid: Grid<String>, direction: Direction): Grid<String> {
        val tilted = mutableListOf<String>()

        grid.forEachColumn { column ->
            // Split column by cubes
            val raw = column.joinToString("") { it.value }.split("#")

            val processed = mutableListOf<String>()
            raw.forEach { chunk ->
                // Sort this chunk to roll the spheres
                val sorted = chunk.split("").filter { it.isNotEmpty() }.sortedWith(
                    Comparator { node, other ->
                        if (node == other) { return@Comparator 0 }
                        return@Comparator when (node) {
                            "O" -> if (direction == Direction.NORTH) -1 else 1
                            "." -> if (direction == Direction.NORTH) 1 else -1
                            else -> 0
                        }
                    }
                )

                // Save the chunk
                processed.add(sorted.joinToString(""))
            }

            // Column fully processed, reconstitute the cubes
            tilted.add(processed.joinToString(separator = "#"))
        }

        // Columns repopulated, convert to grid
        val rows = MutableList<MutableList<Node<String>>>(tilted.size) { mutableListOf() }
        tilted.forEachIndexed { x, column ->
            column.split("").filter { it.isNotEmpty() }.forEachIndexed { y, value ->
                rows[y].add(Node(x.toLong(), y.toLong(), value))
            }
        }

        return Grid(rows)
    }

    /**
     * Tilts the given [Grid]'s rows in the given [Direction].
     * @param grid Grid to tilt.
     * @param direction Direction to tilt.
     * @return Tilted grid.
     */
    private fun tiltRows(grid: Grid<String>, direction: Direction): Grid<String> {
        val tilted = mutableListOf<String>()

        grid.forEachRow { row ->
            // Split row by cubes
            val raw = row.joinToString("") { it.value }.split("#")

            val processed = mutableListOf<String>()
            raw.forEach { chunk ->
                // Sort this chunk to roll the spheres
                val sorted = chunk.split("").filter { it.isNotEmpty() }.sortedWith(
                    Comparator { node, other ->
                        if (node == other) { return@Comparator 0 }
                        return@Comparator when (node) {
                            "O" -> if (direction == Direction.WEST) -1 else 1
                            "." -> if (direction == Direction.WEST) 1 else -1
                            else -> 0
                        }
                    }
                )

                // Save the chunk
                processed.add(sorted.joinToString(""))
            }

            // Column fully processed, reconstitute the cubes
            tilted.add(processed.joinToString(separator = "#"))
        }

        // Rows repopulated, convert to grid
        val rows = MutableList<MutableList<Node<String>>>(tilted.size) { mutableListOf() }
        tilted.forEachIndexed { y, row ->
            row.split("").filter { it.isNotEmpty() }.forEachIndexed { x, value ->
                rows[y].add(Node(x.toLong(), y.toLong(), value))
            }
        }

        return Grid(rows)
    }
}
