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
public class BooleanColumn extends SimpleColumn<Boolean> {

    public BooleanColumn() {
        super( Boolean.class );
    }

    public BooleanColumn( String columnName ) {
        super( columnName, Boolean.class );
    }

    @Override
    public DataFrameColumn<Boolean> cloneColumnEmpty() {
        return new BooleanColumn( getColumnName() );
    }

    @Override
    public void appendRaw( Object element ) {
        if (element instanceof Boolean) {
            append( (Boolean) element );
        }
        else {
            if (element == null) {
                append( null );
            }
            else {
                throw new IllegalArgumentException( "Expecting Boolean or null, but got " + element.getClass().getName() );
            }
        }
    }

    @Override
    public String getColumnType() {
        return ColumnTypes.COLUMN_TYPE_BOOL;
    }

    @Override
    public String getColumnValueType() {
        return ColumnValueTypes.COLUMN_TYPE_BOOL;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    protected Comparator<? super Boolean> getComparator() {
        return Comparator.naturalOrder();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public int findInsertRowIndexRaw( Object element ) {
        return -1;
    }

}
