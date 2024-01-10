package com.lillicoder.adventofcode2023.grids

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
    fun <T> parse(filename: String, converter: (String) -> T): Grid<T> {
        val grid = mutableListOf<MutableList<Node<T>>>()

        var y = 0L
        javaClass.classLoader.getResourceAsStream(filename)!!.reader().forEachLine { line ->
            val row = mutableListOf<Node<T>>()
            line.split("").filter { it.isNotEmpty() }.forEachIndexed { x, value ->
                val node = Node(x.toLong(), y, converter(value))
                row.add(node)
            }

            grid.add(row)
            y++
        }

        return Grid(grid)
    }
}
