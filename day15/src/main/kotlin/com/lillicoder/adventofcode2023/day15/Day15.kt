package com.lillicoder.adventofcode2023.day15

fun main() {
    val day15 = Day15()
    val input = day15.parse("input.txt")
    println("The total of hashes for the given input is ${day15.part1(input)}.")
    println("The total focusing power of loaded lenses is ${day15.part2(input)}.")
}

class Day15 {
    fun part1(input: List<String>) = input.sumOf { hash(it) }

    fun part2(input: List<String>): Long {
        // Put lenses into one of 256 'boxes'; using LinkedHashMap to preserve insertion
        // order for keys and to get O(1) insert/remove performance
        val boxes = MutableList(256) { LinkedHashMap<String, Int>() }
        input.forEach { string ->
            val (label, focal) = parseInstruction(string)
            val box = boxes[hash(label).toInt()]
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

    private fun parseInstruction(input: String): Pair<String, String?> {
        val operation = if (input.last().isDigit()) "=" else "-"
        val split = input.split(operation)
        return Pair(split[0], split.getOrNull(1))
    }

    /**
     * Hashes the given string as per the hashing rules for Day 15.
     */
    private fun hash(input: String): Long {
        var current = 0L
        input.codePoints().forEach { code ->
            current += code
            current *= 17
            current %= 256
        }

        return current
    }

    /**
     * Parses the input for this puzzle from the file with the given filename.
     * @param filename Filename.
     * @return Parsed input.
     */
    fun parse(filename: String) =
        javaClass.classLoader.getResourceAsStream(
            filename,
        )!!.reader().readText().split(",")
}
