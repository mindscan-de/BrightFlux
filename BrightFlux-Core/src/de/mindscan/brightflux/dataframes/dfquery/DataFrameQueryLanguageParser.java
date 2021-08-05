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

import java.util.List;

import de.mindscan.brightflux.dataframes.DataFrameRowFilterPredicate;
import de.mindscan.brightflux.dataframes.filterpredicate.DataFrameRowFilterPredicateFactory;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;

/**
 * 
 */
public class DataFrameQueryLanguageParser {

    private String query;

    public void parseSelect( List<?> tokens ) {
        // we want to extract the column names to parse
        // extract the query to perform on the dataframe
    }

    /**
     * @param query
     * @return <code>true</code> if success
     */
    public boolean parse( String query ) {
        // TODO implement the real parsing.
        // TODO: parse the predicate -> get an AST
        // compile the selectionPredicate,
        // then compile the rowfilterpredicate...
        // get columndescriptions of parsed tree
        // get predicate of parsed tree

        // "parse..." - i mean we only can/must do these at the moment
        // everything else is just a waste of time currently.

        this.query = query;
        return true;
    }

    /**
     * @return
     */
    public String[] getColumnNames() {
        if (query.startsWith( "SELECT * FROM" )) {
            return new String[] { "*" };
        }
        throw new NotYetImplemetedException();
    }

    /**
     * @return
     */
    public DataFrameRowFilterPredicate getPredicate() {
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
}
