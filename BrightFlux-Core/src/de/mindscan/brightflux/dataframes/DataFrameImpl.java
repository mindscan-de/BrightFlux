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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import de.mindscan.brightflux.dataframes.journal.DataFrameJournalEntry;
import de.mindscan.brightflux.dataframes.selection.DataFrameColumnSelection;
import de.mindscan.brightflux.dataframes.selection.DataFrameColumnSelectionImpl;

/**
 * TODO: use some example code for a dataframe, e.g. write them as a unit test
 *       and then develop from there. Use pandas examples for reading some files
 *       and access some of the data - and then develop from here to a java specific 
 *       syntax.
 *       
 * I will probably rewrite this completely and maybe only keep the tests and then 
 * redevelop it once i figured out, what I really need as a minimum viable product.
 * 
 * This whole code is garbage right now. But at least it should work for a prototype.
 */
public class DataFrameImpl implements DataFrame {

    public static final int DEFAULT_HEAD_ROW_COUNT = 7;
    public static final int DEFAULT_TAIL_ROW_COUNT = 7;

    private String nameOfDataFrame;
    private UUID uuidOfDataFrame;

    // we will come from a line / stream dataset to a dataframe with columns. 
    // each column has its own type and own content. Problem is, that 
    // problem is reallocation in case of appending the columns with new values instead of adding columns
    private Map<String, DataFrameColumn<?>> columnsMap = new LinkedHashMap<>();
    private DataFrameColumn<?>[] columns = null;

    private int size = 0;
    private DataFrameJournal dataFrameJournal;

    public DataFrameImpl( String nameOfDataFrame ) {
        this.nameOfDataFrame = nameOfDataFrame;
        this.uuidOfDataFrame = UUID.randomUUID();
        this.dataFrameJournal = new DataFrameJournal();
    }

    public DataFrameImpl( String nameOfDataFrame, UUID uuid ) {
        this.nameOfDataFrame = nameOfDataFrame;
        this.uuidOfDataFrame = UUID.fromString( uuid.toString() );
        this.dataFrameJournal = new DataFrameJournal();
    }

    /**
     * @return the uuid of the data frame
     */
    public UUID getUuid() {
        return uuidOfDataFrame;
    }

    /**
     * @return the name Of DataFrame
     */
    public String getName() {
        return nameOfDataFrame;
    }

    public String getTitle() {
        // + todo add the generation  
        return nameOfDataFrame;
    }

    public void setName( String dfName ) {
        this.nameOfDataFrame = dfName;
    }

    public Collection<String> getColumnNames() {
        return new ArrayList<>( columnsMap.keySet() );
    }

    public Iterator<DataFrameRow> rowIterator() {
        return new Iterator<DataFrameRow>() {
            private int currentIndexPosition = 0;

            @Override
            public boolean hasNext() {
                return currentIndexPosition < DataFrameImpl.this.getSize();
            }

            @Override
            public DataFrameRow next() {
                // check for validity of position
                if (currentIndexPosition >= DataFrameImpl.this.getSize()) {
                    throw new NoSuchElementException();
                }

                DataFrameRow currentRow = new DataFrameRowImpl( DataFrameImpl.this, currentIndexPosition );
                currentIndexPosition++;

                return currentRow;
            }

        };
    }

    /**
     * This will return all columns for this data frame
     * @return
     */
    public Collection<DataFrameColumn<?>> getColumns() {
        return new ArrayList<>( columnsMap.values() );
    }

    // add single data frame column
    public void addColumn( DataFrameColumn<?> column ) {
        if (column == null) {
            throw new IllegalArgumentException( "column must not be null." );
        }

        // ensure we don't have the same name twice.
        if (columnsMap.containsValue( column )) {
            return;
        }

        // pump the size of an empty column to the size of the data frame.
        // so that an access in the data frame won't cause out of index issues on empty columns
        if (column.isEmpty() && this.size > 0) {
            for (int i = 0; i < this.size; i++) {
                column.appendNA();
            }
        }

        // 
        // TODO: implement the add operation for a data frame columns

        // addColumnInternal( column );
        // addHeaderAndTypeInfoInternal( column );

        // TODO: ensure that this column has a name, or something similar (e.g. number in case it is anonymous.)
        columnsMap.put( column.getColumnName(), column );

        // single threaded - problem is that this might be multi-threaded called in future.
        if (columnsMap.size() == 1) {
            this.size = column.getSize();
        }

        // update the indexable Array:
        updateColumns( columnsMap );
    }

    /**
     * @param columnsMap2
     */
    private void updateColumns( Map<String, DataFrameColumn<?>> columnsMap2 ) {
        Collection<DataFrameColumn<?>> values = columnsMap.values();
        columns = (DataFrameColumn<?>[]) values.toArray( new DataFrameColumn<?>[values.size()] );
    }

    // add multiple data frame columns
    public void addColumns( DataFrameColumn<?>... allcolumns ) {
        for (DataFrameColumn<?> dataFrameColumn : allcolumns) {
            addColumn( dataFrameColumn );
        }
    }

    // add multiple data frame columns
    public void addColumns( Collection<DataFrameColumn<?>> allcolumns ) {
        for (DataFrameColumn<?> dataFrameColumn : allcolumns) {
            addColumn( dataFrameColumn );
        }
    }

    public boolean isEmpty() {
        return this.getSize() == 0;
    }

