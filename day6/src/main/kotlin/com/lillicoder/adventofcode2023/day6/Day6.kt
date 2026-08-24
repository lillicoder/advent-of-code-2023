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

package com.lillicoder.adventofcode2023.day6

import com.lillicoder.adventofcode.kotlin.io.Resources
import com.lillicoder.adventofcode.kotlin.text.normalizeLineSeparators
import com.lillicoder.adventofcode.kotlin.text.splitNotEmpty

fun main() {
    val day6 = Day6()
    val input =
        Resources.text(
            "input.txt",
        ) ?: throw IllegalArgumentException("Could not read input from file.")
    println("[Part 1] Total possible winning permutations is ${day6.part1(input)}.")
    println("[Part 2] Total possible winning permutations as one race is ${day6.part2(input)}.")
}

class Day6 {
    fun part1(input: String) =
        input.normalizeLineSeparators().toRaces().map {
            it.countWaysToSetRecord()
        }.reduce { accumulator, count ->
            accumulator * count
        }

    fun part2(input: String): Long {
        val races = input.normalizeLineSeparators().toRaces()
        val time = races.joinToString("") { it.duration.toString() }
        val distance = races.joinToString("") { it.bestDistance.toString() }
        return Race(
            time.toLong(),
            distance.toLong(),
        ).countWaysToSetRecord()
    }
}

/**
 * Represents a race record.
 * @param duration Duration of a race in milliseconds.
 * @param bestDistance Best distance ran in this race.
 */
private data class Race(
    val duration: Long,
    val bestDistance: Long,
) {
    /**
     * Determines the count of possible permutations that would result in a new
     * distance record being set for this [Race].
     * @return Count of permutations that result in a new distance record.
     */
    fun countWaysToSetRecord() =
        LongRange(0, duration).count { speed ->
            /**
             * A race is split into two phases:
             *
             * 1) Pressing a toy boat's button to charge it
             * 2) Boat traveling after releasing the charge button
             *
             * You can depress the charge button for 0 to N milliseconds, where N is the race duration.
             * Boat speed is M millimeters/millisecond, where M is how long the button was pressed.
             *
             * We will only consider whole integer values, no fractions.
             *
             * Naive solution: for each possible value of N, determine how far we go for the remaining
             * time at speed M. If that distance is greater than the record, we have found a
             * desired outcome.
             */
            val remainingTime = duration - speed
            val distance = speed * remainingTime
            distance > bestDistance
        }.toLong()
}

/**
 * Converts this string to an equivalent list of [Race].
 * @return Races.
 */
private fun String.toRaces(): List<Race> {
    val sections = lines()
    val times = sections[0].substringAfter(":").splitNotEmpty(" ").map { it.toLong() }
    val distances = sections[1].substringAfter(":").splitNotEmpty(" ").map { it.toLong() }
    return times.zip(distances).map { Race(it.first, it.second) }
}
