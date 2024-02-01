package com.lillicoder.adventofcode2023.day7

fun main() {
    val day7 = Day7()
    val parser = HandParser()
    val (hands, jokerHands) = parser.parseFile("input.txt")
    println("[Normal] Total winnings for the given hands is ${day7.part1(hands)}.")
    println("[Jokers] Total winnings for the given hands is ${day7.part2(jokerHands)}.")
}

class Day7 {
    fun part1(hands: List<Hand>) = WinningsCalculator().computeWinnings(hands)

    fun part2(hands: List<Hand>) = WinningsCalculator().computeWinnings(hands)
}

/**
 * Represents a single card in a hand of a game of Camel Cards.
 */
enum class Card(
    private val symbol: String,
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
    ACE("A"), ;

    companion object {
        /**
         * Gets the [Card] enum matching the given symbol.
         * @param symbol Card symbol.
         * @param allowJokers True if 'J' should match [JOKER], false if 'J' should match [JACK].
         * @return Corresponding card.
         */
        fun from(
            symbol: String,
            allowJokers: Boolean = false,
        ) = when (symbol) {
            "J" -> if (allowJokers) JOKER else JACK
            else -> entries.find { it.symbol == symbol }!!
        }
    }
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
 */
data class Hand(
    val cards: List<Card>,
    val bid: Int,
    val rank: Rank,
) : Comparable<Hand> {
    override fun compareTo(other: Hand) =
        when (val rankComparison = rank.compareTo(other.rank)) {
            0 -> compareCards(other) // Same rank, sort by strongest card
            else -> rankComparison // Different rank, sort by rank
        }

    /**
     * Compares each card in this hand with the cards in the given [Hand].
     * @param other Hand to compare.
     * @return 1 if this hand's cards are stronger than the other's, -1 if the given hand's
     * cards are stronger, or 0 if the cards are identical.
     */
    private fun compareCards(other: Hand): Int {
        // Walk each card position in order to find the stronger card
        cards.zip(other.cards).forEach {
            val cardComparison = it.first.compareTo(it.second)
            if (cardComparison != 0) return cardComparison
        }

        // Identical cards
        return 0
    }
}

/**
 * Calculates winnings for a given list of [Hand] in a game of Camel Cards.
 */
class WinningsCalculator {
    /**
     * Determines the total winnings for the give list of [Hand].
     * @param hands Hands to evaluate.
     * @return Total winnings.
     */
    fun computeWinnings(hands: List<Hand>) =
        hands.sorted().mapIndexed { index, hand ->
            (index + 1) * hand.bid
        }.sum().toLong()
}

/**
 * Parses one or more [Hand] from some input.
 */
class HandParser {
    /**
     * Parses the raw hands input to a pair of list of [Hand].
     * @param raw Raw hands input.
     * @return Hands parsed as no having [Card.JOKER] and hands parsed as having [Card.JOKER].
     */
    fun parse(raw: List<String>): Pair<List<Hand>, List<Hand>> {
        val noJokers =
            raw.map { line ->
                val parts = line.split(" ")
                val cards = parts[0].split("").filter { it.isNotEmpty() }.map { Card.from(it, false) }
                val bid = parts[1].toInt()
                val rank = rank(cards)

                Hand(cards, bid, rank)
            }
        val jokers =
            noJokers.map { hand ->
                // Swap Jokers in for Jacks and recompute the rank
                val cards = hand.cards.map { if (it == Card.JACK) Card.JOKER else it }
                val rank = rank(cards)
                Hand(cards, hand.bid, rank)
            }

        return Pair(noJokers, jokers)
    }

    /**
     * Parses the file with the given filename to a pair of list of [Hand].
     * @param filename Filename of the file to parse.
     * @return Hands parsed as no having [Card.JOKER] and hands parsed as having [Card.JOKER].
     */
    fun parseFile(filename: String) = parse(javaClass.classLoader.getResourceAsStream(filename)!!.reader().readLines())

    /**
     * Determines the [Rank] of the given list of [Card].
     * @param cards Cards to rank.
     * @return Rank.
     */
    private fun rank(cards: List<Card>): Rank {
        // Count each card type by its frequency
        val frequency = cards.groupingBy { it }.eachCount()

        val noJokers = frequency.filterKeys { it != Card.JOKER }
        val biggestCount = if (noJokers.isEmpty()) 0 else noJokers.maxBy { it.value }.value
        val jokerCount = frequency[Card.JOKER] ?: 0

        if (isFiveOfAKind(frequency, jokerCount, biggestCount)) return Rank.FIVE_OF_A_KIND
        if (isFourOfAKind(frequency, jokerCount, biggestCount)) return Rank.FOUR_OF_A_KIND
        if (isFullHouse(frequency, jokerCount, biggestCount)) return Rank.FULL_HOUSE
        if (isThreeOfAKind(frequency, jokerCount, biggestCount)) return Rank.THREE_OF_A_KIND
        if (isTwoPair(frequency, jokerCount, biggestCount)) return Rank.TWO_PAIR
        if (isOnePair(frequency, jokerCount, biggestCount)) return Rank.ONE_PAIR

        // High card - only option left (should have grouped 5 keys)
        return Rank.HIGH_CARD
    }

    private fun isFiveOfAKind(
        frequency: Map<Card, Int>,
        jokerCount: Int,
        biggestCount: Int,
    ): Boolean {
        // Case 1: no jokers, all the same card
        // 1 1 1 1 1
        val allSame = frequency.size == 1

        // Case 2: any amount of jokers, jokers count plus biggest count = hand size
        // J 1 1 1 1
        // J J 1 1 1
        // J J J 1 1
        // J J J J 1
        return allSame || (jokerCount + biggestCount == frequency.values.sum())
    }

    private fun isFourOfAKind(
        frequency: Map<Card, Int>,
        jokerCount: Int,
        biggestCount: Int,
    ): Boolean {
        // Case 1: no jokers, 4 of one kind of card (4 jokers makes 5 of a kind, so don't need to check that)
        // 1111 5
        val hasFourSameCards = frequency.values.find { it == 4 } != null

        // Case 2: has 3 jokers and two other distinct cards (frequency == 3)
        // JJJ 1 5
        val hasThreeJokersAndTwoOtherDistinct = jokerCount == 3 && frequency.size == 3

        // Case 3: has 2 jokers and two other distinct card types w/ at least 2 of one of those
        // JJ 11 5
        val hasTwoJokersAndTwoSameCards = jokerCount == 2 && frequency.size == 3 && biggestCount == 2

        // Case 4: has 1 joker and two other distinct card types w/ at least 3 of one of those
        // J 111 5
        val hasOneJokerAndThreeSameCards = jokerCount == 1 && frequency.size == 3 && biggestCount == 3

        return hasFourSameCards ||
            hasThreeJokersAndTwoOtherDistinct ||
            hasTwoJokersAndTwoSameCards ||
            hasOneJokerAndThreeSameCards
    }

    private fun isFullHouse(
        frequency: Map<Card, Int>,
        jokerCount: Int,
        biggestCount: Int,
    ): Boolean {
        // Case 1: no jokers, 3 of one kind of card, 2 of another
        // 111 55
        val hasThreeAndTwo = jokerCount == 0 && biggestCount == 3 && frequency.size == 2

        // Case 2: has 1 joker and two pair (joker matches with other of the pair for full house)
        // J 11 55
        val hasOneJokerAndTwoPair = jokerCount == 1 && biggestCount == 2 && frequency.size == 3

        return hasThreeAndTwo || hasOneJokerAndTwoPair
    }

    private fun isThreeOfAKind(
        frequency: Map<Card, Int>,
        jokerCount: Int,
        biggestCount: Int,
    ): Boolean {
        // Case 1: no jokers, 3 of one kind of card nd two other distinct cards
        // 111 5 9
        val hasThreeAndTwoOtherDistinct = jokerCount == 0 && biggestCount == 3 && frequency.size == 3

        // Case 2: has 1 joker, a pair of one kind of card and two other distinct cards
        // J11 5 9
        val hasOneJokerAndOnePair = jokerCount == 1 && biggestCount == 2 && frequency.size == 4

        // Case 3: has 2 jokers and 3 other distinct cards (joker match with any card to make three of a kind)
        // JJ1 5 9
        val hasTwoJokersAndThreeDistinct = jokerCount == 2 && biggestCount == 1 && frequency.size == 4

        return hasThreeAndTwoOtherDistinct || hasOneJokerAndOnePair || hasTwoJokersAndThreeDistinct
    }

    private fun isTwoPair(
        frequency: Map<Card, Int>,
        jokerCount: Int,
        biggestCount: Int,
    ): Boolean {
        // Case 1: no jokers, two pairs of cards with one extra distinct card
        // 11 55 9
        val hasTwoPairAndOneDistinct = jokerCount == 0 && biggestCount == 2 && frequency.size == 3

        return hasTwoPairAndOneDistinct
    }

    private fun isOnePair(
        frequency: Map<Card, Int>,
        jokerCount: Int,
        biggestCount: Int,
    ): Boolean {
        // Case 1: no jokers, one pair and 3 distinct other cards
        // 11 5 6 9
        val hasOnePairAndThreeDistinct = jokerCount == 0 && biggestCount == 2 && frequency.size == 4

        // Case 2: one joker, 4 distinct other cards (joker matches with any for a pair)
        // J 1 5 6 9
        val hasOneJokerAndFourDistinct = jokerCount == 1 && frequency.size == 5

        return hasOnePairAndThreeDistinct || hasOneJokerAndFourDistinct
    }
}
