/*
 * Copyright 2026 Scott Weeden-Moody
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this project except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lillicoder.adventofcode2023.day2

import com.lillicoder.adventofcode.kotlin.io.Resources
import com.lillicoder.adventofcode.kotlin.text.splitNotEmpty

fun main() {
    val day2 = Day2()
    val input =
        Resources.lines(
            "input.txt",
        ) ?: throw IllegalArgumentException("Could not read input from file.")
    println("[Part 1] The sum of all valid game IDs is ${day2.part1(input)}.")
    println("[Part 2] The sum of all minimum cubes powers is ${day2.part2(input)}.")
}

class Day2 {
    fun part1(input: List<String>) =
        input
            .map {
                it.toGame()
            }.sumOf {
                if (it.isValid) it.id else 0
            }

    fun part2(input: List<String>) =
        input
            .map {
                it.toGame()
            }.sumOf { game ->
                listOf(
                    game.rounds
                        .maxByOrNull { it.blue.count }
                        ?.blue
                        ?.count ?: 0,
                    game.rounds
                        .maxByOrNull { it.green.count }
                        ?.green
                        ?.count ?: 0,
                    game.rounds
                        .maxByOrNull { it.red.count }
                        ?.red
                        ?.count ?: 0,
                ).reduce { accumulator, element ->
                    accumulator * element
                }
            }
}

/**
 * Represents a color of cube and the maximum allowed count of that color
 * in any given pull of a round of a game.
 * @param max Maximum amount of this color allowed in any given pull of cubes in a round.
 */
private enum class Color(
    val max: Int,
) {
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
private data class Game(
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
private data class Round(
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
private data class Pull(
    val color: Color,
    val count: Int,
    val isValid: Boolean = count <= color.max,
)

/**
 * Converts this string to an equivalent [Game].
 * @return Game.
 */
private fun String.toGame(): Game {
    val id = substringBefore(": ").substringAfter("Game ").toInt()
    val rounds = substringAfter(": ").splitNotEmpty("; ").map { it.toRound() }
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
        splitNotEmpty(
            ", ",
        ).map {
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
