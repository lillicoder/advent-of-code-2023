package com.lillicoder.adventofcode2023.day4

import com.lillicoder.adventofcode2023.io.Resources
import com.lillicoder.adventofcode2023.io.splitNotEmpty
import kotlin.math.pow

fun main() {
    val day4 = Day4()
    val cards =
        Resources.mapLines(
            "input.txt",
        ) {
            it.toScratchcard()
        } ?: throw IllegalArgumentException("Could not read input from file.")
    println("The total score of the given scratchcards is ${day4.part1(cards)}.")
    println("The total count of cards, including clones, is ${day4.part2(cards)}.")
}

class Day4 {
    fun part1(cards: List<Scratchcard>) = cards.sumOf { it.score }

    fun part2(cards: List<Scratchcard>) = cards.countWithWinners()
}

/**
 * Represents a single scratchcard.
 * @param id ID.
 * @param winningNumbers Numbers to match to win points.
 * @param playNumbers Numbers played.
 * @param matches Number of matches of the play numbers with the winner numbers.
 * @param score Score based on number of matches.
 *
 */
data class Scratchcard(
    val id: Int,
    val winningNumbers: List<Int>,
    val playNumbers: List<Int>,
    val matches: Int = winningNumbers.count { it in playNumbers },
    val score: Int =
        when (matches) {
            0 -> 0
            else -> 2.0.pow(matches - 1).toInt()
        },
)

/**
 * Counts the total number of [Scratchcard] for this list, including
 * cards that get copied due to having one or more matches.
 * @return Count.
 */
fun List<Scratchcard>.countWithWinners(): Int {
    // We start with 1 of each card
    val frequency = associate { it.id to 1 }.toMutableMap()
    forEachIndexed { index, card ->
        val count = frequency[card.id]!!
        val matches = card.matches

        val copyIds = IntRange(1, matches).mapNotNull { getOrNull(index + it)?.id }
        copyIds.forEach { id -> frequency.merge(id, count, Int::plus) }
    }

    // All cards have been copied and their counts updated
    return frequency.values.sum()
}

/**
 * Converts this string to an equivalent [Scratchcard].
 * @return Scratchcard.
 */
internal fun String.toScratchcard(): Scratchcard {
    val id = substringBefore(":").substringAfter("Card ").trim().toInt()
    val blocks = substringAfter(": ").split(" | ")

    // Single digits have an extra space, so when we split we'll end up with some
    // elements as an empty string, so drop those before attempting to cast
    val winning = blocks[0].splitNotEmpty(" ").map { it.toInt() }
    val played = blocks[1].splitNotEmpty(" ").map { it.toInt() }
    return Scratchcard(id, winning, played)
}
