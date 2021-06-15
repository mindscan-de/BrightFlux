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
import de.mindscan.brightflux.ingest.token.IdentifierToken;
import de.mindscan.brightflux.ingest.token.LineSeparatorToken;
import de.mindscan.brightflux.ingest.token.NumberToken;

/**
 * The DataFrame Parser will create a list of DataFrameColumn (in future probably a DataFrame),
 * which reensemble an abstract data frame, which is not yet translated into effective primitive
 * data types.
 * 
 * The DataFrameColumns will contain a list of DataTokens, which may or may not be annotated.
 * 
 */
public class DataFrameParser {

    /**
     * The parser will receive a stream or collection of Tokens and will emit a list of data frame
     * columns containing DataTokens. The most of the token are still in place for the DataFrameCompiler
     * but processed tokens are .
     * 
     * Also instead of a lot of columns, we might consider to return dataFrames or course. 
     *  
     * @param tokenStream the tokens which to compile to a data frame or a list of data frames.
     * @return A list of pre arranged columns. / data frames
     */
    public List<DataFrameColumn<DataToken>> parse( Collection<DataToken> tokenStream ) {

        List<DataFrameColumn<DataToken>> parseResult = new ArrayList<>();
        // always start with an empty column
        DataFrameColumn<DataToken> currentColumn = prepareNewColumn( parseResult );

        int currentRow = 0;
        Iterator<DataFrameColumn<DataToken>> columnIterator = null;

        for (DataToken dataToken : tokenStream) {
            // I don't like the instanceof thing for the tokens / i would prefer an enum, but also keep it extensible.
            if (dataToken instanceof LineSeparatorToken) {
                // TODO: for test purposes limit that to 2 lines 
//                if (currentRow == 2) {
//                    break;
//                }
                currentRow++;

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
                    // select the current row
                    currentColumn = columnIterator.next();
                }
            }

            if (dataToken instanceof IdentifierToken) {
                currentColumn.append( dataToken );
            }
            if (dataToken instanceof NumberToken) {
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
