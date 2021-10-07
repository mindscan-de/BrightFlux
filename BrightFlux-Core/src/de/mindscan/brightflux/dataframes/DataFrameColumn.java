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
public interface DataFrameColumn<T> {

    // -----------------------------------------
    // Extend the column 
    // -----------------------------------------

    public abstract void append( T element );

    public abstract void appendRaw( Object element );

    public abstract void appendNA();

    // -----------------------------------------
    // Column wise functions
    // -----------------------------------------

    public abstract int getSize();

    public abstract boolean isEmpty();

    public abstract void setColumnName( String columnName );

    public abstract String getColumnName();

    public abstract String getColumnType();

    public abstract String getColumnValueType();

    public abstract DataFrameColumn<T> cloneColumnEmpty();

    // return the values of this column
    public abstract T[] toArray();

    // -----------------------------------------
    // Element wise access get / set / isPresent
    // -----------------------------------------    

    public abstract boolean isPresent( int index );

    public abstract T get( int index );

    public abstract void set( int index, T element );

    public abstract void setNA( int index );

    public abstract void setRaw( int index, Object element );

}
