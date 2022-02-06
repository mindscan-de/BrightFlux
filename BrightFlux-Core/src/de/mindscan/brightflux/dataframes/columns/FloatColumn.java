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
package de.mindscan.brightflux.dataframes.columns;

import java.util.Comparator;

import de.mindscan.brightflux.dataframes.DataFrameColumn;
import de.mindscan.brightflux.dataframes.columntypes.ColumnTypes;
import de.mindscan.brightflux.dataframes.columntypes.ColumnValueTypes;

/**
 * 
 */
public class FloatColumn extends SimpleNumberColumn<Float> {

    public FloatColumn() {
        super( Float.class );
    }

    public FloatColumn( String columnName ) {
        super( columnName, Float.class );
    }

    @Override
    public DataFrameColumn<Float> cloneColumnEmpty() {
        FloatColumn newColumn = new FloatColumn( getColumnName() );
        return newColumn;
    }

    @Override
    public void appendRaw( Object element ) {
        if (element instanceof Float) {
            append( (Float) element );
        }
        else {
            if (element == null) {
                append( null );
            }
            else {
                throw new IllegalArgumentException( "Expecting Float or null, but got " + element.getClass().getName() );
            }
        }
    }

    @Override
    public String getColumnType() {
        return ColumnTypes.COLUMN_TYPE_FLOAT;
    }

    @Override
    public String getColumnValueType() {
        return ColumnValueTypes.COLUMN_TYPE_FLOAT;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    protected Comparator<? super Float> getComparator() {
        return Comparator.naturalOrder();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Float max() {
        int size = this.getSize();
        int first = 0;

        float max = Float.NEGATIVE_INFINITY;

        for (first = 0; first < size; first++) {
            if (isPresent( first )) {
                max = get( first ).floatValue();
            }
        }

        for (int i = first + 1; i < size; i++) {
            if (isPresent( i )) {
                max = Math.max( max, get( i ).floatValue() );
            }
        }

        return max;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Float min() {
        int size = this.getSize();
        int first = 0;

        float min = Float.POSITIVE_INFINITY;

        for (first = 0; first < size; first++) {
            if (isPresent( first )) {
                min = get( first ).floatValue();
            }
        }

        for (int i = first + 1; i < size; i++) {
            if (isPresent( i )) {
                min = Math.min( min, get( i ).floatValue() );
            }
        }

        return min;
    }
}
