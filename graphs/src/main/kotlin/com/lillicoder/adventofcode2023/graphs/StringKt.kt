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
 * Converts this list of string to a [Graph]. Each character in each string is considered a node.
 * Nodes are connected to the nodes adjacent to them in each cardinal direction.
 * @param allowDiagonals True to also add diagonally adjacent nodes as edges.
 * @return Graph.
 */
fun List<String>.gridToGraph(allowDiagonals: Boolean = false): Graph<String> {
    val graph = AdjacencyListGraph<String>()

    // Add all vertices
    forEach { row ->
        row.forEach {
            graph.addVertex(it.toString())
        }
    }

    // Connect vertices with edges
    val rows = size
    forEachIndexed { y, row ->
        val columns = row.length
        row.forEachIndexed { x, _ ->
            val position = x + y * columns.toLong()

            val cardinal =
                listOfNotNull(
                    // left
                    if (x == 0) null else graph.vertex(position - 1L),
                    // right
                    if (x == columns - 1) null else graph.vertex(position + 1L),
                    // top
                    if (y == 0) null else graph.vertex(position - columns),
                    // bottom
                    if (y == rows - 1) null else graph.vertex(position + columns),
                )
            val diagonals =
                when (allowDiagonals) {
                    true ->
                        listOfNotNull(
                            // top-left
                            if (x == 0 || y == 0) null else graph.vertex(position - columns - 1L),
                            // top-right
                            if (x == columns - 1 || y == 0) null else graph.vertex(position - columns + 1L),
                            // bottom-left
                            if (x == 0 || y == rows - 1) null else graph.vertex(position + columns - 1L),
                            // bottom-right
                            if (x == columns - 1 || y == rows - 1) null else graph.vertex(position + columns + 1L),
                        )
                    false -> emptyList()
                }

            val vertex = graph.vertex(position)!!
            (cardinal + diagonals).forEach {
                graph.addEdge(vertex, it)
            }
        }
    }

    return graph
}
