package com.lillicoder.adventofcode2023.grids

import kotlin.math.abs

/**
 * An arbitrary two-dimensional grid of [Node].
 */
data class Grid<T>(
    private val nodes: List<List<Node<T>>>,
    val width: Int = nodes[0].size,
    val height: Int = nodes.size
) {

    /**
     * Gets the list of [Node] adjacent to the given node.
     * @param node Node to get neighbors of.
     * @return Adjacent nodes.
     */
    private fun adjacent(node: Node<T>): List<Node<T>> {
        val adjacent = mutableListOf<Node<T>>()
        if (node.y > 0) {
            val top = nodes[node.y.toInt() - 1][node.x.toInt()]
            adjacent.add(top)
        }

        if (node.y < nodes.size - 1) {
            val bottom = nodes[node.y.toInt() + 1][node.x.toInt()]
            adjacent.add(bottom)
        }

        if (node.x > 0) {
            val left = nodes[node.y.toInt()][node.x.toInt() - 1]
            adjacent.add(left)
        }

        if (node.x < nodes[0].size - 1) {
            val right = nodes[node.y.toInt()][node.x.toInt() + 1]
            adjacent.add(right)
        }

        return adjacent
    }

    /**
     * Gets the Manhattan distance between the given starting and ending nodes.
     * @param start Start node.
     * @param end End node.
     * @return Manhattan distance.
     */
    fun distance(start: Node<T>, end: Node<T>) = abs(start.x - end.x) + abs(start.y - end.y)

    /**
     * Finds the first [Node] that satisfies the given predicate.
     * @param predicate Predicate to check.
     * @return Found node or null if no node satisfied the predicate.
     */
    fun find(predicate: (T) -> Boolean): Node<T>? {
        nodes.forEach {
            it.forEach { node ->
                if (predicate.invoke(node.value)) return node
            }
        }

        return null
    }

    /**
     * Performs the given action on each node in this grid.
     * @param action Action to perform.
     */
    private fun forEach(action: (Node<T>) -> Unit) = nodes.forEach {
        it.forEach { node ->
            action(node)
        }
    }

    /**
     * Performs the given action on each column in this grid.
     * @param action Action to perform.
     */
    fun forEachColumn(action: (List<Node<T>>) -> Unit) {
        for (column in 0..<width) {
            action(nodes.map { it[column] })
        }
    }

    /**
     * Performs the given action on each column in this grid.
     * @param action Action to perform.
     */
    fun forEachColumnIndexed(action: (Int, List<Node<T>>) -> Unit) {
        for (column in 0..<width) {
            action(column, nodes.map { it[column] })
        }
    }

    /**
     * Performs the given action on each row in this grid.
     * @param action Action to perform.
     */
    fun forEachRow(action: (List<Node<T>>) -> Unit) = nodes.forEach { action(it) }

    /**
     * Performs the given action on each row in this grid.
     * @param action Action to perform.
     */
    fun forEachRowIndexed(
        action: (Int, List<Node<T>>) -> Unit
    ) = nodes.forEachIndexed { index, node -> action(index, node)}

    /**
     * Filters all [Node] and returns a list of nodes matching the given predicate.
     * @param predicate Predicate to check.
     * @return Nodes matching predicate.
     */
    fun filter(predicate: (T) -> Boolean): List<Node<T>> {
        val filtered = mutableListOf<Node<T>>()
        forEach { if (predicate(it.value)) filtered.add(it) }
        return filtered
    }

    /**
     * Returns a [Grid] containing the results of applying the given transform function
     * to each node in the original grid.
     * @param transform Transform to apply.
     * @return Mapped grid.
     */
    fun map(transform: (Node<T>) -> Node<T>): Grid<T> {
        val mapped = mutableListOf<List<Node<T>>>()

        nodes.forEach { row ->
            val mappedRow = mutableListOf<Node<T>>()
            row.forEach { node ->
                mappedRow.add(transform(node))
            }
            mapped.add(mappedRow)
        }

        return Grid(mapped)
    }

    /**
     * Finds the path of [Node] from the starting node to the ending node.
     * @param start Starting node.
     * @param end Ending node.
     * @return Path or null if there is no path between the given nodes.
     */
    fun path(start: Node<T>, end: Node<T>): List<Node<T>>? {
        val queue = ArrayDeque<Node<T>>()
        val path = mutableListOf<Node<T>>()
        val explored = mutableMapOf<Node<T>, Node<T>>()

        queue.add(start)
        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()
            adjacent(next).forEach { adjacent ->
                if (adjacent == end) {
                    // Path found
                    var traceback = next
                    while (traceback != start) {
                        path.add(traceback)
                        traceback = explored[traceback]!!
                    }

                    // Exit
                    queue.clear()
                    return@forEach
                }

                if (!explored.contains(adjacent)) {
                    explored[adjacent] = next
                    queue.add(adjacent)
                }
            }
        }

        return path.ifEmpty { null }
    }

    override fun toString() = nodes.joinToString("\n") {
        it.joinToString("") {
            node -> node.value.toString()
        }
    }
}
