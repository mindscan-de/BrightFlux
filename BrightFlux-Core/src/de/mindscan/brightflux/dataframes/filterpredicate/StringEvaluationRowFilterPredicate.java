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
 * This implements a row filter using different string functions. This row filter
 * predicate can compare according to
 * 
 * - a contains b
 * - is empty string
 * - starts with 
 * - ends with
 * - matches with
 * 
 * It is best used with strings. But a later implementation may include also
 * string operation on numbers. But for now this is not required.
 */
public class StringEvaluationRowFilterPredicate implements DataFrameRowFilterPredicate {

    private String columnName;
    private StringEvaluationOperation evalOperation;
    private String predicateValue;

    /**
     * 
     */
    public StringEvaluationRowFilterPredicate( String columnName, StringEvaluationOperation evalOperation, String predicateValue ) {
        this.columnName = columnName;
        this.evalOperation = evalOperation;
        this.predicateValue = predicateValue;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public boolean test( DataFrameRow row ) {
        Object rowValue = row.get( columnName );
        if (rowValue == null) {
            // TODO: this is not that easy, because we should figure out the semantics of this operation
            return false;
        }

        if (rowValue instanceof String) {
            String stringRowValue = (String) rowValue;

            return evalOperation.testResult( stringRowValue, predicateValue );
        }
        else {
            // TODO: 
            // We rely here on a proper tostring operation...
            // lets skip that for now...

            // decided against breaking things - because we don't need feature completeness yet, by forced breaks.
            // throw new NotYetImplemetedException();
        }

        return false;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String describeOperation() {
        return "( df.'" + columnName + "'" + evalOperation.describeOperation() + "('" + predicateValue + "') )";
    }

}
