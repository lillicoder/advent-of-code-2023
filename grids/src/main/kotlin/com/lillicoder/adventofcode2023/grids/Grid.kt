package com.lillicoder.adventofcode2023.grids

import com.lillicoder.adventofcode2023.io.Resources
import com.lillicoder.adventofcode2023.io.splitMapNotEmpty
import kotlin.math.abs

/**
 * An arbitrary two-dimensional grid of [Node].
 */
data class Grid<T>(
    private val nodes: List<List<Node<T>>>,
    val width: Int = nodes[0].size,
    val height: Int = nodes.size,
) {
    companion object {
        /**
         * Creates a new [Grid] from the given list of strings. Each string
         * will be considered as a row and each character in each string
         * considered as a column.
         * @param raw Raw values.
         * @return Grid.
         */
        fun create(raw: List<String>) =
            Grid(
                raw.mapIndexed { y, row ->
                    row.mapIndexed { x, node ->
                        Node(x.toLong(), y.toLong(), node.toString())
                    }
                },
            )

        /**
         * Creates a new [Grid] by reading the resource with the given filename.
         * @param filename Resource filename.
         * @return Grid.
         */
        fun read(filename: String) = readAll(filename).first()

        /**
         * Creates one or more [Grid] by reading the resource with the given filename.
         * @param filename Resource filename.
         * @param separator Line separator.
         * @return Grids.
         */
        fun readAll(
            filename: String,
            separator: String = System.lineSeparator(),
        ): List<Grid<String>> {
            val raw = Resources.text(filename) ?: throw IllegalArgumentException("Could not read input from file.")
            return raw.splitMapNotEmpty("$separator$separator") { create(it.lines()) }
        }
    }

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
     * Counts the number of nodes per row matching the given predicate.
     * @param predicate Predicate to check.
     * @return Number of nodes per row satisfying the predicate.
     */
    fun countNodesByRow(predicate: (Node<T>) -> Boolean) = nodes.map { it.count(predicate) }

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
     * Returns a list containing the results of applying the given transform function
     * to each column of nodes in this grid.
     * @param transform Transform to apply.
     * @return Mapped columns.
     */
    fun <R> mapColumns(transform: (List<Node<T>>) -> R): List<R> {
        val transformed = mutableListOf<R>()
        forEachColumn {
            transformed.add(transform(it))
        }
        return transformed
    }

    /**
     * Returns a [Grid] containing the results of applying the given transform function
     * to each node in this grid.
     * @param transform Transform to apply.
     * @return Mapped grid.
     */
    fun <R> mapNodes(transform: (Node<T>) -> Node<R>): Grid<R> {
        val mapped =
            nodes.map { row ->
                row.map {
                    transform(it)
                }
            }

        return Grid(mapped)
    }

    /**
     * Returns a list containing the results of applying the given transform function
     * to each row of nodes in this grid.
     * @param transform Transform to apply.
     * @return Mapped rows.
     */
    fun <R> mapRows(transform: (List<Node<T>>) -> R) = nodes.map(transform)

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
