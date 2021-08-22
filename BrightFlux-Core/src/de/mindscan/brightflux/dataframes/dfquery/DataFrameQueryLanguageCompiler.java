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
package de.mindscan.brightflux.dataframes.dfquery;

import de.mindscan.brightflux.dataframes.DataFrameRowFilterPredicate;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLBinaryOperatorNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLBinaryOperatorType;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLDataFrameColumnNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLEmptyNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLSelectNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLValueNode;
import de.mindscan.brightflux.dataframes.filterpredicate.DataFrameRowFilterPredicateFactory;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;

/**
 * We use a compiler to compile a Node into the objects understood by the dataframe.
 * 
 * I don't know whether it is a good idea to hard code the PredicateFactory here - i have to think about this stuff... 
 * Maybe we can 
 */
public class DataFrameQueryLanguageCompiler {

    public DataFrameRowFilterPredicate compileToRowFilterPredicate( DFQLNode node ) {
        if (node instanceof DFQLEmptyNode) {
            return DataFrameRowFilterPredicateFactory.any();
        }

        if (node instanceof DFQLSelectNode) {
            return compileToRowFilterPredicate( ((DFQLSelectNode) node).getWhereClauseNode() );
        }

        if (node instanceof DFQLBinaryOperatorNode) {
            DFQLBinaryOperatorNode binaryNode = (DFQLBinaryOperatorNode) node;
            DFQLBinaryOperatorType operation = binaryNode.getOperation();

            DFQLNode left = binaryNode.getLeft();
            DFQLNode right = binaryNode.getRight();

            switch (operation) {
                case EQ:
                    // depends on the left and right type...
                    if (left instanceof DFQLDataFrameColumnNode) {
                        String leftColumnName = ((DFQLDataFrameColumnNode) left).getColumnName();

                        // if right side is a value string .... we did it. 
                        if (right instanceof DFQLValueNode) {
                            Object otherValue = ((DFQLValueNode) right).getRawValue();
                            return DataFrameRowFilterPredicateFactory.eq( leftColumnName, otherValue );
                        }

                        throw new NotYetImplemetedException();
                    }

                    // return DataFrameRowFilterPredicateFactory.eq( left, right );
                    throw new NotYetImplemetedException();

                case NEQ:
                    // depends on the left and right type...
                    if (left instanceof DFQLDataFrameColumnNode) {
                        String leftColumnName = ((DFQLDataFrameColumnNode) left).getColumnName();

                        // if right side is a value string .... we did it. 
                        if (right instanceof DFQLValueNode) {
                            Object otherValue = ((DFQLValueNode) right).getRawValue();
                            return DataFrameRowFilterPredicateFactory.neq( leftColumnName, otherValue );
                        }

                        throw new NotYetImplemetedException();
                    }

                    // TODO: DataFrameRowFilterPredicateFactory.neq( left, right );
                    throw new NotYetImplemetedException();

                case GE:
                    // depends on the left and right type...
                    if (left instanceof DFQLDataFrameColumnNode) {
                        String leftColumnName = ((DFQLDataFrameColumnNode) left).getColumnName();

                        // if right side is a value string .... we did it. 
                        if (right instanceof DFQLValueNode) {
                            Object otherValue = ((DFQLValueNode) right).getRawValue();
                            return DataFrameRowFilterPredicateFactory.ge( leftColumnName, otherValue );
                        }

                        throw new NotYetImplemetedException();
                    }

                    throw new NotYetImplemetedException();

                case GT:
                    // depends on the left and right type...
                    if (left instanceof DFQLDataFrameColumnNode) {
                        String leftColumnName = ((DFQLDataFrameColumnNode) left).getColumnName();

                        // if right side is a value string .... we did it. 
                        if (right instanceof DFQLValueNode) {
                            Object otherValue = ((DFQLValueNode) right).getRawValue();
                            return DataFrameRowFilterPredicateFactory.gt( leftColumnName, otherValue );
                        }

                        throw new NotYetImplemetedException();
                    }

                    throw new NotYetImplemetedException();

                case LE:
                    // depends on the left and right type...
                    if (left instanceof DFQLDataFrameColumnNode) {
                        String leftColumnName = ((DFQLDataFrameColumnNode) left).getColumnName();

                        // if right side is a value string .... we did it. 
                        if (right instanceof DFQLValueNode) {
                            Object otherValue = ((DFQLValueNode) right).getRawValue();
                            return DataFrameRowFilterPredicateFactory.le( leftColumnName, otherValue );
                        }

                        throw new NotYetImplemetedException();
                    }

                    throw new NotYetImplemetedException();

                case LT:
                    // depends on the left and right type...
                    if (left instanceof DFQLDataFrameColumnNode) {
                        String leftColumnName = ((DFQLDataFrameColumnNode) left).getColumnName();

                        // if right side is a value string .... we did it. 
                        if (right instanceof DFQLValueNode) {
                            Object otherValue = ((DFQLValueNode) right).getRawValue();
                            return DataFrameRowFilterPredicateFactory.lt( leftColumnName, otherValue );
                        }

                        throw new NotYetImplemetedException();
                    }

                    throw new NotYetImplemetedException();

                case AND:

                    throw new NotYetImplemetedException();

                case OR:

                    throw new NotYetImplemetedException();

                default:

                    throw new NotYetImplemetedException();
            }
        }

        throw new NotYetImplemetedException();
    }
}
