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

import com.lillicoder.adventofcode2023.io.splitMapNotEmpty
import com.lillicoder.adventofcode2023.math.to

/**
 * Converts this string to a [SquareLatticeGraph]. Each character in each string is considered
 * a vertex. Each vertex is connected to the vertices adjacent to it in each cardinal direction.
 * @param allowDiagonals True to also connect diagonally adjacent vertices with edges.
 * @return Graph.
 */
fun String.gridToGraph(allowDiagonals: Boolean = false): SquareLatticeGraph<String> {
    val builder = SquareLatticeGraph.Builder<String>()

    val rows = lines()
    rows.forEachIndexed { y, row ->
        row.forEachIndexed { x, node ->
            builder.vertex(x.to(y), node.toString())
        }
    }

    return builder.build(allowDiagonals)
}

/**
 * Converts this string to a list of [SquareLatticeGraph]. Each graph is considered
 * to be separated by two line breaks.
 * @return Graphs.
 */
fun String.gridsToGraph() = splitMapNotEmpty("${lineSequence()}${lineSequence()}") { it.gridToGraph() }
