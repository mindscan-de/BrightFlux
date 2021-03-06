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
import java.util.Iterator;

import de.mindscan.brightflux.ingest.DataToken;
import de.mindscan.brightflux.ingest.datasource.DataSourceLexer;
import de.mindscan.brightflux.ingest.datasource.DataSourceV2;
import de.mindscan.brightflux.ingest.token.ColumnSeparatorToken;
import de.mindscan.brightflux.ingest.token.IdentifierToken;
import de.mindscan.brightflux.ingest.token.LineSeparatorToken;
import de.mindscan.brightflux.ingest.token.NumberToken;
import de.mindscan.brightflux.ingest.token.QuotedTextToken;
import de.mindscan.brightflux.ingest.token.TextToken;

/**
 *
 * TODO:
 * - add auto configuration mode, for the calculation of "Column-Separators" or user any other defined ColumnSeparators
 *   - String columnSeparator = IngestUtils.calculateColumnSeparator( head );
 * - add auto configuration mode, for detecting the number of columns
 *   - int numberOfColumns = IngestUtils.calculateNumberOfColumns( head, columnSeparator.charAt( 0 ) );
 * - add auto configuration mode, for the newline separators, which will determine newlines.
 * - add support for NA (not available) tokens
 * - add support for max column count, so that the last column can be 
 * - add support for json objects formats starting with '[' or '{'
 * 
 * - using a producer / stream model / for the parser / like the python yield construction
 */
public class CSVTokenizerImpl implements DataTokenizer {

    private static final int _0X0A_LINEFEED = 0x0a;
    private static final int _0X0D_CARRIAGERETURN = 0x0d;

    private String columnSeparator = ",";

    // TODO: we will address the configurable lineseparator later
    @SuppressWarnings( "unused" )
    private String lineSeparator = "\n";

    // TODO: implement a way to declare tokens which are treated as N/A tokens 

    // declare the empty tokens, or not available tokens (e.g. eurostat)
    // http://appsso.eurostat.ec.europa.eu/nui/show.do?dataset=urb_cpop1&lang=en
    // private String[] emptyTokens = { "", ":" };

    // TODO: we will use that thing later, e.g. make the input a text or so until the lineseparator for the last column
    @SuppressWarnings( "unused" )
    private int maxColumnCount;

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

