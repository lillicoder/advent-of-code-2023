package com.lillicoder.adventofcode2023.day15

import com.lillicoder.adventofcode2023.io.Resources
import kotlin.streams.asSequence

fun main() {
    val day15 = Day15()
    val input =
        Resources.text(
            "input.txt",
        )?.split(
            ",",
        ) ?: throw IllegalArgumentException("Could not read input from file.")
    println("The total of hashes for the given input is ${day15.part1(input)}.")
    println("The total focusing power of loaded lenses is ${day15.part2(input)}.")
}

class Day15 {
    fun part1(input: List<String>) = input.sumOf { it.hash() }

    fun part2(input: List<String>): Long {
        // Put lenses into one of 256 'boxes'; using LinkedHashMap to preserve insertion
        // order for keys and to get O(1) insert/remove performance
        val boxes = MutableList(256) { LinkedHashMap<String, Int>() }
        input.forEach { raw ->
            val (label, focal) = raw.toInstructions()
            val box = boxes[label.hash().toInt()]
            when (focal?.isNotEmpty()) {
                true -> box[label] = focal.toInt()
                else -> box.remove(label)
            }
        }

        // Apply the focus power formula to each lens in each box and sum them all up
        return boxes.mapIndexed { boxIndex, box ->
            box.entries.mapIndexed { index, entry ->
                (boxIndex + 1) * (index + 1) * entry.value
            }.sum()
        }.sum().toLong()
    }
}

/**
 * Hashes the given string as per the hashing rules for Day 15.
 */
private fun String.hash() =
    codePoints().asSequence().fold(0L) { left, right ->
        ((left + right) * 17) % 256
    }

/**
 * Converts this string to an instruction pair.
 * @return Instruction pair.
 */
private fun String.toInstructions(): Pair<String, String?> {
    val operation = if (last().isDigit()) "=" else "-"
    val split = split(operation)
    return Pair(split[0], split.getOrNull(1))
}
