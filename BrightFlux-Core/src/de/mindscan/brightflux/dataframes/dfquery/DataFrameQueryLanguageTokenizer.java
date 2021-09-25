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
import java.util.LinkedList;
import java.util.List;

import de.mindscan.brightflux.dataframes.dfquery.tokenizer.DFQLTokenizerTerminals;
import de.mindscan.brightflux.dataframes.dfquery.tokens.DFQLToken;
import de.mindscan.brightflux.dataframes.dfquery.tokens.DFQLTokenType;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;

/**
 * This is just a basic and standard tokenizer for the internal DataFrameQueryLanguage (DFQL). 
 * 
 * The role of this tokenizer is to preprocess the given query into a list of Tokens, which help the parser to build
 * the abstract syntax tree (AST) according to the tokens instead of looking for individual characters. 
 * 
 * The tokenizer doesn't support very much right now, it even ignores escaped quotes (i mean it can't handle them) 
 * and we do not support escape sequences at all. This is definitely an important thing to support in future, but 
 * as of now, this is too much effort, while trying to get the whole thing running. If it becomes a necessity we
 * still can implement a more sophisticated approach. But as of now speaking, we like it simple.
 * 
 * Since we have class state using tokenStart and tokenEnd this code is not thread safe at all.
 * 
 * More cool stuff to come:
 * - CREATE DATAFRAME FROM df.'columnname' USING TOKENIZER "tokenizername"
 */
public class DataFrameQueryLanguageTokenizer {

    private int tokenStart;
    private int tokenEnd;
    private boolean ignoreWhiteSpace;

    public DataFrameQueryLanguageTokenizer() {
        tokenStart = 0;
        tokenEnd = 0;
        ignoreWhiteSpace = false;
    }

    public void setIgnoreWhiteSpace( boolean ignore ) {
        ignoreWhiteSpace = ignore;
    }

    public DFQLTokenType createWhitespaceTokenType() {
        return ignoreWhiteSpace ? DFQLTokenType.NONE : DFQLTokenType.WHITESPACE;
    }

    public Iterator<DFQLToken> tokenize( String dfqlQuery ) {
        List<DFQLToken> tokens = new LinkedList<>();

        if (dfqlQuery == null) {
            return tokens.iterator();
        }

        tokenStart = 0;
        tokenEnd = 0;
        while (tokenStart < dfqlQuery.length() && tokenEnd < dfqlQuery.length()) {
            tokenEnd = tokenStart + 1;

            DFQLTokenType currentTokenType = consumeToken( dfqlQuery );

            if (isValidTokenType( currentTokenType )) {
                tokens.add( createToken( currentTokenType, tokenStart, tokenEnd, dfqlQuery ) );
            }

            tokenStart = tokenEnd;
        }

        return tokens.iterator();
    }

    private boolean isValidTokenType( DFQLTokenType currentTokenType ) {
        return (currentTokenType != null) && (currentTokenType != DFQLTokenType.NONE);
    }

    private DFQLToken createToken( DFQLTokenType currentTokenType, int tokenStart2, int tokenEnd2, String dfqlQuery ) {
        switch (currentTokenType) {
            case STRING:
                return new DFQLToken( currentTokenType, dfqlQuery.substring( tokenStart2 + 1, tokenEnd2 - 1 ), tokenStart2 );
            case KEYWORD:
                return new DFQLToken( currentTokenType, dfqlQuery.substring( tokenStart2, tokenEnd2 ).toUpperCase(), tokenStart2 );
            default:
                return new DFQLToken( currentTokenType, dfqlQuery.substring( tokenStart2, tokenEnd2 ), tokenStart2 );
        }
    }

