package com.lillicoder.adventofcode2023.day4

import kotlin.math.pow

fun main() {
    val scratchcards = ScratchcardParser().parseScratchcards("input.txt")
    val score = scratchcards.sumOf { it.score() }
    println("The total score of the given scratchcards is $score.")

    val count = ScratchcardCloneCalculator().countWithWinners(scratchcards)
    println("The total count of cards, including clones, is $count.")
}

/**
 * Represents a single scratchcard.
 * @param winningNumbers Numbers to match to win points.
 * @param playNumbers Numbers played.
 */
data class Scratchcard(
    val id: Int,
    val winningNumbers: List<Int>,
    val playNumbers: List<Int>
) {

    /**
     * Gets the number of winning numbers that match the play numbers.
     * @return Number of matches.
     */
    fun matches(): Int = winningNumbers.count { it in playNumbers }

    /**
     * Gets the score for this scratchcard.
     * @return Score.
     */
    fun score(): Int {
        return when (val matches = matches()) {
            0 -> 0
            else -> 2.0.pow(matches - 1).toInt()
        }
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
    fun parseScratchcards(filename: String): List<Scratchcard> {
        val scratchcards = mutableListOf<Scratchcard>()

        javaClass.classLoader.getResourceAsStream(filename)!!.reader().forEachLine { line ->
            val scratchcard = parseScratchcard(line)
            scratchcards.add(scratchcard)
        }

        return scratchcards
    }

    /**
     * Parses a [Scratchcard] from the given raw scratchcard input.
     * @param scratchcard Scratchcard to parse.
     * @return Scratchcard.
     */
    private fun parseScratchcard(scratchcard: String): Scratchcard {
        val id = scratchcard.substringBefore(":").substringAfter("Card ").trim().toInt()
        val blocks = scratchcard.substringAfter(": ").split(" | ")

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
        val frequency: MutableMap<Int, Int> = scratchcards.associate { it.id to 1 }.toMutableMap()
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
     * @param end Number of IDs to get.
     * @return List of card IDs.
     */
    private fun getCopyIds(scratchcards: List<Scratchcard>, start: Int, count: Int): List<Int> {
        val next = mutableListOf<Int>()
        for (i in 1..count) { scratchcards.getOrNull(start + i)?.let { next.add(it.id) } }
        return next
    }
}
