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

import de.mindscan.brightflux.dataframes.journal.DataFrameJournalEntry;
import de.mindscan.brightflux.dataframes.selection.DataFrameColumnSelection;

/**
 * 
 */
public interface DataFrame {

    // Short name
    public String getName();

    // TODO: full name?
    // public String getFullName();

    int getSize();

    boolean isEmpty();

    Collection<DataFrameColumn<?>> getColumns();

    Collection<String> getColumnNames();

    Iterator<DataFrameRow> rowIterator();

    Object getAt( int columnIndex, int rowIndex );

    Object getAt( String columnName, int rowIndex );

    DataFrame head();

    DataFrame tail();

    DataFrame selectRows( int from, int to );

    Collection<DataFrameRow> getRows();

    Collection<DataFrameRow> getRows( int from, int to );

    Collection<DataFrameRow> getRowsByPredicate( DataFrameRowFilterPredicate predicate );

    DataFrameColumnSelection select();

    DataFrameColumnSelection select( String... columnname );

    DataFrameColumnSelection select( DataFrameColumn<?> column );

    DataFrameColumnSelection select( DataFrameColumn<?>... columns );

    void addJournalEntry( DataFrameJournalEntry dataFrameJournalEntry );

    void addJournalEntries( Collection<DataFrameJournalEntry> dataFrameJournalEntries );
}
