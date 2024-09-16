package com.lillicoder.adventofcode2023.day2

import com.lillicoder.adventofcode2023.io.Resources
import com.lillicoder.adventofcode2023.io.splitMap

fun main() {
    val day2 = Day2()
    val games =
        Resources.mapLines(
            "input.txt",
        ) {
            it.toGame()
        } ?: throw IllegalArgumentException("Could not read input from file.")
    println("The sum of all valid game IDs is ${day2.part1(games)}.")
    println("The sum of all minimum cubes powers is ${day2.part2(games)}.")
}

class Day2 {
    fun part1(games: List<Game>) = games.sumOf { if (it.isValid) it.id else 0 }

    fun part2(games: List<Game>) =
        games.sumOf { game ->
            listOf(
                game.rounds.maxByOrNull { it.blue.count }?.blue?.count ?: 0,
                game.rounds.maxByOrNull { it.green.count }?.green?.count ?: 0,
                game.rounds.maxByOrNull { it.red.count }?.red?.count ?: 0,
            ).reduce { accumulator, element -> accumulator * element }
        }
}

/**
 * Represents a color of cube and the maximum allowed count of that color
 * in any given pull of a round of a game.
 * @param max Maximum amount of this color allowed in any given pull of cubes in a round.
 */
enum class Color(val max: Int) {
    BLUE(14),
    GREEN(13),
    RED(12),
}

/**
 * Represents a single game.
 * @param id Game ID.
 * @param rounds Game rounds.
 * @param isValid True if this is a valid game, false otherwise.
 */
data class Game(
    val id: Int,
    val rounds: List<Round>,
    val isValid: Boolean = rounds.all { it.isValid },
)

/**
 * Represents a single game round.
 * @param blue Blue [Pull].
 * @param green Green [Pull].
 * @param red Red [Pull].
 * @param isValid True if this is a valid round, false otherwise.
 */
data class Round(
    val blue: Pull,
    val green: Pull,
    val red: Pull,
    val isValid: Boolean = listOf(blue, green, red).all { it.isValid },
)

/**
 * Represents a single pull of a color of cubes in a round.
 * @param color [Color] of the cubes pulled.
 * @param count Number of cubes pulled.
 * @param isValid True if this is a valid pull, false otherwise.
 */
data class Pull(
    val color: Color,
    val count: Int,
    val isValid: Boolean = count <= color.max,
)

/**
 * Converts this string to an equivalent [Game].
 * @return Game.
 */
internal fun String.toGame(): Game {
    val id = substringBefore(": ").substringAfter("Game ").toInt()
    val rounds = substringAfter(": ").splitMap("; ") { it.toRound() }
    return Game(id, rounds)
}

/**
 * Converts this string to an equivalent [Pull].
 * @return Pull.
 */
private fun String.toPull(): Pull {
    val pair = split(" ")
    val count = pair[0].toInt()
    val color = Color.valueOf(pair[1].uppercase())
    return Pull(color, count)
}

/**
 * Converts this string to an equivalent [Round].
 * @return Round.
 */
private fun String.toRound(): Round {
    val pulls =
        splitMap(", ") {
            it.toPull()
        }.associateBy {
            it.color
        }.withDefault {
            Pull(it, 0)
        }
    return Round(
        pulls.getValue(Color.BLUE),
        pulls.getValue(Color.GREEN),
        pulls.getValue(Color.RED),
    )
}
