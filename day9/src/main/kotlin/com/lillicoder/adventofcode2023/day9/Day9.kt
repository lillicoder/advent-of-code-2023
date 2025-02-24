package com.lillicoder.adventofcode2023.day9

import com.lillicoder.adventofcode.kotlin.io.Resources
import com.lillicoder.adventofcode.kotlin.text.splitNotEmpty

fun main() {
    val day9 = Day9()
    val input =
        Resources.lines(
            "input.txt",
        ) ?: throw IllegalArgumentException("Could not read input from file.")
    println("[Part 1] The sum of all next predictions for all sequences is ${day9.part1(input)}.")
    println("[Part 2] The sum of all previous predictions for all sequences is ${day9.part2(input)}.")
}

class Day9 {
    fun part1(input: List<String>) = input.toReadings().sumOf { it.predictNext() }

    fun part2(input: List<String>) = input.toReadings().sumOf { it.predictPreceding() }
}

/**
 * Predicts the preceding history for these values.
 * @return Preceding history.
 */
private fun List<Long>.predictPreceding(): Long {
    var currentRow = this
    val leftmostBranch = mutableListOf(first())

    while (!currentRow.all { it == 0L }) {
        currentRow = currentRow.windowed(2, 1).map { it[1] - it[0] }
        leftmostBranch.add(currentRow.first())
    }

    // Can't do a running value while windowing, need to process list in proper order
    // from 0 to bottom level of the tree
    return leftmostBranch.asReversed().reduce { accumulator, value ->
        value - accumulator
    }
}

/**
 * Predicts the next history for these values.
 * @return Next history.
 */
private fun List<Long>.predictNext(): Long {
    // We can predict solely by summing rightmost branch values as we go
    var currentRow = this
    var prediction = currentRow.last()
    while (currentRow.last() != 0L) {
        currentRow = currentRow.windowed(2, 1).map { it[1] - it[0] }
        prediction += currentRow.last()
    }

    return prediction
}

/**
 * Converts these strings to an equivalent list of readings.
 * @return Readings.
 */
private fun List<String>.toReadings() =
    map { string ->
        string.splitNotEmpty(" ").map { digit ->
            digit.toLong()
        }
    }
