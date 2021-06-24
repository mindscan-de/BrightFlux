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

import de.mindscan.brightflux.dataframes.DataFrameColumn;

/**
 * 
 */
public class IntegerColumn extends SimpleNumberColumn<Integer> {

    public IntegerColumn() {
        super( Integer.class );
    }

    public IntegerColumn( String columnName ) {
        super( columnName, Integer.class );
    }

    @Override
    public DataFrameColumn<Integer> cloneColumnEmpty() {
        IntegerColumn newColumn = new IntegerColumn( getColumnName() );
        return newColumn;
    }

    @Override
    public void appendRaw( Object element ) {
        if (element instanceof Integer) {
            append( (Integer) element );
        }
        else {
            if (element == null) {
                append( null );
            }
            else {
                throw new IllegalArgumentException( "Expecting Integer or null, but got " + element.getClass().getName() );
            }
        }

    }

}
