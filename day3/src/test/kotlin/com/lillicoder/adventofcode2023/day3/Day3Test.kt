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

package com.lillicoder.adventofcode2023.day3

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Day3].
 */
internal class Day3Test {
    private val input =
        """
        467..114..
        ...*......
        ..35..633.
        ......#...
        617*......
        .....+.58.
        ..592.....
        ......755.
        ...$.*....
        .664.598..
        """.trimIndent()
    private val day3 = Day3()

    @Test
    fun part1() {
        val expected = 4361
        val actual = day3.part1(input)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 467835
        val actual = day3.part2(input)
        assertEquals(expected, actual)
    }
}
