package com.lillicoder.adventofcode2023.grids

import kotlin.math.abs

/**
 * An arbitrary two-dimensional grid of [Node].
 */
data class Grid<T>(
    private val nodes: List<List<Node<T>>>,
    val width: Int = nodes[0].size,
    val height: Int = nodes.size,
) {
    /**
     * Gets the list of [Node] adjacent to the given node. A node is considered
     * adjacent if it is directly above, below, to the left of, or to the right of
     * the given node.
     * @param node Node.
     * @return Adjacent nodes.
     */
    fun adjacent(node: Node<T>) = adjacent(node) { _, _ -> true }

    /**
     * Gets the list of [Node] adjacent to the given node that satisfy the given predicate.
     * A node is considered adjacent if it is directly above, below, to the left of, or to the right of
     * the given node.
     * @param node Node.
     * @param predicate Predicate to check.
     * @return Adjacent nodes.
     */
    fun adjacent(
        node: Node<T>,
        predicate: (Node<T>, Direction) -> Boolean,
    ) = listOfNotNull(
        nodes.getOrNull(node.y.toInt())?.getOrNull(node.x.toInt() - 1)?.takeIf { predicate(it, Direction.LEFT) },
        nodes.getOrNull(node.y.toInt() - 1)?.getOrNull(node.x.toInt())?.takeIf { predicate(it, Direction.UP) },
        nodes.getOrNull(node.y.toInt() + 1)?.getOrNull(node.x.toInt())?.takeIf { predicate(it, Direction.DOWN) },
        nodes.getOrNull(node.y.toInt())?.getOrNull(node.x.toInt() + 1)?.takeIf { predicate(it, Direction.RIGHT) },
    )

    /**
     * Gets the column for this grid at the given index.
     * @param index Column index.
     * @return Column nodes.
     * @throws IndexOutOfBoundsException
     */
    fun column(index: Int) =
        nodes.map {
            it[index]
        }

    /**
     * Gets the Manhattan distance between the given starting and ending nodes.
     * @param start Start node.
     * @param end End node.
     * @return Manhattan distance.
     */
    fun distance(
        start: Node<T>,
        end: Node<T>,
    ) = abs(start.x - end.x) + abs(start.y - end.y)

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
    fun forEach(action: (Node<T>) -> Unit) =
        nodes.forEach {
            it.forEach { node ->
                action(node)
            }
        }

    /**
     * Performs the given action on each column in this grid.
     * @param action Action to perform.
     */
    fun forEachColumn(action: (List<Node<T>>) -> Unit) {
        for (index in 0..<width) {
            action(column(index))
        }
    }

    /**
     * Performs the given action on each column in this grid.
     * @param action Action to perform.
     */
    fun forEachColumnIndexed(action: (Int, List<Node<T>>) -> Unit) {
        for (index in 0..<width) {
            action(index, column(index))
        }
    }

    /**
     * Performs the given action on each row in this grid.
     * @param action Action to perform.
     */
    fun forEachRow(action: (List<Node<T>>) -> Unit) =
        nodes.forEach {
            action(it)
        }

    /**
     * Performs the given action on each row in this grid.
     * @param action Action to perform.
     */
    fun forEachRowIndexed(action: (Int, List<Node<T>>) -> Unit) =
        nodes.forEachIndexed { index, node ->
            action(index, node)
        }

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
     * Gets the list of [Node] neighboring the given node. A node is considered a neighbor
     * if it is adjacent to or diagonally touching the given node.
     * @param node Node.
     * @return Neighboring nodes.
     */
    fun neighbors(node: Node<T>) =
        adjacent(node) +
            listOfNotNull(
                nodes.getOrNull(node.y.toInt() - 1)?.getOrNull(node.x.toInt() - 1),
                nodes.getOrNull(node.y.toInt() + 1)?.getOrNull(node.x.toInt() - 1),
                nodes.getOrNull(node.y.toInt() - 1)?.getOrNull(node.x.toInt() + 1),
                nodes.getOrNull(node.y.toInt() + 1)?.getOrNull(node.x.toInt() + 1),
            )

    /**
     * Gets the row for this grid at the given index.
     * @param index Row index.
     * @return Row nodes.
     * @throws IndexOutOfBoundsException
     */
    fun row(index: Int) = nodes[index]

    override fun toString() =
        nodes.joinToString("\n") {
            it.joinToString("") { node ->
                node.value.toString()
            }
        }
}
