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

package com.lillicoder.adventofcode2023.math

import kotlin.math.abs
import kotlin.math.max

/**
 * Utility for additional math functions.
 */
object Math {
    /**
     * Gets the area of a polygon with the given number of interior points and boundary points.
     * @param interior Points interior to the polygon.
     * @param boundary Points on polygon boundary.
     * @return Area.
     * @see [Pick's Theorem](https://en.wikipedia.org/wiki/Pick%27s_theorem)
     */
    fun area(
        interior: Long,
        boundary: Int,
    ) = interior - (boundary / 2) + 1

    /**
     * Gets the area of the polygon with the given list of [Vertex].
     * @return Area.
     * @see [Shoelace Formula](https://en.wikipedia.org/wiki/Shoelace_formula)
     */
    fun area(vertices: List<Vertex>): Long {
        val crossProducts =
            vertices.windowed(2, 1).sumOf { pair ->
                cross(pair[0], pair[1])
            }.plus(
                cross(vertices.last(), vertices.first()),
            )
        return abs(crossProducts) / 2
    }

    /**
     * Gets the cross product of the given [Vertex].
     * @param first First vertex.
     * @param second Second vertex.
     * @return Cross product.
     */
    private fun cross(
        first: Vertex,
        second: Vertex,
    ) = (first.x * second.y) - (second.x * first.y)

    /**
     * Finds the least common multiple among the given values.
     * @param values Values to find the least common multiple of.
     * @return least common multiple.
     */
    fun leastCommonMultiple(values: List<Long>): Long {
        var lcm = values.first()
        for (index in 1 until values.size) {
            lcm = leastCommonMultiple(lcm, values[index])
        }

        return lcm
    }

    /**
     * Finds the least common multiple of the two given numbers.
     * @param first First number.
     * @param second Second number.
     * @return least common multiple.
     */
    private fun leastCommonMultiple(
        first: Long,
        second: Long,
    ): Long {
        val largest = max(first, second)
        val max = first * second

        var lcm = largest
        while (lcm < max) {
            if (lcm % first == 0L && lcm % second == 0L) return lcm
            lcm += largest
        }

        return max
    }
}
