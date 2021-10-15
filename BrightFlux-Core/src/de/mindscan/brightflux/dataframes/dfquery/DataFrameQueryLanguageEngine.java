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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.dataframes.DataFrameRowFilterPredicate;
import de.mindscan.brightflux.dataframes.DataFrameRowQueryCallback;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLApplyNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLBinaryOperatorNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLCallbackStatementNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLIdentifierNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLListNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLPrimarySelectionNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLSelectStatementNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLStringNode;
import de.mindscan.brightflux.dataframes.dfquery.runtime.TypedDFQLCallbackStatementNode;
import de.mindscan.brightflux.dataframes.dfquery.runtime.TypedDFQLDataFrameColumnNode;
import de.mindscan.brightflux.dataframes.dfquery.runtime.TypedDFQLDataFrameNode;
import de.mindscan.brightflux.dataframes.dfquery.runtime.TypedDFQLSelectStatementNode;
import de.mindscan.brightflux.dataframes.dfquery.tokens.DFQLToken;
import de.mindscan.brightflux.dataframes.dfquery.tokens.DFQLTokenProvider;

/**
 * 
 */
public class DataFrameQueryLanguageEngine {

    public DataFrame executeDFQuery( DataFrame df, String query ) {
        DataFrameQueryLanguageParser parser = createParser( query );

        // this is currently only a fully abstract syntax tree, which doesn't know that "df" is a dataframe...
        // or df.'columnname' is a  dataframe column name... So at least we have to type that AST into a typed AST
        DFQLSelectStatementNode statement = (DFQLSelectStatementNode) parser.parseDFQLStatement();

        // node transformation to more target-like types - dataframes, columns, method invocations etc.
        DFQLNode transformed = transformAST( statement, df );

        // TODO: type evaluation...
        // type type evaluation
        // DFQLNode transformed = transformAST( statement, df );

        // compile for runtime?

        // runtimeConfiguration = new DFQLRuntimeConfiguration();
        // runtimeConfiguration.setVar("df", df);

        // TODO: Problem here is that the identifier df must be mapped to a dataframe
        // TODO: selection of a column of a dataframe is also not yet implemented here....
        DataFrameQueryLanguageCompiler compiler = new DataFrameQueryLanguageCompiler();
        DataFrameRowFilterPredicate rowPredicate = compiler.compileToRowFilterPredicate( ((TypedDFQLSelectStatementNode) transformed).getWhereClauseNode() );
        List<String> columNamesAsStrings = compiler.getColumNamesAsStrings( transformed );

        // runtime = new DFQLRuntime();
        // runtime.execute(statement, runtimeConfiguration)

        // runtime selectStatement
        if (columNamesAsStrings.size() > 1) {
            return df.select( columNamesAsStrings.toArray( new String[columNamesAsStrings.size()] ) ).where( rowPredicate );
        }
        else if (columNamesAsStrings.size() == 1) {
            return df.select( columNamesAsStrings.toArray( new String[columNamesAsStrings.size()] ) ).where( rowPredicate );
        }

        // df.select().where( predicate )
        // df.select(columnList).where( predicate )

        return df.select().where( rowPredicate );
    }

    public DataFrame executeDFCallbackQuery( DataFrame df, String query, Map<String, DataFrameRowQueryCallback> callbacks ) {

        DataFrameQueryLanguageParser parser = createParser( query );
        DFQLCallbackStatementNode callbackStatement = (DFQLCallbackStatementNode) parser.parseDFQLStatement();

        TypedDFQLCallbackStatementNode transformed = (TypedDFQLCallbackStatementNode) transformAST( callbackStatement, df );

        DataFrameQueryLanguageCompiler compiler = new DataFrameQueryLanguageCompiler();
        DataFrameRowFilterPredicate rowPredicate = compiler.compileToRowFilterPredicate( transformed.getWhereClauseNode() );

        Collection<DataFrameRow> rows = df.getRowsByPredicate( rowPredicate );
        String callbackFunction = (String) ((DFQLIdentifierNode) transformed.getCallbackFunction()).getRawValue();
        if (callbackFunction == null) {
            return df;
        }

        DataFrameRowQueryCallback callback = callbacks.get( callbackFunction );
        if (callback != null) {
            for (DataFrameRow dataFrameRow : rows) {
                callback.apply( dataFrameRow );
            }
        }

        return df;
    }

