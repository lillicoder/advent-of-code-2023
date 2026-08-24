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

package com.lillicoder.adventofcode2023.day1

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for {Day1].
 */
internal class Day1Test {
    private val day1 = Day1()

    @Test
    fun part1() {
        val input =
            """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
            """.trimIndent()
        val expected = 142L
        val actual = day1.part1(input.lines())
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val input =
            """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen
            """.trimIndent()
        val expected = 281L
        val actual = day1.part2(input.lines())
        assertEquals(expected, actual)
    }
}
