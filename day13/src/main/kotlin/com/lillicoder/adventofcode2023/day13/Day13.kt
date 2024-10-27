package com.lillicoder.adventofcode2023.day13

import com.lillicoder.adventofcode2023.graphs.SquareLatticeGraph
import com.lillicoder.adventofcode2023.graphs.Vertex
import com.lillicoder.adventofcode2023.graphs.gridsToGraph
import com.lillicoder.adventofcode2023.io.Resources

fun main() {
    val day13 = Day13()
    val graphs =
        Resources.text(
            "input.txt",
        )?.gridsToGraph() ?: throw IllegalArgumentException("Could not read input from file.")
    println("Sum of reflections of the graphs is ${day13.part1(graphs)}.")
    println("Sum of reflections w/ smudges is ${day13.part2(graphs)}.")
}

class Day13 {
    fun part1(graphs: List<SquareLatticeGraph<String>>) = graphs.sumOf { it.countReflectedColumnsAndRows(false) }

    fun part2(graphs: List<SquareLatticeGraph<String>>) = graphs.sumOf { it.countReflectedColumnsAndRows(true) }
}

/**
 * Gets the number of [Vertex] whose values differ at each position of the given lists.
 * @param first First list.
 * @param second Second list.
 * @return Count of mismatches.
 */
private fun countMismatches(
    first: List<Vertex<String>>,
    second: List<Vertex<String>>,
) = first.map { it.value }.zip(second.map { it.value }).count { it.first != it.second }

/**
 * Counts the number of columns and rows from a reflection point in this [SquareLatticeGraph].
 * @param allowSmudge True to allow symmetry when a single symbol doesn't match for a row or column pair.
 * @return Count of column to the left of the vertical reflection point, if any, or
 * the count of rows above the vertical reflection point times 100, if any.
 */
private fun SquareLatticeGraph<String>.countReflectedColumnsAndRows(allowSmudge: Boolean = false): Long {
    // Try to find the horizontal reflection (if any)
    val horizontalIndex = findHorizontalSymmetry(allowSmudge)
    if (horizontalIndex > -1) return (horizontalIndex + 1L) * 100L

    // Try to find the vertical reflection (if any)
    val verticalIndex = findVerticalSymmetry(allowSmudge)
    if (verticalIndex > -1) return verticalIndex + 1L

    return 0L
}

/**
 * Finds the row index of horizontal symmetry for this [SquareLatticeGraph].
 * @param allowSmudge True if symmetry includes rows with a single symbol mismatch.
 * @return Row index or -1 if there was no point of symmetry found.
 */
private fun SquareLatticeGraph<String>.findHorizontalSymmetry(allowSmudge: Boolean = false): Long {
    for (index in 0..<height - 1) {
        if (hasHorizontalSymmetry(index, allowSmudge)) return index.toLong()
    }

    return -1L
}

/**
 * Finds the column index of vertical symmetry for this [SquareLatticeGraph].
 * @param allowSmudge True if symmetry includes columns with a single symbol mismatch.
 * @return Column index or -1 if there was no point of symmetry found.
 */
private fun SquareLatticeGraph<String>.findVerticalSymmetry(allowSmudge: Boolean = false): Long {
    for (index in 0..<width - 1) {
        if (hasVerticalSymmetry(index, allowSmudge)) return index.toLong()
    }

    return -1L
}

/**
 * Checks this [SquareLatticeGraph] for horizontal symmetry starting from the given row index.
 * @param index Starting row index.
 * @param allowSmudge True if symmetry includes rows with a single symbol mismatch.
 * @return True if graph is symmetrical, false otherwise.
 */
private fun SquareLatticeGraph<String>.hasHorizontalSymmetry(
    index: Int,
    allowSmudge: Boolean = false,
): Boolean {
    var start = index
    var end = index + 1

    var mismatches = 0L
    while (start >= 0 && end < height) {
        val first = row(start) ?: emptyList()
        val second = row(end) ?: emptyList()
        mismatches += countMismatches(first, second)

        start--
        end++
    }

    return mismatches == if (allowSmudge) 1L else 0L
}

/**
 * Checks this [SquareLatticeGraph] for vertical symmetry starting from the given column index.
 * @param index Starting column index.
 * @param allowSmudge True if symmetry includes columns with a single symbol mismatch.
 * @return True if graph is symmetrical, false otherwise.
 */
private fun SquareLatticeGraph<String>.hasVerticalSymmetry(
    index: Int,
    allowSmudge: Boolean = false,
): Boolean {
    // Start and neighbor have identical content
    var start = index
    var end = index + 1

    var mismatches = 0L
    while (start >= 0 && end < width) {
        val first = column(start) ?: emptyList()
        val second = column(end) ?: emptyList()
        mismatches += countMismatches(first, second)

        start--
        end++
    }

    return mismatches == if (allowSmudge) 1L else 0L
}
