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
public class DataFrameRowImpl implements DataFrameRow {

    private DataFrame df;
    private int rowIndex;
    private int maxCols;

    /**
     * 
     */
    public DataFrameRowImpl( DataFrame df, int rowIndex ) {
        this.df = df;
        this.rowIndex = rowIndex;
        this.maxCols = df.getColumnNames().size();
    }

    public Object get( int columnIndex ) {
        return df.getAt( columnIndex, this.rowIndex );
    }

    public Object get( String columnName ) {
        return df.getAt( columnName, this.rowIndex );
    }

    public int getSize() {
        return maxCols;
    }

    @Override
    public int getRowIndex() {
        return rowIndex;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public int getOriginalRowIndex() {
        return df.getOriginalRowIndex( rowIndex );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Object[] getAll() {
        Object[] values = new Object[maxCols];

        for (int i = 0; i < maxCols; i++) {
            values[i] = df.getAt( i, this.rowIndex );
        }

        return values;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Object getType( String columnName ) {
        return Integer.class;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public int compareToRaw( String columnName, Object convertedPredicateValue ) {
        // TODO: this is highly inefficient, it would be better to ask for the right comparator and the right converter
        //       too many instanceof operations ... The casts are already expensive enough.
        //       it should be somehow a switch case statement / or a function method map to have a O(1) decision

        Object rowValue = get( columnName );

        if (rowValue instanceof Integer) {
            return ((Integer) rowValue).compareTo( (Integer) convertedPredicateValue );
        }

        if (rowValue instanceof Double) {
            return ((Double) rowValue).compareTo( (Double) convertedPredicateValue );
        }

        if (rowValue instanceof Float) {
            return ((Float) rowValue).compareTo( (Float) convertedPredicateValue );
        }

        // TODO: all the other comparisons and conversions / casts
        return 0;
    }
}
