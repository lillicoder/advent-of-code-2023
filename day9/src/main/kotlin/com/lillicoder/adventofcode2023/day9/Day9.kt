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

package com.lillicoder.adventofcode2023.day9

import com.lillicoder.adventofcode.kotlin.io.Resources
import com.lillicoder.adventofcode.kotlin.text.splitNotEmpty

fun main() {
    val day9 = Day9()
    val input =
        Resources.lines(
            "input.txt",
        ) ?: throw IllegalArgumentException("Could not read input from file.")
    println("[Part 1] The sum of all next predictions for all sequences is ${day9.part1(input)}.")
    println("[Part 2] The sum of all previous predictions for all sequences is ${day9.part2(input)}.")
}

class Day9 {
    fun part1(input: List<String>) = input.toReadings().sumOf { it.predictNext() }

    fun part2(input: List<String>) = input.toReadings().sumOf { it.predictPreceding() }
}

/**
 * Predicts the preceding history for these values.
 * @return Preceding history.
 */
private fun List<Long>.predictPreceding(): Long {
    var currentRow = this
    val leftmostBranch = mutableListOf(first())

    while (!currentRow.all { it == 0L }) {
        currentRow = currentRow.windowed(2, 1).map { it[1] - it[0] }
        leftmostBranch.add(currentRow.first())
    }

    // Can't do a running value while windowing, need to process list in proper order
    // from 0 to bottom level of the tree
    return leftmostBranch.asReversed().reduce { accumulator, value ->
        value - accumulator
    }
}

/**
 * Predicts the next history for these values.
 * @return Next history.
 */
private fun List<Long>.predictNext(): Long {
    // We can predict solely by summing rightmost branch values as we go
    var currentRow = this
    var prediction = currentRow.last()
    while (currentRow.last() != 0L) {
        currentRow = currentRow.windowed(2, 1).map { it[1] - it[0] }
        prediction += currentRow.last()
    }

    return prediction
}

/**
 * Converts these strings to an equivalent list of readings.
 * @return Readings.
 */
private fun List<String>.toReadings() =
    map { string ->
        string.splitNotEmpty(" ").map { digit ->
            digit.toLong()
        }
    }
