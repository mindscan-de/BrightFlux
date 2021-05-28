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

import java.lang.reflect.Array;
import java.util.Arrays;

import de.mindscan.brightflux.dataframes.DataFrameColumn;

/**
 * 
 */
public abstract class SimpleColumn<T> extends DataFrameColumn<T> {

    private final int INITIAL_SIZE = 128;

    private T[] columnValues;
    private int size;

    public SimpleColumn( Class<T> clzz ) {
        this( null, clzz );
    }

    @SuppressWarnings( "unchecked" )
    public SimpleColumn( String columnName, Class<T> clzz ) {
        setColumnName( columnName );
        this.columnValues = (T[]) Array.newInstance( clzz, INITIAL_SIZE );
        this.size = 0;
    }

    public SimpleColumn( String columnname, T[] columnValues ) {
        setColumnName( columnname );
        this.columnValues = columnValues;
        this.size = columnValues.length;
    }

    public boolean isEmpty() {
        return this.getSize() == 0;
    }

    public T at( int index ) {
        return columnValues[index];
    }

    @Override
    public void append( T element ) {
        if (this.columnValues.length <= this.size) {
            // linear growth right now 
            this.columnValues = Arrays.copyOf( this.columnValues, this.columnValues.length + INITIAL_SIZE );
        }
        this.columnValues[this.size] = element;
        this.size++;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public T get( int index ) {
        return at( index );
    }

    @Override
    public void set( int index, T element ) {

    }

}
