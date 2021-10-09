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
 * 
 */
public interface DataFrameRow {

    public Object get( int columnIndex );

    public Object get( String columnName );

    public Object[] getAll();

    public int getSize();

    /**
     * This method returns the type of the column referenced by the given columnName. This type is needed to provide
     * the correct conversion and comparison method.
     *  
     * @param columnName Name of the column
     * @return the type of the column
     */
    public Object getValueType( String columnName );

    /**
     * This method will invoke the correct compareToRaw-operation of the columnName 
     * @param columnName identify the column 
     * @param convertedPredicateValue a value which is assignable to the type of the column identified by the columnname
     * @return 0 if rowValue is equal to convertedPredicateValue, 1 if convertedPredicateValue is bigger than rowvalue
     */
    public int compareToRaw( String columnName, Object convertedPredicateValue );

    /**
     * Current Rowindex in the filtered / unfiltered dataset.
     * @return
     */
    int getRowIndex();

    /**
     * The If this is a filtered dataset, we still want to keep a copy of the index, in the unfiltered dataset.
     * And with such a information we can annotate childframes, and the indexes are correct and can be used in 
     * the ancestor frame for the report.
     * @return
     */
    int getOriginalRowIndex();

    DataFrame getDataFrameInternal();

}
