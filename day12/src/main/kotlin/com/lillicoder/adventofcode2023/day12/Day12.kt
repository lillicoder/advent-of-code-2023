package com.lillicoder.adventofcode2023.day12

import kotlin.math.min

fun main() {
    val day12 = Day12()
    val springs = SpringsParser().parse("input.txt")
    println("The number of valid arrangements for factor 1 is ${day12.part1(springs)}.")
    println("The number of valid arrangements for factor 5 is ${day12.part2(springs)}.")
}

class Day12 {
    fun part1(springs: List<Row>) = SpringPermutationCalculator().sumArrangements(springs)

    fun part2(springs: List<Row>) = SpringPermutationCalculator().sumArrangements(springs, 5)
}

/**
 * Represents a row of springs and their associated pattern of contiguous broken springs.
 */
class Row(
    val springs: String,
    val pattern: List<Int>,
)

class SpringPermutationCalculator {
    private val damaged = '#'
    private val operational = '.'
    private val unknown = '?'

    private val cache = mutableMapOf<String, Long>()

    /**
     * Finds all valid arrangements of each of the given [Row] and sums them.
     * @param rows Rows to evaluate.
     * @param factor Fold factor. Defaults to 1.
     * @return Sum of valid arrangements.
     */
    fun sumArrangements(
        rows: List<Row>,
        factor: Int = 1,
    ) = rows.sumOf {
        var expandedSprings = it.springs
        val expandedPattern = it.pattern.toMutableList()

        for (index in 1..<factor) {
            expandedSprings += "$unknown${it.springs}"
            expandedPattern += it.pattern
        }

        arrangements(expandedSprings, expandedPattern)
    }

    /**
     * Gets the number of valid arrangements for the given springs and their broken springs pattern.
     * @param springs Springs to evaluate.
     * @param pattern Pattern of contiguous blocks of broken springs.
     * @return Number of valid arrangements.
     */
    private fun arrangements(
        springs: String,
        pattern: List<Int>,
    ): Long {
        val key = "$springs|${pattern.joinToString(",")}"
        return cache[key] ?: computeArrangements(springs, pattern).apply { cache[key] = this }
    }

    private fun computeArrangements(
        springs: String,
        pattern: List<Int>,
    ): Long {
        // Case 1 - no more patterns to check; spring must not have anything damaged as we've exhausted
        // available damaged gear blocks
        if (pattern.isEmpty()) return if (springs.contains(damaged)) 0L else 1L

        // Case 2 - not enough remaining springs; we need at least 1 space per pattern plus 1 in between each pattern
        val spaceNeeded = pattern.sum() + pattern.size - 1
        if (springs.length < spaceNeeded) return 0

        // Case 3 - starts with operational spring; we only want to evaluate unknown or damaged springs
        // just advance ahead to the next substring
        if (springs.startsWith(operational)) return arrangements(springs.drop(1), pattern)

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
        if (areAllNonOperational && (a || b)) count += arrangements(springs.drop(end), pattern.drop(1))

        // Block has an unknown string, work the sub-problem by dropping this spring
        if (springs.startsWith(unknown)) count += arrangements(springs.drop(1), pattern)

        return count
    }
}

class SpringsParser {
    fun parse(raw: List<String>) =
        raw.map { line ->
            val parts = line.split(" ")
            val springs = parts[0]
            val pattern = parts[1].split(",").map { it.toInt() }
            Row(springs, pattern)
        }

    /**
     * Parses the file with the given filename to a list of [Row].
     * @param filename Filename.
     * @return Parsed spring rows.
     */
    fun parse(filename: String) = parse(javaClass.classLoader.getResourceAsStream(filename)!!.reader().readLines())
}
