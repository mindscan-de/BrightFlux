/**
 * 
 * MIT License
 *
 * Copyright (c) 2021 Maxim Gansert, Mindscan
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */
package de.mindscan.brightflux.dataframes.journal;

/**
 * 
 * 
 * TODO: this can also be used to simplify the parsing, since we know which kind of DataFrameQueryLanguage - Expression to parse... 
 */
public enum DataFrameJournalEntryType {

    LOAD,

    SELECT_WHERE,

    // TODO: use a column of a data frame and tokenize it using a different dataframe tokenizer on it
    // TOKENIZE,

    // TODO: use multiple columns to join multiple dataframes on existing and nonnull values PK / FK relationship
    // INNER_JOIN,

    // TODO: use multiple also support null values on the join operation
    // OUTER_JOIN,

    // TODO: ato de?
    // LEFT_OUTER_JOIN
    ;

}
