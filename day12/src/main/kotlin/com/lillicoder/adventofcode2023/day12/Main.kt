package com.lillicoder.adventofcode2023.day12

fun main() {
    val springs = SpringsParser().parse("input.txt")
    val calculator = SpringPermutationCalculator()
    //val arrangements = calculator.countValidArrangements(springs)
    //println("The number of valid arrangements for factor 1 is $arrangements.")

    val foldedArrangements = calculator.countValidArrangements(springs, 5)
    println("The number of valid arrangements for factor 5 is $foldedArrangements.")
}

class SpringCondition(
    val springs: String,
    val pattern: List<Int>
)

class SpringPermutationCalculator {

    private val damaged = '#'
    private val operational = '.'
    private val unknown = '?'

    private val cache = mutableMapOf<String, Boolean>()

    fun countValidArrangements(conditions: List<SpringCondition>, factor: Int = 1) = conditions.sumOf {
        var expandedSprings = it.springs
        val expandedPattern = it.pattern.toMutableList()

        for (index in 1..<factor) {
            expandedSprings += "$unknown${it.springs}"
            expandedPattern += it.pattern
        }

        countValidArrangements(expandedSprings, expandedPattern)
    }

    private fun countValidArrangements(springs: String, pattern: List<Int>): Long {
        var count = 0L

        springs.forEach { spring ->
            // Only consider unknown springs
            if (spring != unknown) return@forEach

            // Solve sub-problem as DAMAGED
            val asDamaged = springs.replaceFirst(spring.toString(), damaged.toString())
            count += countValidArrangements(asDamaged, pattern)

            // Solve sub-problem as OPERATIONAL
            val asOperational = springs.replaceFirst(spring.toString(), operational.toString())
            return count + countValidArrangements(asOperational, pattern)
        }

        // If we reach here, we have a set of springs where there are no UNKNOWN, check if valid
        return if (isValid(springs, pattern)) 1L else 0L
    }

    private fun isValid(springs: String, pattern: List<Int>): Boolean {
        val key = springs + pattern.joinToString("")
        return when (val cached = cache[key]) {
            null -> {
                // Springs must be all damaged or operational for this check, so
                // the length of each damaged group must match the pattern
                val groups = springs.split(".").filter { it.isNotEmpty() }
                val isValid = groups.size == pattern.size && groups.zip(pattern).all { it.first.length == it.second }
                cache[key] = isValid

                isValid
            }
            else -> cached
        }
    }
}

class SpringsParser {

    fun parse(filename: String): List<SpringCondition> {
        val conditions = mutableListOf<SpringCondition>()
        javaClass.classLoader.getResourceAsStream(filename)!!.reader().forEachLine { line ->
            val parts = line.split(" ")
            val springs = parts[0]
            val pattern = parts[1].split(",").map { it.toInt() }
            conditions.add(SpringCondition(springs, pattern))
        }

        return conditions
    }
}
