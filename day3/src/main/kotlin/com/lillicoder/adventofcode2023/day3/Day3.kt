package com.lillicoder.adventofcode2023.day3

import com.lillicoder.adventofcode2023.grids.Grid
import com.lillicoder.adventofcode2023.grids.GridParser
import com.lillicoder.adventofcode2023.grids.Node

fun main() {
    val day3 = Day3()
    val schematic = GridParser().parseFile("input.txt").first().toSchematic()
    println("The sum of all part numbers in the schematic is ${day3.part1(schematic)}.")
    println("The sum of all gear ratios in the schematic is ${day3.part2(schematic)}.")
}

class Day3 {
    fun part1(schematic: Schematic) =
        schematic.numbers.sumOf { number ->
            when (schematic.isAnyNodeNeighborToNonPeriodNonNumericSymbol(number)) {
                true -> number.join()
                false -> 0
            }
        }

    fun part2(schematic: Schematic) = schematic.gears().sumOf { it.ratio }
}

/**
 * Converts this grid to an equivalent [Schematic].
 * @return Schematic.
 */
internal fun Grid<String>.toSchematic(): Schematic {
    val numbers = mutableListOf<List<Node<String>>>()
    val buffer = mutableListOf<Node<String>>()
    forEach { node ->
        when (node.value.toIntOrNull() != null) {
            true -> buffer.add(node) // Found a number, accumulate
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
 * Joins the values of this list of [Node] into a single number.
 * @return Joined digits.
 */
private fun List<Node<String>>.join() = joinToString("") { it.value }.toInt()

/**
 * Represents a gear in an engine [Schematic].
 * @param node Gear [Node].
 * @param neighbors Part numbers neighboring this gear in a schematic.
 * @param ratio Gear ratio.
 */
data class Gear(
    val node: Node<String>,
    val neighbors: List<Int>,
    val ratio: Int = neighbors.reduce { accumulator, element -> accumulator * element },
)

/**
 * Represents an engine schematic.
 * @param grid Grid of all [Node] in this schematic.
 * @param numbers List of all number node sets in this schematic.
 */
data class Schematic(
    val grid: Grid<String>,
    val numbers: List<List<Node<String>>>,
) {
    /**
     * Gets the list of [Gear] in this [Schematic].
     * @return Gears.
     */
    fun gears(): List<Gear> {
        // Since gears depend on all neighbor nodes, I can't preprocess this in the parser unless I do
        // a second loop through all the nodes; since there's no gain efficiency-wise, and I'm not reusing this
        // code for another purpose, I'm just going to walk the data structure here
        val ratios = mutableListOf<Gear>()

        grid.forEach { node ->
            if (node.value == "*") {
                // Potential gear, check conditions:
                // * At least two neighbors are numeric
                // * Neighbors belong to exactly two distinct part numbers
                val neighbors = grid.neighbors(node).filter { it.value.toIntOrNull() != null }
                if (neighbors.size > 1) {
                    val partNumbers =
                        neighbors.mapNotNull { neighbor ->
                            numbers.find { it.contains(neighbor) }?.join()
                        }.toSet()

                    if (partNumbers.size == 2) {
                        ratios.add(Gear(node, partNumbers.toList()))
                    }
                }
            }
        }

        return ratios
    }

    /**
     * Determines if any [Node] neighboring one or more of the given nodes is a non-period,
     * non-numeric symbol.
     * @param nodes Nodes to check.
     * @return True if any of the given nodes is neighbor to a non-period, non-numeric symbol, false otherwise.
     */
    fun isAnyNodeNeighborToNonPeriodNonNumericSymbol(nodes: List<Node<String>>): Boolean {
        nodes.forEach { node ->
            val neighbors = grid.neighbors(node)
            if (
                neighbors.any {
                    it.value != "." && it.value.toIntOrNull() == null
                }
            ) {
                return true
            }
        }

        return false
    }
}
