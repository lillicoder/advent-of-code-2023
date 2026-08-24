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

package com.lillicoder.adventofcode2023.day12

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Day12].
 */
internal class Day12Test {
    private val input =
        """
        ???.### 1,1,3
        .??..??...?##. 1,1,3
        ?#?#?#?#?#?#?#? 1,3,1,6
        ????.#...#... 4,1,1
        ????.######..#####. 1,6,5
        ?###???????? 3,2,1
        """.trimIndent().lines()
    private val day12 = Day12()

    @Test
    fun part1() {
        val expected = 21L
        val actual = day12.part1(input)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 525152L
        val actual = day12.part2(input)
        assertEquals(expected, actual)
    }
}
