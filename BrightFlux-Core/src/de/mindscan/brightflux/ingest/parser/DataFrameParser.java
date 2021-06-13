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
import de.mindscan.brightflux.ingest.token.ColumnSeparatorToken;
import de.mindscan.brightflux.ingest.token.IdentifierToken;
import de.mindscan.brightflux.ingest.token.LineSeparatorToken;

/**
 * A DataFrame parser will get a Stream/Collection of Tokens and will emit a data frame from it. 
 * - It may also provide an unoptimized dataframe, with still the tokens in place, but in column 
 *   order, so that the tokens can be checked for validity.
 */
public class DataFrameParser {

    public List<DataFrameColumn<DataToken>> parse( Collection<DataToken> tokenStream ) {

        List<DataFrameColumn<DataToken>> parseResult = new ArrayList<>();
        // always start with an empty column
        DataFrameColumn<DataToken> currentColumn = prepareNewColumn( parseResult );

        int currentRow = 0;
        Iterator<DataFrameColumn<DataToken>> columnIterator = null;

        for (DataToken dataToken : tokenStream) {
            if (dataToken instanceof LineSeparatorToken) {
                if (currentRow == 2) {
                    break;
                }
                currentRow++;

                // also reset the current row...
                columnIterator = parseResult.iterator();
                currentColumn = columnIterator.next();
            }
            if (dataToken instanceof ColumnSeparatorToken) {
                if (currentRow == 0) {
                    currentColumn = prepareNewColumn( parseResult );
                }
                else {
                    // select the current row
                    currentColumn = columnIterator.next();
                }
            }

            if (dataToken instanceof IdentifierToken) {
                currentColumn.append( dataToken );
            }
        }

        // if LineSeparatorToken -> start over with first column
        // if ColumnSeparatorToken -> select next DataColumn as Target

        // until first line separator also create new DataFrameColumn

        // if emptyToken -> addNA to the currentDataColumn
        // if IdentifierToken -> (if in first line -> make it a columnname or add String to column

        // return a dataframe containing DataFrameColumns of DataTokens
        // return a collection of DataFramecolumns of DataTokens?

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
