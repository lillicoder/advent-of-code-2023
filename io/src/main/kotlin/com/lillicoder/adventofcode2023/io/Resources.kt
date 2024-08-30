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

package com.lillicoder.adventofcode2023.io

/**
 * Utility for handling source file I/O.
 */
object Resources {
    /**
     * Gets all lines of text from the resource with the given filename.
     * @param filename Resource filename.
     * @return Lines or null if there is no such resource.
     */
    fun lines(filename: String) = stream(filename)?.use { it.reader().readLines() }

    /**
     * Gets all lines of text from the resource with the given filename and maps them with
     * the given transform function.
     * @param filename Resource filename.
     * @return Mapped lines or null if there is no such resource.
     */
    fun <T> mapLines(
        filename: String,
        transform: (String) -> T,
    ) = lines(filename)?.map { transform(it) }

    /**
     * Gets all text from the resource with the given filename.
     * @param filename Resource filename.
     * @return Text or null if there is no such resource.
     */
    fun text(filename: String) = stream(filename)?.use { it.reader().readText() }

    /**
     * Opens a stream for the resource with the given filename.
     * @param filename Resource filename.
     * @return Resource stream or null if there is no such resource.
     */
    private fun stream(filename: String) = javaClass.classLoader.getResourceAsStream(filename)
}
