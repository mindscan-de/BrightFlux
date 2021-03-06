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
package de.mindscan.brightflux.dataframes;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import de.mindscan.brightflux.dataframes.journal.DataFrameJournalEntry;
import de.mindscan.brightflux.dataframes.journal.DataFrameJournalEntryType;
import de.mindscan.brightflux.dataframes.selection.DataFrameColumnSelection;

/**
 * TODO: split that interface into smaller ones?
 */
public interface DataFrame {

    // Short name
    String getName();

    String getTitle();

    // TODO: full name?
    // public String getFullName();

    int getSize();

    boolean isEmpty();

    Collection<DataFrameColumn<?>> getColumns();

    Collection<String> getColumnNames();

    DataFrameColumn<?> getColumn( String name );

    boolean hasColumn( String columnName );

    void addColumns( DataFrameColumn<?>... selectedColumnsCopy );

    Iterator<DataFrameRow> rowIterator();

    Object getAt( int columnIndex, int rowIndex );

    Object getAt( String columnName, int rowIndex );

    void setAt( int column, int row, Object value );

    void setAt( String columnName, int row, Object value );

    void setNA( int columnIndex, int rowIndex );

    void setNA( String columnName, int rowIndex );

    int predictRowIndex( String columnName, Object value );

    DataFrame head();

    DataFrame tail();

    DataFrame selectRows( int from, int to );

    DataFrameRow getRow( int rowIndex );

    Collection<DataFrameRow> getRows();

    Collection<DataFrameRow> getRows( int from, int to );

    Collection<DataFrameRow> getRowsByPredicate( DataFrameRowFilterPredicate predicate );

    DataFrameColumnSelection select();

    DataFrameColumnSelection select( String... columnname );

    DataFrameColumnSelection select( DataFrameColumn<?> column );

    DataFrameColumnSelection select( DataFrameColumn<?>... columns );

    DataFrame query( String query );

    DataFrame queryCB( String query, Map<String, DataFrameRowQueryCallback> callbacks );

    DataFrame queryTKN( String query, Function<DataFrameTokenizerJob, DataFrame> jobToDataFrameExecutor );

    void appendJournal( DataFrameJournalEntryType entryType, String message );

    void appendJournal( DataFrameJournalEntry dataFrameJournalEntry );

    void appendJournal( Collection<DataFrameJournalEntry> dataFrameJournalEntries );

    void appendJournal( DataFrameJournal otherJournal );

    DataFrameJournal getJournal();

    DataFrame inheritNewDataFrame();

    int getDataFrameGeneration();

    boolean isPresent( String columName, int row );

    boolean isPresent( int column, int row );

    UUID getUuid();

    int getOriginalRowIndex( int row );

}