    private DFQLTokenType consumeToken( String dfqlQuery ) {
        DFQLTokenType currentTokenType = DFQLTokenType.NONE;

        char currentChar = dfqlQuery.charAt( tokenStart );

        if (DFQLTokenizerTerminals.isWhiteSpace( currentChar )) {
            currentTokenType = consumeWhiteSpaces( dfqlQuery );
        }
        else if (DFQLTokenizerTerminals.isParenthesis( currentChar )) {
            currentTokenType = consumeParenthesis( dfqlQuery );
        }
        else if (DFQLTokenizerTerminals.isStartOfOperator( currentChar )) {
            currentTokenType = consumeOperator( dfqlQuery );
        }
        else if (DFQLTokenizerTerminals.isStartOfQuote( currentChar )) {
            currentTokenType = consumeQuotedText( dfqlQuery );
        }
        else if (DFQLTokenizerTerminals.isDigit( currentChar )) {
            currentTokenType = consumeNumber( dfqlQuery );
        }
        else if (DFQLTokenizerTerminals.isStartOfIdentifier( currentChar )) {
            currentTokenType = consumeIdentifier( dfqlQuery );
        }

        return currentTokenType;
    }

    private DFQLTokenType consumeWhiteSpaces( String dfqlQuery ) {
        while (tokenEnd < dfqlQuery.length() && DFQLTokenizerTerminals.isWhiteSpace( dfqlQuery.charAt( tokenEnd ) )) {
            tokenEnd++;
        }

        return createWhitespaceTokenType();
    }

    private DFQLTokenType consumeParenthesis( String dfqlQuery ) {
        return DFQLTokenType.PARENTHESIS;
    }

    private DFQLTokenType consumeOperator( String dfqlQuery ) {

        // handle case that the string is possibly exceeded... (Exception found due to unit test) 
        if (tokenStart + 1 < dfqlQuery.length()) {
            String twoChars = dfqlQuery.substring( tokenStart, tokenStart + 2 );
            if (DFQLTokenizerTerminals.isTwoCharOperator( twoChars )) {
                // advance to the second char
                tokenEnd++;
                return DFQLTokenType.OPERATOR;
            }
        }

        String oneChar = dfqlQuery.substring( tokenStart, tokenStart + 1 );
        if (DFQLTokenizerTerminals.isOneCharOperator( oneChar )) {
            return DFQLTokenType.OPERATOR;
        }

        // TODO: we have some kind of issue here, that the operator is unknown, it matched the first char, but was incomplete...
        throw new NotYetImplemetedException();
    }

    private DFQLTokenType consumeQuotedText( String dfqlQuery ) {
        char firstChar = dfqlQuery.charAt( tokenStart );

        if (tokenEnd > dfqlQuery.length()) {
            throw new NotYetImplemetedException( "The string is too short for an end of String operation." );
        }

        while (tokenEnd < dfqlQuery.length()) {
            char currentChar = dfqlQuery.charAt( tokenEnd );
            if (currentChar == firstChar) {
                // TODO Handle if there is a escape sequence right before the quoting char. (use a simple state machine)
                tokenEnd++;
                return DFQLTokenType.STRING;
            }

            tokenEnd++;
        }

        throw new NotYetImplemetedException( "The String is not ended." );
    }

    private DFQLTokenType consumeNumber( String dfqlQuery ) {
        while (tokenEnd < dfqlQuery.length() && DFQLTokenizerTerminals.isDigit( dfqlQuery.charAt( tokenEnd ) )) {
            tokenEnd++;
        }

        return DFQLTokenType.NUMBER;
    }

    private DFQLTokenType consumeIdentifier( String dfqlQuery ) {
        while (tokenEnd < dfqlQuery.length() && DFQLTokenizerTerminals.isPartOfIdentifier( dfqlQuery.charAt( tokenEnd ) )) {
            tokenEnd++;
        }

        String currentIdentifier = dfqlQuery.substring( tokenStart, tokenEnd ).toUpperCase();

        if (DFQLTokenizerTerminals.isKeyword( currentIdentifier )) {
            return DFQLTokenType.KEYWORD;
        }

        return DFQLTokenType.IDENTIFIER;
    }

}
