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

/**
 * [Adjacency list](https://en.wikipedia.org/wiki/Adjacency_list) implementation of a [Graph].
 */
class AdjacencyListGraph<T>(
    private val vertices: Map<Vertex<T>, Set<Edge<T>>>,
    private val edges: Set<Edge<T>>,
) : Graph<T> {
    internal constructor(builder: Graph.Builder<T>) : this(builder.vertices, builder.edges)

    override fun adjacent(
        first: Vertex<T>,
        second: Vertex<T>,
    ) = edges.contains(Edge(first, second))

    override fun neighbors(vertex: Vertex<T>) =
        vertices[vertex]?.map {
            if (vertex == it.source) it.destination else it.source
        }?.toSet() ?: setOf()

    override fun next(vertex: Vertex<T>) = vertex(vertex.id.inc())

    override fun root() = vertices.keys.first()

    override fun size() = vertices.keys.size

    override fun vertex(id: Long) = vertices.keys.find { it.id == id }
}
