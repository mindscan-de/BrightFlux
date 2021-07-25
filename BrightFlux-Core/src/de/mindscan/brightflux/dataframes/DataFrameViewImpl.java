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

/**
 * A dataframe view is a construction which is backed by one or more dataframes, using join operations
 * A dataframe view can have columns and rows which are set invisible
 * 
 * A dataframe view can be fused and transformed to a new dataframe and loses the transformation history
 * A dataframe view should behave like a dataFrame
 * 
 * A dataframe view can have a history of transformations (called a DataFrameJournal), as long it is not fused together
 * 
 * Maybe a Dataframe can have a history too, which might not be reversible
 * 
 * Problem is groupby and order by and maybe all the other aggregation stuff
 * 
 * At the moment column-wise filter and row-wise filtering is of interest (or "delete from where" ), will mark rows 
 * and columns as deleted, and the Dataframe view will remove its visibility, rather rearranging the array. Only
 * after a fuse, the full view is reduced to a DataFrame again. 
 * 
 * 
 */
public class DataFrameViewImpl {

    public DataFrame fuseJournal() {
        return null;
    }

    public DataFrameJournal getJournal() {
        return null;
    }
}
