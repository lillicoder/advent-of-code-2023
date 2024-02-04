package com.lillicoder.adventofcode2023.grids

import javax.swing.JPopupMenu.Separator

/**
 * Parser for creating a [Grid] from a file.
 */
class GridParser {

    /**
     * Parses a [Grid] from the file corresponding to the given file name. File will be pulled from resources.
     * @param filename Filename.
     * @param converter Function to convert parsed nodes to their expected type.
     * @return Parsed grid.
     */
    fun <T> parse(filename: String, converter: (String) -> T): Grid<T> = parseList(filename, converter).first()

    /**
     * Parses one or more [Grid] from the file corresponding to the given file name. File will be pulled from resources.
     * @param filename Filename.
     * @param converter Function to convert parsed nodes to their expected type.
     * @return Parsed grids.
     */
    fun <T> parseList(filename: String, converter: (String) -> T): List<Grid<T>> {
        val input = javaClass.classLoader.getResourceAsStream(filename)!!.reader().readText()
        return input.split("\r\n\r\n").map { parseGrid(it, converter = converter) }
    }

    /**
     * Parses a [Grid] from the given raw input.
     * @param raw Raw input.
     * @param converter Function to convert parsed nodes to their expected type.
     * @return Parsed grid.
     */
    fun <T> parseGrid(
        raw: String,
        separator: String = System.lineSeparator(),
        converter: (String) -> T
    ): Grid<T> {
        val grid = mutableListOf<MutableList<Node<T>>>()

        raw.split(separator).forEachIndexed { y, line ->
            val row = mutableListOf<Node<T>>()
            line.split("").filter { it.isNotEmpty() }.forEachIndexed { x, value ->
                val node = Node(x.toLong(), y.toLong(), converter(value))
                row.add(node)
            }

            grid.add(row)
        }

        return Grid(grid)
    }
}
