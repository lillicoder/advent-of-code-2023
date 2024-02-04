package com.lillicoder.adventofcode2023.day9

import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day9Test {

    private val input =
        """0 3 6 9 12 15
           |1 3 6 10 15 21
           |10 13 16 21 30 45
        """.trimMargin()
    private val readings = ReadingsParser().parse(input.lines())
    private val day9 = Day9()

    @Test
    fun part1() {
        val expected = 114L
        val actual = day9.part1(readings)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 2L
        val actual = day9.part2(readings)
        assertEquals(expected, actual)
    }
}