    private DataFrameQueryLanguageParser createParser( String dfqlQuery ) {
        DataFrameQueryLanguageTokenizer tokenizer = new DataFrameQueryLanguageTokenizer();
        tokenizer.setIgnoreWhiteSpace( true );

        Iterator<DFQLToken> tokenIterator = tokenizer.tokenize( dfqlQuery );

        DataFrameQueryLanguageParser parser = new DataFrameQueryLanguageParser();
        parser.setTokenProvider( new DFQLTokenProvider( tokenIterator ) );
        return parser;
    }

    private DFQLNode transformAST( DFQLNode node, DataFrame df ) {

        if (node instanceof DFQLSelectStatementNode) {
            DFQLNode selectedColumns = transformAST( ((DFQLSelectStatementNode) node).getDataframeColumns(), df );

            // TODO table selection

            DFQLNode transformedWhere = transformAST( ((DFQLSelectStatementNode) node).getWhereClause(), df );

            // TODO table selection
            return new TypedDFQLSelectStatementNode( selectedColumns, new ArrayList<>(), transformedWhere );
        }
        else if (node instanceof DFQLCallbackStatementNode) {

            DFQLNode callbackIdentifier = transformAST( ((DFQLCallbackStatementNode) node).getCallBackIdentifier(), df );

            DFQLNode transformedWhere = transformAST( ((DFQLCallbackStatementNode) node).getWhereClause(), df );

            return new TypedDFQLCallbackStatementNode( callbackIdentifier, new ArrayList<>(), transformedWhere );
        }
        else if (node instanceof DFQLIdentifierNode) {
            if ("df".equals( ((DFQLIdentifierNode) node).getRawValue() )) {
                return new TypedDFQLDataFrameNode( df );
            }
            return node;
        }
        else if (node instanceof DFQLPrimarySelectionNode) {
            DFQLNode oldValue = ((DFQLPrimarySelectionNode) node).getValue();
            DFQLNode oldSelector = ((DFQLPrimarySelectionNode) node).getSelector();

            DFQLNode newValue = transformAST( oldValue, df );
            DFQLNode newSelector = transformAST( oldSelector, df );

            if (newValue instanceof TypedDFQLDataFrameNode) {
                if (newSelector instanceof DFQLStringNode) {
                    return new TypedDFQLDataFrameColumnNode( (TypedDFQLDataFrameNode) newValue, ((DFQLStringNode) newSelector).getRawValue().toString() );
                }
            }
            return new DFQLPrimarySelectionNode( newValue, newSelector );
        }
        else if (node instanceof DFQLBinaryOperatorNode) {
            DFQLNode oldLeft = ((DFQLBinaryOperatorNode) node).getLeft();
            DFQLNode oldRight = ((DFQLBinaryOperatorNode) node).getRight();

            DFQLNode newLeft = transformAST( oldLeft, df );
            DFQLNode newRight = transformAST( oldRight, df );

            return new DFQLBinaryOperatorNode( ((DFQLBinaryOperatorNode) node).getOperation(), newLeft, newRight );
        }
        else if (node instanceof DFQLListNode) {
            DFQLListNode newList = new DFQLListNode();
            List<DFQLNode> nodes = ((DFQLListNode) node).getNodes();
            for (DFQLNode dfqlNode : nodes) {
                newList.append( transformAST( dfqlNode, df ) );
            }
            return newList;
        }
        else if (node instanceof DFQLApplyNode) {
            List<DFQLNode> formerArguments = ((DFQLApplyNode) node).getArguments();

            List<DFQLNode> newArguments = formerArguments.stream().map( dfqlNode -> transformAST( dfqlNode, df ) ).collect( Collectors.toList() );
            DFQLNode newFunction = transformAST( ((DFQLApplyNode) node).getFunction(), df );

            DFQLApplyNode newApplyNode = new DFQLApplyNode( newFunction, newArguments );
            return newApplyNode;
        }

        // TODO repackage String, number
        return node;
    }

}
