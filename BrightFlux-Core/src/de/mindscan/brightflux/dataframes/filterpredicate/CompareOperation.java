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

/**
 * 
 * This class implements and collects different compare operations, these 
 * operations depend on the compateTo contract of {@link Comparable}.
 * 
 * TODO: [Ato de] Performance Evaluation 
 * 
 * Which approach is faster? 
 * - The static method 
 * - The abstract method with the operation implemented in the enum?  
 * 
 */
public enum CompareOperation {
    EQ {
        @Override
        public boolean testResult( int compareResult ) {
            return compareResult == 0;
        }

        @Override
        public String describeOperation() {
            return "==";
        }
    },

    NEQ {
        @Override
        public boolean testResult( int compareResult ) {
            return compareResult != 0;
        }

        @Override
        public String describeOperation() {
            return "!=";
        }
    },
    GT {
        @Override
        public boolean testResult( int compareResult ) {
            return compareResult > 0;
        }

        @Override
        public String describeOperation() {
            return ">";
        }
    },
    GE {
        @Override
        public boolean testResult( int compareResult ) {
            return compareResult >= 0;
        }

        @Override
        public String describeOperation() {
            return ">=";
        }
    },
    LT {
        @Override
        public boolean testResult( int compareResult ) {
            return compareResult < 0;
        }

        @Override
        public String describeOperation() {
            return "<";
        }
    },
    LE {
        @Override
        public boolean testResult( int compareResult ) {
            return compareResult <= 0;
        }

        @Override
        public String describeOperation() {
            return "<=";
        }
    };

    /**
     * Tests the result of the compareTo operation according to the selected CompareOperation.
     * 
     * @param compareResult is the result of the compareTo contract of {@link Comparable}
     * @return <code>true</code> iff the result matches the comparison 
     */
    public abstract boolean testResult( int compareResult );

    public abstract String describeOperation();

    /**
     * Tests the result of the compareTo operation according to the selected CompareOperation.
     *
     * @param op the CompareOperation
     * @param compareResult compareResult is the result of the compareTo contact of {@link Comparable}
     * @return <code>true</code> iff the result matches the given CompareOperation.
     */
    public static boolean testResult( CompareOperation op, int compareResult ) {
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
}
