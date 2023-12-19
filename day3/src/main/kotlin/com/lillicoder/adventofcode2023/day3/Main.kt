package com.lillicoder.adventofcode2023.day3

fun main() {
    val schematic = EngineSchematicParser().parseSchematic("input.txt")
    val partNumbersSum = SumPartNumbersCalculator().sumPartNumbers(schematic)
    println("The sum of all part numbers in the schematic is $partNumbersSum.")

    val gearRatioSum = SumGearRatiosCalculator().sumGearRatios(schematic)
    println("The sum of all gear ratios in the schematic is $gearRatioSum.")
}

/**
 * Represents a single node in an engine schematic.
 * @param x X-position.
 * @param y Y-position.
 * @param value Node value.
 */
data class Node(
    val x: Int,
    val y: Int,
    val value: String,
) {
    /**
     * Determines if the value of this node is numeric.
     * @return True if numeric, false otherwise.
     */
    fun isNumeric() = value.toIntOrNull() != null
}

/**
 * Converts the given list of [Node] to a single integer.
 */
fun List<Node>.toInt(): Int = joinToString("") { it.value }.toInt()

/**
 * Represents a gear in an engine [Schematic].
 * @param node Gear [Node].
 * @param adjacentParts Part numbers adjacent to this gear in a schematic.
 * @param ratio Gear ratio.
 */
data class Gear(
    val node: Node,
    val adjacentParts: List<Int>,
    val ratio: Int = adjacentParts.reduce { accumulator, element -> accumulator * element },
)

/**
 * Represents an engine schematic.
 * @param grid Grid of all [Node] in this schematic.
 * @param numbers List of all number node sets in this schematic.
 */
data class Schematic(
    val grid: List<List<Node>>,
    val numbers: List<List<Node>>,
) {
    /**
     * Returns the adjacent nodes for the given [Node].
     * @param node Node.
     */
    fun adjacent(node: Node): List<Node> {
        return listOfNotNull(
            grid.getOrNull(node.y - 1)?.getOrNull(node.x - 1),
            grid.getOrNull(node.y)?.getOrNull(node.x - 1),
            grid.getOrNull(node.y + 1)?.getOrNull(node.x - 1),
            grid.getOrNull(node.y - 1)?.getOrNull(node.x),
            grid.getOrNull(node.y + 1)?.getOrNull(node.x),
            grid.getOrNull(node.y - 1)?.getOrNull(node.x + 1),
            grid.getOrNull(node.y)?.getOrNull(node.x + 1),
            grid.getOrNull(node.y + 1)?.getOrNull(node.x + 1),
        )
    }

    /**
     * Determines if any [Node] adjacent to one or more of the given nodes is a non-period,
     * non-numeric symbol.
     * @param nodes Nodes to check.
     * @return True if any of the given nodes is adjacent to a non-period, non-numeric symbol, false otherwise.
     */
    fun isAnyNodeAdjacentToNonPeriodNonNumericSymbol(nodes: List<Node>): Boolean {
        nodes.forEach { node ->
            val adjacent = adjacent(node)
            if (
                adjacent.any {
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
 * Parses a raw engine schematic into a [Schematic].
 */
class EngineSchematicParser {
    /**
     * Parses the file with the given filename and returns a [Schematic].
     * @param filename Name of the file to parse.
     * @return Engine schematic.
     */
    fun parseSchematic(filename: String): Schematic {
        val grid = mutableListOf<List<Node>>()
        val numbers = mutableListOf<List<Node>>()

        val numberBuffer = mutableListOf<Node>()
        var row = 0 // Reader extensions don't provide an indexed forEachLine, just count manually
        javaClass.classLoader.getResourceAsStream(filename)!!.reader().forEachLine { line ->
            val columns = mutableListOf<Node>()

            // Split on "" is like Java 7 and has leading empty value, filter to drop that
            line.split("").filter { it.isNotEmpty() }.forEachIndexed { column, value ->
                // Save the node
                val node = Node(column, row, value)
                columns.add(node)

                // Process the node in the numbers list if numeric
                when (node.isNumeric()) {
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

            grid.add(columns)
            row += 1
        }

        return Schematic(grid, numbers)
    }
}

/**
 * Calculator that sums the valid part numbers from a [Schematic].
 */
class SumPartNumbersCalculator {
    /**
     * Sums the valid part numbers from the given [Schematic].
     * @param schematic Schematic to evaluate.
     * @return Sum of valid part numbers.
     */
    fun sumPartNumbers(schematic: Schematic): Int {
        var sum = 0

        schematic.numbers.forEach { number ->
            if (schematic.isAnyNodeAdjacentToNonPeriodNonNumericSymbol(number)) {
                sum += number.toInt()
            }
        }

        return sum
    }
}

/**
 * Calculator that sums the gear ratios of all gears from a [Schematic].
 */
class SumGearRatiosCalculator {
    /**
     * Sums the gear ratios of all gears in the given [Schematic].
     * @param schematic Schematic to evaluate.
     * @return Sum of gear ratios.
     */
    fun sumGearRatios(schematic: Schematic): Int {
        var sum = 0

        val gears: List<Gear> = findGears(schematic)
        gears.forEach { gear -> sum += gear.ratio }

        return sum
    }

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

        schematic.grid.forEach { row ->
            row.forEach { node ->
                if (node.value == "*") {
                    // Potential gear, check conditions:
                    // * At least two neighbors are numeric
                    // * Neighbors belong to exactly two distinct part numbers
                    val adjacent = schematic.adjacent(node)
                    val neighbors = adjacent.filter { it.isNumeric() }
                    if (neighbors.size > 1) {
                        val partNumbers = mutableSetOf<Int>() // Use set to avoid dupes from digits in the same number
                        neighbors.forEach { neighbor ->
                            val partNumber = schematic.numbers.find { it.contains(neighbor) }?.toInt()
                            if (partNumber != null) partNumbers.add(partNumber)
                        }

                        if (partNumbers.size == 2) {
                            ratios.add(Gear(node, partNumbers.toList()))
                        }
                    }
                }
            }
        }

        return ratios
    }
}
