package com.lillicoder.adventofcode2023.day5

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Day5].
 */
internal class Day5Test {
    private val input =
        """seeds: 79 14 55 13
           |
           |seed-to-soil map:
           |50 98 2
           |52 50 48
           |
           |soil-to-fertilizer map:
           |0 15 37
           |37 52 2
           |39 0 15
           |
           |fertilizer-to-water map:
           |49 53 8
           |0 11 42
           |42 0 7
           |57 7 4
           |
           |water-to-light map:
           |88 18 7
           |18 25 70
           |
           |light-to-temperature map:
           |45 77 23
           |81 45 19
           |68 64 13
           |
           |temperature-to-humidity map:
           |0 69 1
           |1 0 69
           |
           |humidity-to-location map:
           |60 56 37
           |56 93 4
        """.trimMargin()
    private val almanac = input.toAlmanac("\n")
    private val day5 = Day5()

    @Test
    fun part1() {
        val expected = 35L
        val actual = day5.part1(almanac)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 46L
        val actual = day5.part2(almanac)
        assertEquals(expected, actual)
    }
}
