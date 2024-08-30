package com.lillicoder.adventofcode2023.day8

import com.lillicoder.adventofcode2023.io.Resources
import com.lillicoder.adventofcode2023.io.splitNotEmpty
import com.lillicoder.adventofcode2023.math.Math

fun main() {
    val day8 = Day8()
    val network =
        Resources.text(
            "input.txt",
        )?.toNetwork() ?: throw IllegalArgumentException("Could not read input from file.")
    println("Total number of steps required to navigate the network is ${day8.part1(network)}.")
    println("Total number of steps required to navigate the network for a ghost is ${day8.part2(network)}.")
}

class Day8 {
    fun part1(network: Network) = network.stepsRequiredToNavigateWholeNodes()

    fun part2(network: Network) = network.stepsRequiredToNavigatePartialNodes()
}

/**
 * Represents a network of [Node] and their associated navigation instructions.
 * @param instructions Navigation instructions (e.g. "RL").
 * @param nodes Map of nodes keyed by node ID.
 */
data class Network(
    val instructions: String,
    val nodes: Map<String, Node>,
) {
    /**
     * Determines the number of steps required to navigate this network.
     * Nodes are treated as partial IDs based on last letter of the ID.
     * @return Number of steps to navigate from all starting nodes (__A) to all ending nodes (__Z).
     */
    fun stepsRequiredToNavigatePartialNodes(): Long {
        /**
         * Brute force solution here will take a very long time due to the combinatorial explosion
         * of possibilities. Annoyingly, the input for this question is structured such that you
         * can actually trace cycles of each starting node and find the least common multiple of
         * the steps required per starting node to actually compute the total steps needed. This isn't
         * actually an explicit guarantee of the problem statement for Day 8, so it's totally possible
         * to have input that doesn't really work out that way.
         *
         * To avoid spending a huge amount of time on a more generic solution, here's the LCM approach.
         */
        var steps = 0L

        // We only care about the last character of an ID for the purposes of
        // getting starting nodes and ending nodes
        var filtered = nodes.values.filter { it.id.endsWith("A") }
        val stepCountPerStart = MutableList(filtered.size) { 0L }
        val instructions = instructions.splitNotEmpty("")
        do {
            instructions.forEach { instruction ->
                // Navigate all nodes per instruction
                filtered =
                    filtered.map { node ->
                        // Get the next node ID
                        val nodeId =
                            when (instruction) {
                                "L" -> node.left!!
                                else -> node.right!!
                            }
                        // Navigate to next node
                        nodes[nodeId]!!
                    }.toMutableList()

                // Update steps by 1
                steps++

                // Update final condition check
                filtered.forEachIndexed { index, node ->
                    if (node.id.endsWith("Z") && stepCountPerStart[index] == 0L) {
                        // Found the end of a path for one of the starts, update
                        stepCountPerStart[index] = steps
                    }
                }
            }
        } while (stepCountPerStart.any { it == 0L })

        // All cycles checked, we want the least common multiple between them all to determine
        // total steps
        return Math.leastCommonMultiple(stepCountPerStart)
    }

    /**
     * Determines the number of steps required to navigate this network.
     * Nodes are treated as whole IDs.
     * @return Number of steps to navigate from starting node (AAA) to ending node (ZZZ).
     */
    fun stepsRequiredToNavigateWholeNodes(): Long {
        var steps = 0L

        // First node is always AAA
        var node = nodes[nodes.keys.first()]!!
        val instructions = instructions.splitNotEmpty("")

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
                node = nodes[nodeId]!!

                println("[Whole] Next node discovered. [node=$node]")

                // Update steps by 1
                steps++
            }
        } while (node.id.contentEquals("ZZZ").not())

        println("[Whole] Final node discovered in $steps steps. [node=$node]")
        return steps
    }
}

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
 * Converts this string to an equivalent [Network].
 * @param separator Line separator.
 * @return Network.
 */
internal fun String.toNetwork(separator: String = System.lineSeparator()): Network {
    val parts = split("$separator$separator")
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
