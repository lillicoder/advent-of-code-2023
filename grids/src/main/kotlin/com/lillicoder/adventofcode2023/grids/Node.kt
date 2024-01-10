package com.lillicoder.adventofcode2023.grids

/**
 * A single element of a [Grid].
 */
data class Node<T>(
    val x: Long,
    val y: Long,
    val value: T
)
