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
package de.mindscan.brightflux.dataframes.filterpredicate;

import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.dataframes.DataFrameRowFilterPredicate;

/**
 * This currently works only on Integers because of the integer cast. (I don't like it right now)
 * 
 * TODO: This could be interesting... for the SimpleNumberColumn
 * - <T extends Number & Comparable<T>> int compareTo(T otherValue)
 */
public class DataFrameRowFilterPredicateFactory {

    /**
     * Returns a filter which returns true, for any row. Therefore any row gets selected by this filter.
     * @return
     */
    public static DataFrameRowFilterPredicate any() {
        return new DataFrameRowFilterPredicate() {
            @Override
            public boolean test( DataFrameRow row ) {
                return true;
            }
        };
    }

    public static boolean testCompareResult( int compareResult, DataFrameColumnCompareOperation op ) {
        switch (op) {
            case EQ:
                return compareResult == 0;
            case NEQ:
                return compareResult != 0;
            case GE:
                return compareResult >= 0;
            case GT:
                return compareResult > 0;
            case LE:
                return compareResult <= 0;
            case LT:
                return compareResult < 0;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static DataFrameRowFilterPredicate eq( String columnName, Object otherValue ) {
        return new DataFrameRowFilterPredicate() {

            @Override
            public boolean test( DataFrameRow row ) {
                Object rowValue = row.get( columnName );
                if (rowValue == null) {
                    return otherValue == null;
                }

                // TODO: should be part of the ColumnType. in the DataframeRow?
                int compareToResult = ((Integer) rowValue).compareTo( ((Integer) otherValue) );

                return testCompareResult( compareToResult, DataFrameColumnCompareOperation.EQ );
            }
        };
    }

    public static DataFrameRowFilterPredicate neq( String columnName, Object otherValue ) {
        return new DataFrameRowFilterPredicate() {

            @Override
            public boolean test( DataFrameRow row ) {
                Object rowValue = row.get( columnName );
                if (rowValue == null) {
                    return otherValue != null;
                }

                // TODO: should be part of the ColumnType. in the DataframeRow?
                int compareToResult = ((Integer) rowValue).compareTo( ((Integer) otherValue) );

                return testCompareResult( compareToResult, DataFrameColumnCompareOperation.NEQ );
            }
        };
    }

    // ################################################################################
    // Comparisons raw data / greater than, greater or equal, less than, less or equal
    // ################################################################################    
    // TODO : find some interesting and performant idea
    // TODO Later? must support number types / Strings? / dates? 

    public static DataFrameRowFilterPredicate gt( String columnName, Object otherValue ) {
        return new DataFrameRowFilterPredicate() {

            @Override
            public boolean test( DataFrameRow row ) {
                Object rowValue = row.get( columnName );

                // TODO: should be part of the ColumnType. in the DataframeRow?
                int compareToResult = ((Integer) rowValue).compareTo( ((Integer) otherValue) );

                return testCompareResult( compareToResult, DataFrameColumnCompareOperation.GT );
            }
        };
    }

    public static DataFrameRowFilterPredicate ge( String columnName, Object otherValue ) {
        return new DataFrameRowFilterPredicate() {

            @Override
            public boolean test( DataFrameRow row ) {
                Object rowValue = row.get( columnName );

                // TODO: should be part of the ColumnType. in the DataframeRow?
                int compareToResult = ((Integer) rowValue).compareTo( ((Integer) otherValue) );

                return testCompareResult( compareToResult, DataFrameColumnCompareOperation.GE );
            }
        };
    }

    public static DataFrameRowFilterPredicate lt( String columnName, Object otherValue ) {
        return new DataFrameRowFilterPredicate() {

            @Override
            public boolean test( DataFrameRow row ) {
                Object rowValue = row.get( columnName );

                // TODO: should be part of the ColumnType. in the DataframeRow?
                int compareToResult = ((Integer) rowValue).compareTo( ((Integer) otherValue) );

                return testCompareResult( compareToResult, DataFrameColumnCompareOperation.LT );
            }
        };
    }

    public static DataFrameRowFilterPredicate le( String columnName, Object otherValue ) {
        return new DataFrameRowFilterPredicate() {

            @Override
            public boolean test( DataFrameRow row ) {
                Object rowValue = row.get( columnName );

                // TODO: should be part of the ColumnType. in the DataframeRow?
                int compareToResult = ((Integer) rowValue).compareTo( ((Integer) otherValue) );

                return testCompareResult( compareToResult, DataFrameColumnCompareOperation.LE );
            }
        };
    }

}
