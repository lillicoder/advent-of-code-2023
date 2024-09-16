package com.lillicoder.adventofcode2023.day12

import com.lillicoder.adventofcode2023.io.Resources
import com.lillicoder.adventofcode2023.io.splitMap
import kotlin.math.min

fun main() {
    val day12 = Day12()
    val springs =
        Resources.lines(
            "input.txt",
        )?.toRows() ?: throw IllegalArgumentException("Could not read input from file.")
    println("The number of valid arrangements for factor 1 is ${day12.part1(springs)}.")
    println("The number of valid arrangements for factor 5 is ${day12.part2(springs)}.")
}

class Day12 {
    fun part1(springs: List<Row>) = mutableMapOf<String, Long>().arrangements(springs)

    fun part2(springs: List<Row>) = mutableMapOf<String, Long>().arrangements(springs, 5)
}

/**
 * Represents a row of springs and their associated pattern of contiguous broken springs.
 */
data class Row(
    val springs: String,
    val pattern: List<Int>,
) {
    private val damaged = '#'
    private val operational = '.'
    private val unknown = '?'

    /**
     * Gets the number of possible arrangements for this row.
     * @param selector Function that can get arrangements for subsets of this row's springs.
     * @return Arrangements.
     */
    fun arrangements(selector: (Row) -> Long): Long {
        // Case 1 - no more patterns to check; spring must not have anything damaged as we've exhausted
        // available damaged gear blocks
        if (pattern.isEmpty()) return if (springs.contains(damaged)) 0L else 1L

        // Case 2 - not enough remaining springs; we need at least 1 space per pattern plus 1 in between each pattern
        val spaceNeeded = pattern.sum() + pattern.size - 1
        if (springs.length < spaceNeeded) return 0

        // Case 3 - starts with operational spring; we only want to evaluate unknown or damaged springs
        // just advance ahead to the next substring
        if (springs.startsWith(operational)) return selector(Row(springs.drop(1), pattern))

        // Get the next block from the pattern
        val block = pattern.first()

        // Determine if we have any operational springs in this block
        val areAllNonOperational = springs.substring(0, block).contains(operational).not()

        // End of block is either next character or end of string, whichever comes first
        val end = min(block + 1, springs.length)

        var count = 0L

        // We have more springs available and we don't start
        val a = springs.length > block && springs[block] != damaged
        val b = springs.length <= block

        // Block is all # or ? AND is either the end of the springs OR is there are more spring blocks to come;
        // if so, drop this block and work the sub-problem
        if (areAllNonOperational && (a || b)) count += selector(Row(springs.drop(end), pattern.drop(1)))

        // Block has an unknown string, work the sub-problem by dropping this spring
        if (springs.startsWith(unknown)) count += selector(Row(springs.drop(1), pattern))

        return count
    }

    /**
     * Expands this row of springs based on the given factor.
     * @param factor Fold factor.
     * @return Expanded row.
     */
    fun expand(factor: Int): Row {
        var expandedSprings = springs
        val expandedPattern = pattern.toMutableList()

        for (index in 1..<factor) {
            expandedSprings += "$unknown$springs"
            expandedPattern += pattern
        }

        return Row(expandedSprings, expandedPattern)
    }
}

/**
 * Converts these strings to an equivalent list of [Row].
 * @return Rows.
 */
internal fun List<String>.toRows() = map { it.toRow() }

/**
 * Gets all valid arrangements of each of the given [Row] and sums them.
 * This map will be used as a cache during evaluation.
 * @param rows Rows to evaluate.
 * @param factor Fold factor. Defaults to 1.
 * @return Sum of valid arrangements.
 */
private fun MutableMap<String, Long>.arrangements(
    rows: List<Row>,
    factor: Int = 1,
) = rows.sumOf {
    val expanded = it.expand(factor)
    arrangements(expanded)
}

/**
 * Gets the number of valid arrangements for the given [Row].
 * This map will be used as a cache during evaluation.
 * @param row Row.
 * @return Number of valid arrangements.
 */
private fun MutableMap<String, Long>.arrangements(row: Row): Long {
    val key = "${row.springs}|${row.pattern.joinToString(",")}"
    return get(key) ?: row.arrangements { arrangements(it) }.apply { put(key, this) }
}

/**
 * Converts this string to an equivalent [Row].
 * @return Row.
 */
private fun String.toRow(): Row {
    val parts = split(" ")
    val springs = parts[0]
    val pattern = parts[1].splitMap(",") { it.toInt() }
    return Row(springs, pattern)
}
