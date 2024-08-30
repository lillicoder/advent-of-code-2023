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
 * Splits this string with the given delimiter then maps each substring
 * with the given transform function.
 * @param delimiter Delimiter.
 * @param transform Transform function.
 * @return Split and mapped string.
 */
fun <T> String.splitMap(
    delimiter: String,
    transform: (String) -> T,
) = splitNotEmpty(delimiter).map(transform)

/**
 * Splits this string with the given delimiter and filters out all empty substrings.
 * @return Split.
 */
fun String.splitNotEmpty(delimiter: String) = split(delimiter).filter { it.isNotEmpty() }
