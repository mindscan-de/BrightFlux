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
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

import de.mindscan.brightflux.exceptions.NotYetImplemetedException;
import de.mindscan.brightflux.ingest.DataToken;
import de.mindscan.brightflux.ingest.datasource.DataSourceLexer;
import de.mindscan.brightflux.ingest.datasource.DataSourceLexerRowMode;
import de.mindscan.brightflux.ingest.datasource.DataSourceV2;
import de.mindscan.brightflux.ingest.token.ColumnSeparatorToken;
import de.mindscan.brightflux.ingest.token.LineSeparatorToken;
import de.mindscan.brightflux.ingest.token.NumberToken;

/**
 * This is a good enough tokenizer base, which should be reusable for different data frame column backed 
 * datasources to create new data frames by parsing and tokenizing the content of one column and extract
 * multiple columns from it.
 */
public abstract class SpecialDataFrameColumnTokenizerBase implements DataTokenizer {

    /**
     * 
     */
    public SpecialDataFrameColumnTokenizerBase() {
        super();
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

    /** 
     * {@inheritDoc}
     */
    @Override
    public Iterator<DataToken> tokenize( DataSourceV2 dataSource ) {

        // Actually it would be good to do a real producer-consumer-pattern here. For now - it is good enough.
        ArrayList<DataToken> resultTokens = new ArrayList<>();

        // This thing can't do anything else than a DataSourceLexerInRowMode
        DataSourceLexer lexer = dataSource.getAsStringBackedDataSourceLexer();
        if (!(lexer instanceof DataSourceLexerRowMode)) {
            throw new NotYetImplemetedException( "Non-Row-Mode DataSource found. Can't tokenize this data source." );
        }

        DataSourceLexerRowMode rowModeLexer = (DataSourceLexerRowMode) lexer;

        generateHeaderTokens( resultTokens::add, rowModeLexer );

        String[] transferColumn = rowModeLexer.getTransferColumnNames();
        boolean hasTransferColumn = transferColumn.length > 0;

        while (rowModeLexer.hasNextRow()) {
            // we start with the new row
            rowModeLexer.advanceToNextRow();

            if (hasTransferColumn) {
                for (String string : transferColumn) {
                    // TODO: the correct Type / Tokentype must be inferred... (Must only be calculated once)
                    // Currently we just assume we only transfer numeric columns....
                    resultTokens.add( TokenUtils.createToken( NumberToken.class, String.valueOf( rowModeLexer.getColumnValueRaw( string ) ) ) );
                    resultTokens.add( TokenUtils.createToken( ColumnSeparatorToken.class, "" ) );
                }
            }

            invokeRowTokenizer( resultTokens::add, rowModeLexer );

            if (rowModeLexer.hasNextRow()) {
                resultTokens.add( TokenUtils.createToken( LineSeparatorToken.class, "" ) );
            }
        }

        return resultTokens.iterator();
    }

    protected void generateHeaderTokens( Consumer<DataToken> consumer, DataSourceLexerRowMode rowModeLexer ) {
        ArrayList<InternalTokenizerColumnDescription> allColumns = new ArrayList<InternalTokenizerColumnDescription>();

        allColumns.addAll( getDataFrameTransferColumnsDescription( rowModeLexer ) );
        allColumns.addAll( getAppendedColumnDescription() );

        ColumnUtils.exportAllColumnHeaderData( allColumns, consumer );
    }

    private Collection<InternalTokenizerColumnDescription> getDataFrameTransferColumnsDescription( DataSourceLexerRowMode rowModeLexer ) {
        ArrayList<InternalTokenizerColumnDescription> allColumns = new ArrayList<>();

        String[] transferColumnNames = rowModeLexer.getTransferColumnNames();
        if (transferColumnNames != null) {
            for (String columnName : transferColumnNames) {
                // TODO: convert columnType to DataTokenType and store for transfer use. 
                allColumns.add( InternalTokenizerColumnDescription.create( columnName, rowModeLexer.getColumnType( columnName ), 0, 0 ) );
            }
        }

        return allColumns;
    }

    // Provide column description for newly tokenized columns
    protected abstract Collection<InternalTokenizerColumnDescription> getAppendedColumnDescription();

    // User defined tokenizer for the row x column of the dataframe
    protected abstract void invokeRowTokenizer( Consumer<DataToken> consumer, DataSourceLexerRowMode rowModeLexer );
}