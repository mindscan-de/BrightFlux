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
 *  
 */
public final class ComparingRowFilterPredicate implements DataFrameRowFilterPredicate {

    private final String columnName;
    private final Object predicateValue;
    private CompareOperation operation;

    public ComparingRowFilterPredicate( String columnName, CompareOperation compareOperation, Object predicateValue ) {
        this.columnName = columnName;
        this.operation = compareOperation;
        this.predicateValue = predicateValue;
    }

    @Override
    public boolean test( DataFrameRow row ) {
        // TODO: get the type of the row, so we know which Type we should convert the value of the Predicate to, so it
        //       is comparable by the Column, so - can we convert the predicate value to the target type?
        // TODO: we should write a test that fails for our toy dataset.
        // TODO: actually we need two things a conversion then we need a comparator instance which can do the comparison Job.
        // Object columnType = row.getType( columnName );

        Object rowValue = row.get( columnName );
        if (rowValue == null) {
            return predicateValue == null;
        }

        // check some rowValues

        // TODO: should be part of the ColumnType. in the DataframeRow?

        // We currently only support Integers in a non clean way... 
        // But actually we have to convert it into something which is assignable...
        Object convertedPredicateValue = predicateValue;

        // Optimization is YAGNI.. 
        // And we should optimize this kind of conversion if needed, (but for now it is YAGNI - you ain't gonna need it)

        int compareToResult = row.compareToRaw( columnName, convertedPredicateValue );

        // TODO: evaluate which is faster - because this method will be used a lot. 
        // CompareOperation.testResult( operation, compareResult ); 
        // vs.
        // operation.testResult(compareToResult)

        return operation.testResult( compareToResult );
    }
}