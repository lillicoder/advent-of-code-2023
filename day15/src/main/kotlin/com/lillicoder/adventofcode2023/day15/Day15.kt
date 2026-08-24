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

import com.lillicoder.adventofcode.kotlin.io.Resources
import kotlin.streams.asSequence

fun main() {
    val day15 = Day15()
    val input =
        Resources.text(
            "input.txt",
        ) ?: throw IllegalArgumentException("Could not read input from file.")
    println("[Part 1] The total of hashes for the given input is ${day15.part1(input)}.")
    println("[Part 2] The total focusing power of loaded lenses is ${day15.part2(input)}.")
}

class Day15 {
    fun part1(input: String) = input.split(",").sumOf { it.hash() }

    fun part2(input: String): Long {
        // Put lenses into one of 256 'boxes'; using LinkedHashMap to preserve insertion
        // order for keys and to get O(1) insert/remove performance
        val boxes = MutableList(256) { LinkedHashMap<String, Int>() }
        input.split(",").forEach { raw ->
            val (label, focal) = raw.toInstructions()
            val box = boxes[label.hash().toInt()]
            when (focal?.isNotEmpty()) {
                true -> box[label] = focal.toInt()
                else -> box.remove(label)
            }
        }

        // Apply the focus power formula to each lens in each box and sum them all up
        return boxes.mapIndexed { boxIndex, box ->
            box.entries.mapIndexed { index, entry ->
                (boxIndex + 1) * (index + 1) * entry.value
            }.sum()
        }.sum().toLong()
    }
}

/**
 * Hashes the given string as per the hashing rules for Day 15.
 */
private fun String.hash() =
    codePoints().asSequence().fold(0L) { left, right ->
        ((left + right) * 17) % 256
    }

/**
 * Converts this string to an instruction pair.
 * @return Instruction pair.
 */
private fun String.toInstructions(): Pair<String, String?> {
    val operation = if (last().isDigit()) "=" else "-"
    val split = split(operation)
    return Pair(split[0], split.getOrNull(1))
}
