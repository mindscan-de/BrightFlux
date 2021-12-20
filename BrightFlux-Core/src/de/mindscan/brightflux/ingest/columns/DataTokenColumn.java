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
package de.mindscan.brightflux.ingest.columns;

import java.util.Comparator;

import de.mindscan.brightflux.dataframes.DataFrameColumn;
import de.mindscan.brightflux.dataframes.columns.SimpleColumn;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;
import de.mindscan.brightflux.ingest.DataToken;

/**
 * This column type helps to implement the concept of an abstract data frame, which 
 * can can untyped column until the final compile step. There is an implicit typing
 * due to the DataToken in the column and due to the values of the DataToken in the 
 * column.
 * 
 * The major drawback right now is the fact that we can't add multiple tokens per row
 * unless we have a DataToken implementing a list of DataToken. (We might change that)
 * This would be intersting if we want support annotation on column, e.g. type, names,
 * visibility, default values, and such. 
 */
public class DataTokenColumn extends SimpleColumn<DataToken> {

    /**
     * @param clzz
     */
    public DataTokenColumn() {
        super( DataToken.class );
    }

    public DataTokenColumn( String columnName ) {
        super( columnName, DataToken.class );
    }

    @Override
    public DataFrameColumn<DataToken> cloneColumnEmpty() {
        DataTokenColumn newColumn = new DataTokenColumn( getColumnName() );
        return newColumn;
    }

    @Override
    public void appendRaw( Object element ) {
        if (element instanceof DataToken) {
            append( (DataToken) element );
        }
        else {
            if (element == null) {
                append( null );
            }
            else {
                throw new IllegalArgumentException( "Expecting DataToken or null, but got " + element.getClass().getName() );
            }
        }
    }

    @Override
    public String getColumnType() {
        throw new NotYetImplemetedException( "Currently do not support columntypes for datatoken columns" );
    }

    @Override
    public String getColumnValueType() {
        throw new NotYetImplemetedException( "Currently do not support columnValueTtype for datatoken columns" );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public int findInsertRowIndexRaw( Object element ) {
        return -1;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    protected Comparator<? super DataToken> getComparator() {
        return null;
    }

}
