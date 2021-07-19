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
 * 
 * TODO : find some interesting and performant idea
 * TODO Later? must support number types / Strings? / dates? 
 * 
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

    // ################################################################################
    // Comparisons raw data / equal to, not equal to - this was general with "equals"
    // ################################################################################    

    public static DataFrameRowFilterPredicate eq( String columnName, Object otherValue ) {
        return new ComparingRowFilterPredicate( columnName, CompareOperation.EQ, otherValue );
    }

    public static DataFrameRowFilterPredicate neq( String columnName, Object otherValue ) {
        return new ComparingRowFilterPredicate( columnName, CompareOperation.NEQ, otherValue );
    }

    // ################################################################################
    // Comparisons raw data / greater than, greater or equal, less than, less or equal
    // ################################################################################    

    public static DataFrameRowFilterPredicate gt( String columnName, Object otherValue ) {
        return new ComparingRowFilterPredicate( columnName, CompareOperation.GT, otherValue );
    }

    public static DataFrameRowFilterPredicate ge( String columnName, Object otherValue ) {
        return new ComparingRowFilterPredicate( columnName, CompareOperation.GE, otherValue );
    }

    public static DataFrameRowFilterPredicate lt( String columnName, Object otherValue ) {
        return new ComparingRowFilterPredicate( columnName, CompareOperation.LT, otherValue );
    }

    public static DataFrameRowFilterPredicate le( String columnName, Object otherValue ) {
        return new ComparingRowFilterPredicate( columnName, CompareOperation.LE, otherValue );
    }

    // ############################################################
    // TODO: some string operations, since i need them right now...
    // TODO: what about lowercase/Versions?
    // ############################################################    

    public static DataFrameRowFilterPredicate containsStr( String columnName, String containedString ) {
        return new StringEvaluationRowFilterPredicate( columnName, StringEvaluationOperation.CONTAINS, containedString );
    }

    public static DataFrameRowFilterPredicate startsWithStr( String columnName, String startWithString ) {
        return null;
    }

    public static DataFrameRowFilterPredicate endsWithStr( String columnName, String endsWithString ) {
        return null;
    }

    public static DataFrameRowFilterPredicate matchesWithStr( String columnName, String containedString ) {
        return null;
    }

}
