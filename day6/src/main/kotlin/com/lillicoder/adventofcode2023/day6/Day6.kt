package com.lillicoder.adventofcode2023.day6

import com.lillicoder.adventofcode2023.io.Resources
import com.lillicoder.adventofcode2023.io.splitMap

fun main() {
    val day6 = Day6()
    val races =
        Resources.text(
            "input.txt",
        )?.toRaces() ?: throw IllegalArgumentException("Could not read input from file.")
    println("Total possible winning permutations is ${day6.part1(races)}.")
    println("Total possible winning permutations as one race is ${day6.part2(races)}.")
}

class Day6 {
    fun part1(races: List<Race>) =
        races.map {
            it.countWaysToSetRecord()
        }.reduce { accumulator, count ->
            accumulator * count
        }

    fun part2(races: List<Race>): Long {
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
data class Race(
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
 * @param separator Line separator.
 * @return Races.
 */
internal fun String.toRaces(separator: String = System.lineSeparator()): List<Race> {
    val sections = split(separator)
    val times = sections[0].substringAfter(":").splitMap(" ") { it.toLong() }
    val distances = sections[1].substringAfter(":").splitMap(" ") { it.toLong() }
    return times.zip(distances).map { Race(it.first, it.second) }
}
