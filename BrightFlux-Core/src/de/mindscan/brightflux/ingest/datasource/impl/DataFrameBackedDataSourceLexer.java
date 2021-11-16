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
package de.mindscan.brightflux.ingest.datasource.impl;

import java.util.Iterator;
import java.util.function.Predicate;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.ingest.datasource.DataSourceLexerRowMode;

/**
 * This lexer treats each datarow as a new line and at the end of the row it will do something? (create a newLineToken?)
 * 
 * This lexer will convert a column of a dataframe into basically a sequence of Stringbased Lexers until the end of the frames is reached...
 * 
 * TODO: try a very simple conversion for the datasource lexer... Only one input column can be defined for the lexer at a time.
 */
public class DataFrameBackedDataSourceLexer implements DataSourceLexerRowMode {

    private DataFrame df;
    private String inputColumn;

    private Iterator<DataFrameRow> currentDataRowIterator;
    private DataFrameRow currentRow;
    private StringBackedDataSourceLexer currentRowLexer;

    public DataFrameBackedDataSourceLexer( DataFrame df, String inputColumn ) {
        this.df = df;
        this.inputColumn = inputColumn;

        // we start at first row of the dataFrame
        this.resetRow();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public char charAtTokenEnd() {
        return currentRowLexer.charAtTokenEnd();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public char charAtTokenStart() {
        return currentRowLexer.charAtTokenStart();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public boolean isTokenEndBeforeInputEnd() {
        return currentRowLexer.isTokenEndBeforeInputEnd();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public boolean isTokenStartBeforeInputEnd() {
        return currentRowLexer.isTokenStartBeforeInputEnd();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void incrementTokenEnd() {
        currentRowLexer.incrementTokenEnd();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String getTokenString() {
        return currentRowLexer.getTokenString();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void incrementTokenEndWhileNot( Predicate<Character> predicate ) {
        currentRowLexer.incrementTokenEndWhileNot( predicate );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void incrementTokenEndWhile( Predicate<Character> predicate ) {
        currentRowLexer.incrementTokenEndWhile( predicate );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public int getTokenEnd() {
        return currentRowLexer.getTokenEnd();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public int getTokenStart() {
        return currentRowLexer.getTokenStart();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void prepareNextToken() {
        currentRowLexer.prepareNextToken();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void resetTokenPositions() {
        // TODO Auto-generated method stub
        // TODO maybe reinitialize the dataframe iterator.. here
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void advanceToNextToken() {
        currentRowLexer.advanceToNextToken();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void close() throws Exception {
        // intentionally left empty, because no extra resource is acquired, but we may 
        // somehow close the iterator... or just stop it..
    }

    // ---------------------------------------
    // Special Mode for RowDataBased
    // ---------------------------------------

    /** 
     * {@inheritDoc}
     */
    @Override
    public void resetRow() {
        this.currentDataRowIterator = df.rowIterator();

        prepareCurrentDataRow();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void advanceToNextRow() {
        this.currentRow = currentDataRowIterator.next();

        // reset token position for the current row
        this.resetTokenPositions();

        prepareCurrentDataRow();

        // read the text for the current row 
        // TODO Auto-generated method stub

    }

    /**
     * 
     */
    private void prepareCurrentDataRow() {
        this.currentRowLexer = new StringBackedDataSourceLexer( String.valueOf( this.currentRow.get( inputColumn ) ) );
    }

    /** 
     * {@inheritDoc}
     * @return 
     */
    @Override
    public boolean hasNextRow() {
        return currentDataRowIterator.hasNext();
    }

}
