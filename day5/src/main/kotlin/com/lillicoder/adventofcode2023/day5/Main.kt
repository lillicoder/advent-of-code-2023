package com.lillicoder.adventofcode2023.day5

import java.time.temporal.TemporalAmount
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.pow

fun main() {
    val almanac = AlmanacParser().parse("input.txt")

    val searcher = LowestLocationSearcher()
    val lowestForSingles = searcher.searchSingle(almanac)
    println("The lowest location number for all input single seed numbers is ${lowestForSingles.min()}.")

    val lowestForRanges = searcher.searchRanges(almanac)
    println("The lowest location number for all input seed number ranges is $lowestForRanges.")
}

/**
 * Represents a seed almanac.
 */
data class Almanac(
    val seeds: List<Long>,
    val seedToSoil: List<Triple<Long, Long, Long>>,
    val soilToFertilizer: List<Triple<Long, Long, Long>>,
    val fertilizerToWater: List<Triple<Long, Long, Long>>,
    val waterToLight: List<Triple<Long, Long, Long>>,
    val lightToTemperature: List<Triple<Long, Long, Long>>,
    val temperatureToHumidity: List<Triple<Long, Long, Long>>,
    val humidityToLocation: List<Triple<Long, Long, Long>>
)

/**
 * Searcher that can find the lowest location numbers for one or more seed numbers.
 */
class LowestLocationSearcher {

    /**
     * Searches for the lowest location numbers for each seed number in the given [Almanac]'s seeds.
     * @param almanac Almanac to search.
     * @return List of lowest location numbers.
     */
    fun searchSingle(almanac: Almanac): List<Long> {
        val lowestLocationNumbers = mutableListOf<Long>()

        almanac.seeds.forEach { seed ->
            val lowest = findLowestLocation(seed, almanac)
            lowestLocationNumbers.add(lowest)
        }

        return lowestLocationNumbers
    }

    /**
     * Searches for the lowest location numbers for each seed number range in the given [Almanac]'s seeds.
     * @param almanac Almanac to search.
     * @return List of lowest location numbers.
     */
    fun searchRanges(almanac: Almanac): Long {
        var lowestLocationNumber: Long = Long.MAX_VALUE

        almanac.seeds.windowed(2, 2).forEach{ pair ->
            // First value is seed, second value is range
            for(seed in pair.first()..(pair.sum())) {
                lowestLocationNumber = min(lowestLocationNumber, findLowestLocation(seed, almanac))
            }
        }

        return lowestLocationNumber
    }

    /**
     * Finds the lowest eligible location number for the given seed number in
     * the given [Almanac].
     * @param seed Seed number.
     * @param almanac Almanac to search.
     * @return Lowest eligible location number.
     */
    private fun findLowestLocation(seed: Long, almanac: Almanac): Long {
        /**
         * We need to hop from map to map until we get the final value from the [Almanac.humidityToLocation] map.
         */
        val soil = findMatch(seed, almanac.seedToSoil)
        val fertilizer = findMatch(soil, almanac.soilToFertilizer)
        val water = findMatch(fertilizer, almanac.fertilizerToWater)
        val light = findMatch(water, almanac.waterToLight)
        val temperature = findMatch(light, almanac.lightToTemperature)
        val humidity = findMatch(temperature, almanac.temperatureToHumidity)
        return findMatch(humidity, almanac.humidityToLocation)
    }

    /**
     * Finds the corresponding destination from the given mapping for the given source.
     * @param source Source value.
     * @param map Mapping of sources to destinations.
     * @return Corresponding destination.
     */
    private fun findMatch(source: Long, map: List<Triple<Long, Long, Long>>): Long {
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
        return when(val mapping = map.find { source in it.second..(it.second + it.third) }) {
            null -> source
            else -> source + (mapping.first - mapping.second)
        }
    }
}

class AlmanacParser {

    fun parse(filename: String): Almanac {
        val input = javaClass.classLoader.getResourceAsStream(filename)!!.reader().readText()
        val sections = input.split("\r\n\r\n")

        // First section is seeds list
        val seeds = parseSeeds(sections[0])

        // Second section is seed-to-soil map
        val seedToSoil = parseMap(sections[1])

        // Third section is soil-to-fertilizer map
        val soilToFertilizer = parseMap(sections[2])

        // Fourth section is fertilizer-to-water map
        val fertilizerToWater = parseMap(sections[3])

        // Fifth section is water-to-light map
        val waterToLight = parseMap(sections[4])

        // Sixth section is light-to-temperature map
        val lightToTemperature = parseMap(sections[5])

        // Seventh section is temperature-to-humidity map
        val temperatureToHumidity = parseMap(sections[6])

        // Eight section is humidity-to-location map
        val humidityToLocation = parseMap(sections[7])

        return Almanac(
            seeds,
            seedToSoil,
            soilToFertilizer,
            fertilizerToWater,
            waterToLight,
            lightToTemperature,
            temperatureToHumidity,
            humidityToLocation
        )
    }

    /**
     * Parses the given section of almanac mappings to a list of triplets.
     * @param section Map section to parse.
     * @return Section map.
     */
    private fun parseMap(section: String): List<Triple<Long, Long, Long>> {
        val mappings = mutableListOf<Triple<Long, Long, Long>>()
        section.substringAfter(":").split("\r\n").filter{ it.isNotEmpty() }.forEach { line ->
            val parts = line.split(" ").filter { it.isNotEmpty() }.map { it.toLong() }
            mappings.add(Triple(parts[0], parts[1], parts[2]))
        }

        return mappings
    }

    /**
     * Parses the list of seed numbers from the given input.
     * @param section Seeds section to parse.
     * @return Seed numbers.
     */
    private fun parseSeeds(
        section: String
    ) = section.substringAfter(": ").split(" ").filter { it.isNotEmpty() }.map { it.toLong() }
}
