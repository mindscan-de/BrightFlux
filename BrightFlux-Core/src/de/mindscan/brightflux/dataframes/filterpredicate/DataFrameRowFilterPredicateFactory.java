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
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;

/**
 * TODO: find some interesting and performant idea
 * TODO: Later? must support number dates? 
 * TODO: Implement comparison operations for two columns
 * 
 * TODO logical Rowfilterpredicates: implication - tt -> t, tf -> f, ft -> t, ff -> t  ((not a) or b - is this useful?)
 * 
 * TODO: some string operations, since i need them next ...
 * TODO: what about lowercase string operations /Versions? e.g. contains ignorecase.
 * TODO: isNA(IsPresent)/isNotNA(isNotPresent) - implement the Avail/NotAvail checks.
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

            @Override
            public String describeOperation() {
                return "true";
            }
        };
    }

    // unary not 
    public static DataFrameRowFilterPredicate not( DataFrameRowFilterPredicate predicate ) {
        return new DataFrameRowFilterPredicate() {
            @Override
            public boolean test( DataFrameRow row ) {
                return !predicate.test( row );
            }

            @Override
            public String describeOperation() {
                return "( NOT " + predicate.describeOperation() + ")";
            }
        };
    }

    // conjunction - a.k.a. 'and'
    public static DataFrameRowFilterPredicate and( DataFrameRowFilterPredicate left, DataFrameRowFilterPredicate right ) {
        return new DataFrameRowFilterPredicate() {
            @Override
            public boolean test( DataFrameRow row ) {
                return left.test( row ) && right.test( row );
            }

            @Override
            public String describeOperation() {
                return "( " + left.describeOperation() + " AND " + right.describeOperation() + "  )";
            }
        };
    }

    // disjunction - a.k.a. 'or'
    public static DataFrameRowFilterPredicate or( DataFrameRowFilterPredicate left, DataFrameRowFilterPredicate right ) {
        return new DataFrameRowFilterPredicate() {
            @Override
            public boolean test( DataFrameRow row ) {
                return left.test( row ) || right.test( row );
            }

            @Override
            public String describeOperation() {
                return "( " + left.describeOperation() + " OR " + right.describeOperation() + "  )";
            }
        };
    }

    // equivalent - a.k.a. 'eq'
    public static DataFrameRowFilterPredicate eq( DataFrameRowFilterPredicate left, DataFrameRowFilterPredicate right ) {
        return new DataFrameRowFilterPredicate() {
            @Override
            public boolean test( DataFrameRow row ) {
                return left.test( row ) == right.test( row );
            }

            @Override
            public String describeOperation() {
                return "( " + left.describeOperation() + " == " + right.describeOperation() + "  )";
            }
        };
    }

    // antivalent - a.k.a. 'xor' (also not equivalent - neq)
    public static DataFrameRowFilterPredicate neq( DataFrameRowFilterPredicate left, DataFrameRowFilterPredicate right ) {
        return new DataFrameRowFilterPredicate() {
            @Override
            public boolean test( DataFrameRow row ) {
                return left.test( row ) != right.test( row );
            }

            @Override
            public String describeOperation() {
                return "( " + left.describeOperation() + " != " + right.describeOperation() + "  )";
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

    // ############################
    // String Evaluation Operations
    // ############################    

    public static DataFrameRowFilterPredicate containsStr( String columnName, String containedString ) {
        return new StringEvaluationRowFilterPredicate( columnName, StringEvaluationOperation.CONTAINS, containedString );
    }

    public static DataFrameRowFilterPredicate startsWithStr( String columnName, String startWithString ) {
        return new StringEvaluationRowFilterPredicate( columnName, StringEvaluationOperation.STARTS_WITH, startWithString );
    }

    public static DataFrameRowFilterPredicate endsWithStr( String columnName, String endsWithString ) {
        return new StringEvaluationRowFilterPredicate( columnName, StringEvaluationOperation.ENDS_WITH, endsWithString );
    }

    public static DataFrameRowFilterPredicate matchesWithStr( String columnName, String matcherString ) {
        // TODO: implement Regexfilter predicate
        throw new NotYetImplemetedException();
        // return null;
    }

}
