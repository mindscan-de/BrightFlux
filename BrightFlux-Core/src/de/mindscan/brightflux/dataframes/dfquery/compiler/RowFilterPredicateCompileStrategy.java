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
package de.mindscan.brightflux.dataframes.dfquery.compiler;

import java.util.function.BiFunction;

import de.mindscan.brightflux.dataframes.DataFrameColumn;
import de.mindscan.brightflux.dataframes.DataFrameRowFilterPredicate;
import de.mindscan.brightflux.dataframes.columntypes.ColumnValueTypes;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLApplyNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLBinaryOperatorNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLBinaryOperatorType;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLEmptyNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNumberNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLValueNode;
import de.mindscan.brightflux.dataframes.dfquery.runtime.TypedDFQLDataFrameColumnNode;
import de.mindscan.brightflux.dataframes.dfquery.runtime.TypedDFQLSelectStatementNode;
import de.mindscan.brightflux.dataframes.filterpredicate.DataFrameRowFilterPredicateFactory;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;

/**
 * 
 */
public class RowFilterPredicateCompileStrategy {

    public BiFunction<String, Object, DataFrameRowFilterPredicate> eqFunctionColImm = DataFrameRowFilterPredicateFactory::eq;
    public BiFunction<String, Object, DataFrameRowFilterPredicate> neqFunctionColImm = DataFrameRowFilterPredicateFactory::neq;
    public BiFunction<String, Object, DataFrameRowFilterPredicate> geFunctionColImm = DataFrameRowFilterPredicateFactory::ge;
    public BiFunction<String, Object, DataFrameRowFilterPredicate> gtFunctionColImm = DataFrameRowFilterPredicateFactory::gt;
    public BiFunction<String, Object, DataFrameRowFilterPredicate> leFunctionColImm = DataFrameRowFilterPredicateFactory::le;
    public BiFunction<String, Object, DataFrameRowFilterPredicate> ltFunctionColImm = DataFrameRowFilterPredicateFactory::lt;

    public DataFrameRowFilterPredicate compile( DFQLNode node ) {
        if (node instanceof DFQLEmptyNode) {
            return DataFrameRowFilterPredicateFactory.any();
        }

        if (node instanceof TypedDFQLSelectStatementNode) {
            return compile_TypedDFQLSelectStatementNode( (TypedDFQLSelectStatementNode) node );
        }

        if (node instanceof DFQLBinaryOperatorNode) {
            return compile_DFQLBinaryOperatorNode( (DFQLBinaryOperatorNode) node );
        }

        if (node instanceof DFQLApplyNode) {
            return compile_DFQLApplyNode( (DFQLApplyNode) node );
        }

        throw new NotYetImplemetedException( "Node type (" + node.getClass().getSimpleName() + ") is not supported." );
    }

    private DataFrameRowFilterPredicate compile_TypedDFQLSelectStatementNode( TypedDFQLSelectStatementNode typedDFQLSelectStatementNode ) {
        return compile( typedDFQLSelectStatementNode.getWhereClauseNode() );
    }

