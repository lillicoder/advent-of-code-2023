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

package com.lillicoder.adventofcode2023.day11

import com.lillicoder.adventofcode.kotlin.grids.Grid
import com.lillicoder.adventofcode.kotlin.grids.toGrid
import com.lillicoder.adventofcode.kotlin.io.Resources
import com.lillicoder.adventofcode.kotlin.math.Coordinates
import com.lillicoder.adventofcode.kotlin.math.Vertex
import com.lillicoder.adventofcode.kotlin.math.to

fun main() {
    val day11 = Day11()
    val input =
        Resources.text(
            "input.txt",
        ) ?: throw IllegalArgumentException("Could not read input from file.")
    println("[Part 1] The shortest path for all pairs of galaxies is ${day11.part1(input)}. [factor=2]")
    println("[Part 2] The shortest path for all pairs of galaxies is ${day11.part2(input)}. [factor=1,000,000]")
}

class Day11 {
    fun part1(input: String) = input.toGrid().expandAndSum(2)

    fun part2(input: String) = input.toGrid().expandAndSum(1_000_000)

    /**
     * Expands this [Grid] by the given expansion factor, finds all galaxy pairs,
     * and then sums the distances.
     * @param factor Expansion factor.
     * @return Sum of galaxy pair distances after expansion.
     */
    private fun Grid<String>.expandAndSum(factor: Long): Long {
        val expanded = expand(factor)
        val pairs = expanded.galaxyPairs()
        return pairs.sumOf {
            val first = expanded[it.first] ?: return 0
            val second = expanded[it.second] ?: return 0
            first.distance(second)
        }
    }

    /**
     * Expands this [Grid] based on the given cosmic expansion factor.
     * @param factor Expansion factor.
     * @return New coordinates of existing vertices keyed by vertex.
     */
    private fun Grid<String>.expand(factor: Long): Map<Vertex<String>, Coordinates> {
        // Actually inserting values into the grid for huge factors will bust the heap,
        // just update X, Y positions as though those things really existed
        val emptyRows = mutableListOf<Long>()
        rows().forEachIndexed { index, row ->
            if (row.all { it.value == "." }) emptyRows.add(index.toLong())
        }

        val emptyColumns = mutableListOf<Long>()
        columns().forEachIndexed { index, column ->
            if (column.all { it.value == "." }) emptyColumns.add(index.toLong())
        }

        return associateWith { vertex ->
            val coordinates = coordinates(vertex)!!
            (
                coordinates.x + emptyColumns.count { it < coordinates.x } * (factor - 1)
            ).to(
                coordinates.y + emptyRows.count { it < coordinates.y } * (factor - 1),
            )
        }
    }

    /**
     * Gets the unique pairs of galaxies in this map of [Coordinates] by [Vertex].
     * @return Unique pairs of galaxies.
     */
    private fun Map<Vertex<String>, Coordinates>.galaxyPairs(): List<Pair<Vertex<String>, Vertex<String>>> {
        val galaxies = keys.filter { it.value == "#" }
        val ids = galaxies.associateWith { galaxies.indexOf(it) }
        return galaxies.flatMap { galaxy ->
            (galaxies - galaxy).map {
                if (ids[galaxy]!! > ids[it]!!) Pair(it, galaxy) else Pair(galaxy, it)
            }
        }.distinct()
    }
}
