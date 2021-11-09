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
package de.mindscan.brightflux.dataframes.columntypes;

/**
 * I think this thing should be transformed in such a way, that we have real typeinformation, such that this
 * typeinformation and its type information contract can be used to convert data from and to the correct target 
 * type depending on the columns.
 * 
 * This is needed also for the caclulation of the types of columns, e.g if we have a column with a long type,
 * that it can be compared to a value, which should be treated as long not as an integer. So we have to solcve 
 * some type interference issues.
 * 
 * This type is needed for many things, like predicates and all such things. Also this would allow an integeger 
 * row to accept integers instead of only "raw" values... 
 * 
 * I guess right now, that the column types will enable some next level features. "e.g." providing 
 * contains, startswith, endswith operations not only for string value type columns, but also for integers, with 
 * an additional wrapper, using the correct typewrapper.
 * 
 * This will also add/provide some more typesafety and implicit conversion abilities to the current dataframe 
 * concept.
 */
public class ColumnTypes {

    public static final String COLUMN_TYPE_INT = "int";
    public static final String COLUMN_TYPE_INTEGER = "integer";
    public static final String COLUMN_TYPE_BOOL = "bool";
    public static final String COLUMN_TYPE_BOOLEAN = "boolean";
    public static final String COLUMN_TYPE_FLOAT = "float";
    public static final String COLUMN_TYPE_DOUBLE = "double";
    public static final String COLUMN_TYPE_LONG = "long";
    public static final String COLUMN_TYPE_STRING = "string";
    public static final String COLUMN_TYPE_SPARSE_STRING = "s_string";
    public static final String COLUMN_TYPE_SPARSE_INT = "s_int";
    public static final String COLUMN_TYPE_SPARSE_LONG = "s_long";

}
