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
package de.mindscan.brightflux.dataframes.selection;

import java.util.Collection;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameColumn;
import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.dataframes.DataFrameRowFilterPredicate;
import de.mindscan.brightflux.dataframes.journal.DataFrameJournalEntryType;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;

/**
 * 
 */
public class DataFrameColumnSelectionImpl implements DataFrameColumnSelection {

    private DataFrame df;
    private DataFrameColumn<?>[] selectedColumns;

    /**
     * 
     */
    public DataFrameColumnSelectionImpl( DataFrame df, DataFrameColumn<?>... selectedColumns ) {
        this.df = df;
        this.selectedColumns = selectedColumns;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public DataFrame where( DataFrameRowFilterPredicate predicate ) {
        Collection<DataFrameRow> selectedRows = df.getRowsByPredicate( predicate );

        DataFrame newDataFrame = df.inheritNewDataFrame();

        buildNewDataFrame( newDataFrame, selectedRows );

        // add the current operation to the Journal
        newDataFrame.appendJournal( DataFrameJournalEntryType.SELECT_WHERE, "SELECT * FROM df WHERE " + predicate.describeOperation() );

        return newDataFrame;
    }

    // TODO: implement a string based Predicate (The part after the "WHERE"-clause ) then compile the predicate into the predicate and then execute the other where statement
    // the dataframe must be known fo this.
    @Override
    public DataFrame where( String predicate ) {
        // TODO compile the predicate to a DataFrameRowFilterPredicate
        throw new NotYetImplemetedException();
    };

    private void buildNewDataFrame( DataFrame newDataFrame, Collection<DataFrameRow> selectedRows ) {
        DataFrameColumn<?>[] selectedColumnsCopy = new DataFrameColumn<?>[selectedColumns.length];
        String[] selectedColumnsNames = new String[selectedColumns.length];

        // create an empty but type correct copy of the columns
        for (int i = 0; i < selectedColumns.length; i++) {
            selectedColumnsCopy[i] = selectedColumns[i].cloneColumnEmpty();
            selectedColumnsNames[i] = selectedColumns[i].getColumnName();
        }

        for (DataFrameRow row : selectedRows) {
            for (int idx = 0; idx < selectedColumnsNames.length; idx++) {
                // TODO: selectedColumnsNames[idx] is a bit expensive? because it is a map lookup in the get operation.
                // anyhow, this is good enough for now...
                selectedColumnsCopy[idx].appendRaw( row.get( selectedColumnsNames[idx] ) );
            }
        }

        newDataFrame.addColumns( selectedColumnsCopy );
    }
}
