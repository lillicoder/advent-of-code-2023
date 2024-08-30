package com.lillicoder.adventofcode2023.day11

import com.lillicoder.adventofcode2023.grids.Grid
import com.lillicoder.adventofcode2023.grids.GridParser
import com.lillicoder.adventofcode2023.grids.Node

fun main() {
    val day11 = Day11()
    val grid = GridParser().parseFile("input.txt").first()
    println("The shortest path for all pairs of galaxies is ${day11.part1(grid)}. [factor=2]")
    println("The shortest path for all pairs of galaxies is ${day11.part2(grid)}. [factor=1,000,000]")
}

class Day11 {
    fun part1(grid: Grid<String>) = grid.expandAndSum(2)

    fun part2(grid: Grid<String>) = grid.expandAndSum(1_000_000)

    /**
     * Expands this [Grid] by the given expansion factor, finds all galaxy pairs,
     * and then sums the distances.
     * @param factor Expansion factor.
     * @return Sum of galaxy pair distances after expansion.
     */
    private fun Grid<String>.expandAndSum(factor: Long): Long {
        val expanded = expand(factor)
        val pairs = expanded.galaxyPairs()
        return pairs.sumOf { expanded.distance(it.first, it.second) }
    }

    /**
     * Expands this [Grid] based on the given cosmic expansion factor.
     * @param factor Expansion factor.
     * @return Expanded grid.
     */
    private fun Grid<String>.expand(factor: Long): Grid<String> {
        // Actually inserting values into the grid for huge factors will bust the heap,
        // just update X, Y positions as though those things really existed
        val emptyRows = mutableListOf<Long>()
        forEachRowIndexed { index, row ->
            if (row.all { it.value == "." }) emptyRows.add(index.toLong())
        }

        val emptyColumns = mutableListOf<Long>()
        forEachColumnIndexed { index, column ->
            if (column.all { it.value == "." }) emptyColumns.add(index.toLong())
        }

        return mapNodes { node ->
            Node(
                node.x + emptyColumns.count { it < node.x } * (factor - 1),
                node.y + emptyRows.count { it < node.y } * (factor - 1),
                node.value,
            )
        }
    }

    /**
     * Gets the unique pairs of galaxies in this [Grid].
     * @return Unique pairs of galaxies.
     */
    private fun Grid<String>.galaxyPairs(): List<Pair<Node<String>, Node<String>>> {
        val galaxies = filter { it == "#" }
        val ids = galaxies.associateWith { galaxies.indexOf(it) }
        return galaxies.flatMap { galaxy ->
            (galaxies - galaxy).map {
                if (ids[galaxy]!! > ids[it]!!) Pair(it, galaxy) else Pair(galaxy, it)
            }
        }.distinct()
    }
}