    /** 
     * {@inheritDoc}
     */
    @Override
    public boolean isStringBased() {
        return true;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public boolean isBinaryBased() {
        return false;
    }

    // TODO: Actually I want a producer - consumer - producer model in a pipe like architecture
    // TODO: something like yield.... Anyways the iterator is the correct interface, which allows any of these implementations.

    //  // TODO: we define the input (BufferedInputStream, some other kind of InputStream?)

    // Or a different reader type for binary input for binary files.
    //  // also binary formats then can be handled better, by providing/injecting the necessary ColumnSeparatorTokens and LineSeparatorTokens, so the data frames
    //  // can be processed more generically and we don't have to reinvent the data processing and data conversion in the readers, but later.

    /** 
     * {@inheritDoc}
     */
    @Override
    public Iterator<DataToken> tokenize( DataSourceV2 dataSource ) {
        // this is good enough for now.
        ArrayList<DataToken> tokens = new ArrayList<DataToken>();

        try (DataSourceLexer lexer = dataSource.getAsStringBackedDataSourceLexer()) {
            while (lexer.isTokenStartBeforeInputEnd()) {
                lexer.prepareNextToken();

                Class<? extends DataToken> currentTokenType = consumeToken( lexer );

                if (isValidTokenType( currentTokenType )) {
                    tokens.add( createToken( currentTokenType, lexer ) );
                }
                else {
                    int tokenStart = lexer.getTokenStart();
                    int tokenEnd = lexer.getTokenEnd();

                    System.out.println( "could not process string (" + tokenStart + ";" + tokenEnd + ")" );

                    String tokenString = lexer.getTokenString();
                    for (int i = tokenStart; i < tokenEnd; i++) {
                        System.out.println( "0x" + Integer.toString( i, 16 ) + ": 0x" + Integer.toString( tokenString.charAt( i - tokenStart ), 16 ) );
                    }
                    // ignore that unknown "token"....
                }

                // ---------------------
                // Advance to next token
                // ---------------------            

                lexer.advanceToNextToken();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return tokens.iterator();
    }

    private boolean isValidTokenType( Class<? extends DataToken> currentTokenType ) {
        return currentTokenType != null;
    }

    private DataToken createToken( Class<? extends DataToken> currentTokenType, DataSourceLexer lexer ) {

        String valueString = lexer.getTokenString();

        if (currentTokenType.equals( QuotedTextToken.class )) {

            char startsWith = valueString.charAt( 0 );
            char endsWith = valueString.charAt( valueString.length() - 1 );

            if (startsWith == endsWith) {
                valueString = valueString.substring( 1, valueString.length() - 1 );
            }

            return TokenUtils.createToken( TextToken.class, valueString );
        }

        return TokenUtils.createToken( currentTokenType, valueString );
    }

    private Class<? extends DataToken> consumeToken( DataSourceLexer lexer ) {
        char charAtTokenStart = lexer.charAtTokenStart();

        Class<? extends DataToken> currentTokenType = null;

        if (isColumnSeparator( charAtTokenStart )) {
            currentTokenType = ColumnSeparatorToken.class;
        }
        else if (CSVTokenizerTerminals.isStartOfLineSeparator( charAtTokenStart )) {
            currentTokenType = consumeLineSeparator( lexer );
        }
        // TODO: add whitespace consumer here?
        else if (CSVTokenizerTerminals.isStartOfQuote( charAtTokenStart )) {
            currentTokenType = consumeQuotedText( lexer );
        }
        else if (CSVTokenizerTerminals.isDigit( charAtTokenStart )) {
            currentTokenType = consumeNumber( lexer );
        }
        else if (CSVTokenizerTerminals.isStartOfIdentifier( charAtTokenStart )) {
            currentTokenType = consumeIdentifier( lexer );
        }
        return currentTokenType;
    }

    private Class<? extends DataToken> consumeQuotedText( DataSourceLexer lexer ) {
        char firstChar = lexer.charAtTokenStart();

        if (CSVTokenizerTerminals.isStartOfQuote( firstChar )) {
            lexer.incrementTokenEndWhileNot( c -> c == firstChar );
        }

        if (!lexer.isTokenEndBeforeInputEnd()) {
            return QuotedTextToken.class;
        }

        // we increment here because we found the first char again.
        lexer.incrementTokenEnd();

        return QuotedTextToken.class;
    }

    private Class<? extends DataToken> consumeIdentifier( DataSourceLexer lexer ) {
        if (CSVTokenizerTerminals.isStartOfIdentifier( lexer.charAtTokenStart() )) {
            lexer.incrementTokenEndWhile( CSVTokenizerTerminals::isPartOfIdentifier );
        }

        // End of input reached, we declare this an identifier
        if (!lexer.isTokenEndBeforeInputEnd()) {
            return IdentifierToken.class;
        }

        if (isLineOrColumnSeparator( lexer.charAtTokenEnd() )) {
            return IdentifierToken.class;
        }

        lexer.incrementTokenEndWhileNot( this::isLineOrColumnSeparator );

        return TextToken.class;
    }

    private Class<NumberToken> consumeNumber( DataSourceLexer lexer ) {
        lexer.incrementTokenEndWhile( CSVTokenizerTerminals::isDigitOrFraction );

        return NumberToken.class;
    }

    private Class<? extends DataToken> consumeLineSeparator( DataSourceLexer lexer ) {
        char firstChar = lexer.charAtTokenStart();
        char secondChar = lexer.charAtTokenEnd();
        if (firstChar == _0X0A_LINEFEED) {
            // Unix, Linux, Android, macOS, AmigaOS, BSD, ....
        }
        else if (firstChar == _0X0D_CARRIAGERETURN) {
            if (secondChar == _0X0A_LINEFEED) {
                // windows, DOS,OS/2, CP/M, TOS (Atari) Ordering and 
                // consume second part of the full new line
                lexer.incrementTokenEnd();
            }
            else {
                // Mac OS Classic, Apple II, C64
            }
        }

        return LineSeparatorToken.class;
    }

    private boolean isLineOrColumnSeparator( char nextChar ) {
        return CSVTokenizerTerminals.isStartOfLineSeparator( nextChar ) || isColumnSeparator( nextChar );
    }

    private boolean isColumnSeparator( char currentChar ) {
        return this.columnSeparator.equals( Character.toString( currentChar ) );
    }

    // Idea Area: 

    // This is part of the Ingest configuration.
    // CSVTokenizerImpl tokenizer = new CSVTokenizerImpl();
    // tokenizer.setColumnSeparator( columnSeparator );
    // tokenizer.setLineSeparator( "\n" );
    // tokenizer.tokenize( "" );

    // basically we have a line parser
    // basically we have a columnparser (will go to next column)
    // basically we have a data parser (dependent on the current column)

    // then we init the dataparser with the stop symbol until we see a columnseparator
    // then we init the columndataparser with the columnseparator,
    // then we init the columndataparser with the lineseparator as a stop symbol
    // then we init the last columndataparser with the stopymbol for the line
    // then we init the line separator with either "\n\r", "\r\n", "\n"

}
