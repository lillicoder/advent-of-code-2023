package com.lillicoder.adventofcode2023.day16

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Day16].
 */
internal class Day16Test {
    private val input =
        """
        .|...\....
        |.-.\.....
        .....|-...
        ........|.
        ..........
        .........\
        ..../.\\..
        .-.-/..|..
        .|....-|.\
        ..//.|....
        """.trimIndent()
    private val day16 = Day16()

    @Test
    fun part1() {
        val expected = 46L
        val actual = day16.part1(input)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 51L
        val actual = day16.part2(input)
        assertEquals(expected, actual)
    }
}
