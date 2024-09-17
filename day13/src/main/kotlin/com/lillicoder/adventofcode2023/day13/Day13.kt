package com.lillicoder.adventofcode2023.day13

import com.lillicoder.adventofcode2023.grids.Grid
import com.lillicoder.adventofcode2023.grids.Node

fun main() {
    val day13 = Day13()
    val grids = Grid.readAll("input.txt")
    println("Sum of reflections of the grids is ${day13.part1(grids)}.")
    println("Sum of reflections w/ smudges is ${day13.part2(grids)}.")
}

class Day13 {
    fun part1(grids: List<Grid<String>>) = grids.sumOf { it.countReflectedColumnsAndRows(false) }

    fun part2(grids: List<Grid<String>>) = grids.sumOf { it.countReflectedColumnsAndRows(true) }
}

/**
 * Gets the number of [Node] whose values differ at each position of the given lists.
 * @param first First list.
 * @param second Second list.
 * @return Count of mismatches.
 */
private fun countMismatches(
    first: List<Node<String>>,
    second: List<Node<String>>,
) = first.map { it.value }.zip(second.map { it.value }).count { it.first != it.second }

/**
 * Counts the number of columns and rows from a reflection point in this [Grid].
 * @param allowSmudge True to allow symmetry when a single symbol doesn't match for a row or column pair.
 * @return Count of column to the left of the vertical reflection point, if any, or
 * the count of rows above the vertical reflection point times 100, if any.
 */
private fun Grid<String>.countReflectedColumnsAndRows(allowSmudge: Boolean = false): Long {
    // Try to find the horizontal reflection (if any)
    val horizontalIndex = findHorizontalSymmetry(allowSmudge)
    if (horizontalIndex > -1) return (horizontalIndex + 1L) * 100L

    // Try to find the vertical reflection (if any)
    val verticalIndex = findVerticalSymmetry(allowSmudge)
    if (verticalIndex > -1) return verticalIndex + 1L

    return 0L
}

/**
 * Finds the row index of horizontal symmetry for this [Grid].
 * @param allowSmudge True if symmetry includes rows with a single symbol mismatch.
 * @return Row index or -1 if there was no point of symmetry found.
 */
private fun Grid<String>.findHorizontalSymmetry(allowSmudge: Boolean = false): Long {
    for (index in 0..<height - 1) {
        if (hasHorizontalSymmetry(index, allowSmudge)) return index.toLong()
    }

    return -1L
}

/**
 * Finds the column index of vertical symmetry for this [Grid].
 * @param allowSmudge True if symmetry includes columns with a single symbol mismatch.
 * @return Column index or -1 if there was no point of symmetry found.
 */
private fun Grid<String>.findVerticalSymmetry(allowSmudge: Boolean = false): Long {
    for (index in 0..<width - 1) {
        if (hasVerticalSymmetry(index, allowSmudge)) return index.toLong()
    }

    return -1L
}

/**
 * Checks this [Grid] for horizontal symmetry starting from the given row index.
 * @param index Starting row index.
 * @param allowSmudge True if symmetry includes rows with a single symbol mismatch.
 * @return True if grid is symmetrical, false otherwise.
 */
private fun Grid<String>.hasHorizontalSymmetry(
    index: Int,
    allowSmudge: Boolean = false,
): Boolean {
    var start = index
    var end = index + 1

    var mismatches = 0L
    while (start >= 0 && end < height) {
        val first = row(start)
        val second = row(end)
        mismatches += countMismatches(first, second)

        start--
        end++
    }

    return mismatches == if (allowSmudge) 1L else 0L
}

/**
 * Checks this [Grid] for vertical symmetry starting from the given column index.
 * @param index Starting column index.
 * @param allowSmudge True if symmetry includes columns with a single symbol mismatch.
 * @return True if grid is symmetrical, false otherwise.
 */
private fun Grid<String>.hasVerticalSymmetry(
    index: Int,
    allowSmudge: Boolean = false,
): Boolean {
    // Start and neighbor have identical content
    var start = index
    var end = index + 1

    var mismatches = 0L
    while (start >= 0 && end < width) {
        val first = column(start)
        val second = column(end)
        mismatches += countMismatches(first, second)

        start--
        end++
    }

    return mismatches == if (allowSmudge) 1L else 0L
}
