package com.lillicoder.adventofcode2023.day8

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Day8].
 */
internal class Day8Test {
    private val input1 =
        """LLR
           |
           |AAA = (BBB, BBB)
           |BBB = (AAA, ZZZ)
           |ZZZ = (ZZZ, ZZZ)
        """.trimMargin()
    private val network1 = input1.toNetwork("\n")

    private val input2 =
        """LR
           |
           |11A = (11B, XXX)
           |11B = (XXX, 11Z)
           |11Z = (11B, XXX)
           |22A = (22B, XXX)
           |22B = (22C, 22C)
           |22C = (22Z, 22Z)
           |22Z = (22B, 22B)
           |XXX = (XXX, XXX)
        """.trimMargin()
    private val network2 = input2.toNetwork("\n")

    private val day8 = Day8()

    @Test
    fun part1() {
        val expected = 6L
        val actual = day8.part1(network1)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 6L
        val actual = day8.part2(network2)
        assertEquals(expected, actual)
    }
}
