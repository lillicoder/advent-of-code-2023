package com.lillicoder.adventofcode2023.day1

import com.lillicoder.adventofcode2023.io.Resources

fun main() {
    val day1 = Day1()
    val calibrationDocument =
        Resources.lines(
            "input.txt",
        ) ?: throw IllegalArgumentException("Could not read input from file.")
    println("[Part 1] The sum of calibration values is ${day1.part1(calibrationDocument)}.")
    println("[Part 2] The sum of calibration values is ${day1.part2(calibrationDocument)}.")
}

class Day1 {
    fun part1(document: List<String>) =
        document.sumOf { line ->
            "${line.first { it.isDigit() }}${line.last { it.isDigit() }}".toInt()
        }.toLong()

    fun part2(document: List<String>) =
        document.sumOf { line ->
            line.joinFirstAndLastDigits()
        }.toLong()
}

/**
 * Represents a position in a line.
 */
private enum class Position {
    FIRST,
    LAST,
}

/**
 * Set of functions for doing digit lookups in a string.
 * @param digitSelector Selects a [Char] that is also a digit from a string.
 * @param indexSelector Selects a position for a [Char] within a string.
 * @param wordSelector Selects a position for a numeric word from a string.
 * @param keySelector Selects a key from a set of digit positions.
 */
private data class DigitLookup(
    val digitSelector: (String) -> Char?,
    val indexSelector: (String, Char) -> Int,
    val wordSelector: (String) -> Int,
    val keySelector: (Set<Int>) -> Int,
)

/**
 * Finds the digit in this string for the given [Position]. Both numeric characters
 * and numeric words will be parsed.
 * @return Digit.
 */
private fun String.digit(position: Position): Int {
    val wordToNumber =
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
    val (digitSelector, indexSelector, wordSelector, keySelector) =
        when (position) {
            Position.FIRST -> {
                DigitLookup(
                    { it.firstOrNull { it.isDigit() } },
                    { line, digit -> line.indexOf(digit) },
                    { it.toRegex().findAll(this).firstOrNull()?.range?.first ?: Int.MAX_VALUE },
                    { it.min() },
                )
            }
            Position.LAST -> {
                DigitLookup(
                    { it.lastOrNull { it.isDigit() } },
                    { line, digit -> line.lastIndexOf(digit) },
                    { it.toRegex().findAll(this).lastOrNull()?.range?.last ?: Int.MIN_VALUE },
                    { it.max() },
                )
            }
        }

    // Pack digit (if any) into a map of position -> digit
    val digitsByPosition = mutableMapOf<Int, Int>()
    digitSelector(this)?.let { digitsByPosition[indexSelector(this, it)] = it.digitToInt() }

    // Pack position for "one" through "nine" into map of position -> digit
    wordToNumber.map { wordSelector(it.key) to it.value }.toMap(digitsByPosition)

    // Smallest key is the first digit value in the line; largest key is the last digit value in the line
    return digitsByPosition[keySelector(digitsByPosition.keys)]!!
}

/**
 * Finds the first digit in this string. Both numeric characters
 * and numeric words will be parsed.
 * @return First digit.
 */
private fun String.firstDigit() = digit(Position.FIRST)

/**
 * Finds the last digit in this string. Both numeric characters
 * and numeric words will be parsed.
 * @return Last digit.
 */
private fun String.lastDigit() = digit(Position.LAST)

/**
 * Finds the first and last digits in this string and concatenates them.
 * @return Joined digits.
 */
private fun String.joinFirstAndLastDigits() = "${firstDigit()}${lastDigit()}".toInt()
