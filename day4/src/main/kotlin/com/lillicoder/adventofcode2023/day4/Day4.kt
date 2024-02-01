package com.lillicoder.adventofcode2023.day4

import kotlin.math.pow

fun main() {
    val day4 = Day4()
    val cards = ScratchcardParser().parse("input.txt")
    println("The total score of the given scratchcards is ${day4.part1(cards)}.")
    println("The total count of cards, including clones, is ${day4.part2(cards)}.")
}

class Day4 {
    fun part1(cards: List<Scratchcard>) = cards.sumOf { it.score() }

    fun part2(cards: List<Scratchcard>) = ScratchcardCloneCalculator().countWithWinners(cards)
}

/**
 * Represents a single scratchcard.
 * @param winningNumbers Numbers to match to win points.
 * @param playNumbers Numbers played.
 */
data class Scratchcard(
    val id: Int,
    val winningNumbers: List<Int>,
    val playNumbers: List<Int>,
) {
    /**
     * Gets the number of winning numbers that match the play numbers.
     * @return Number of matches.
     */
    fun matches() = winningNumbers.count { it in playNumbers }

    /**
     * Gets the score for this scratchcard.
     * @return Score.
     */
    fun score() =
        when (val matches = matches()) {
            0 -> 0
            else -> 2.0.pow(matches - 1).toInt()
        }
}

/**
 * Parses scratchcard records into a list of [Scratchcard].
 */
class ScratchcardParser {
    /**
     * Parses the file with the given filename and returns a list of [Scratchcard].
     * @param filename Name of the file to parse.
     * @return List of scratchcards.
     */
    fun parse(filename: String) = parse(javaClass.classLoader.getResourceAsStream(filename)!!.reader().readLines())

    /**
     * Parses the given raw scratchcard input to an equivalent list of [Scratchcard].
     * @param raw Raw list of scratchcards.
     * @return List of scratchcards.
     */
    fun parse(raw: List<String>) = raw.map { parseScratchcard(it) }

    /**
     * Parses a [Scratchcard] from the given raw scratchcard input.
     * @param raw Raw scratchcard.
     * @return Scratchcard.
     */
    private fun parseScratchcard(raw: String): Scratchcard {
        val id = raw.substringBefore(":").substringAfter("Card ").trim().toInt()
        val blocks = raw.substringAfter(": ").split(" | ")

        // Single digits have an extra space, so when we split we'll end up with some
        // elements as an empty string, so drop those before attempting to cast
        val winning = blocks[0].split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
        val played = blocks[1].split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
        return Scratchcard(id, winning, played)
    }
}

/**
 * Calculator for counting the total number of scratchcards, including winning copies.
 */
class ScratchcardCloneCalculator {
    /**
     * Counts the total number of scratchcards represented by the given list of [Scratchcard], including
     * cards that get copied due to having one or more matches.
     * @param scratchcards Scratchcards to evaluate.\
     * @return Count of scratchcards.
     */
    fun countWithWinners(scratchcards: List<Scratchcard>): Int {
        // We start with 1 of each card
        val frequency = scratchcards.associate { it.id to 1 }.toMutableMap()
        scratchcards.forEachIndexed { index, card ->
            val count = frequency[card.id]!!
            val matches = card.matches()

            val copyIds = getCopyIds(scratchcards, index, matches)
            copyIds.forEach { id -> frequency.merge(id, count, Int::plus) }
        }

        // All cards have been copied and their counts updated
        return frequency.values.sum()
    }

    /**
     * Gets the IDs of the scratchcards that are after the card at the given start index.
     * @param scratchcards List of [Scratchcard].
     * @param start Starting index.
     * @return List of card IDs.
     */
    private fun getCopyIds(
        scratchcards: List<Scratchcard>,
        start: Int,
        count: Int,
    ) = IntRange(1, count).mapNotNull {
        scratchcards.getOrNull(start + it)?.id
    }
}
