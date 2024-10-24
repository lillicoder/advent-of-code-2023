package com.lillicoder.adventofcode2023.day11

import com.lillicoder.adventofcode2023.graphs.Graph
import com.lillicoder.adventofcode2023.graphs.SquareLatticeGraph
import com.lillicoder.adventofcode2023.graphs.Vertex
import com.lillicoder.adventofcode2023.graphs.gridToGraph
import com.lillicoder.adventofcode2023.io.Resources
import com.lillicoder.adventofcode2023.math.to

fun main() {
    val day11 = Day11()
    val graph =
        Resources.text("input.txt")?.gridToGraph()
            ?: throw IllegalArgumentException("Could not read input from file.")
    println("The shortest path for all pairs of galaxies is ${day11.part1(graph)}. [factor=2]")
    println("The shortest path for all pairs of galaxies is ${day11.part2(graph)}. [factor=1,000,000]")
}

class Day11 {
    fun part1(graph: SquareLatticeGraph<String>) = graph.expandAndSum(2)

    fun part2(graph: SquareLatticeGraph<String>) = graph.expandAndSum(1_000_000)

    /**
     * Expands this [SquareLatticeGraph] by the given expansion factor, finds all galaxy pairs,
     * and then sums the distances.
     * @param factor Expansion factor.
     * @return Sum of galaxy pair distances after expansion.
     */
    private fun SquareLatticeGraph<String>.expandAndSum(factor: Long): Long {
        val expanded = expand(factor)
        val pairs = expanded.galaxyPairs()
        return pairs.sumOf {
            val first = expanded.coordinates(it.first) ?: return 0
            val second = expanded.coordinates(it.second) ?: return 0
            first.distance(second)
        }
    }

    /**
     * Expands this [SquareLatticeGraph] based on the given cosmic expansion factor.
     * @param factor Expansion factor.
     * @return Expanded graph.
     */
    private fun SquareLatticeGraph<String>.expand(factor: Long): SquareLatticeGraph<String> {
        // Actually inserting values into the graph for huge factors will bust the heap,
        // just update X, Y positions as though those things really existed
        val emptyRows = mutableListOf<Long>()
        rows().forEachIndexed { index, row ->
            if (row.all { it.value == "." }) emptyRows.add(index.toLong())
        }

        val emptyColumns = mutableListOf<Long>()
        columns().forEachIndexed { index, column ->
            if (column.all { it.value == "." }) emptyColumns.add(index.toLong())
        }

        val builder = SquareLatticeGraph.Builder<String>()
        forEach { vertex ->
            val coordinates = coordinates(vertex) ?: return@forEach
            val shifted =
                (
                    coordinates.x + emptyColumns.count { it < coordinates.x } * (factor - 1)
                ).to(
                    coordinates.y + emptyRows.count { it < coordinates.y } * (factor - 1),
                )
            builder.vertex(shifted, vertex.value)
        }

        return builder.build()
    }

    /**
     * Gets the unique pairs of galaxies in this [Graph].
     * @return Unique pairs of galaxies.
     */
    private fun Graph<String>.galaxyPairs(): List<Pair<Vertex<String>, Vertex<String>>> {
        val galaxies = filter { it.value == "#" }
        val ids = galaxies.associateWith { galaxies.indexOf(it) }
        return galaxies.flatMap { galaxy ->
            (galaxies - galaxy).map {
                if (ids[galaxy]!! > ids[it]!!) Pair(it, galaxy) else Pair(galaxy, it)
            }
        }.distinct()
    }
}
