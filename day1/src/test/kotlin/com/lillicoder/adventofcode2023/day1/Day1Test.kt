package com.lillicoder.adventofcode2023.day1

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for {Day1].
 */
internal class Day1Test {
    private val day1 = Day1()

    @Test
    fun part1() {
        val input =
            """1abc2
               |pqr3stu8vwx
               |a1b2c3d4e5f
               |treb7uchet
            """.trimMargin()
        val expected = 142L
        val actual = day1.part1(input.lines())
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val input =
            """two1nine
               |eightwothree
               |abcone2threexyz
               |xtwone3four
               |4nineeightseven2
               |zoneight234
               |7pqrstsixteen
            """
        val expected = 281L
        val actual = day1.part2(input.trimMargin().lines())
        assertEquals(expected, actual)
    }
}
