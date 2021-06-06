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
package de.mindscan.brightflux.ingest.tokenizers;

import java.util.ArrayList;
import java.util.List;

import de.mindscan.brightflux.ingest.DataToken;
import de.mindscan.brightflux.ingest.token.ColumnSeparatorToken;
import de.mindscan.brightflux.ingest.token.NumberToken;

/**
 * 
 */
public class CSVTokenizer {

    private String columnSeparator = ",";
    private String lineSeparator = "\n";

    private int maxColumnCount;

    /**
     * 
     */
    private int tokenStart;
    private int tokenEnd;

    /**
     * @param columnSeparator
     */
    public void setColumnSeparator( String columnSeparator ) {
        this.columnSeparator = columnSeparator;

    }

    public void setMaxColumnCount( int maxColumnCount ) {
        this.maxColumnCount = maxColumnCount;

    }

    /**
     * @param string
     */
    public void setLineSeparator( String lineSeparator ) {
        this.lineSeparator = lineSeparator;
    }

    // TODO:  Actually I want a producer - consumer - producer model in a pipe like architecture

    public List<DataToken> tokenize( String inputString ) {
        // this is good enough for now.
        ArrayList<DataToken> tokens = new ArrayList<DataToken>();

        tokenStart = 0;
        tokenEnd = 0;

        while (tokenStart < inputString.length()) {
            tokenEnd = tokenStart + 1;

            char currentChar = inputString.charAt( tokenStart );

            Class<? extends DataToken> currentTokenType = null;

            if (isColumnSeparator( currentChar )) {
                currentTokenType = ColumnSeparatorToken.class;
            }
            else
//            if (isStartOfQuote( currentTokenType )) {
//                currentTokenTypwe = consumeString();
//            }
//            else
            if (isDigit( currentChar )) {
                currentTokenType = consumeNumber( inputString );
            }
//
//            if (isStartOfIdentifier( currentChar )) {
//                currentTokenType = consumeIdentifier();
//            }
//
//            if (currentTokenType == null) {
//
//            }
//
            tokens.add( createToken( currentTokenType, inputString, tokenStart, tokenEnd ) );

            tokenStart = tokenEnd;
        }
//
//        // TODO: we define the input (BufferedInputStream, some other kind of InputStream?)
//
//        // we convert the input into a intermediate representation
//        // At start we create a collection of tokens, which are much more easy to parse.
//        // also binary formats then can be handled better, by providing/injecting the necessary ColumnSeparatorTokens and LineSeparatorTokens, so the data frames
//        // can be processed more generically and we don't have to reinvent the data processing and data conversion in the readers, but later.

        return tokens;
    }

    /**
     * @param currentChar
     * @return
     */
    private boolean isColumnSeparator( char currentChar ) {
        return this.columnSeparator.equals( Character.toString( currentChar ) );
    }

    private DataToken createToken( Class<? extends DataToken> currentTokenType, String inputString, int startIndex, int endIndex ) {

        if (NumberToken.class.equals( currentTokenType )) {
            return NumberToken.create( inputString.substring( startIndex, endIndex ) );
        }
        else if (ColumnSeparatorToken.class.equals( currentTokenType )) {
            return ColumnSeparatorToken.create();
        }

        throw new IllegalArgumentException();
    }

    private Class<NumberToken> consumeNumber( String inputString ) {
        int i = tokenStart;

        while (i < inputString.length() && isDigit( inputString.charAt( i ) )) {
            i = i + 1;
        }

        this.tokenEnd = i;

        return NumberToken.class;
    }

    private static boolean isDigit( char currentChar ) {
        return "0123456789".contains( Character.toString( currentChar ) );
    }

}
