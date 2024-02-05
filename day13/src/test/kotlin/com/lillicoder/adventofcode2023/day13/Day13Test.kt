package com.lillicoder.adventofcode2023.day13

import com.lillicoder.adventofcode2023.grids.GridParser
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day13Test {
    private val input =
        """#.##..##.
           |..#.##.#.
           |##......#
           |##......#
           |..#.##.#.
           |..##..##.
           |#.#.##.#.
           |
           |#...##..#
           |#....#..#
           |..##..###
           |#####.##.
           |#####.##.
           |..##..###
           |#....#..#
        """.trimMargin()
    private val grids = GridParser().parseGrids(input, "\n")
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
