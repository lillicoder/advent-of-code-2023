package com.lillicoder.adventofcode2023.day3

import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day3Test {
    private val input =
        """467..114..
           |...*......
           |..35..633.
           |......#...
           |617*......
           |.....+.58.
           |..592.....
           |......755.
           |...$.*....
           |.664.598..
        """.trimMargin()
    private val schematic = EngineSchematicParser().parse(input.lines())
    private val day3 = Day3()

    @Test
    fun part1() {
        val expected = 4361
        val actual = day3.part1(schematic)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 467835
        val actual = day3.part2(schematic)
        assertEquals(expected, actual)
    }
}
