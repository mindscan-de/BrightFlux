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

import java.util.Iterator;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRowFilterPredicate;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLSelectStatementNode;
import de.mindscan.brightflux.dataframes.dfquery.tokens.DFQLToken;
import de.mindscan.brightflux.dataframes.dfquery.tokens.DFQLTokenProvider;
import de.mindscan.brightflux.dataframes.filterpredicate.DataFrameRowFilterPredicateFactory;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;

/**
 * 
 */
public class DataFrameQueryLanguageEngine {

    public DataFrame executeDFQuery( DataFrame df, String query ) {
        DataFrameQueryLanguageParser parser = createParser( query );

        DFQLSelectStatementNode statement = (DFQLSelectStatementNode) parser.parseDFQLStatement();
        // compile for runtime?

        // runtimeConfiguration = new DFQLRuntimeConfiguration();
        // runtimeConfiguration.setVar("df", df);

        // TODO: Problem here is that the identifier df must be mapped to a dataframe
        // TODO: selection of a column of a dataframe is also not yet implemented here....
        DataFrameQueryLanguageCompiler compiler = new DataFrameQueryLanguageCompiler();
        DataFrameRowFilterPredicate rowPredicate = compiler.compileToRowFilterPredicate( statement.getWhereClause() );

        // runtime = new DFQLRuntime();
        // runtime.execute(statement, runtimeConfiguration)

        // runtime selectStatement
        // df.select().where( predicate )
        // df.select(columnList).where( predicate )

        return df.select().where( rowPredicate );
    }

    /**
     * @return
     */
    @Deprecated
    public static DataFrameRowFilterPredicate deprecatedGetPredicate( String query ) {
        // TODO use the AST/Parseresult of the parse operation to compile a predicate.

        // compile The Predicate 

        if (query.endsWith( "WHERE ( df.'h2.msg'.contains ('0x666') )" )) {
            return DataFrameRowFilterPredicateFactory.containsStr( "h2.msg", "0x666" );
        }

        if (query.endsWith( "WHERE ( ( df.'h2.sysctx' == 6 )  AND ( df.'h2.b8' == 10 )   )" )) {

            return DataFrameRowFilterPredicateFactory.and( // h2.sysctx == 6 && h2.b8==10 
                            DataFrameRowFilterPredicateFactory.eq( "h2.sysctx", 6 ), DataFrameRowFilterPredicateFactory.eq( "h2.b8", 10 ) );

        }

        throw new NotYetImplemetedException();
    }

    /**
     * @return
     */
    @Deprecated
    public static String[] getColumnNames( String query ) {
        if (query.startsWith( "SELECT * FROM" )) {
            return new String[] { "*" };
        }
        throw new NotYetImplemetedException();
    }

    private DataFrameQueryLanguageParser createParser( String dfqlQuery ) {
        DataFrameQueryLanguageTokenizer tokenizer = new DataFrameQueryLanguageTokenizer();
        tokenizer.setIgnoreWhiteSpace( true );

        Iterator<DFQLToken> tokenIterator = tokenizer.tokenize( dfqlQuery );

        DataFrameQueryLanguageParser parser = new DataFrameQueryLanguageParser();
        parser.setTokenProvider( new DFQLTokenProvider( tokenIterator ) );
        return parser;
    }

}
