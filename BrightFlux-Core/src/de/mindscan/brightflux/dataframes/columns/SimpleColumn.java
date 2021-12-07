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
import java.util.Comparator;

import de.mindscan.brightflux.exceptions.NotYetImplemetedException;

/**
 * 
 */
public abstract class SimpleColumn<T> extends DataFrameColumnBase<T> {

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

    @Override
    public boolean isEmpty() {
        return this.getSize() == 0;
    }

    public T at( int index ) {
        return columnValues[index];
    }

    @Override
    public void append( T element ) {
        if (this.columnValues.length <= this.size) {
            // in case someone uses an empty length array in the first place we add and then we multiply
            this.columnValues = Arrays.copyOf( this.columnValues, ((this.columnValues.length + INITIAL_SIZE) * 3) >> 1 );
        }
        this.columnValues[this.size] = element;
        this.size++;
    }

    @Override
    public void appendNA() {
        this.append( null );
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isPresent( int index ) {
        return at( index ) != null;
    }

    @Override
    public T get( int index ) {
        return at( index );
    }

    @Override
    public T[] toArray() {
        return Arrays.copyOf( columnValues, getSize() );
    }

    @Override
    public void set( int index, T element ) {
        // TODO: Implement this, that we can override the value at a certain position.
        throw new NotYetImplemetedException( "'set' is not yet implemented in SimpleColumn" );
    }

    @Override
    public void setRaw( int index, Object element ) {
        // TODO: implement this, such that we can override the value at acertain position.
        throw new NotYetImplemetedException( "'setRaw' is not yet implemented in SimpleColumn" );
    }

    @Override
    public void setNA( int index ) {
        // TODO: implement this, such that we can override the value at acertain position.
        throw new NotYetImplemetedException( "'setNa' is not yet implemented in SimpleColumn" );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public int findInsertRowIndexRaw( String columnName, Object element ) {
        Comparator<? super T> comparator = this.getComparator();
        if (comparator == null) {
            return -1;
        }

        if (element == null) {
            return -1;
        }

        int index = Arrays.binarySearch( columnValues, 0, getSize() - 1, (T) element, comparator );
        return Math.abs( index );
    }

    /**
     * @return
     */
    protected abstract Comparator<? super T> getComparator();
}
