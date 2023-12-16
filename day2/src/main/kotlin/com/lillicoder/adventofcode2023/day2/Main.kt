package com.lillicoder.adventofcode2023.day2

import kotlin.math.max

fun main() {
	val games = GameParser().parseGames("input.txt")

	val idSum = GameIdSummationCalculator().sumValidGameIds(games)
	println("The sum of all valid game IDs is $idSum.")

	val powerSum = GameMinimumCubesPowerSummationCalculator().sumMinimumCubesPowers(games)
	println("The sum of all minimum cubes powers is $powerSum.")
}

/**
 * Represents a color of cube and the maximum allowed count of that color
 * in any given pull of a round of a game.
 * @param max Maximum amount of this color allowed in any given pull of cubes in a round.
 */
enum class Color(val max: Int) {
	BLUE(14),
	GREEN(13),
	RED(12)
}

/**
 * Represents a single game.
 * @param id Game ID.
 * @param rounds Game rounds.
 */
data class Game(
	val id: Int,
	val rounds: List<Round>
)

/**
 * Represents a single game round.
 * @param blue Blue [Pull].
 * @param green Green [Pull].
 * @param red Red [Pull].
 */
data class Round (
	val blue: Pull,
	val green: Pull,
	val red: Pull
)

/**
 * Represents a single pull of a color of cubes in a round.
 * @param color [Color] of the cubes pulled.
 * @param count Number of cubes pulled.
 */
data class Pull(
	val color: Color,
	val count: Int
)

/**
 * Parses game records into a list of [Game].
 */
class GameParser {

	/**
	 * Parses the file with the given filename and returns a list of [Game].
	 * @param filename Name of the file to parse.
	 * @return List of games.
	 */
	fun parseGames(filename: String): List<Game> {
		val games: MutableList<Game> = mutableListOf()
		javaClass.classLoader.getResourceAsStream(filename)!!.reader().forEachLine { line ->
			val id = line.substringBefore(": ").substringAfter("Game ").toInt()
			val rounds = parseRounds(line)
			games.add(Game(id, rounds))
		}

		return games
	}

	/**
	 * Parses the list of [Round] for the given raw game input.
	 * @param game Game to parse.
	 * @return List of rounds.
	 */
	private fun parseRounds(game: String): List<Round> {
		val rounds: MutableList<Round> = mutableListOf()

		game.substringAfter(": ").split("; ").forEach { round ->
			rounds.add(parseRound(round))
		}

		return rounds
	}

	/**
	 * Parses a [Round] from the given raw round input.
	 * @param round Round to parse.
	 * @return Round.
	 */
	private fun parseRound(round: String): Round {
		// Pack defaults, not all colors are guaranteed to be present in a round
		val pulls: MutableMap<Color, Pull> = mutableMapOf(
			Color.BLUE to Pull(Color.BLUE, 0),
			Color.GREEN to Pull(Color.GREEN, 0),
			Color.RED to Pull(Color.RED, 0)
		)

		round.split(", ").forEach { pull ->
			val parsed = parsePull(pull)
			pulls[parsed.color] = parsed
		}

		return Round(
			pulls[Color.BLUE]!!,
			pulls[Color.GREEN]!!,
			pulls[Color.RED]!!
		)
	}

	/**
	 * Parses a [Pull] from the given raw pull input.
	 * @param pull Pull to parse.
	 * @return Pull.
	 */
	private fun parsePull(pull: String): Pull {
		val pair = pull.split(" ")
		val count = pair[0].toInt()
		val color = Color.valueOf(pair[1].uppercase())
		return Pull(color, count)
	}
}

/**
 * Calculates the minimum number of cubes of each color that would satisfy a game's
 * pulls, finds the power of those minimum numbers for each game, and sums all
 * of those powers into a single result.
 */
class GameMinimumCubesPowerSummationCalculator {

	/**
	 * Sums all powers of required minimum cube sets for each [Game] in the given
	 * list of games.
	 * @param games Games to evaluate.
	 * @return Sum of minimum cubes powers.
	 */
	fun sumMinimumCubesPowers(games: List<Game>): Int {
		var sum = 0
		games.forEach { game ->
			val minimumCubes = minimumCubes(game)
			val power = minimumCubes.reduce { accumulator, element ->
				accumulator * element
			}
			sum += power
		}

		return sum
	}

	private fun minimumCubes(game: Game): List<Int> {
		// Find largest value per color in all rounds
		var blue = 0
		var green = 0
		var red = 0

		game.rounds.forEach { round ->
			blue = max(blue, round.blue.count)
			green = max(green, round.green.count)
			red = max(red, round.red.count)
		}

		return listOf(blue, green, red)
	}
}

/**
 * Calculates the sum of [Game] IDs whose pulls do not exceed the allowed maximums
 * for a given color.
 */
class GameIdSummationCalculator {

	/**
	 * Sums all IDs of values [Game] from the given list of games.
	 * @param games Games to evaluate.
	 * @return Sum of all valid game IDs.
	 */
	fun sumValidGameIds(games: List<Game>): Int {
		var sum = 0
		games.forEach { game ->
			if (isValidGame(game)) sum += game.id
		}

		return sum
	}

	/**
	 * Determines if the given [Game] is valid for the known maximum allowed
	 * cube per color.
	 * @param game Game to check.
	 * @return True if game is valid, false otherwise.
	 */
	private fun isValidGame(game: Game): Boolean {
		game.rounds.forEach { round ->
			if (!isValidRound(round)) return false
		}

		return true
	}

	/**
	 * Determines if the given [Round] is valid for the known maximum allowed cubes
	 * per color.
	 * @param round Round to check.
	 * @return True if the round is valid, false otherwise.
	 */
	private fun isValidRound(round: Round): Boolean {
		val pulls = listOf(round.blue, round.green, round.red)
		pulls.forEach { pull ->
			if (!isValidPull(pull)) return false
		}

		return true
	}

	/**
	 * Determines if the given [Pull] is valid for the known
	 * maximum allowed cubes per color.
	 * @param pull Pull to check.
	 * @return True if a valid pull, false otherwise.
	 */
	private fun isValidPull(pull: Pull) = pull.count <= pull.color.max
}
