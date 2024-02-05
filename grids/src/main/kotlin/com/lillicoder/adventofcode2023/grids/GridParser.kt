package com.lillicoder.adventofcode2023.grids

/**
 * Parser for creating a [Grid] from a file.
 */
class GridParser {

    /**
     * Parses a [Grid] from the file corresponding to the given file name. File will be pulled from resources.
     * @param filename Filename.
     * @return Parsed grid.
     */
    fun parseFile(filename: String) = parseFile(filename) { it }

    /**
     * Parses a list of [Grid] from the file corresponding to the given file name. File will be pulled from resources.
     * @param filename Filename.
     * @param separator Line separator for the given file.
     * @return Parsed grids.
     */
    fun parseFile(
        filename: String,
        separator: String = System.lineSeparator()
    ): List<Grid<String>> = parseFile(filename, separator) { it }

    /**
     * Parses a list of [Grid] from the file corresponding to the given file name. File will be pulled from resources.
     * @param filename Filename.
     * @param separator Line separator for the given file.
     * @param converter Function to convert parsed nodes to their expected type.
     * @return Parsed grids.
     */
    fun <T> parseFile(
        filename: String,
        separator: String = System.lineSeparator(),
        converter: (String) -> T
    ) = parseGrids(javaClass.classLoader.getResourceAsStream(filename)!!.reader().readText(), separator, converter)

    /**
     * Parses a [Grid] from the given raw input.
     * @param raw Raw input.
     * @param separator Line separator for the given input.
     * @return Parsed grid.
     */
    fun parseGrid(raw: String, separator: String = System.lineSeparator()) = parseGrid(raw, separator) { it }

    /**
     * Parses a [Grid] from the given raw input.
     * @param raw Raw input.
     * @param separator Line separator for the given input.
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

    /**
     * Parses a list of [Grid] from the given raw input.
     * @param raw Raw input.
     * @param separator Line separator for the given input.
     * @return Parsed grids.
     */
    fun parseGrids(
        raw: String,
        separator: String = System.lineSeparator()
    ) = parseGrids(raw, separator) { it }

    /**
     * Parses a list of [Grid] from the given raw input.
     * @param raw Raw input.
     * @param separator Line separator for the given input.
     * @param converter Function to convert parsed nodes to their expected type.
     * @return Parsed grids.
     */
    private fun <T> parseGrids(
        raw: String,
        separator: String = System.lineSeparator(),
        converter: (String) -> T
    ) = raw.split("$separator$separator").map { parseGrid(it, separator, converter) }
}
