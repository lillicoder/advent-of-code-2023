package com.lillicoder.adventofcode2023.day11

import com.lillicoder.adventofcode2023.graphs.gridToGraph
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Day11].
 */
internal class Day11Test {
    private val input =
        """...#......
           |.......#..
           |#.........
           |..........
           |......#...
           |.#........
           |.........#
           |..........
           |.......#..
           |#...#.....
        """.trimMargin()
    private val graph = input.gridToGraph()
    private val day11 = Day11()

    @Test
    fun part1() {
        val expected = 374L
        val actual = day11.part1(graph)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 82000210L
        val actual = day11.part2(graph)
        assertEquals(expected, actual)
    }
}
