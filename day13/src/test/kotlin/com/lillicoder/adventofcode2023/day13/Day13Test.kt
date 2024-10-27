package com.lillicoder.adventofcode2023.day13

import com.lillicoder.adventofcode2023.graphs.gridToGraph
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Day13].
 */
internal class Day13Test {
    private val input1 =
        """#.##..##.
           |..#.##.#.
           |##......#
           |##......#
           |..#.##.#.
           |..##..##.
           |#.#.##.#.
        """.trimMargin()
    private val input2 =
        """#...##..#
           |#....#..#
           |..##..###
           |#####.##.
           |#####.##.
           |..##..###
           |#....#..# 
        """.trimMargin()
    private val graphs =
        listOf(
            input1.gridToGraph(),
            input2.gridToGraph(),
        )
    private val day13 = Day13()

    @Test
    fun part1() {
        val expected = 405L
        val actual = day13.part1(graphs)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 400L
        val actual = day13.part2(graphs)
        assertEquals(expected, actual)
    }
}
