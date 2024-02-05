package com.lillicoder.adventofcode2023.day11

import com.lillicoder.adventofcode2023.grids.Grid
import com.lillicoder.adventofcode2023.grids.GridParser
import com.lillicoder.adventofcode2023.grids.Node

fun main() {
    val day11 = Day11()
    val grid = GridParser().parse("input.txt")
    println("The shortest path for all pairs of galaxies is ${day11.part1(grid)}. [factor=2]")
    println("The shortest path for all pairs of galaxies is ${day11.part2(grid)}. [factor=1,000,000]")
}

class Day11 {
    fun part1(grid: Grid<String>) = expandAndSum(grid, 2)

    fun part2(grid: Grid<String>) = expandAndSum(grid, 1_000_000)

    /**
     * Expands the given [Grid] by the given expansion factor, finds all galaxy pairs, and then sums
     * the distances.
     * @param grid Grid to expand.
     * @param factor Expansion factor.
     * @return Sum of galaxy pair distances after expansion.
     */
    private fun expandAndSum(
        grid: Grid<String>,
        factor: Long,
    ): Long {
        val expanded = expand(grid, factor)
        val pairs = galaxyPairs(expanded)
        val shortestPaths = pairs.map { expanded.distance(it.first, it.second) }
        return shortestPaths.sum()
    }

    /**
     * Expands the given [Grid] based on the given cosmic expansion factor.
     * @param grid Grid to expand.
     * @param factor Expansion factor.
     * @return Expanded grid.
     */
    private fun expand(
        grid: Grid<String>,
        factor: Long,
    ): Grid<String> {
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
                node.value,
            )
        }
    }

    /**
     * Gets the unique pairs of galaxies in the given grid.
     * @param grid Grid to search.
     * @return Unique pairs of galaxies.
     */
    private fun galaxyPairs(grid: Grid<String>): List<Pair<Node<String>, Node<String>>> {
        val galaxies = grid.filter { it == "#" }
        val ids = galaxies.associateWith { galaxies.indexOf(it) }
        return galaxies.flatMap { galaxy ->
            (galaxies - galaxy).map {
                if (ids[galaxy]!! > ids[it]!!) Pair(it, galaxy) else Pair(galaxy, it)
            }
        }.distinct()
    }
}
