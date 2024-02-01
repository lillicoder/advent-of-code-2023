package com.lillicoder.adventofcode2023.day6

fun main() {
    val day6 = Day6()
    val races = RaceParser().parseFile("input.txt")
    println("Total possible winning permutations is ${day6.part1(races)}.")
    println("Total possible winning permutations as one race is ${day6.part2(races)}.")
}

class Day6 {
    fun part1(races: List<Race>) = RecordSetterCalculator().countWaysToSetRecords(races).toLong()

    fun part2(races: List<Race>): Long {
        val time = races.joinToString("") { it.duration.toString() }
        val distance = races.joinToString("") { it.bestDistance.toString() }
        val asOneRace = Race(time.toLong(), distance.toLong())
        return RecordSetterCalculator().countWaysToSetRecord(asOneRace).toLong()
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
)

class RaceParser {
    /**
     * Parses the given raw race input to an equivalent list of [Race].
     * @param raw Raw races input.
     * @param separator Line separator for the given input.
     * @return Races.
     */
    fun parse(
        raw: String,
        separator: String = System.lineSeparator(),
    ): List<Race> {
        val sections = raw.split(separator)
        val times = sections[0].substringAfter(":").split(" ").filter { it.isNotEmpty() }.map { it.toLong() }
        val distances = sections[1].substringAfter(":").split(" ").filter { it.isNotEmpty() }.map { it.toLong() }
        return times.zip(distances).map { Race(it.first, it.second) }
    }

    /**
     * Parses the file for the given filename to an equivalent list of [Race].
     * @param filename Filename.
     * @return Races.
     */
    fun parseFile(filename: String) = parse(javaClass.classLoader.getResourceAsStream(filename)!!.reader().readText())
}

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
    fun countWaysToSetRecord(race: Race) =
        LongRange(0, race.duration).count { speed ->
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
            val remainingTime = race.duration - speed
            val distance = speed * remainingTime
            distance > race.bestDistance
        }

    /**
     * Determines the total possible permutations that would result in a new distance
     * record being set for the given list of [Race].
     * @param races Races to evaluate.
     * @return Count of permutations that result in a new distance record across all races.
     */
    fun countWaysToSetRecords(races: List<Race>) =
        races.map {
            countWaysToSetRecord(it)
        }.reduce { accumulator, count ->
            accumulator * count
        }
}
