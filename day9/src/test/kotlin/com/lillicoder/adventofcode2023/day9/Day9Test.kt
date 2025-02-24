package com.lillicoder.adventofcode2023.day9

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Day9].
 */
internal class Day9Test {
    private val input =
        """
        0 3 6 9 12 15
        1 3 6 10 15 21
        10 13 16 21 30 45
        """.trimIndent().lines()
    private val day9 = Day9()

    @Test
    fun part1() {
        val expected = 114L
        val actual = day9.part1(input)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 2L
        val actual = day9.part2(input)
        assertEquals(expected, actual)
    }
}
