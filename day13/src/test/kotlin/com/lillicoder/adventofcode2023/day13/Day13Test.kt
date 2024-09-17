package com.lillicoder.adventofcode2023.day13

import com.lillicoder.adventofcode2023.grids.Grid
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
    private val grids =
        listOf(
            Grid.create(input1.lines()),
            Grid.create(input2.lines()),
        )
    private val day13 = Day13()

    @Test
    fun part1() {
        val expected = 405L
        val actual = day13.part1(grids)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 400L
        val actual = day13.part2(grids)
        assertEquals(expected, actual)
    }
}
