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

package com.lillicoder.adventofcode2023.day14

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Day14].
 */
internal class Day14Test {
    private val input =
        """
        O....#....
        O.OO#....#
        .....##...
        OO.#O....O
        .O.....O#.
        O.#..O.#.#
        ..O..#O..O
        .......O..
        #....###..
        #OO..#....
        """.trimIndent()
    private val day14 = Day14()

    @Test
    fun part1() {
        val expected = 136L
        val actual = day14.part1(input)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 64L
        val actual = day14.part2(input)
        assertEquals(expected, actual)
    }
}
