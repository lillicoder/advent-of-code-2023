package com.lillicoder.adventofcode2023.day11

import com.lillicoder.adventofcode2023.grids.Grid
import com.lillicoder.adventofcode2023.grids.GridParser
import com.lillicoder.adventofcode2023.grids.Node
import kotlin.math.exp

fun main() {
    val grid = GridParser().parse("input.txt") { it }

    // Part 1
    val expanded = expand(grid, 2)
    val pairs = galaxyPairs(expanded)
    val shortestPaths = pairs.map { expanded.distance(it.first, it.second) }
    val sum = shortestPaths.sum()
    println("The shortest path for all pairs of galaxies is $sum. [factor=2]")

    // Part 2
    val superExpanded = expand(grid, 1_000_000)
    val superPairs = galaxyPairs(superExpanded)
    val superShortestPaths = superPairs.map { superExpanded.distance(it.first, it.second) }
    val superSum = superShortestPaths.sum()
    println("The shortest path for all pairs of galaxies is $superSum. [factor=1000000]")
}

/**
 * Expands the given [Grid] based on the given cosmic expansion factor.
 * @param grid Grid to expand.
 * @param factor Expansion factor.
 * @return Expanded grid.
 */
fun expand(grid: Grid<String>, factor: Long): Grid<String> {
    // Actually inserting values into the grid for huge factors will bust the heap, just update X, Y positions
    // as though those things really existed
    val emptyRows = mutableListOf<Long>()
    grid.forEachRowIndexed { index, row ->
        if (row.all { it.value == "." }) emptyRows.add(index.toLong())
    }

    val emptyColumns = mutableListOf<Long>()
    grid.forEachColumnIndexed { index, column ->
        if (column.all { it.value == "." }) emptyColumns.add(index.toLong())
    }

    return grid.map { node ->
        Node(
            node.x + emptyColumns.count { it < node.x } * (factor - 1),
            node.y + emptyRows.count { it < node.y } * (factor - 1),
            node.value
        )
    }
}

/**
 * Gets the unique pairs of galaxies in the given grid.
 * @param grid Grid to search.
 * @return Unique pairs of galaxies.
 */
fun galaxyPairs(grid: Grid<String>): List<Pair<Node<String>, Node<String>>> {
    val galaxies = grid.filter { it == "#" }
    val ids = galaxies.associateWith { galaxies.indexOf(it) }
    return galaxies.flatMap { galaxy ->
        (galaxies - galaxy).map {
            if (ids[galaxy]!! > ids[it]!!) Pair(it, galaxy) else Pair(galaxy, it)
        }
    }.distinct()
}
