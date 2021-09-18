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
package de.mindscan.brightflux.dataframes;

import de.mindscan.brightflux.dataframes.columns.IntegerColumn;

/**
 * This class describes the special columns which are present in a dataframe. 
 */
public class DataFrameSpecialColumns {

    /**
     * Every dataframe should have an index column. Such that its index describes the row position.
     */
    public final static String INDEX_COLUMN_NAME = "__idx";

    /**
     * Every dataframe should have an original index column. The orginal index plays two roles
     * either in the re-indexing process, where we assign new indexes (e.g.) the dataframe became spare...
     * or in case the dataframe is derived (e.g view or as a materialized dataframe), we want 
     * these dataframes, that we remember the original position, such that we can then use the original index
     * to refer to the index of the original dataframe.
     * 
     * In case we annotate the child frame, we want this annotation to be present / accessible in the 
     * orignal dataframe, as well as the derived frames. therefore the original index must be retained
     * through the selecting and filtering processes.
     */
    public final static String ORIGINAL_INDEX_COLUMN_NAME = "__org_idx";

    /**
     * @return
     */
    public static DataFrameColumn<?> createIndexColumn( int size ) {
        IntegerColumn index_column = new IntegerColumn( INDEX_COLUMN_NAME );
        for (int i = 0; i < size; i++) {
            index_column.append( i );
        }
        return index_column;
    }

    // TODO: we might want to create anoriginalIndex column as well

}
