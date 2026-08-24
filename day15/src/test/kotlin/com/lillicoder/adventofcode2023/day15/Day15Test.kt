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

package com.lillicoder.adventofcode2023.day15

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Day15].
 */
internal class Day15Test {
    private val input = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"
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
