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
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLApplyNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLBinaryOperatorNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLBinaryOperatorType;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLIdentifierNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNumberNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLPrimarySelectionNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLStringNode;
import de.mindscan.brightflux.dataframes.dfquery.tokens.DFQLToken;
import de.mindscan.brightflux.dataframes.dfquery.tokens.DFQLTokenProvider;
import de.mindscan.brightflux.dataframes.dfquery.tokens.DFQLTokenType;
import de.mindscan.brightflux.dataframes.filterpredicate.DataFrameRowFilterPredicateFactory;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;

/**
 * 
 */
public class DataFrameQueryLanguageParser {

    private String query;
    private DFQLTokenProvider tokens;

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

    void setTokenProvider( DFQLTokenProvider newProvider ) {
        this.tokens = newProvider;
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

    public void parseSelect( List<?> tokens ) {
        // we want to extract the column names to parse
        // extract the query to perform on the dataframe
    }

    // ---------------------------------------
    // parser rule sections
    // ---------------------------------------

    public DFQLNode parseDFQLStatement() {
        // TODO: according to the keywords we use then the keywords specific parsers
        return null;
    }

    // TODO operator precedence AND; OR; +, -, Comparisons
    public DFQLNode parseExpression() {
        DFQLNode current = null;

        if (tryAndConsumeAsString( "(" )) {
            current = parseExpression();

            // TODO: "((expr) ==" won't work at the moment. i will have to think about it.
            if (!tryAndConsumeAsString( ")" )) {
                DFQLToken lasttoken = tokens.last();
                throw new NotYetImplemetedException( "Expected an ')' " + lasttoken.getValue() );
            }
        }
        else {
            current = parseMemberSelectionInvocation();

            if (tryAndAcceptType( DFQLTokenType.OPERATOR )) {
                // collect the last operator token
                DFQLToken operator = tokens.last();

                DFQLNode left = current;
                DFQLNode right = parseExpression();
                current = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.asType( operator.getValue() ), left, right );
            }
        }

        return current;
    }

    public DFQLNode parseMemberSelectionInvocation() {
        DFQLNode current = parseMemberSelection();

        if (tryAndConsumeAsString( "(" )) {
            // This is a method call
            DFQLApplyNode applyNode = new DFQLApplyNode( current );
            current = applyNode;

            while (!tryAndConsumeAsString( ")" )) {
                // TODO : collect the expressions -> This This must use a different parser rule later.
                DFQLNode argument = parseMemberSelectionInvocation();
                applyNode.appendArgument( argument );

                // its either a "," which means, that we have more arguments after the first one
                while (tryAndConsumeAsString( "," )) {
                    // TODO : collect the expressions -> This This must use a different parser rule later.
                    argument = parseMemberSelectionInvocation();
                    applyNode.appendArgument( argument );
                }
                // or its a ")", which means we are complete here.
            }
        }

        return current;
    }

    public DFQLNode parseMemberSelection() {
        DFQLNode current = parseLiteral();

        if (tryAndConsumeAsString( "." )) {
            DFQLNode selector = parseLiteral();

            // TODO handle number dot number as a float or double value... / 
            //      it depends on what is done in the tokenizer.

            while (tryAndConsumeAsString( "." )) {
                current = new DFQLPrimarySelectionNode( current, selector );
                selector = parseLiteral();
            }

            return new DFQLPrimarySelectionNode( current, selector );
        }

        return current;
    }

    public DFQLNode parseLiteral() {
        if (tryAndAcceptType( DFQLTokenType.STRING )) {
            DFQLToken string = tokens.last();
            return new DFQLStringNode( string.getValue() );
        }

        if (tryAndAcceptType( DFQLTokenType.NUMBER )) {
            DFQLToken number = tokens.last();
            // TODO Maybe convert that? integer, float, double, long? and then give that as an object?
            //      or let the runtime or the compiler deal with that?
            return new DFQLNumberNode( number.getValue() );
        }

        if (tryAndAcceptType( DFQLTokenType.IDENTIFIER )) {
            DFQLToken identifier = tokens.last();
            return new DFQLIdentifierNode( identifier.getValue() );
        }

        throw new NotYetImplemetedException( "Can't parse the curent token. '" + String.valueOf( tokens.last() ) + "'" );
    }

    // --------------------------
    // parser support code
    // --------------------------

    private boolean tryAndAcceptType( DFQLTokenType acceptableType ) {
        DFQLToken la = tokens.lookahead();

        // TODO: this does not support some kind of inheritance as of now speaking - do we need that here? YAGNI?
        if (la.getType() != acceptableType) {
            return false;
        }

        tokens.next();
        return true;
    }

    private boolean tryAndConsumeAsString( String acceptableString ) {
        DFQLToken la = tokens.lookahead();

        if (!la.getValue().equals( acceptableString )) {
            return false;
        }

        tokens.next();
        return true;
    }

}
