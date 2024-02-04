package com.lillicoder.adventofcode2023.day8

import kotlin.math.max

fun main() {
    val day8 = Day8()
    val network = NetworkParser().parseFile("input.txt")
    println("Total number of steps required to navigate the network is ${day8.part1(network)}.")
    println("Total number of steps required to navigate the network for a ghost is ${day8.part2(network)}.")
}

class Day8 {
    fun part1(network: Network) = Navigator().stepsRequiredToNavigateWholeNodes(network)

    fun part2(network: Network) = Navigator().stepsRequiredToNavigatePartialNodes(network)
}

/**
 * Represents a network of [Node] and their associated navigation instructions.
 * @param instructions Navigation instructions (e.g. "RL").
 * @param nodes Map of nodes keyed by node ID.
 */
data class Network(
    val instructions: String,
    val nodes: Map<String, Node>,
)

/**
 * Represents a single node in a network.
 * @param id Node ID (e.g. AAA).
 * @param left Left node ID.
 * @param right Right node ID.
 */
data class Node(
    val id: String,
    var left: String? = null,
    var right: String? = null,
)

/**
 * Finds the number of steps required for navigating a [Network].
 */
class Navigator {
    /**
     * Determines the number of steps required to navigate the given [Network]. Nodes
     * are treated as whole IDs.
     * @param network Network to evaluate.
     * @return Number of steps to navigate from starting node (AAA) to ending node (ZZZ).
     */
    fun stepsRequiredToNavigateWholeNodes(network: Network): Long {
        var steps = 0L

        // First node is always AAA
        var node = network.nodes[network.nodes.keys.first()]!!
        val instructions = network.instructions.split("").filter { it.isNotEmpty() }

        println("[Whole] Starting node discovered. [node=$node]")

        do {
            // Until we find ZZZ, loop over each instruction and track to the next node
            instructions.forEach { instruction ->
                // Get the next node ID
                val nodeId =
                    when (instruction) {
                        "L" -> node.left!!
                        else -> node.right!!
                    }
                // Navigate to next node
                node = network.nodes[nodeId]!!

                println("[Whole] Next node discovered. [node=$node]")

                // Update steps by 1
                steps++
            }
        } while (node.id.contentEquals("ZZZ").not())

        println("[Whole] Final node discovered in $steps steps. [node=$node]")
        return steps
    }

    /**
     * Determines the number of steps required to navigate the given [Network]. Nodes
     * are treated as partial IDs based on last letter of the ID.
     * @param network Network to evaluate.
     * @return Number of steps to navigate from all starting nodes (__A) to all ending nodes (__Z).
     */
    fun stepsRequiredToNavigatePartialNodes(network: Network): Long {
        /**
         * Brute force solution here will take a very long time due to the combinatorial explosion
         * of possibilities. Annoyingly, the input for this question is structured such that you
         * can actually trace cycles of each starting node and find the least common multiple of
         * the steps required per starting node to actually compute the total steps needed. This isn't
         * actually an explicit guarantee of the problem statement for Day 8 and its totally possible
         * to have input that doesn't really work out that way.
         *
         * To avoid spending a huge amount of time on a more generic solution, here's the LCM approach.
         */

        var steps = 0L

        // We only care about the last character of an ID for the purposes of
        // getting starting nodes and ending nodes
        var nodes = network.nodes.values.filter { it.id.endsWith("A") }
        val stepCountPerStart = MutableList(nodes.size) { 0L }
        val instructions = network.instructions.split("").filter { it.isNotEmpty() }
        do {
            instructions.forEach { instruction ->
                // Navigate all nodes per instruction
                nodes =
                    nodes.map { node ->
                        // Get the next node ID
                        val nodeId =
                            when (instruction) {
                                "L" -> node.left!!
                                else -> node.right!!
                            }
                        // Navigate to next node
                        network.nodes[nodeId]!!
                    }.toMutableList()

                // Update steps by 1
                steps++

                // Update final condition check
                nodes.forEachIndexed { index, node ->
                    if (node.id.endsWith("Z") && stepCountPerStart[index] == 0L) {
                        // Found the end of a path for one of the starts, update
                        stepCountPerStart[index] = steps
                    }
                }
            }
        } while (stepCountPerStart.any { it == 0L })

        // All cycles checked, we want the least common multiple between them all to determine
        // total steps
        return leastCommonMultiple(stepCountPerStart)
    }

    /**
     * Finds the least common multiple among the given values.
     * @param values Values to find the least common multiple of.
     * @return least common multiple.
     */
    private fun leastCommonMultiple(values: List<Long>): Long {
        var lcm = values.first()
        for (index in 1 until values.size) {
            lcm = leastCommonMultiple(lcm, values[index])
        }

        return lcm
    }

    /**
     * Finds the least common multiple of the two given numbers.
     * @param first First number.
     * @param second Second number.
     * @return least common multiple.
     */
    private fun leastCommonMultiple(
        first: Long,
        second: Long,
    ): Long {
        val largest = max(first, second)
        val max = first * second

        var lcm = largest
        while (lcm < max) {
            if (lcm % first == 0L && lcm % second == 0L) return lcm
            lcm += largest
        }

        return max
    }
}

/**
 * Parser for [Network] instances.
 */
class NetworkParser {
    /**
     * Parses the given raw network input to an equivalent [Network].
     * @param raw Raw network input.
     * @param separator Line separator for the given input.
     * @return Network.
     */
    fun parse(
        raw: String,
        separator: String = System.lineSeparator(),
    ): Network {
        val parts = raw.split("$separator$separator")
        val instructions = parts[0] // First line is instructions

        val nodes = mutableMapOf<String, Node>()
        val rawNodes = parts[1].split(separator).sorted() // Second part is a line-separated list of nodes
        rawNodes.forEach { node ->
            // Node format is ID = (leftId, rightId)
            // e.g. AAA = (BBB, CCC)
            val id = node.substringBefore("=").trim()
            val childIds = node.substringAfter("= (").dropLast(1) // Removes parentheses
            val leftId = childIds.substringBefore(",")
            val rightId = childIds.substringAfter(", ")

            nodes[id]?.let {
                // Previously processed, update references
                it.left = leftId
                it.right = rightId
            } ?: Node(id, leftId, rightId).let {
                // New node, cache
                nodes[id] = it
            }
        }

        return Network(instructions, nodes)
    }

    /**
     * Parses the file with the given filename to a [Network].
     * @param filename Filename.
     * @return Network.
     */
    fun parseFile(filename: String) = parse(javaClass.classLoader.getResourceAsStream(filename)!!.reader().readText())
}
