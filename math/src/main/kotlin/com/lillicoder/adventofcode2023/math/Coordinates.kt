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

/**
 * Represents a pair of Cartesian coordinate.
 * @param x X-coordinate.
 * @param y Y-coordinate.
 */
data class Coordinates(
    val x: Long,
    val y: Long,
) {
    /**
     * Gets the cross product of these [Coordinates] with the given coordinates.
     * @param coordinates Coordinates.
     * @return Cross product.
     */
    fun cross(coordinates: Coordinates) = (x * coordinates.y) - (coordinates.x * y)

    /**
     * Creates a new [Coordinates] whose second value is incremented.
     * @return Coordinates.
     */
    fun down() = copy(y = y.inc())

    /**
     * Creates a new [Coordinates] whose first value is decremented.
     * @return Coordinates.
     */
    fun left() = copy(x = x.dec())

    /**
     * Creates a new [Coordinates] whose first value is incremented.
     * @return Coordinates.
     */
    fun right() = copy(x = x.inc())

    /**
     * Creates a new [Coordinates] whose second value is decremented.
     * @return Coordinates.
     */
    fun up() = copy(y = y.dec())

    /**
     * Creates a new [Coordinates] whose first value is decremented and its second value is incremented.
     * @return Coordinates.
     */
    fun leftDown() = copy(x = x.dec(), y = y.inc())

    /**
     * Creates a new [Coordinates] whose values are decremented.
     * @return Coordinates.
     */
    fun leftUp() = copy(x = x.dec(), y = y.dec())

    /**
     * Creates a new [Coordinates] whose values are incremented.
     * @return Coordinates.
     */
    fun rightDown() = copy(x = x.inc(), y = y.inc())

    /**
     * Creates a new [Coordinates] whose first value is incremented and second value is decremented.
     * @return Coordinates.
     */
    fun rightUp() = copy(x = x.inc(), y = y.dec())

    override fun toString() = "($x, $y)"
}

/**
 * Pairs this with the given value as [Coordinates].
 * @param that Value.
 * @return Coordinates.
 */
fun Int.to(that: Int) = Coordinates(this.toLong(), that.toLong())

/**
 * Pairs this with the given value as [Coordinates].
 * @param that Value.
 * @return Coordinates.
 */
fun Long.to(that: Long) = Coordinates(this, that)

/**
 * Gets the area of the polygon with the given list of [Coordinates].
 * @return Area.
 * @see [Shoelace Formula](https://en.wikipedia.org/wiki/Shoelace_formula)
 */
fun List<Coordinates>.area(): Long {
    val crossProducts =
        windowed(2, 1).sumOf { pair ->
            pair[0].cross(pair[1])
        }.plus(
            last().cross(first()),
        )
    return abs(crossProducts) / 2
}
