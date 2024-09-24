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
class AdjacencyListGraph<T> : Graph<T> {
    private val nodes = mutableMapOf<Vertex<T>, MutableSet<Vertex<T>>>()

    override fun addEdge(
        first: Vertex<T>,
        second: Vertex<T>,
    ) {
        nodes[first]?.add(second)
        nodes[second]?.add(first)
    }

    override fun addVertex(element: T) =
        Vertex(
            nodes.size.toLong(),
            element,
        ).also {
            nodes[it] = mutableSetOf()
        }

    override fun adjacent(
        first: Vertex<T>,
        second: Vertex<T>,
    ): Boolean {
        val firstHasSecond = nodes[first]?.contains(second) ?: false
        val secondHasFirst = nodes[second]?.contains(first) ?: false
        return firstHasSecond && secondHasFirst
    }

    override fun neighbors(vertex: Vertex<T>) = nodes[vertex]?.map { it }?.toSet() ?: setOf()

    override fun next(vertex: Vertex<T>) = vertex(vertex.id.inc())

    override fun removeEdge(
        first: Vertex<T>,
        second: Vertex<T>,
    ) {
        nodes[first]?.remove(second)
        nodes[second]?.remove(first)
    }

    override fun removeVertex(vertex: Vertex<T>) {
        nodes.values.forEach { it.remove(vertex) }
        nodes.remove(vertex)
    }

    override fun root() = nodes.keys.first()

    override fun size() = nodes.keys.size

    override fun vertex(id: Long) = nodes.keys.find { it.id == id }
}
