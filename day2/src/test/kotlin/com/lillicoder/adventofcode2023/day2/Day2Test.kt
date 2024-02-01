package com.lillicoder.adventofcode2023.day2

import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day2Test {
    private val input =
        """Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
          |Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
          |Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
          |Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
          |Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
        """.trimMargin()
    private val games = GameParser().parse(input.lines())
    private val day2 = Day2()

    @Test
    fun part1() {
        val expected = 8
        val actual = day2.part1(games)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 2286
        val actual = day2.part2(games)
        assertEquals(expected, actual)
    }
}
