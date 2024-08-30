package com.lillicoder.adventofcode2023.day7

import com.lillicoder.adventofcode2023.io.Resources
import com.lillicoder.adventofcode2023.io.splitMap

fun main() {
    val day7 = Day7()
    val input =
        Resources.lines(
            "input.txt",
        ) ?: throw IllegalArgumentException("Could not read input from file.")
    val hands = input.toHands { it != Card.JOKER } // Don't allow jokers
    val jokerHands = input.toHands { true } // Allow all cards
    println("[Normal] Total winnings for the given hands is ${day7.part1(hands)}.")
    println("[Jokers] Total winnings for the given hands is ${day7.part2(jokerHands)}.")
}

class Day7 {
    fun part1(hands: List<Hand>) = hands.winnings()

    fun part2(hands: List<Hand>) = hands.winnings()
}

/**
 * Represents a single card in a hand.
 */
enum class Card(
    val symbol: String,
) {
    JOKER("J"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("T"),
    JACK("J"),
    QUEEN("Q"),
    KING("K"),
    ACE("A"),
}

/**
 * Represents the rank of a hand in a game of Camel Cards.
 */
enum class Rank {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_OF_A_KIND,
    FULL_HOUSE,
    FOUR_OF_A_KIND,
    FIVE_OF_A_KIND,
}

/**
 * Represents a hand in a game of Camel Cards.
 * @param cards Cards for this hand.
 * @param bid Bid.
 */
data class Hand(
    val cards: List<Card>,
    val bid: Int,
) : Comparable<Hand> {
    private val rank = rank()

    override fun compareTo(other: Hand) =
        when (val rankComparison = rank.compareTo(other.rank)) {
            0 -> cards.compareTo(other.cards) // Same rank, sort by strongest card
            else -> rankComparison // Different rank, sort by rank
        }

    /**
     * Counts the cards in this hand.
     * @return Map of card to how often it appears in this hand.
     */
    private fun countCards(): Map<Card, Int> = cards.groupingBy { it }.eachCount()

    /**
     * Determines the [Rank] of this hand.
     * @return Hand rank.
     */
    private fun rank(): Rank {
        val counts = countCards()
        if (isFiveOfAKind(counts)) return Rank.FIVE_OF_A_KIND
        if (isFourOfAKind(counts)) return Rank.FOUR_OF_A_KIND
        if (isFullHouse(counts)) return Rank.FULL_HOUSE
        if (isThreeOfAKind(counts)) return Rank.THREE_OF_A_KIND
        if (isTwoPair(counts)) return Rank.TWO_PAIR
        if (isOnePair(counts)) return Rank.ONE_PAIR

        // High card - only option left (should have grouped 5 keys)
        return Rank.HIGH_CARD
    }

    /**
     * Determines if this hand is a five of a kind.
     * @param counts Count of cards in this hand.
     * @return True if five of a kind, false otherwise.
     */
    private fun isFiveOfAKind(counts: Map<Card, Int>): Boolean {
        // Case 1: no jokers, all the same card
        // 1 1 1 1 1
        val allSame = counts.size == 1

        // Case 2: any amount of jokers, jokers count plus biggest count = hand size
        // J 1 1 1 1
        // J J 1 1 1
        // J J J 1 1
        // J J J J 1
        return allSame || (counts.jokers() + counts.max() == counts.values.sum())
    }

    /**
     * Determines if this hand is a four of a kind.
     * @param counts Count of cards in this hand.
     * @return True if four of a kind, false otherwise.
     */
    private fun isFourOfAKind(counts: Map<Card, Int>): Boolean {
        // Case 1: no jokers, 4 of one kind of card (4 jokers makes 5 of a kind, so don't need to check that)
        // 1111 5
        val hasFourSameCards = counts.values.find { it == 4 } != null

        // Case 2: has 3 jokers and two other distinct cards (frequency == 3)
        // JJJ 1 5
        val hasThreeJokersAndTwoOtherDistinct = counts.jokers() == 3 && counts.size == 3

        // Case 3: has 2 jokers and two other distinct card types w/ at least 2 of one of those
        // JJ 11 5
        val hasTwoJokersAndTwoSameCards = counts.jokers() == 2 && counts.size == 3 && counts.max() == 2

        // Case 4: has 1 joker and two other distinct card types w/ at least 3 of one of those
        // J 111 5
        val hasOneJokerAndThreeSameCards = counts.jokers() == 1 && counts.size == 3 && counts.max() == 3

        return hasFourSameCards ||
            hasThreeJokersAndTwoOtherDistinct ||
            hasTwoJokersAndTwoSameCards ||
            hasOneJokerAndThreeSameCards
    }

    /**
     * Determines if this hand is a full house.
     * @param counts Count of cards in this hand.
     * @return True if a full house, false otherwise.
     */
    private fun isFullHouse(counts: Map<Card, Int>): Boolean {
        // Case 1: no jokers, 3 of one kind of card, 2 of another
        // 111 55
        val hasThreeAndTwo = counts.jokers() == 0 && counts.max() == 3 && counts.size == 2

        // Case 2: has 1 joker and two pair (joker matches with other of the pair for full house)
        // J 11 55
        val hasOneJokerAndTwoPair = counts.jokers() == 1 && counts.max() == 2 && counts.size == 3

        return hasThreeAndTwo || hasOneJokerAndTwoPair
    }

    /**
     * Determines if this hand is a three of a kind.
     * @param counts Count of cards in this hand.
     * @return True if three of a kind, false otherwise.
     */
    private fun isThreeOfAKind(counts: Map<Card, Int>): Boolean {
        // Case 1: no jokers, 3 of one kind of card nd two other distinct cards
        // 111 5 9
        val hasThreeAndTwoOtherDistinct = counts.jokers() == 0 && counts.max() == 3 && counts.size == 3

        // Case 2: has 1 joker, a pair of one kind of card and two other distinct cards
        // J11 5 9
        val hasOneJokerAndOnePair = counts.jokers() == 1 && counts.max() == 2 && counts.size == 4

        // Case 3: has 2 jokers and 3 other distinct cards (joker match with any card to make three of a kind)
        // JJ1 5 9
        val hasTwoJokersAndThreeDistinct = counts.jokers() == 2 && counts.max() == 1 && counts.size == 4

        return hasThreeAndTwoOtherDistinct || hasOneJokerAndOnePair || hasTwoJokersAndThreeDistinct
    }

    /**
     * Determines if this hand is a two pair.
     * @param counts Count of cards in this hand.
     * @return True if two pair, false otherwise.
     */
    private fun isTwoPair(counts: Map<Card, Int>): Boolean {
        // Case 1: no jokers, two pairs of cards with one extra distinct card
        // 11 55 9
        val hasTwoPairAndOneDistinct = counts.jokers() == 0 && counts.max() == 2 && counts.size == 3

        return hasTwoPairAndOneDistinct
    }

    /**
     * Determines if this hand is a pair.
     * @param counts Count of cards in this hand.
     * @return True if a pair, false otherwise.
     */
    private fun isOnePair(counts: Map<Card, Int>): Boolean {
        // Case 1: no jokers, one pair and 3 distinct other cards
        // 11 5 6 9
        val hasOnePairAndThreeDistinct = counts.jokers() == 0 && counts.max() == 2 && counts.size == 4

        // Case 2: one joker, 4 distinct other cards (joker matches with any for a pair)
        // J 1 5 6 9
        val hasOneJokerAndFourDistinct = counts.jokers() == 1 && counts.size == 5

        return hasOnePairAndThreeDistinct || hasOneJokerAndFourDistinct
    }
}

/**
 * Compares this list of [Card] to the given list of cards.
 * @param other Cards to compare.
 * @return 1 if these cards are stronger than the other cards,
 * -1 if these cards are stronger,
 * or 0 if the cards are identical.
 */
private fun List<Card>.compareTo(other: List<Card>): Int {
    // Walk each card position in order to find the stronger card
    zip(other).forEach {
        val cardComparison = it.first.compareTo(it.second)
        if (cardComparison != 0) return cardComparison
    }

    // Identical cards
    return 0
}

/**
 * Gets the winnings for this list of [Hand].
 * @return Winnings.
 */
private fun List<Hand>.winnings() =
    sorted().mapIndexed { rank, hand ->
        (rank + 1) * hand.bid // Indexes are 0-based, add 1 to get true rank value
    }.sum().toLong()

/**
 * Converts this list of string to a pair of list of [Hand].
 * @param filter Filter for which card types are allowed.
 * @return Hands.
 */
internal fun List<String>.toHands(filter: (Card) -> Boolean) =
    map {
        val parts = it.split(" ")
        Hand(parts[0].toCards(filter), parts[1].toInt())
    }

/**
 * Gets the number of times [Card.JOKER] appears in this map.
 * @return Joker count.
 */
private fun Map<Card, Int>.jokers() = get(Card.JOKER) ?: 0

/**
 * Gets the largest number of times a single card appears, excluding jokers.
 * @return Card count or 0 if there are only jokers.
 */
private fun Map<Card, Int>.max() = filterKeys { it != Card.JOKER }.maxByOrNull { it.value }?.value ?: 0

/**
 * Converts this string an equivalent [Card].
 * @param filter Filter for which card types are allowed.
 * @return Card.
 */
private fun String.toCard(filter: (Card) -> Boolean) =
    Card.entries.filter {
        filter(it)
    }.find {
        it.symbol == this
    }!!

/**
 * Converts this string to a list of [Card].
 * @param filter Filter for which card types are allowed.
 * @return Cards.
 */
private fun String.toCards(filter: (Card) -> Boolean): List<Card> = splitMap("") { it.toCard(filter) }
