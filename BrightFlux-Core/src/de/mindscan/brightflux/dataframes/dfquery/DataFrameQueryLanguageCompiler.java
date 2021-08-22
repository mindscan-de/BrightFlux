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

import java.util.function.BiFunction;

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

    BiFunction<String, Object, DataFrameRowFilterPredicate> eqFunctionColImm = ( a, b ) -> DataFrameRowFilterPredicateFactory.eq( a, b );
    BiFunction<String, Object, DataFrameRowFilterPredicate> neqFunctionColImm = ( a, b ) -> DataFrameRowFilterPredicateFactory.neq( a, b );
    BiFunction<String, Object, DataFrameRowFilterPredicate> geFunctionColImm = ( a, b ) -> DataFrameRowFilterPredicateFactory.ge( a, b );
    BiFunction<String, Object, DataFrameRowFilterPredicate> gtFunctionColImm = ( a, b ) -> DataFrameRowFilterPredicateFactory.gt( a, b );
    BiFunction<String, Object, DataFrameRowFilterPredicate> leFunctionColImm = ( a, b ) -> DataFrameRowFilterPredicateFactory.le( a, b );
    BiFunction<String, Object, DataFrameRowFilterPredicate> ltFunctionColImm = ( a, b ) -> DataFrameRowFilterPredicateFactory.lt( a, b );

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

            BiFunction<String, Object, DataFrameRowFilterPredicate> factoryColImm;

            switch (operation) {
                case EQ:
                    factoryColImm = eqFunctionColImm;

                    // depends on the left and right type...
                    if (left instanceof DFQLDataFrameColumnNode) {
                        String leftColumnName = ((DFQLDataFrameColumnNode) left).getColumnName();

                        // if right side is a value string .... we did it. 
                        if (right instanceof DFQLValueNode) {
                            Object otherValue = ((DFQLValueNode) right).getRawValue();
                            return factoryColImm.apply( leftColumnName, otherValue );
                        }

                        throw new NotYetImplemetedException( "Right argument type (" + right.getClass().getSimpleName() + ") is not supported." );
                    }

                    // return DataFrameRowFilterPredicateFactory.eq( left, right );
                    throw new NotYetImplemetedException( "Left argument type (" + right.getClass().getSimpleName() + ") is not supported." );

                case NEQ:
                    factoryColImm = neqFunctionColImm;

                    // depends on the left and right type...
                    if (left instanceof DFQLDataFrameColumnNode) {
                        String leftColumnName = ((DFQLDataFrameColumnNode) left).getColumnName();

                        // if right side is a value string .... we did it. 
                        if (right instanceof DFQLValueNode) {
                            Object otherValue = ((DFQLValueNode) right).getRawValue();
                            return factoryColImm.apply( leftColumnName, otherValue );
                        }

                        throw new NotYetImplemetedException( "Right argument type (" + right.getClass().getSimpleName() + ") is not supported." );
                    }

                    // TODO: DataFrameRowFilterPredicateFactory.neq( left, right );
                    throw new NotYetImplemetedException( "Left argument type (" + right.getClass().getSimpleName() + ") is not supported." );

                case GE:
                    factoryColImm = geFunctionColImm;

                    // depends on the left and right type...
                    if (left instanceof DFQLDataFrameColumnNode) {
                        String leftColumnName = ((DFQLDataFrameColumnNode) left).getColumnName();

                        // if right side is a value string .... we did it. 
                        if (right instanceof DFQLValueNode) {
                            Object otherValue = ((DFQLValueNode) right).getRawValue();
                            return factoryColImm.apply( leftColumnName, otherValue );
                        }

                        throw new NotYetImplemetedException( "Right argument type (" + right.getClass().getSimpleName() + ") is not supported." );
                    }

                    throw new NotYetImplemetedException( "Left argument type (" + right.getClass().getSimpleName() + ") is not supported." );

                case GT:
                    factoryColImm = gtFunctionColImm;

                    // depends on the left and right type...
                    if (left instanceof DFQLDataFrameColumnNode) {
                        String leftColumnName = ((DFQLDataFrameColumnNode) left).getColumnName();

                        // if right side is a value string .... we did it. 
                        if (right instanceof DFQLValueNode) {
                            Object otherValue = ((DFQLValueNode) right).getRawValue();
                            return factoryColImm.apply( leftColumnName, otherValue );
                        }

                        throw new NotYetImplemetedException( "Right argument type (" + right.getClass().getSimpleName() + ") is not supported." );
                    }

                    throw new NotYetImplemetedException( "Left argument type (" + right.getClass().getSimpleName() + ") is not supported." );

                case LE:
                    factoryColImm = leFunctionColImm;

                    // depends on the left and right type...
                    if (left instanceof DFQLDataFrameColumnNode) {
                        String leftColumnName = ((DFQLDataFrameColumnNode) left).getColumnName();

                        // if right side is a value string .... we did it. 
                        if (right instanceof DFQLValueNode) {
                            Object otherValue = ((DFQLValueNode) right).getRawValue();
                            return factoryColImm.apply( leftColumnName, otherValue );
                        }

                        throw new NotYetImplemetedException( "Right argument type (" + right.getClass().getSimpleName() + ") is not supported." );
                    }

                    throw new NotYetImplemetedException( "Left argument type (" + right.getClass().getSimpleName() + ") is not supported." );

                case LT:
                    factoryColImm = ltFunctionColImm;

                    // depends on the left and right type...
                    if (left instanceof DFQLDataFrameColumnNode) {
                        String leftColumnName = ((DFQLDataFrameColumnNode) left).getColumnName();

                        // if right side is a value string .... we did it. 
                        if (right instanceof DFQLValueNode) {
                            Object otherValue = ((DFQLValueNode) right).getRawValue();
                            return factoryColImm.apply( leftColumnName, otherValue );
                        }

                        throw new NotYetImplemetedException( "Right argument type (" + right.getClass().getSimpleName() + ") is not supported." );
                    }

                    throw new NotYetImplemetedException( "Left argument type (" + right.getClass().getSimpleName() + ") is not supported." );

                case AND:

                    if (left instanceof DFQLBinaryOperatorNode) {
                        DataFrameRowFilterPredicate left_compiled = this.compileToRowFilterPredicate( left );

                        if (right instanceof DFQLBinaryOperatorNode) {
                            DataFrameRowFilterPredicate right_compild = this.compileToRowFilterPredicate( right );

                            return DataFrameRowFilterPredicateFactory.and( left_compiled, right_compild );
                        }

                        throw new NotYetImplemetedException( "Right argument type (" + right.getClass().getSimpleName() + ") is not supported." );
                    }

                    throw new NotYetImplemetedException( "Left argument type (" + right.getClass().getSimpleName() + ") is not supported." );

                case OR:

                    if (left instanceof DFQLBinaryOperatorNode) {
                        DataFrameRowFilterPredicate left_compiled = this.compileToRowFilterPredicate( left );

                        if (right instanceof DFQLBinaryOperatorNode) {
                            DataFrameRowFilterPredicate right_compild = this.compileToRowFilterPredicate( right );

                            return DataFrameRowFilterPredicateFactory.or( left_compiled, right_compild );
                        }

                        throw new NotYetImplemetedException( "Right argument type (" + right.getClass().getSimpleName() + ") is not supported." );
                    }

                    throw new NotYetImplemetedException( "Left argument type (" + right.getClass().getSimpleName() + ") is not supported." );

                default:

                    throw new NotYetImplemetedException( "Binary Operation not supported: " + operation.name() );
            }
        }

        throw new NotYetImplemetedException( "Node type (" + node.getClass().getSimpleName() + ") is not supported." );
    }

}
