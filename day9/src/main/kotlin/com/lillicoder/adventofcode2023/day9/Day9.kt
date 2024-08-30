package com.lillicoder.adventofcode2023.day9

import com.lillicoder.adventofcode2023.io.Resources
import com.lillicoder.adventofcode2023.io.splitMap

fun main() {
    val day9 = Day9()
    val readings =
        Resources.lines(
            "input.txt",
        )?.toReadings() ?: throw IllegalArgumentException("Could not read input from file.")
    println("The sum of all next predictions for all sequences is ${day9.part1(readings)}.")
    println("The sum of all previous predictions for all sequences is ${day9.part2(readings)}.")
}

class Day9 {
    fun part1(readings: List<List<Long>>) = readings.sumOf { it.predictNext() }

    fun part2(readings: List<List<Long>>) = readings.sumOf { it.predictPreceding() }
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
internal fun List<String>.toReadings() =
    map { string ->
        string.splitMap(" ") { digit ->
            digit.toLong()
        }
    }
