package com.lillicoder.adventofcode2023.day9

fun main() {
    val readings = ReadingsParser().parse("input.txt")

    val predictor = ReadingsPredictor()
    val proceedingSum = predictor.sumAllProceedingPredictions(readings)
    println("The sum of all next predictions for all sequences is $proceedingSum.")

    val precedingSum = predictor.sumAllPrecedingPredictions(readings)
    println("The sum of all previous predictions for all sequences is $precedingSum.")
}

class ReadingsPredictor {

    /**
     * Finds all predicted values that proceed the last reading in each of the given sequences
     * and sums them.
     * @param readings Sequence readings to predict.
     * @return Sum of all proceeding predictions.
     */
    fun sumAllProceedingPredictions(readings: List<List<Long>>) = readings.sumOf { predictNext(it) }

    /**
     * Finds all predicted values that precede the first reading in each of the given sequences
     * and sums them.
     * @param readings Sequence of readings to predict.
     * @return Sum of all preceding predictions.
     */
    fun sumAllPrecedingPredictions(readings: List<List<Long>>) = readings.sumOf { predictPreceding(it) }

    /**
     * Predicts the next value in the given sequence.
     * @param sequence Sequence to predict.
     * @return Next predicted value in the sequence.
     */
    private fun predictNext(sequence: List<Long>): Long {
        // We can predict solely by summing rightmost branch values as we go
        var currentRow = sequence
        var prediction = currentRow.last()
        while (currentRow.last() != 0L) {
            currentRow = currentRow.windowed(2, 1).map { it[1] - it[0] }
            prediction += currentRow.last()
        }

        return prediction
    }

    /**
     * Predicts the preceding value in the given sequence.
     * @param sequence Sequence to predict.
     * @return Preceding predicted value in the sequence.
     */
    private fun predictPreceding(sequence: List<Long>): Long {
        var currentRow = sequence
        val leftmostBranch = mutableListOf(sequence.first())

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
}

class ReadingsParser {

    /**
     * Parses the file with the given filename in a list of reading sequences.
     * @param filename Filename.
     * @return List of reading sequences.
     */
    fun parse(filename: String): List<List<Long>> {
        val readings = mutableListOf<List<Long>>()

        javaClass.classLoader.getResourceAsStream(filename)!!.reader().forEachLine { line ->
            val sequence = line.split(" ").filter { it.isNotEmpty() }.map { it.toLong() }
            readings.add(sequence)
        }

        return readings
    }
}
