package com.lillicoder.adventofcode2023.day5

import com.lillicoder.adventofcode.kotlin.io.Resources
import com.lillicoder.adventofcode.kotlin.text.normalizeLineSeparators
import com.lillicoder.adventofcode.kotlin.text.splitNotEmpty
import kotlin.math.min

fun main() {
    val day5 = Day5()
    val input =
        Resources.text(
            "input.txt",
        ) ?: throw IllegalArgumentException("Could not read input from file.")
    println("[Part 1] The lowest location number for all input single seed numbers is ${day5.part1(input)}.")
    println("[Part 2] The lowest location number for all input seed number ranges is ${day5.part2(input)}.")
}

class Day5 {
    fun part1(input: String) =
        input.normalizeLineSeparators().toAlmanac().let { almanac ->
            almanac.seeds.minOfOrNull {
                almanac.findLowestLocation(it)
            }
        }

    fun part2(input: String): Long {
        var lowestLocationNumber = Long.MAX_VALUE

        val almanac = input.normalizeLineSeparators().toAlmanac()
        almanac.seeds.windowed(2, 2).forEach { pair ->
            // First value is seed, second value is range
            for (seed in pair.first()..(pair.sum())) {
                lowestLocationNumber = min(lowestLocationNumber, almanac.findLowestLocation(seed))
            }
        }

        return lowestLocationNumber
    }
}

/**
 * Represents a seed almanac.
 * @param seeds List of seed IDs.
 * @param seedToSoil [Mapping]s of seed ID to soil ID.
 * @param soilToFertilizer [Mapping]s of soil ID to fertilizer ID.
 * @param fertilizerToWater [Mapping]s of fertilizer ID to water ID.
 * @param waterToLight [Mapping]s of water ID to light ID.
 * @param lightToTemperature [Mapping]s of light ID to temperature ID.
 * @param temperatureToHumidity [Mapping]s of temperature ID to humidity ID.
 * @param humidityToLocation [Mapping]s of humidity ID to location ID.
 */
private data class Almanac(
    val seeds: List<Long>,
    val seedToSoil: List<Mapping>,
    val soilToFertilizer: List<Mapping>,
    val fertilizerToWater: List<Mapping>,
    val waterToLight: List<Mapping>,
    val lightToTemperature: List<Mapping>,
    val temperatureToHumidity: List<Mapping>,
    val humidityToLocation: List<Mapping>,
) {
    /**
     * Finds the lowest eligible location number for the given seed number in this [Almanac].
     * @param seed Seed number.
     * @return Lowest eligible location number.
     */
    fun findLowestLocation(seed: Long): Long {
        // Hop from map to map until we get the final value from the humidity-to-location map
        val soil = seedToSoil.findMatch(seed)
        val fertilizer = soilToFertilizer.findMatch(soil)
        val water = fertilizerToWater.findMatch(fertilizer)
        val light = waterToLight.findMatch(water)
        val temperature = lightToTemperature.findMatch(light)
        val humidity = temperatureToHumidity.findMatch(temperature)
        return humidityToLocation.findMatch(humidity)
    }
}

/**
 * Represents a mapping of one resource to another.
 * @param destinationStart Destination range start.
 * @param sourceStart Source range start.
 * @param range Range length.
 */
private data class Mapping(
    val destinationStart: Long,
    val sourceStart: Long,
    val range: Long,
)

/**
 * Finds the corresponding destination from these mappings for the given source ID.
 * @param source Source ID.
 * @return Corresponding destination ID.
 */
private fun List<Mapping>.findMatch(source: Long) =
    when (val mapping = find { source in it.sourceStart..(it.sourceStart + it.range) }) {
        /**
         * Each section has the following format:
         *
         * T destination    U source    range
         *
         * Example:
         *
         * seed-to-soil map:
         * 2702707184 1771488746 32408643
         *
         * To determine if an arbitrary source is in a mapping:
         *
         * if (source is in [U, U + range]) { destination = source + (T - U) }
         * else { destination = source }
         *
         * Procedure should be:
         * 1) Determine which, if any, of the mappings has a range that contains the source
         * 2) If (1), then compute the destination value using said range
         * 3) If not (1), then the source is one-to-one with the destination, just return that value
         */
        null -> source
        else -> source + (mapping.destinationStart - mapping.sourceStart)
    }

/**
 * Converts this string to an equivalent [Almanac].
 * @return Almanac.
 */
private fun String.toAlmanac(): Almanac {
    val sections = split("${System.lineSeparator()}${System.lineSeparator()}")

    // First section is seeds list; format: `seeds: ### ### ###`
    val seeds = sections[0].substringAfter(":").splitNotEmpty(" ").map { it.toLong() }

    // All maps are a heading line followed by 3 numbers, e.g.:
    //
    // label:
    // ### ### ###
    // ### ### ###
    val seedToSoil = sections[1].toMappings()
    val soilToFertilizer = sections[2].toMappings()
    val fertilizerToWater = sections[3].toMappings()
    val waterToLight = sections[4].toMappings()
    val lightToTemperature = sections[5].toMappings()
    val temperatureToHumidity = sections[6].toMappings()
    val humidityToLocation = sections[7].toMappings()
    return Almanac(
        seeds,
        seedToSoil,
        soilToFertilizer,
        fertilizerToWater,
        waterToLight,
        lightToTemperature,
        temperatureToHumidity,
        humidityToLocation,
    )
}

/**
 * Converts this string to an equivalent list of [Mapping].
 * @return Mappings
 */
private fun String.toMappings() =
    substringAfter(
        ":",
    ).splitNotEmpty(
        System.lineSeparator(),
    ).map { line ->
        val parts = line.splitNotEmpty(" ").map { it.toLong() }
        Mapping(parts[0], parts[1], parts[2])
    }
