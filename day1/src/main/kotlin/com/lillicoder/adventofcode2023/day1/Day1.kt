package com.lillicoder.adventofcode2023.day1

fun main() {
    val day1 = Day1()
    val calibrationDocument = CalibrationDocumentParser().parse("input.txt")
    println("[Part 1] The sum of calibration values is ${day1.part1(calibrationDocument)}.")
    println("[Part 2] The sum of calibration values is ${day1.part2(calibrationDocument)}.")
}

class Day1 {
    fun part1(document: List<String>) =
        document.sumOf { line ->
            "${line.find { it.isDigit() }}${line.findLast { it.isDigit() }}".toInt()
        }.toLong()

    fun part2(
        document: List<String>,
        finder: CalibrationValueFinder = CalibrationValueFinder(),
    ) = document.sumOf { line ->
        finder.find(line)
    }.toLong()
}

class CalibrationDocumentParser {
    /**
     * Parses the calibration document from the file with the given filename.
     * @param filename Filename.
     * @return Calibration document.
     */
    fun parse(filename: String) = javaClass.classLoader.getResourceAsStream(filename)!!.reader().readLines()
}

/**
 * Utility that can find calibration values from a calibration document.
 */
class CalibrationValueFinder {
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
     * Parses the given line to find the first and last digit and concatenate them to a single value.
     * @param line Line to parse.
     * @return Calibration value.
     */
    fun find(line: String) = "${findFirstDigit(line)}${findLastDigit(line)}".toInt()

    /**
     * Finds the first digit in the given line. Both numeric characters and numeric words
     * will be parsed.
     * @param line Line to parse.
     * @return First digit.
     */
    private fun findFirstDigit(line: String): Int {
        val digitsByPosition = mutableMapOf<Int, Int>()

        // Pack first digit (if any) into a map of position -> digit
        line.firstOrNull { it.isDigit() }?.let { digitsByPosition[line.indexOf(it)] = it.digitToInt() }

        // Pack position for "one" through "nine" into map of position -> digit
        wordToNumber.map {
            (it.key.toRegex().findAll(line).firstOrNull()?.range?.first ?: Int.MAX_VALUE) to it.value
        }.toMap(digitsByPosition)

        // Smallest key is the first digit value in the line
        return digitsByPosition[digitsByPosition.keys.min()]!!
    }

    /**
     * Finds the last digit in the given line. Both numeric characters and numeric words
     * will be parsed.
     * @param line Line to parse.
     * @return Last digit.
     */
    private fun findLastDigit(line: String): Int {
        val digitsByPosition = mutableMapOf<Int, Int>()

        // Pack last digit (if any) into a map of position -> digit
        line.lastOrNull { it.isDigit() }?.let { digitsByPosition[line.lastIndexOf(it)] = it.digitToInt() }

        // Pack position for "one" through "nine" into map of position -> digit
        wordToNumber.map {
            (it.key.toRegex().findAll(line).lastOrNull()?.range?.last ?: Int.MIN_VALUE) to it.value
        }.toMap(digitsByPosition)

        // Largest key is the last digit value in the line
        return digitsByPosition[digitsByPosition.keys.max()]!!
    }
}
