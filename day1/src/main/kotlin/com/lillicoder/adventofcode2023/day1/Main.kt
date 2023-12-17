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
    /**
     * Map of the digits 1 through 9 to their English-language word equivalent.
     */
    private val wordToNumber: Map<String, Int> =
        mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9,
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
        return sum
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
        // Pack first digit into a map of position -> digit
        val firstDigit = line.first { it.isDigit() }
        val digitsByPosition = mutableMapOf(line.indexOf(firstDigit) to firstDigit.toString().toInt())

        // Pack position for "one" through "nine" into map of position -> digit
        wordToNumber.map {
            (it.key.toRegex().findAll(line).firstOrNull()?.range?.first ?: Int.MAX_VALUE) to it.value
        }.toMap(digitsByPosition)

        // Smallest key is the first digit value in the line
        return digitsByPosition[digitsByPosition.keys.min()]!! // We are guaranteed to have packed a value here
    }

    /**
     * Finds the last digit in the given line. Both numeric characters and numeric words
     * will be parsed.
     * @param line Line to parse.
     * @return Last digit.
     */
    private fun findLastDigit(line: String): Int {
        // Pack last digit into a map of position -> digit
        val lastDigit = line.last { it.isDigit() }
        val digitsByPosition = mutableMapOf(line.lastIndexOf(lastDigit) to lastDigit.toString().toInt())

        // Pack position for "one" through "nine" into map of position -> digit
        wordToNumber.map {
            (it.key.toRegex().findAll(line).lastOrNull()?.range?.last ?: Int.MIN_VALUE) to it.value
        }.toMap(digitsByPosition)

        // Largest key is the last digit value in the line
        return digitsByPosition[digitsByPosition.keys.max()]!!
    }
}
