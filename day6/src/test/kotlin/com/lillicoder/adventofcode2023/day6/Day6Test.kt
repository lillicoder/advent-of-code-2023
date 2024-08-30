package com.lillicoder.adventofcode2023.day6

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Day6].
 */
internal class Day6Test {
    private val input =
        """Time:      7  15   30
           |Distance:  9  40  200
        """.trimMargin()
    private val races = input.toRaces("\n")
    private val day6 = Day6()

    @Test
    fun part1() {
        val expected = 288L
        val actual = day6.part1(races)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 71503L
        val actual = day6.part2(races)
        assertEquals(expected, actual)
    }
}
