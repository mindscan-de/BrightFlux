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
import java.util.Iterator;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRowFilterPredicate;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLBinaryOperatorNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLIdentifierNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLPrimarySelectionNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLSelectStatementNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLStringNode;
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

        // TODO: first do the typing using an environment / we do a simple visitor pattern
        DFQLNode transformed = transformAST( statement, df );

        // compile for runtime?

        // runtimeConfiguration = new DFQLRuntimeConfiguration();
        // runtimeConfiguration.setVar("df", df);

        // TODO: Problem here is that the identifier df must be mapped to a dataframe
        // TODO: selection of a column of a dataframe is also not yet implemented here....
        DataFrameQueryLanguageCompiler compiler = new DataFrameQueryLanguageCompiler();
        DataFrameRowFilterPredicate rowPredicate = compiler.compileToRowFilterPredicate( ((TypedDFQLSelectStatementNode) transformed).getWhereClauseNode() );

        // runtime = new DFQLRuntime();
        // runtime.execute(statement, runtimeConfiguration)

        // runtime selectStatement
        // df.select().where( predicate )
        // df.select(columnList).where( predicate )

        return df.select().where( rowPredicate );
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
            // TODO columnselection
            // TODO table selection
            // TODO: transform where clause....
            DFQLNode transformedWhere = transformAST( ((DFQLSelectStatementNode) node).getWhereClause(), df );

            // TODO columnselection
            // TODO table selection
            return new TypedDFQLSelectStatementNode( new ArrayList<>(), new ArrayList<>(), transformedWhere );
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

        // TODO repackage String, number, 
        return node;
    }

}
