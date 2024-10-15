package com.lillicoder.adventofcode2023.day10

import com.lillicoder.adventofcode2023.graphs.gridToGraph
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Day10].
 */
internal class Day10Test {
    private val input1 =
        """..F7.
           >.FJ|.
           >SJ.L7
           >|F--J
           >LJ...
        """.trimMargin(">")

    private val grid1 = PipeMaze(input1.gridToGraph())

    private val input2 =
        """.F----7F7F7F7F-7....
            >.|F--7||||||||FJ....
            >.||.FJ||||||||L7....
            >FJL7L7LJLJ||LJ.L-7..
            >L--J.L7...LJS7F-7L7.
            >....F-J..F7FJ|L7L7L7
            >....L7.F7||L7|.L7L7|
            >.....|FJLJ|FJ|F7|.LJ
            >....FJL-7.||.||||...
            >....L---J.LJ.LJLJ...
        """.trimMargin(">")
    private val grid2 = PipeMaze(input2.gridToGraph())

    private val day10 = Day10()

    @Test
    fun part1() {
        val expected = 8L
        val actual = day10.part1(grid1)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 8L
        val actual = day10.part2(grid2)
        assertEquals(expected, actual)
    }
}
