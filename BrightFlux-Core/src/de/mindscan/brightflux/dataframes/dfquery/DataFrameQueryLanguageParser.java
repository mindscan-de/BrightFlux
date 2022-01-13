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

import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLApplyNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLBinaryOperatorNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLBinaryOperatorType;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLCallbackStatementNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLEmptyNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLIdentifierNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLListNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNumberNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLPrimarySelectionNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLSelectStatementNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLStringNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLUnaryOperatorNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLUnaryOperatorType;
import de.mindscan.brightflux.dataframes.dfquery.tokens.DFQLToken;
import de.mindscan.brightflux.dataframes.dfquery.tokens.DFQLTokenProvider;
import de.mindscan.brightflux.dataframes.dfquery.tokens.DFQLTokenType;
import de.mindscan.brightflux.dataframes.dfquery.tokens.DFQLTokens;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;

/**
 * Compiles a sequence of tokens of the data frame query language into an abstract syntax tree. Which needs 
 * to be typed and compiled. Maybe that compiled tree will be then passed to a runtime engine. 
 */
public class DataFrameQueryLanguageParser {

    private DFQLTokenProvider tokens;

    void setTokenProvider( DFQLTokenProvider newProvider ) {
        this.tokens = newProvider;
    }

    public void parseSelect( List<?> tokens ) {
        // we want to extract the column names to parse
        // extract the query to perform on the dataframe
    }

    // ---------------------------------------
    // parser rule sections
    // ---------------------------------------

    public DFQLNode parseDFQLStatement() {
        if (tryToken( DFQLTokens.KEYWORD_SELECT )) {
            return parseDFQLSelectStatement();
        }

        if (tryToken( DFQLTokens.KEYWORD_ROWCALLBACK )) {
            return parseDFQLCallbackStatement();
        }

        // TODO: according to the keywords we use then the keywords specific parsers
        return null;
    }

    /**
     * ROWCALLBACK identifier FROM df WHERE
     * 
     * @return
     */
    public DFQLCallbackStatementNode parseDFQLCallbackStatement() {

        // ROWCALLBACK

        if (!tryAndAcceptToken( DFQLTokens.KEYWORD_ROWCALLBACK )) {
            throw new NotYetImplemetedException( "" );
        }

        DFQLCallbackStatementNode statement = new DFQLCallbackStatementNode();

        // we want to parse the callback identifier here
        DFQLNode callbackIdentifier = parseLiteral();
        statement.setCallBackIdentifier( callbackIdentifier );

        // FROM

        if (!tryAndAcceptToken( DFQLTokens.KEYWORD_FROM )) {
            throw new NotYetImplemetedException( "" );
        }

        // parseDataframe
        DFQLNode parsedDataFrames = parseSelectStatementDataframeList();
        statement.setDataFrames( parsedDataFrames );

        // WHERE :: Optional
        if (tryAndAcceptToken( DFQLTokens.KEYWORD_WHERE )) {

            // parseExpression
            DFQLNode whereClause = parseExpression();
            statement.setWhereClause( whereClause );
        }

        return statement;
    }

    /**
     * @return
     */
    public DFQLNode parseDFQLSelectStatement() {

        // SELECT

        if (!tryAndAcceptToken( DFQLTokens.KEYWORD_SELECT )) {
            throw new NotYetImplemetedException( "" );
        }

        // parseSelectStatementColumnList
        DFQLNode parsedColumnList = parseSelectStatementColumnList();

        if (tryAndAcceptToken( DFQLTokens.KEYWORD_FROM )) {
            return parseDFQLSelectFromStatement( parsedColumnList );
        }
        else if (tryAndAcceptToken( DFQLTokens.KEYWORD_TOKENIZE )) {
            return parseDFQLSelectTokenizeStatement( parsedColumnList );
        }
        else {
            throw new NotYetImplemetedException( "" );
        }
    }

