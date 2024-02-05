package com.lillicoder.adventofcode2023.day13

import com.lillicoder.adventofcode2023.grids.Grid
import com.lillicoder.adventofcode2023.grids.GridParser
import com.lillicoder.adventofcode2023.grids.Node

fun main() {
    val day13 = Day13()
    val grids = GridParser().parseFile("input.txt")
    println("Sum of reflections of the grids is ${day13.part1(grids)}.")
    println("Sum of reflections w/ smudges is ${day13.part2(grids)}.")
}

class Day13 {
    fun part1(grids: List<Grid<String>>) = ReflectionCalculator().sumReflections(grids)

    fun part2(grids: List<Grid<String>>) = ReflectionCalculator().sumReflectionsWithSmudges(grids)
}

class ReflectionCalculator {
    /**
     * Sums the count of reflected columns or rows for each of the given [Grid].
     * @param grids Grids to evaluate.
     * @return Sum of reflections.
     */
    fun sumReflections(grids: List<Grid<String>>) = grids.sumOf { countReflectedColumnsAndRows(it, false) }

    /**
     * Sums the count of reflected columns or rows for each of the given [Grid] allowing a single incorrect
     * symbol per grid.
     * @param grids Grids to evaluate.
     * @return Sum of reflections.
     */
    fun sumReflectionsWithSmudges(grids: List<Grid<String>>) = grids.sumOf { countReflectedColumnsAndRows(it, true) }

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
     * Counts the number of columns and rows from a reflection point in given [Grid].
     * @param grid Grid to evaluate.
     * @param allowSmudge True to allow grids to have symmetry when a single symbol doesn't match an analyzed row
     * or column pair.
     * @return Count of column to the left of the vertical reflection point, if any, or
     * the count of rows above the vertical reflection point times 100, if any.
     */
    private fun countReflectedColumnsAndRows(
        grid: Grid<String>,
        allowSmudge: Boolean = false,
    ): Long {
        // Try to find the horizontal reflection (if any)
        val horizontalIndex = findHorizontalSymmetry(grid, allowSmudge)
        if (horizontalIndex > -1) return (horizontalIndex + 1L) * 100L

        // Try to find the vertical reflection (if any)
        val verticalIndex = findVerticalSymmetry(grid, allowSmudge)
        if (verticalIndex > -1) return verticalIndex + 1L

        return 0L
    }

    /**
     * Finds the row index of horizontal symmetry for the given [Grid].
     * @param grid Grid to evaluate.
     * @param allowSmudge True if symmetry includes rows with a single symbol mismatch.
     * @return Row index or -1 if there was no point of symmetry found.
     */
    private fun findHorizontalSymmetry(
        grid: Grid<String>,
        allowSmudge: Boolean = false,
    ): Long {
        for (index in 0..<grid.height - 1) {
            if (hasHorizontalSymmetry(grid, index, allowSmudge)) return index.toLong()
        }

        return -1L
    }

    /**
     * Finds the column index of vertical symmetry for the given [Grid].
     * @param grid Grid to evaluate.
     * @param allowSmudge True if symmetry includes columns with a single symbol mismatch.
     * @return Column index or -1 if there was no point of symmetry found.
     */
    private fun findVerticalSymmetry(
        grid: Grid<String>,
        allowSmudge: Boolean = false,
    ): Long {
        for (index in 0..<grid.width - 1) {
            if (hasVerticalSymmetry(grid, index, allowSmudge)) return index.toLong()
        }

        return -1L
    }

    /**
     * Checks the given [Grid] for horizontal symmetry starting from the given row index.
     * @param grid Grid to check.
     * @param index Starting row index.
     * @param allowSmudge True if symmetry includes rows with a single symbol mismatch.
     * @return True if grid is symmetrical, false otherwise.
     */
    private fun hasHorizontalSymmetry(
        grid: Grid<String>,
        index: Int,
        allowSmudge: Boolean = false,
    ): Boolean {
        var start = index
        var end = index + 1

        var mismatches = 0L
        while (start >= 0 && end < grid.height) {
            val first = grid.row(start)
            val second = grid.row(end)
            mismatches += countMismatches(first, second)

            start--
            end++
        }

        return mismatches == if (allowSmudge) 1L else 0L
    }

    /**
     * Checks the given [Grid] for vertical symmetry starting from the given column index.
     * @param grid Grid to check.
     * @param index Starting column index.
     * @param allowSmudge True if symmetry includes columns with a single symbol mismatch.
     * @return True if grid is symmetrical, false otherwise.
     */
    private fun hasVerticalSymmetry(
        grid: Grid<String>,
        index: Int,
        allowSmudge: Boolean = false,
    ): Boolean {
        // Start and neighbor have identical content
        var start = index
        var end = index + 1

        var mismatches = 0L
        while (start >= 0 && end < grid.width) {
            val first = grid.column(start)
            val second = grid.column(end)
            mismatches += countMismatches(first, second)

            start--
            end++
        }

        return mismatches == if (allowSmudge) 1L else 0L
    }
}
