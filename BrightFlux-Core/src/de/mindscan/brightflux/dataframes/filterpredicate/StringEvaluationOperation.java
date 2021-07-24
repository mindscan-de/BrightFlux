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

import de.mindscan.brightflux.exceptions.NotYetImplemetedException;

/**
 * 
 * This class implements and collects different useful string evaluation operations,
 * these operations are 'contains', 'startsWith', 'endsWith', 'matchesWith'.
 * 
 * TODO: maybe extract matchesWith to a complete RegexImplementation 
 * 
 */
public enum StringEvaluationOperation {

    CONTAINS {
        @Override
        boolean testResult( String stringRowValue, String predicateValue ) {
            return stringRowValue.contains( predicateValue );
        }
    },

    IS_EMPTY_STRING {
        @Override
        boolean testResult( String stringRowValue, String predicateValue ) {
            return stringRowValue.isEmpty();
        }
    },

    STARTS_WITH {
        @Override
        boolean testResult( String stringRowValue, String predicateValue ) {
            return stringRowValue.startsWith( predicateValue );
        }
    },

    ENDS_WITH {
        @Override
        boolean testResult( String stringRowValue, String predicateValue ) {
            return stringRowValue.endsWith( predicateValue );
        }
    },

    MATCHES_WITH {
        @Override
        boolean testResult( String stringRowValue, String predicateValue ) {
            throw new NotYetImplemetedException();
        }
    };

    /**
     * Test the stringRowValue against the predicateValue. 
     * @param stringRowValue the value of the Row
     * @param predicateValue the value to test against.
     * @return <code>true</code> iff, the stringRowValue fulfills the condition against the predicateValue. 
     */
    abstract boolean testResult( String stringRowValue, String predicateValue );

}