    public DFQLNode parseDFQLSelectFromStatement( DFQLNode parsedColumnList ) {
        DFQLSelectStatementNode statement = new DFQLSelectStatementNode();
        statement.setDataframeColumns( parsedColumnList );

        // parseDataframe
        DFQLNode parsedDataFrames = parseSelectStatementDataframeList();
        statement.setDataFrames( parsedDataFrames );

        // WHERE :: Optional
        if (tryAndAcceptToken( DFQLTokens.KEYWORD_WHERE )) {

            // parseExpression
            DFQLNode whereClause = parseExpression();
            statement.setWhereClause( whereClause );
        }

        return statement;
    }

    protected DFQLNode parseDFQLSelectTokenizeStatement( DFQLNode parsedColumnList ) {
        throw new NotYetImplemetedException( "the tokenize command is not yet fully implemented" );
    }

    public DFQLNode parseSelectStatementColumnList() {
        DFQLListNode columnList = new DFQLListNode();

        while (!tryToken( DFQLTokens.KEYWORD_FROM )) {
            if (tryAndAcceptToken( DFQLTokens.OPERATOR_STAR )) {
                columnList.append( new DFQLIdentifierNode( "*" ) );
            }
            else {
                DFQLNode member = parseMemberSelection();
                columnList.append( member );
                while (tryAndAcceptToken( DFQLTokens.OPERATOR_COMMA )) {
                    member = parseMemberSelection();
                    columnList.append( member );
                }
            }
        }

        return columnList;
    }

    public DFQLNode parseSelectStatementDataframeList() {
        while (!tryToken( DFQLTokens.KEYWORD_WHERE ) && !tryType( DFQLTokenType.ENDOFINPUT )) {
            // TODO implement the correct rules... currently we just skip the parsing....
            tokens.next();
        }

        // TODO implement the correct return value.
        return new DFQLIdentifierNode( "df" );
    }

    // TODO operator precedence AND; OR; +, -, Comparisons
    public DFQLNode parseExpression() {
        if (tryType( DFQLTokenType.ENDOFINPUT )) {
            // The empty token must not be consumed
            return new DFQLEmptyNode();
        }

        DFQLNode current = null;
        boolean mustCloseParenthesis = false;

        if (tryAndConsumeAsString( "(" )) {
            mustCloseParenthesis = true;
            current = parseExpression();
        }
        else if (tryAndAcceptToken( DFQLTokens.KEYWORD_NOT )) {
            DFQLToken operator = tokens.last();
            DFQLNode post = parseExpression();
            current = new DFQLUnaryOperatorNode( DFQLUnaryOperatorType.asType( operator.getValue() ), post );
        }
        else {
            current = parseMemberSelectionInvocation();
        }

        if (tryAndAcceptType( DFQLTokenType.OPERATOR )) {
            // get the operator token
            DFQLToken operator = tokens.last();

            DFQLNode left = current;
            DFQLNode right = parseExpression();
            current = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.asType( operator.getValue() ), left, right );
        }

        if (mustCloseParenthesis) {
            if (!tryAndConsumeAsString( ")" )) {
                DFQLToken lasttoken = tokens.last();
                throw new NotYetImplemetedException( "Expected an ')' but found: '" + lasttoken.getValue() + "' instead." );
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

    private boolean tryType( DFQLTokenType acceptableType ) {
        DFQLToken la = tokens.lookahead();

        if (la.getType() != acceptableType) {
            return false;
        }

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

    private boolean tryAndAcceptToken( DFQLToken acceptableToken ) {
        if (acceptableToken == null) {
            throw new IllegalArgumentException( " acceptableToken must not be null " );
        }

        DFQLToken la = tokens.lookahead();

        if (!acceptableToken.equalsIgnorePosition( la )) {
            return false;
        }

        tokens.next();

        return true;
    }

    private boolean tryToken( DFQLToken acceptableToken ) {
        if (acceptableToken == null) {
            throw new IllegalArgumentException( " acceptableToken must not be null " );
        }

        DFQLToken la = tokens.lookahead();

        if (!acceptableToken.equalsIgnorePosition( la )) {
            return false;
        }

        return true;
    }

}
