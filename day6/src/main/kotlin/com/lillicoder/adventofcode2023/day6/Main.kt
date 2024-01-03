package com.lillicoder.adventofcode2023.day6

fun main() {
    // Part one: Treat data as separate races
    val races = RaceParser().parse("input.txt")
    val count = RecordSetterCalculator().countWaysToSetRecords(races)
    println("Total possible winning permutations is $count.")

    // Part two: Treat data as a single race
    val time = races.joinToString { it.duration.toString() }
    val distance = races.joinToString { it.bestDistance.toString() }
    val asOneRace = Race(time.toLong(), distance.toLong())
    val asOneRaceCount = RecordSetterCalculator().countWaysToSetRecord(asOneRace)
    println("Total possible winning permutations as one race is $asOneRaceCount.")
}

/**
 * Represents a race record.
 * @param duration Duration of a race in milliseconds.
 * @param bestDistance Best distance ran in this race.
 */
data class Race(
    val duration: Long,
    val bestDistance: Long
)

/**
 * Determines possible ways of beating a record distance for a given [Race].
 */
class RecordSetterCalculator {

    /**
     * Determines the count of possible permutations that would result in a new
     * distance record being set for the given [Race].
     * @param race Race to evaluate.
     * @return Count of permutations that result in a new distance record.
     */
    fun countWaysToSetRecord(race: Race): Long {
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
        var count = 0L
        for (speed in 0..race.duration) {
            val remainingTime = race.duration - speed
            val distance = speed * remainingTime
            if (distance > race.bestDistance) count++
        }

        return count
    }

    /**
     * Determines the total possible permutations that would result in a new distance
     * record being set for the given list of [Race].
     * @param races Races to evaluate.
     * @return Count of permutations that result in a new distance record across all races.
     */
    fun countWaysToSetRecords(races: List<Race>): Long {
        val combinations = mutableListOf<Long>()
        races.forEach { race ->
            combinations.add(countWaysToSetRecord(race))
        }

        return combinations.reduce { accumulator, combo -> accumulator * combo }
    }
}

class RaceParser {

    fun parse(filename: String): List<Race> {
        val input = javaClass.classLoader.getResourceAsStream(filename)!!.reader().readText()
        val sections = input.split("\r\n")
        val times = sections[0].substringAfter(":").split(" ").filter { it.isNotEmpty() }.map { it.toLong() }
        val distances = sections[1].substringAfter(":").split(" ").filter { it.isNotEmpty() }.map { it.toLong() }

        val records = mutableListOf<Race>()
        times.zip(distances).forEach {
            records.add(Race(it.first, it.second))
        }

        return records
    }
}
