package com.lillicoder.adventofcode2023.day13

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Day13].
 */
internal class Day13Test {
    private val input =
        """
        #.##..##.
        ..#.##.#.
        ##......#
        ##......#
        ..#.##.#.
        ..##..##.
        #.#.##.#.
        
        #...##..#
        #....#..#
        ..##..###
        #####.##.
        #####.##.
        ..##..###
        #....#..# 
        """.trimIndent()
    private val day13 = Day13()

    @Test
    fun part1() {
        val expected = 405L
        val actual = day13.part1(input)

        "".lineSequence()
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 400L
        val actual = day13.part2(input)
        assertEquals(expected, actual)
    }
}
