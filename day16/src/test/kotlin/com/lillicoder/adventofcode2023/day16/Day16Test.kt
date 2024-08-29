package com.lillicoder.adventofcode2023.day16

import com.lillicoder.adventofcode2023.grids.Grid
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Day 16].
 */
internal class Day16Test {
    private val input =
        """.|...\....
           >|.-.\.....
           >.....|-...
           >........|.
           >..........
           >.........\
           >..../.\\..
           >.-.-/..|..
           >.|....-|.\
           >..//.|....
        """.trimMargin(">")
    private val grid = Grid.create(input.lines())
    private val day16 = Day16()

    @Test
    fun part1() {
        val expected = 46L
        val actual = day16.part1(grid)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 51L
        val actual = day16.part2(grid)
        assertEquals(expected, actual)
    }
}
