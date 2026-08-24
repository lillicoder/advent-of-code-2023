/*
 * Copyright 2026 Scott Weeden-Moody
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this project except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lillicoder.adventofcode2023.day7

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Day7].
 */
internal class Day7Test {
    private val input =
        """
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
        """.trimIndent().lines()
    private val day7 = Day7()

    @Test
    fun part1() {
        val expected = 6440L
        val actual = day7.part1(input)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 5905L
        val actual = day7.part2(input)
        assertEquals(expected, actual)
    }
}
