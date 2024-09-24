package com.lillicoder.adventofcode2023.day3

import com.lillicoder.adventofcode2023.graphs.Graph
import com.lillicoder.adventofcode2023.graphs.Vertex
import com.lillicoder.adventofcode2023.graphs.gridToGraph
import com.lillicoder.adventofcode2023.io.Resources

fun main() {
    val day3 = Day3()
    val schematic =
        Resources.text("input.txt")?.lines()?.gridToGraph(allowDiagonals = true)?.toSchematic()
            ?: throw IllegalArgumentException("")
    println("The sum of all part numbers in the schematic is ${day3.part1(schematic)}.")
    println("The sum of all gear ratios in the schematic is ${day3.part2(schematic)}.")
}

class Day3 {
    fun part1(schematic: Schematic) =
        schematic.numbers.sumOf { number ->
            when (schematic.isAnyNeighborToNonPeriodNonNumericSymbol(number)) {
                true -> number.join()
                false -> 0
            }
        }

    fun part2(schematic: Schematic) = schematic.gears().sumOf { it.ratio }
}

/**
 * Converts this graph to an equivalent [Schematic].
 * @return Schematic.
 */
internal fun Graph<String>.toSchematic(): Schematic {
    val numbers = mutableListOf<List<Vertex<String>>>()
    val buffer = mutableListOf<Vertex<String>>()
    forEach { vertex ->
        when (vertex.value.toIntOrNull() != null) {
            true -> buffer.add(vertex) // Found a number, accumulate
            false -> {
                if (buffer.isNotEmpty()) {
                    // No longer on a number, flush accumulator
                    numbers.add(buffer.toList())
                    buffer.clear()
                }
            }
        }
    }

    return Schematic(this, numbers)
}

/**
 * Joins the values of this list of [Vertex] into a single number.
 * @return Joined digits.
 */
private fun List<Vertex<String>>.join() = joinToString("") { it.value }.toInt()

/**
 * Represents a gear in an engine [Schematic].
 * @param vertex Gear [Vertex].
 * @param neighbors Part numbers neighboring this gear in a schematic.
 * @param ratio Gear ratio.
 */
data class Gear(
    val vertex: Vertex<String>,
    val neighbors: List<Int>,
    val ratio: Int = neighbors.reduce { accumulator, element -> accumulator * element },
)

/**
 * Represents an engine schematic.
 * @param graph Graph of all vertices in this schematic.
 * @param numbers List of all number vertex sets in this schematic.
 */
data class Schematic(
    val graph: Graph<String>,
    val numbers: List<List<Vertex<String>>>,
) {
    /**
     * Gets the list of [Gear] in this [Schematic].
     * @return Gears.
     */
    fun gears(): List<Gear> {
        // Since gears depend on all neighbor vertices, I can't preprocess this in the parser unless I do
        // a second loop through all the vertices; since there's no gain efficiency-wise, and I'm not reusing this
        // code for another purpose, I'm just going to walk the data structure here
        val ratios = mutableListOf<Gear>()

        graph.forEach { vertex ->
            if (vertex.value == "*") {
                // Potential gear, check conditions:
                // * At least two neighbors are numeric
                // * Neighbors belong to exactly two distinct part numbers
                val neighbors = graph.neighbors(vertex).filter { it.value.toIntOrNull() != null }
                if (neighbors.size > 1) {
                    val partNumbers =
                        neighbors.mapNotNull { neighbor ->
                            numbers.find { it.contains(neighbor) }?.join()
                        }.toSet()

                    if (partNumbers.size == 2) {
                        ratios.add(Gear(vertex, partNumbers.toList()))
                    }
                }
            }
        }

        return ratios
    }

    /**
     * Determines if any vertex neighboring one or more of the given vertices is a non-period,
     * non-numeric symbol.
     * @param vertices Vertices to check.
     * @return True if any of the given vertices is neighbor to a non-period, non-numeric symbol, false otherwise.
     */
    fun isAnyNeighborToNonPeriodNonNumericSymbol(vertices: List<Vertex<String>>) =
        vertices.map {
            graph.neighbors(it)
        }.any { neighbors ->
            neighbors.any {
                it.value != "." && it.value.toIntOrNull() == null
            }
        }
}
