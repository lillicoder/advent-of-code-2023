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

package com.lillicoder.adventofcode2023.day16

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Day16].
 */
internal class Day16Test {
    private val input =
        """
        .|...\....
        |.-.\.....
        .....|-...
        ........|.
        ..........
        .........\
        ..../.\\..
        .-.-/..|..
        .|....-|.\
        ..//.|....
        """.trimIndent()
    private val day16 = Day16()

    @Test
    fun part1() {
        val expected = 46L
        val actual = day16.part1(input)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 51L
        val actual = day16.part2(input)
        assertEquals(expected, actual)
    }
}
