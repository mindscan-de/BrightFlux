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
import java.util.function.Predicate;

import de.mindscan.brightflux.ingest.DataToken;
import de.mindscan.brightflux.ingest.datasource.DataSource;
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

    private DataSourceCsvStringImpl data = new DataSourceCsvStringImpl();

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
    public Iterator<DataToken> tokenize( DataSource input ) {
        // TODO: rework that whole thing to a DataSource, such that we can also use a column of a frame as a datasource as well.
        // TODO also we don't care what the source actually is, only how we want to consume this data, and whether the source can provide the data the desired way.
        // TODO: the datasource can announce its capabilities and the tokenizer may use the source according to its capabilities.
        return null;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Iterator<DataToken> tokenize( String inputString ) {
        // this is good enough for now.
        ArrayList<DataToken> tokens = new ArrayList<DataToken>();

        data.resetTokenPositions();
        data.setInputString( inputString );

        while (data.isTokenStartBeforeInputEnd()) {
            data.tokenEnd = data.tokenStart + 1;

            Class<? extends DataToken> currentTokenType = consumeToken( inputString );

            if (isValidTokenType( currentTokenType )) {
                tokens.add( createToken( currentTokenType, inputString, data.tokenStart, data.tokenEnd ) );
            }
            else {
                System.out.println( "could not process string (" + data.tokenStart + ";" + data.tokenEnd + ")" );
                for (int i = data.tokenStart; i < data.tokenEnd; i++) {
                    System.out.println( "0x" + Integer.toString( i, 16 ) );
                }
                // ignore that unknown "token"....
            }

            // ---------------------
            // Advance to next token
            // ---------------------            

            data.advanceToNextToken();
        }

        return tokens.iterator();
    }

    private boolean isValidTokenType( Class<? extends DataToken> currentTokenType ) {
        return currentTokenType != null;
    }

    private DataToken createToken( Class<? extends DataToken> currentTokenType, String inputString, int startIndex, int endIndex ) {

        String valueString = inputString.substring( startIndex, endIndex );

        if (currentTokenType.equals( QuotedTextToken.class )) {

            char startsWith = inputString.charAt( startIndex );
            char endsWith = inputString.charAt( endIndex - 1 );

            if (startsWith == endsWith) {
                valueString = inputString.substring( startIndex + 1, endIndex - 1 );
            }

            return TokenUtils.createToken( TextToken.class, valueString );
        }

        return TokenUtils.createToken( currentTokenType, valueString );
    }

    private Class<? extends DataToken> consumeToken( String inputString ) {
        char charAtTokenStart = data.charAtTokenStart();

        Class<? extends DataToken> currentTokenType = null;

        if (isColumnSeparator( charAtTokenStart )) {
            currentTokenType = ColumnSeparatorToken.class;
        }
        else if (isStartOfLineSeparator( charAtTokenStart )) {
            currentTokenType = consumeLineSeparator( inputString );
        }
        // TODO: add whitespace consumer here?
        else if (CSVTokenizerTerminals.isStartOfQuote( charAtTokenStart )) {
            currentTokenType = consumeQuotedText( inputString );
        }
        else if (CSVTokenizerTerminals.isDigit( charAtTokenStart )) {
            currentTokenType = consumeNumber( data );
        }
        else if (CSVTokenizerTerminals.isStartOfIdentifier( charAtTokenStart )) {
            currentTokenType = consumeIdentifier( data );
        }
        return currentTokenType;
    }

    private Class<? extends DataToken> consumeQuotedText( String inputString ) {

        int i = this.data.tokenStart;

        char firstChar = data.charAtTokenStart();

        if (CSVTokenizerTerminals.isStartOfQuote( firstChar )) {
            i++;
            // This will ignore or miss line endings... for now.
            while (i < inputString.length() && inputString.charAt( i ) != firstChar) {
                i++;
            }
        }

        if (i >= inputString.length()) {
            this.data.tokenEnd = i;
            return QuotedTextToken.class;
        }

        this.data.tokenEnd = i + 1;
        return QuotedTextToken.class;
    }

    private Class<? extends DataToken> consumeIdentifier( DataSourceCsvStringImpl data ) {
        if (CSVTokenizerTerminals.isStartOfIdentifier( data.charAtTokenStart() )) {
            incrementTokenEndWhile( data, CSVTokenizerTerminals::isPartOfIdentifier );
        }

        // End of input reached, we declare this an identifier
        if (!data.isTokenEndBeforeInputEnd()) {
            return IdentifierToken.class;
        }

        if (isLineOrColumnSeparator( data.charAtTokenEnd() )) {
            return IdentifierToken.class;
        }

        incrementTokenEndWhileNot( data, this::isLineOrColumnSeparator );

        return TextToken.class;
    }

    private Class<NumberToken> consumeNumber( DataSourceCsvStringImpl data ) {
        incrementTokenEndWhile( data, CSVTokenizerTerminals::isDigitOrFraction );

        return NumberToken.class;
    }

    // TODO: This is some tokenizer level logic... This should not be part of the data source.
    void incrementTokenEndWhile( DataSourceCsvStringImpl data, Predicate<Character> object ) {
        while (data.isTokenEndBeforeInputEnd() && object.test( data.charAtTokenEnd() )) {
            data.incrementTokenEnd();
        }
    }

    // TODO: This is some tokenizer level logic... This should not be part of the data source.    
    void incrementTokenEndWhileNot( DataSourceCsvStringImpl data, Predicate<Character> object ) {
        while (data.isTokenEndBeforeInputEnd() && !object.test( data.charAtTokenEnd() )) {
            data.incrementTokenEnd();
        }
    }

    private Class<? extends DataToken> consumeLineSeparator( String inputString ) {
        // good enough for now
        data.tokenEnd = data.tokenStart + 1;

        return LineSeparatorToken.class;
    }

    private boolean isLineOrColumnSeparator( char nextChar ) {
        return isStartOfLineSeparator( nextChar ) || isColumnSeparator( nextChar );
    }

    private boolean isColumnSeparator( char currentChar ) {
        return this.columnSeparator.equals( Character.toString( currentChar ) );
    }

    private boolean isStartOfLineSeparator( char currentChar ) {
        // TODO: Firstmenge of this.lineSeparator
        return currentChar == '\n' || currentChar == '\r';
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