    private DataFrameRowFilterPredicate compile_DFQLBinaryOperatorNode( DFQLBinaryOperatorNode binaryNode ) {
        DFQLBinaryOperatorType operation = binaryNode.getOperation();

        DFQLNode left = binaryNode.getLeft();
        DFQLNode right = binaryNode.getRight();

        BiFunction<String, Object, DataFrameRowFilterPredicate> factoryColImm;

        switch (operation) {
            case EQ:
                factoryColImm = eqFunctionColImm;

                if (left instanceof TypedDFQLDataFrameColumnNode) {
                    TypedDFQLDataFrameColumnNode leftColumn = (TypedDFQLDataFrameColumnNode) left;
                    // if right side is a value string .... we did it. 
                    if (right instanceof DFQLValueNode) {
                        Object otherValue = ((DFQLValueNode) right).getRawValue();

                        if ((right instanceof DFQLNumberNode) && (otherValue instanceof String)) {
                            return factoryColImm.apply( leftColumn.getColumnName(), toColumnValueType( leftColumn, (String) otherValue ) );
                        }

                        return factoryColImm.apply( leftColumn.getColumnName(), otherValue );
                    }

                    throw new NotYetImplemetedException( "Right argument type (" + right.getClass().getSimpleName() + ") is not supported." );
                }

                if (left instanceof DFQLBinaryOperatorNode) {
                    DataFrameRowFilterPredicate left_compiled = compile( left );

                    if (right instanceof DFQLBinaryOperatorNode) {
                        DataFrameRowFilterPredicate right_compiled = compile( right );

                        return DataFrameRowFilterPredicateFactory.eq( left_compiled, right_compiled );
                    }

                    throw new NotYetImplemetedException( "Right argument type (" + right.getClass().getSimpleName() + ") is not supported." );
                }

                // return DataFrameRowFilterPredicateFactory.eq( left, right );
                throw new NotYetImplemetedException( "Left argument type (" + right.getClass().getSimpleName() + ") is not supported." );

            case NEQ:
                factoryColImm = neqFunctionColImm;

                if (left instanceof TypedDFQLDataFrameColumnNode) {
                    TypedDFQLDataFrameColumnNode leftColumn = (TypedDFQLDataFrameColumnNode) left;
                    // if right side is a value string .... we did it. 
                    if (right instanceof DFQLValueNode) {
                        Object otherValue = ((DFQLValueNode) right).getRawValue();

                        if ((right instanceof DFQLNumberNode) && (otherValue instanceof String)) {
                            return factoryColImm.apply( leftColumn.getColumnName(), toColumnValueType( leftColumn, (String) otherValue ) );
                        }

                        return factoryColImm.apply( leftColumn.getColumnName(), otherValue );
                    }

                    throw new NotYetImplemetedException( "Right argument type (" + right.getClass().getSimpleName() + ") is not supported." );
                }

                if (left instanceof DFQLBinaryOperatorNode) {
                    DataFrameRowFilterPredicate left_compiled = compile( left );

                    if (right instanceof DFQLBinaryOperatorNode) {
                        DataFrameRowFilterPredicate right_compiled = compile( right );

                        return DataFrameRowFilterPredicateFactory.neq( left_compiled, right_compiled );
                    }

                    throw new NotYetImplemetedException( "Right argument type (" + right.getClass().getSimpleName() + ") is not supported." );
                }

                // TODO: DataFrameRowFilterPredicateFactory.neq( left, right );
                throw new NotYetImplemetedException( "Left argument type (" + right.getClass().getSimpleName() + ") is not supported." );

            case GE:
                return buildColumnValuePredicate( geFunctionColImm, left, right );

            case GT:
                return buildColumnValuePredicate( gtFunctionColImm, left, right );

            case LE:
                return buildColumnValuePredicate( leFunctionColImm, left, right );

            case LT:
                return buildColumnValuePredicate( ltFunctionColImm, left, right );

            case AND:

                if (left instanceof DFQLBinaryOperatorNode) {
                    DataFrameRowFilterPredicate left_compiled = compile( left );

                    if (right instanceof DFQLBinaryOperatorNode) {
                        DataFrameRowFilterPredicate right_compild = compile( right );

                        return DataFrameRowFilterPredicateFactory.and( left_compiled, right_compild );
                    }

                    throw new NotYetImplemetedException( "Right argument type (" + right.getClass().getSimpleName() + ") is not supported." );
                }

                throw new NotYetImplemetedException( "Left argument type (" + right.getClass().getSimpleName() + ") is not supported." );

            case OR:

                if (left instanceof DFQLBinaryOperatorNode) {
                    DataFrameRowFilterPredicate left_compiled = compile( left );

                    if (right instanceof DFQLBinaryOperatorNode) {
                        DataFrameRowFilterPredicate right_compild = compile( right );

                        return DataFrameRowFilterPredicateFactory.or( left_compiled, right_compild );
                    }

                    throw new NotYetImplemetedException( "Right argument type (" + right.getClass().getSimpleName() + ") is not supported." );
                }

                throw new NotYetImplemetedException( "Left argument type (" + right.getClass().getSimpleName() + ") is not supported." );

            default:

                throw new NotYetImplemetedException( "Binary Operation not supported: " + operation.name() );
        }
    }

    private DataFrameRowFilterPredicate compile_DFQLApplyNode( DFQLApplyNode node ) {
        throw new NotYetImplemetedException( "Node type (" + node.getClass().getSimpleName() + ") is not supported." );
    }

    private DataFrameRowFilterPredicate buildColumnValuePredicate( BiFunction<String, Object, DataFrameRowFilterPredicate> factoryColImm, DFQLNode left,
                    DFQLNode right ) {

        if (left instanceof TypedDFQLDataFrameColumnNode) {
            TypedDFQLDataFrameColumnNode leftColumn = ((TypedDFQLDataFrameColumnNode) left);
            // if right side is a value string .... we did it. 
            if (right instanceof DFQLValueNode) {
                Object otherValue = ((DFQLValueNode) right).getRawValue();

                if ((right instanceof DFQLNumberNode) && (otherValue instanceof String)) {
                    return factoryColImm.apply( leftColumn.getColumnName(), toColumnValueType( leftColumn, (String) otherValue ) );
                }

                return factoryColImm.apply( leftColumn.getColumnName(), otherValue );
            }

            throw new NotYetImplemetedException( "Right argument type (" + right.getClass().getSimpleName() + ") is not supported." );
        }

        throw new NotYetImplemetedException( "Left argument type (" + right.getClass().getSimpleName() + ") is not supported." );
    }

    private Object toColumnValueType( TypedDFQLDataFrameColumnNode leftColumn, String value ) {
        if (leftColumn.isValidColumn()) {
            DataFrameColumn<?> column = leftColumn.getColumn();
            switch (column.getColumnValueType()) {
                case ColumnValueTypes.COLUMN_TYPE_INT:
                    return Integer.parseInt( value );
                case ColumnValueTypes.COLUMN_TYPE_LONG:
                    return Long.parseLong( value );
                case ColumnValueTypes.COLUMN_TYPE_FLOAT:
                    return Float.parseFloat( value );
                case ColumnValueTypes.COLUMN_TYPE_DOUBLE:
                    return Double.parseDouble( value );
                case ColumnValueTypes.COLUMN_TYPE_STRING:
                    return value;

                default:
                    throw new NotYetImplemetedException( "value type '" + column.getColumnValueType() + "' is not yet supported for conversion." );
            }

        }
        throw new NotYetImplemetedException( "This Columnname is not present/known in the dataframe." );
    }
}
