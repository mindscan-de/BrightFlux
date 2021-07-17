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
package de.mindscan.brightflux.ingest.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.mindscan.brightflux.dataframes.DataFrameColumn;
import de.mindscan.brightflux.ingest.DataToken;
import de.mindscan.brightflux.ingest.columns.DataTokenColumn;
import de.mindscan.brightflux.ingest.token.ColumnSeparatorToken;
import de.mindscan.brightflux.ingest.token.EmptyToken;
import de.mindscan.brightflux.ingest.token.IdentifierToken;
import de.mindscan.brightflux.ingest.token.LineSeparatorToken;
import de.mindscan.brightflux.ingest.token.NumberToken;
import de.mindscan.brightflux.ingest.token.TextToken;

/**
 * The DataFrame Parser will create a list of DataFrameColumn (in future probably a DataFrame),
 * which reensemble an abstract data frame, which is not yet translated into effective primitive
 * data types.
 * 
 * The DataFrameColumns will contain a list of DataTokens, which may or may not be annotated.
 * 
 */
public class DataFrameParserImpl implements DataFrameParser {

    /** 
     * {@inheritDoc}
     */
    @Override
    public List<DataFrameColumn<DataToken>> parse( Collection<DataToken> tokenStream ) {

        List<DataFrameColumn<DataToken>> parseResult = new ArrayList<>();
        // always start with an empty column
        DataFrameColumn<DataToken> currentColumn = prepareNewColumn( parseResult );

        int currentRow = 0;
        Iterator<DataFrameColumn<DataToken>> columnIterator = null;

        DataToken lastToken = null;
        for (DataToken dataToken : tokenStream) {
            // I don't like the instanceof thing for the tokens / i would prefer an enum, but also keep it extensible.
            if (dataToken instanceof LineSeparatorToken) {
                // TODO: test if this is the last column, otherwise we must fill the remaining columns with N/A values

                currentRow++;

                // if something is ColumnSeparatorToken LineSeparatorToken, then append an NA token
                // this will fill the last selected column right before the Lineseparator was found,
                // but this might not be the last column expected.
                if (lastToken instanceof ColumnSeparatorToken) {
                    currentColumn.appendNA();
                }

                // also reset the current row...
                columnIterator = parseResult.iterator();
                currentColumn = columnIterator.next();
            }

            // I don't like the instanceof thing for the tokens / i would prefer an enum, but also keep it extensible.
            if (dataToken instanceof ColumnSeparatorToken) {
                if (currentRow == 0) {
                    currentColumn = prepareNewColumn( parseResult );
                }
                else {
                    if (lastToken instanceof ColumnSeparatorToken || lastToken instanceof LineSeparatorToken) {
                        currentColumn.appendNA();
                    }

                    try {
                        // select the current row
                        currentColumn = columnIterator.next();
                    }
                    catch (Exception e) {
                        // That means that the current row has more columns than built by the first lines
                        // we should report an error for the current row.
                        System.out.println( "[READERROR] Too many Columns/Columnseparators found for row: " + currentRow );

                        // TODO: InputFileFormatException, with the collected errors.
                        // Also set a kind of format exception which should be thrown at the end of processing
                        // nothing is more worse, than processing a dataset finding one issue, processing it 
                        // the next time and then find the next issue.
                    }
                }
            }

            if (dataToken instanceof IdentifierToken) {
                currentColumn.append( dataToken );
            }

            if (dataToken instanceof TextToken) {
                currentColumn.append( dataToken );
            }

            if (dataToken instanceof NumberToken) {
                currentColumn.append( dataToken );
            }

            if (dataToken instanceof EmptyToken) {
                currentColumn.appendNA();
            }

            lastToken = dataToken;

            // System.out.println( "tokentype: " + lastToken.getClass().toString() + " value: " + lastToken.getValue() );
        }

        return parseResult;
    }

    /**
     * @param parseResult
     * @return 
     */
    private DataTokenColumn prepareNewColumn( List<DataFrameColumn<DataToken>> parseResult ) {
        int size = parseResult.size();

        DataTokenColumn dataTokenColumn = new DataTokenColumn();
        dataTokenColumn.setColumnName( Integer.toString( size ) );
        parseResult.add( dataTokenColumn );
        return dataTokenColumn;
    }

}
