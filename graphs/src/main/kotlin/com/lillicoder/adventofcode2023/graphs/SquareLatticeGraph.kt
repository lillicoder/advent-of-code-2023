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

import com.lillicoder.adventofcode2023.math.Coordinates
import com.lillicoder.adventofcode2023.math.Direction

/**
 * [Graph] in which all vertices form a square lattice.
 * @param graph Internal [Graph] containing each [Vertex] and [Edge].
 * @param coordinatesByVertex Each [Vertex] mapped to its Cartesian coordinates.
 * @param vertexByCoordinates Each Cartesian coordinate mapped to its vertex.
 */
class SquareLatticeGraph<T>(
    private val graph: Graph<T>,
    private val coordinatesByVertex: Map<Vertex<T>, Coordinates>,
    private val vertexByCoordinates: Map<Coordinates, Vertex<T>>,
) : Graph<T> by graph {
    private constructor(builder: Builder<T>) : this(
        AdjacencyListGraph(builder),
        builder.coordinatesByVertex,
        builder.vertexByCoordinates,
    )

    /**
     * Gets the [Coordinates] for the given [Vertex].
     * @param vertex Vertex.
     * @Return Coordinates or null if there are no coordinates for the given vertex.
     */
    fun coordinates(vertex: Vertex<T>) = coordinatesByVertex[vertex]

    /**
     * Determines the [Direction] from the given source [Vertex] to the given destination vertex.
     * @param source Source vertex.
     * @param destination Destination vertex.
     * @return Direction from source to destination or [Direction.UNKNOWN] if there is no
     * direction between the two vertices.
     */
    fun direction(
        source: Vertex<T>,
        destination: Vertex<T>,
    ) = coordinatesByVertex[source]?.let {
        when (coordinatesByVertex[destination]) {
            it.left() -> Direction.LEFT
            it.right() -> Direction.RIGHT
            it.down() -> Direction.DOWN
            it.up() -> Direction.UP
            else -> Direction.UNKNOWN
        }
    } ?: Direction.UNKNOWN

    /**
     * [Graph.Builder] for [SquareLatticeGraph] instances.
     * @param coordinatesByVertex Each [Vertex] mapped to its Cartesian coordinates.
     * @param vertexByCoordinates Each Cartesian coordinate mapped to its vertex.
     */
    class Builder<T>(
        internal val coordinatesByVertex: MutableMap<Vertex<T>, Coordinates> = mutableMapOf(),
        internal val vertexByCoordinates: MutableMap<Coordinates, Vertex<T>> = mutableMapOf(),
    ) : Graph.Builder<T>() {
        override fun build() = build(false)

        /**
         * Creates a new [SquareLatticeGraph] from this builder.
         * @param allowDiagonals True to connect diagonally adjacent vertices with edges.
         * @return Graph.
         */
        fun build(allowDiagonals: Boolean = false) =
            vertices.keys.forEach {
                connectToNeighbors(
                    it,
                    allowDiagonals,
                )
            }.let {
                SquareLatticeGraph(this)
            }

        /**
         * Adds a new [Vertex] to this graph with the given coordinates.
         * @param coordinates Coordinates.
         * @param element Value.
         * @return Builder.
         */
        fun vertex(
            coordinates: Coordinates,
            element: T,
        ) = apply {
            vertex(element) {
                coordinatesByVertex[it] = coordinates
                vertexByCoordinates[coordinates] = it
            }
        }

        /**
         * Connects the given [Vertex] to all of its neighboring vertices
         * with an [Edge].
         * @param vertex Vertex to connect.
         * @param allowDiagonals True to also connect diagonally adjacent vertices with edges.
         */
        private fun connectToNeighbors(
            vertex: Vertex<T>,
            allowDiagonals: Boolean,
        ) {
            coordinatesByVertex[vertex]?.apply {
                listOfNotNull(
                    vertexByCoordinates[left()],
                    vertexByCoordinates[right()],
                    vertexByCoordinates[up()],
                    vertexByCoordinates[down()],
                    if (allowDiagonals) vertexByCoordinates[leftUp()] else null,
                    if (allowDiagonals) vertexByCoordinates[rightUp()] else null,
                    if (allowDiagonals) vertexByCoordinates[leftDown()] else null,
                    if (allowDiagonals) vertexByCoordinates[rightDown()] else null,
                ).forEach {
                    edge(vertex, it)
                }
            }
        }
    }
}
