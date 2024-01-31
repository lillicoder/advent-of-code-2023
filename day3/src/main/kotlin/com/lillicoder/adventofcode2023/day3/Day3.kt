package com.lillicoder.adventofcode2023.day3

import com.lillicoder.adventofcode2023.grids.Grid
import com.lillicoder.adventofcode2023.grids.GridParser
import com.lillicoder.adventofcode2023.grids.Node

fun main() {
    val day3 = Day3()
    val schematic = EngineSchematicParser().parse("input.txt")
    println("The sum of all part numbers in the schematic is ${day3.part1(schematic)}.")
    println("The sum of all gear ratios in the schematic is ${day3.part2(schematic)}.")
}

class Day3 {
    /**
     * Sums the valid part numbers from the given [Schematic].
     * @param schematic Schematic to evaluate.
     * @return Sum of valid part numbers.
     */
    fun part1(schematic: Schematic) =
        schematic.numbers.sumOf { number ->
            when (schematic.isAnyNodeNeighborToNonPeriodNonNumericSymbol(number)) {
                true -> {
                    number.toInt()
                }
                false -> 0
            }
        }

    fun part2(schematic: Schematic) = GearRatiosCalculator().sum(schematic)
}

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

/**
 * Converts this list of [Node] into a single number whose digits consist of each node's value.
 * @return Node values as a single number.
 */
fun List<Node<String>>.toInt() = joinToString("") { it.value }.toInt()

/**
 * Parses a raw engine schematic into a [Schematic].
 */
class EngineSchematicParser(
    private val gridParser: GridParser = GridParser(),
) {
    /**
     * Parses the file with the given filename and returns a [Schematic].
     * @param filename Name of the file to parse.
     * @return Engine schematic.
     */
    fun parse(filename: String) = parse(javaClass.classLoader.getResourceAsStream(filename)!!.reader().readLines())

    /**
     * Parses the given raw schematic to an equivalent [Schematic].
     * @params raw Raw input.
     * @return Engine schematic.
     */
    fun parse(raw: List<String>): Schematic {
        val grid = gridParser.parseGrid(raw.joinToString("\r\n")) { it }
        val numbers = mutableListOf<List<Node<String>>>()

        val numberBuffer = mutableListOf<Node<String>>()
        grid.forEach { node ->
            when (node.value.toIntOrNull() != null) {
                true -> numberBuffer.add(node) // Found a number, accumulate
                false -> {
                    if (numberBuffer.isNotEmpty()) {
                        // No longer on a number, flush accumulator
                        numbers.add(numberBuffer.toList())
                        numberBuffer.clear()
                    }
                }
            }
        }

        return Schematic(grid, numbers)
    }
}

/**
 * Calculator that sums the gear ratios of all gears from a [Schematic].
 */
class GearRatiosCalculator {
    /**
     * Sums the gear ratios of all gears in the given [Schematic].
     * @param schematic Schematic to evaluate.
     * @return Sum of gear ratios.
     */
    fun sum(schematic: Schematic) = findGears(schematic).sumOf { it.ratio }

    /**
     * Finds the list of [Gear] in the given [Schematic].
     * @param schematic Schematic to evaluate.
     * @return Gears.
     */
    private fun findGears(schematic: Schematic): List<Gear> {
        // Since gears depend on all neighbor nodes, I can't preprocess this in the parser unless I do
        // a second loop through all the nodes; since there's no gain efficiency-wise, and I'm not reusing this
        // code for another purpose, I'm just going to walk the data structure here
        val ratios = mutableListOf<Gear>()

        schematic.grid.forEach { node ->
            if (node.value == "*") {
                // Potential gear, check conditions:
                // * At least two neighbors are numeric
                // * Neighbors belong to exactly two distinct part numbers
                val neighbors = schematic.grid.neighbors(node).filter { it.value.toIntOrNull() != null }
                if (neighbors.size > 1) {
                    val partNumbers =
                        neighbors.mapNotNull { neighbor ->
                            schematic.numbers.find { it.contains(neighbor) }?.toInt()
                        }.toSet()

                    if (partNumbers.size == 2) {
                        ratios.add(Gear(node, partNumbers.toList()))
                    }
                }
            }
        }

        return ratios
    }
}
