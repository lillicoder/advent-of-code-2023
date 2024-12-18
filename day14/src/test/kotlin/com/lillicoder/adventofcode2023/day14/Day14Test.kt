package com.lillicoder.adventofcode2023.day14

import com.lillicoder.adventofcode2023.graphs.gridToGraph
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Day14].
 */
internal class Day14Test {
    private val input =
        """O....#....
           |O.OO#....#
           |.....##...
           |OO.#O....O
           |.O.....O#.
           |O.#..O.#.#
           |..O..#O..O
           |.......O..
           |#....###..
           |#OO..#....
        """.trimMargin()
    private val graph = input.gridToGraph()
    private val day14 = Day14()

    @Test
    fun part1() {
        val expected = 136L
        val actual = day14.part1(graph)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 64L
        val actual = day14.part2(graph)
        assertEquals(expected, actual)
    }
}
