package com.lillicoder.adventofcode2023.day15

import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day15Test {
    private val input = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7".split(",")
    private val day15 = Day15()

    @Test
    fun part1() {
        val expected = 1320L
        val actual = day15.part1(input)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 145L
        val actual = day15.part2(input)
        assertEquals(expected, actual)
    }
}
