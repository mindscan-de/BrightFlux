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

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.mindscan.brightflux.dataframes.dfquery.tokens.DFQLToken;
import de.mindscan.brightflux.dataframes.dfquery.tokens.DFQLTokenType;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;

/**
 * 
 */
public class DataFrameQueryLanguageTokenizer {

    // At the starting pooint we will start with very basic tokens
    // but we will also not only support SELECT ALL FROM
    // but also things like EXECUTE RECIPE "" ON
    // and CREATE DATAFRAME FROM df.'columnname' USING TOKENIZER "tokenizername"

    public final static String[] keywords = new String[] { "SELECT", "FROM", "WHERE", "ALL" /*, "AS"*/ };
    public final static String[] operators = new String[] { "==", "!=", "<=", ">=", "<", ">", ".", ",", "*", "+", "-", "!" };
    public final static Set<String> operatorTwoChars = filteredByLength( 2, operators );
    public final static Set<String> operatorOneChar = filteredByLength( 1, operators );
    public final static char[] firstMengeOperators = firstMenge( operators );
    public final static char[] whitespace = new char[] { ' ', '\t', '\r', '\n' };
    public final static char[] numbers = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    public final static char[] parenthesis = new char[] { '(', ')' };
    public final static String[] quotes = new String[] { "'", "\"" };

    private int tokenStart = 0;
    private int tokenEnd = 0;

    public Iterator<DFQLToken> tokenize( String dfqlQuery ) {
        List<DFQLToken> tokens = new LinkedList<>();

        if (dfqlQuery == null) {
            return tokens.iterator();
        }

        tokenStart = 0;
        tokenEnd = 0;
        while (tokenStart < dfqlQuery.length() && tokenEnd < dfqlQuery.length()) {
            tokenEnd = tokenStart + 1;

            char currentChar = dfqlQuery.charAt( tokenStart );

            DFQLTokenType currentTokenType = DFQLTokenType.NONE;

            if (isWhiteSpace( currentChar )) {
                currentTokenType = consumeWhiteSpaces( dfqlQuery );
            }
            else if (isParenthesis( currentChar )) {
                currentTokenType = consumeParenthesis( dfqlQuery );
            }
            else if (isStartOfOperator( currentChar )) {
                currentTokenType = consumeOperator( dfqlQuery );
            }
            else if (isStartOfQuote( currentChar )) {
                currentTokenType = consumeQuotedText( dfqlQuery );
            }
            else if (isDigit( currentChar )) {
                currentTokenType = consumeNumber( dfqlQuery );
            }
            else if (isStartOfIdentifier( currentChar )) {
                currentTokenType = consumeIdentifier( dfqlQuery );
            }

//            if(endPosition==dfqlQuery.length()) {
//                //TODO produce last token.
//            }

            if (currentTokenType != null && currentTokenType != DFQLTokenType.NONE) {
                // produce token and add to tokenlist
                tokens.add( createToken( currentTokenType, tokenStart, tokenEnd, dfqlQuery ) );
            }

            tokenStart = tokenEnd;
        }

        return tokens.iterator();
    }

    private DFQLToken createToken( DFQLTokenType currentTokenType, int tokenStart2, int tokenEnd2, String dfqlQuery ) {
        return new DFQLToken( currentTokenType, dfqlQuery.substring( tokenStart2, tokenEnd2 ) );
    }

    private DFQLTokenType consumeWhiteSpaces( String dfqlQuery ) {
        while (tokenEnd < dfqlQuery.length()) {
            if (!isWhiteSpace( dfqlQuery.charAt( tokenEnd ) )) {
                return DFQLTokenType.WHITESPACE;
            }
            tokenEnd++;
        }

        return DFQLTokenType.WHITESPACE;
    }

    private DFQLTokenType consumeParenthesis( String dfqlQuery ) {
        return DFQLTokenType.PARENTHESIS;
    }

    private DFQLTokenType consumeOperator( String dfqlQuery ) {

        // handle case that the string is possibly exceeded... (Exception found due to unit test) 
        if (tokenStart + 1 < dfqlQuery.length()) {
            String twoChars = dfqlQuery.substring( tokenStart, tokenStart + 2 );
            if (operatorTwoChars.contains( twoChars )) {
                // advance to the second char
                tokenEnd++;
                return DFQLTokenType.OPERATOR;
            }
        }

        String oneChar = dfqlQuery.substring( tokenStart, tokenStart + 1 );
        if (operatorOneChar.contains( oneChar )) {
            return DFQLTokenType.OPERATOR;
        }

        // TODO: we have some kind of issue here, that the operator is unknown, it matched the first char, but was incomplete...
        throw new NotYetImplemetedException();
    }

    private DFQLTokenType consumeQuotedText( String dfqlQuery ) {
        // TODO return Quoted Text class
        return null;
    }

    private DFQLTokenType consumeNumber( String dfqlQuery ) {
        while (tokenEnd < dfqlQuery.length()) {
            if (!isDigit( dfqlQuery.charAt( tokenEnd ) )) {
                return DFQLTokenType.NUMBER;
            }
            tokenEnd++;
        }

        return DFQLTokenType.NUMBER;
    }

    private DFQLTokenType consumeIdentifier( String dfqlQuery ) {
        // TODO first consume string, and then check if this string is a keyword.
        // if it is a keyword, we must return a keyword-class instead of an identifier-class
        return null;
    }

    private boolean isWhiteSpace( char currentChar ) {
        return isCharIn( currentChar, whitespace );
    }

    private boolean isParenthesis( char currentChar ) {
        return isCharIn( currentChar, parenthesis );
    }

    private boolean isStartOfOperator( char currentChar ) {
        return isCharIn( currentChar, firstMengeOperators );
    }

    private boolean isCharIn( char currentChar, char[] charSet ) {
        for (int i = 0; i < charSet.length; i++) {
            if (currentChar == charSet[i]) {
                return true;
            }
        }

        return false;
    }

    private boolean isStartOfQuote( char currentChar ) {
        return false;
    }

    private boolean isDigit( char currentChar ) {
        return isCharIn( currentChar, numbers );
    }

    private boolean isStartOfIdentifier( char currentChar ) {
        return false;
    }

    private static char[] firstMenge( String[] elements ) {
        // each first char only once
        Set<String> firstCharacters = Arrays.stream( elements ).map( e -> e.substring( 0, 1 ) ).collect( Collectors.toSet() );
        // convert strings of length 1 to char array
        return firstCharacters.stream().collect( Collectors.joining() ).toCharArray();
    }

    private static Set<String> filteredByLength( int length, String[] elements ) {
        Set<String> collected = Arrays.stream( elements ).filter( e -> e.length() == length ).collect( Collectors.toSet() );

        return collected;
    }

}
