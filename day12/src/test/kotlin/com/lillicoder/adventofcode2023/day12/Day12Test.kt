package com.lillicoder.adventofcode2023.day12

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Day12].
 */
internal class Day12Test {
    private val input =
        """???.### 1,1,3
           |.??..??...?##. 1,1,3
           |?#?#?#?#?#?#?#? 1,3,1,6
           |????.#...#... 4,1,1
           |????.######..#####. 1,6,5
           |?###???????? 3,2,1
        """.trimMargin()
    private val springs = input.lines().toRows()
    private val day12 = Day12()

    @Test
    fun part1() {
        val expected = 21L
        val actual = day12.part1(springs)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 525152L
        val actual = day12.part2(springs)
        assertEquals(expected, actual)
    }
}