    /**
     * @return
     */
    public int getSize() {
        return size;
    }

    public Object getAt( int columnIndex, int rowIndex ) {
        DataFrameColumn<?> column = columns[columnIndex];
        return column.get( rowIndex );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Object getAt( String columnName, int rowIndex ) {
        DataFrameColumn<?> column = columnsMap.get( columnName );
        return column.get( rowIndex );
    }

    // TODO? is that needed? remove/delete data frame column
    // TODO? is that needed? rename data frame column
    // TODO? is that needed? replace data frame column

    public DataFrame head() {
        return head( DEFAULT_HEAD_ROW_COUNT );
    }

    public DataFrame tail() {
        return tail( DEFAULT_TAIL_ROW_COUNT );
    }

    public DataFrame head( int headCount ) {
        if (headCount < 0) {
            headCount = 0;
        }

        DataFrameImpl result = new DataFrameImpl( this.getName() );

        int to = Math.min( headCount, this.getSize() );

        result.initRows( getRows( 0, to ), columns );
        return result;
    }

    public DataFrame tail( int tailCount ) {
        if (tailCount < 0) {
            tailCount = 0;
        }

        DataFrameImpl result = new DataFrameImpl( this.getName() );

        int from = Math.max( 0, this.getSize() - tailCount );

        result.initRows( getRows( from, this.getSize() ), columns );
        return result;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public DataFrame selectRows( int from, int to ) {
        DataFrameImpl result = new DataFrameImpl( this.getName() );

        result.initRows( getRows( from, to ), columns );

        return result;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Collection<DataFrameRow> getRows() {
        return getRows( 0, size );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Collection<DataFrameRow> getRows( int fromIndex, int toIndex ) {
        List<DataFrameRow> result = new ArrayList<>();

        if (fromIndex < toIndex) {
            // collect that stuff...
            for (int index = fromIndex; index < toIndex; index++) {
                result.add( new DataFrameRowImpl( this, index ) );
            }
        }

        return result;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Collection<DataFrameRow> getRowsByPredicate( DataFrameRowFilterPredicate predicate ) {
        List<DataFrameRow> selectedResultRows = new ArrayList<>();

        // get all rows from this dataframe, having a positive outcome if the predicate
        Iterator<DataFrameRow> rowIterator = this.rowIterator();
        while (rowIterator.hasNext()) {
            DataFrameRow row = (DataFrameRow) rowIterator.next();

            // TODO: the predicate contains the info where are you looking what for.
            // e.g 'age' >= 50 - how to do that ?
            if (predicate.test( row )) {
                selectedResultRows.add( row );
            }
        }

        return selectedResultRows;
    }

    private void initRows( Collection<DataFrameRow> otherDFRows, DataFrameColumn<?>[] otherDFColumns ) {
        // TODO: make sure the dataframe is empty

        // so first we need to create clones of the DataFrameColumns without the values.
        for (DataFrameColumn<?> otherColumn : otherDFColumns) {
            // we also want a correctly typed copy of the column including the columnname
            this.addColumn( otherColumn.cloneColumnEmpty() );
        }

        // then we need to append each column with each row value
        for (DataFrameRow otherRow : otherDFRows) {
            this.appendRow( otherRow );
        }

        // TODO: it would be far more effective to prepare columns and then init columnwise... 
    }

    private void appendRow( DataFrameRow otherRow ) {
        int numOfColumns = otherRow.getSize();
        Object[] elements = otherRow.getAll();
        for (int i = 0; i < numOfColumns; i++) {
            columns[i].appendRaw( elements[i] );
        }
        this.size++;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public DataFrameColumnSelection select() {
        return new DataFrameColumnSelectionImpl( this, this.columns );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public DataFrameColumnSelection select( String... columnname ) {
        // TODO Auto-generated method stub
        return null;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public DataFrameColumnSelection select( DataFrameColumn<?> column ) {
        // TODO Auto-generated method stub
        return null;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public DataFrameColumnSelection select( DataFrameColumn<?>... columns ) {
        // TODO Auto-generated method stub
        return null;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void appendJournal( String message ) {
        dataFrameJournal.addJournalEntry( DataFrameJournal.create( message ) );

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void appendJournal( DataFrameJournalEntry dataFrameJournalEntry ) {
        dataFrameJournal.addJournalEntry( dataFrameJournalEntry );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void appendJournal( Collection<DataFrameJournalEntry> dataFrameJournalEntries ) {
        dataFrameJournal.addAllJournalEntries( dataFrameJournalEntries );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public DataFrameJournal getJournal() {
        return dataFrameJournal;
    }

//    // column-names
//    // columns
//    // rows
//
//    // how about calculated columns (eg. uncorrected time, synced time?)
//    // each dataframe has an implicit or explicit id-column (which can serve as a primary key)
//    // they can be numbered, but they can also be sparse integers (e.g. position of a start of a line in a file)

    // #####################################################################################################
    // Do the write operations here as a service of this API or let the consumer of this data frame find out 
    // about the other API on his own?
    // #####################################################################################################

//    public void toCsv( Path path ) {
//        DataFrameWriterFactory.create( "csv" ).writeToFile( this, path );
//    }
//
//    public void toH5( Path path ) {
//        DataFrameWriterFactory.create( "h5" ).writeToFile( this, path );
//    }

}
