package com.lillicoder.adventofcode2023.day1

fun main() {
	/**
	 * Steps to solve:
	 *
	 * 1) For each line of input
	 *     a) Find first occurring digit from left to right
	 *     b) Find last occurring digit from left to right
	 *     c) Concat those digits into a number
	 * 2) Accumulate concatenated numbers as a running sum
	 */
	val sum = CalibrationValueParser().sumCalibrationValues("input.txt")
	println("Total sum of calibration values is $sum.")
}

class CalibrationValueParser {

	private val wordToNumber: Map<String, Int> = mapOf(
		"one" to 1,
		"two" to 2,
		"three" to 3,
		"four" to 4,
		"five" to 5,
		"six" to 6,
		"seven" to 7,
		"eight" to 8,
		"nine" to 9
	)

	/**
	 * Sums all calibration values for each line of input for the file with the given filename.
	 * @param filename Name of the file to parse. File will be loaded from resources.
	 * @return Sum of all calibration values.
	 */
	fun sumCalibrationValues(filename: String): Int {
		var sum = 0
		javaClass.classLoader.getResourceAsStream(filename)!!.reader().forEachLine { line ->
			sum += parseCalibrationValue(line)
		}
		return sum //54953 is wrong
	}

	/**
	 * Parses the given line to find the first and last digit and concatenate them to a single
	 * value.
	 * @param line Line to parse.
	 * @return Calibration value.
	 */
	private fun parseCalibrationValue(line: String): Int {
		val first = findFirstDigit(line)
		val last = findLastDigit(line)
		val combined = "$first$last".toInt()
		println("Line parsed. [first=$first, last=$last, calibrationValue=$combined]")

		return combined
	}

	/**
	 * Finds the first digit in the given line. Both numeric characters and numeric words
	 * will be parsed.
	 * @param line Line to parse.
	 * @return First digit.
	 */
	private fun findFirstDigit(line: String): Int {
		val firstDigit = line.first { it.isDigit() }
		val firstDigitPosition = line.indexOf(firstDigit)
		val onePosition = "one".toRegex().findAll(line).firstOrNull()?.range?.first ?: Int.MAX_VALUE
		val twoPosition = "two".toRegex().findAll(line).firstOrNull()?.range?.first ?: Int.MAX_VALUE
		val threePosition = "three".toRegex().findAll(line).firstOrNull()?.range?.first ?: Int.MAX_VALUE
		val fourPosition = "four".toRegex().findAll(line).firstOrNull()?.range?.first ?: Int.MAX_VALUE
		val fivePosition = "five".toRegex().findAll(line).firstOrNull()?.range?.first ?: Int.MAX_VALUE
		val sixPosition = "six".toRegex().findAll(line).firstOrNull()?.range?.first ?: Int.MAX_VALUE
		val sevenPosition = "seven".toRegex().findAll(line).firstOrNull()?.range?.first ?: Int.MAX_VALUE
		val eightPosition = "eight".toRegex().findAll(line).firstOrNull()?.range?.first ?: Int.MAX_VALUE
		val ninePosition = "nine".toRegex().findAll(line).firstOrNull()?.range?.first ?: Int.MAX_VALUE
		return when (
			minOf(
				firstDigitPosition,
				onePosition,
				twoPosition,
				threePosition,
				fourPosition,
				fivePosition,
				sixPosition,
				sevenPosition,
				eightPosition,
				ninePosition
			)
		) {
			firstDigitPosition -> firstDigit.toString().toInt()
			onePosition -> wordToNumber["one"]!!
			twoPosition -> wordToNumber["two"]!!
			threePosition -> wordToNumber["three"]!!
			fourPosition -> wordToNumber["four"]!!
			fivePosition -> wordToNumber["five"]!!
			sixPosition -> wordToNumber["six"]!!
			sevenPosition -> wordToNumber["seven"]!!
			eightPosition -> wordToNumber["eight"]!!
			ninePosition -> wordToNumber["nine"]!!
			Int.MAX_VALUE -> throw NumberFormatException("This shouldn't happen.")
			else -> throw NumberFormatException("We didn't make a match :(")
		}
	}

	/**
	 * Finds the last digit in the given line. Both numeric characters and numeric words
	 * will be parsed.
	 * @param line Line to parse.
	 * @return Last digit.
	 */
	private fun findLastDigit(line: String): Int {
		val lastDigit = line.last { it.isDigit() }
		val lastDigitPosition = line.lastIndexOf(lastDigit)
		val onePosition = "one".toRegex().findAll(line).lastOrNull()?.range?.last ?: Int.MIN_VALUE
		val twoPosition = "two".toRegex().findAll(line).lastOrNull()?.range?.last ?: Int.MIN_VALUE
		val threePosition = "three".toRegex().findAll(line).lastOrNull()?.range?.last ?: Int.MIN_VALUE
		val fourPosition = "four".toRegex().findAll(line).lastOrNull()?.range?.last ?: Int.MIN_VALUE
		val fivePosition = "five".toRegex().findAll(line).lastOrNull()?.range?.last ?: Int.MIN_VALUE
		val sixPosition = "six".toRegex().findAll(line).lastOrNull()?.range?.last ?: Int.MIN_VALUE
		val sevenPosition = "seven".toRegex().findAll(line).lastOrNull()?.range?.last ?: Int.MIN_VALUE
		val eightPosition = "eight".toRegex().findAll(line).lastOrNull()?.range?.last ?: Int.MIN_VALUE
		val ninePosition = "nine".toRegex().findAll(line).lastOrNull()?.range?.last ?: Int.MIN_VALUE
		return when (
			maxOf(
				lastDigitPosition,
				onePosition,
				twoPosition,
				threePosition,
				fourPosition,
				fivePosition,
				sixPosition,
				sevenPosition,
				eightPosition,
				ninePosition
			)
		) {
			lastDigitPosition -> lastDigit.toString().toInt()
			onePosition -> wordToNumber["one"]!!
			twoPosition -> wordToNumber["two"]!!
			threePosition -> wordToNumber["three"]!!
			fourPosition -> wordToNumber["four"]!!
			fivePosition -> wordToNumber["five"]!!
			sixPosition -> wordToNumber["six"]!!
			sevenPosition -> wordToNumber["seven"]!!
			eightPosition -> wordToNumber["eight"]!!
			ninePosition -> wordToNumber["nine"]!!
			Int.MAX_VALUE -> throw NumberFormatException("This shouldn't happen.")
			else -> throw NumberFormatException("We didn't make a match :(")
		}
	}
}
