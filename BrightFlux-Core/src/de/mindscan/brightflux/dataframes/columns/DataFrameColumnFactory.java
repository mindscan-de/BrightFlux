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
import de.mindscan.brightflux.dataframes.columntypes.ColumnTypes;

/**
 * 
 */
public class DataFrameColumnFactory {

    public static DataFrameColumn<?> createColumnForType( String type ) {
        switch (type.toLowerCase()) {
            case ColumnTypes.COLUMN_TYPE_INT:
            case ColumnTypes.COLUMN_TYPE_INTEGER:
                return new IntegerColumn();
            case ColumnTypes.COLUMN_TYPE_LONG:
                return new LongColumn();
            case ColumnTypes.COLUMN_TYPE_FLOAT:
                return new FloatColumn();
            case ColumnTypes.COLUMN_TYPE_DOUBLE:
                return new DoubleColumn();
            case ColumnTypes.COLUMN_TYPE_BOOL:
            case ColumnTypes.COLUMN_TYPE_BOOLEAN:
                return new BooleanColumn();
            case ColumnTypes.COLUMN_TYPE_STRING:
                return new StringColumn();
            case ColumnTypes.COLUMN_TYPE_SPARSE_STRING:
                return new SparseStringColumn();
            case ColumnTypes.COLUMN_TYPE_SPARSE_INT:
                return new SparseIntegerColumn();
            case ColumnTypes.COLUMN_TYPE_SPARSE_LONG:
                return new SparseLongColumn();
            default:
                throw new IllegalArgumentException( "Unknown columntype (" + type + ") for unnamed column." );
        }
    }

    public static DataFrameColumn<?> createColumnForType( String columnName, String columnType ) {
        switch (columnType) {
            case ColumnTypes.COLUMN_TYPE_INT:
            case ColumnTypes.COLUMN_TYPE_INTEGER:
                return new IntegerColumn( columnName );
            case ColumnTypes.COLUMN_TYPE_BOOL:
            case ColumnTypes.COLUMN_TYPE_BOOLEAN:
                return new BooleanColumn( columnName );
            case ColumnTypes.COLUMN_TYPE_FLOAT:
                return new FloatColumn( columnName );
            case ColumnTypes.COLUMN_TYPE_DOUBLE:
                return new DoubleColumn( columnName );
            case ColumnTypes.COLUMN_TYPE_LONG:
                return new LongColumn( columnName );
            case ColumnTypes.COLUMN_TYPE_STRING:
                return new StringColumn( columnName );
            case ColumnTypes.COLUMN_TYPE_SPARSE_STRING:
                return new SparseStringColumn( columnName );
            case ColumnTypes.COLUMN_TYPE_SPARSE_INT:
                return new SparseIntegerColumn( columnName );
            case ColumnTypes.COLUMN_TYPE_SPARSE_LONG:
                return new SparseLongColumn( columnName );
            default:
                throw new IllegalArgumentException( "Unknown columntype (" + columnType + ") for column '" + columnName + "'" );
        }

    }
}
