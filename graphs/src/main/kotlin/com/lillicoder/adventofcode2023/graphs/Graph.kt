/*
 * Copyright 2024 Scott Weeden-Moody
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this project except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lillicoder.adventofcode2023.graphs

interface Graph<T> : Iterable<Vertex<T>> {
    override fun iterator() = InsertOrderTraversal(this)

    /**
     * Adds an edge connecting the two given [Vertex].
     * @param first First vertex.
     * @param second Second vertex.
     */
    fun addEdge(
        first: Vertex<T>,
        second: Vertex<T>,
    )

    /**
     * Adds a new [Vertex] to this graph.
     * @param element Vertex value.
     * @return Added vertex.
     */
    fun addVertex(element: T): Vertex<T>

    /**
     * Determines if there is an edge between the two given [Vertex].
     * @param first First vertex.
     * @param second Second vertex.
     * @return True if there is an edge between the vertices, false otherwise.
     */
    fun adjacent(
        first: Vertex<T>,
        second: Vertex<T>,
    ): Boolean

    /**
     * Gets all neighbors of the given [Vertex]. A vertex is considered
     * a neighbor if there is an edge to it from the given element.
     * @param vertex Vertex.
     * @return Neighbors.
     */
    fun neighbors(vertex: Vertex<T>): Set<Vertex<T>>

    /**
     * Gets the [Vertex] that was inserted after the given vertex.
     * @param vertex Vertex.
     * @return Next vertex or null if the given vertex was the last one inserted.
     */
    fun next(vertex: Vertex<T>): Vertex<T>?

    /**
     * Removes an edge, if any, connecting the two given [Vertex].
     * @param first First vertex.
     * @param second Second vertex.
     */
    fun removeEdge(
        first: Vertex<T>,
        second: Vertex<T>,
    )

    /**
     * Removes the given [Vertex] from this graph.
     * @param vertex Vertex to remove.
     */
    fun removeVertex(vertex: Vertex<T>)

    /**
     * Gets the first node added to this graph.
     * @return First node.
     */
    fun root(): Vertex<T>

    /**
     * Gets the number of nodes in this graph.
     */
    fun size(): Int

    /**
     * Gets the [Vertex] from this graph with the given ID.
     * @param id Vertex ID.
     * @return Vertex or null if there is no matching vertex.
     */
    fun vertex(id: Long): Vertex<T>?
}
