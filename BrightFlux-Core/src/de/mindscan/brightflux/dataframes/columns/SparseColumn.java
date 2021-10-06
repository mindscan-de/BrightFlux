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

import java.util.HashMap;
import java.util.Map;

/**
 * Instead of an array backed column, we may only have a sparse representation, where we only
 * want to save a key to value pair. An annotation may be so sparse, that we only have a few 
 * annotations for a few thousands lines of log, and only have. In that case we want tom 
 * implement a HashMap instead.   
 */
public abstract class SparseColumn<T> extends DataFrameColumnBase<T> {

    private Map<Integer, T> columnValues;
    private int size;

    protected SparseColumn( Class<T> clzz ) {
        this( null, clzz );
    }

    protected SparseColumn( String columnName, Class<T> clzz ) {
        setColumnName( columnName );
        this.columnValues = new HashMap<>();
        this.size = 0;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void append( T element ) {
        if (element != null) {
            columnValues.put( Integer.valueOf( size ), element );
        }
        size++;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void appendNA() {
        this.append( null );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return size;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return columnValues.size() == 0 && this.getSize() == 0;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public boolean isPresent( int index ) {
        return columnValues.containsKey( Integer.valueOf( index ) );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public T get( int index ) {
        return columnValues.get( Integer.valueOf( index ) );

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void set( int index, T element ) {
        columnValues.put( Integer.valueOf( index ), element );
    }

    /**
     * @param index
     */
    public void setNA( int index ) {
        columnValues.remove( Integer.valueOf( index ) );
    }

    /** 
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    @Override
    public T[] toArray() {
        // TODO: is there a good ordering?
        // TODO: is this even a good implmentation?
        return ((T[]) columnValues.values().toArray());
    }

}
