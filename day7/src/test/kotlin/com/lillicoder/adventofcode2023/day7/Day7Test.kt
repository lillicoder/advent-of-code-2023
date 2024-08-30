package com.lillicoder.adventofcode2023.day7

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Day7].
 */
internal class Day7Test {
    private val input =
        """32T3K 765
           |T55J5 684
           |KK677 28
           |KTJJT 220
           |QQQJA 483
        """.trimMargin()
    private val hands = input.lines().toHands { it != Card.JOKER }
    private val jokerHands = input.lines().toHands { true }
    private val day7 = Day7()

    @Test
    fun part1() {
        val expected = 6440L
        val actual = day7.part1(hands)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 5905L
        val actual = day7.part2(jokerHands)
        assertEquals(expected, actual)
    }
}
