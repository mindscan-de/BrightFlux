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
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.mindscan.brightflux.dataframes.columns.SparseColumn;
import de.mindscan.brightflux.dataframes.dfquery.DataFrameQueryLanguageEngine;
import de.mindscan.brightflux.dataframes.journal.DataFrameJournalEntry;
import de.mindscan.brightflux.dataframes.journal.DataFrameJournalEntryType;
import de.mindscan.brightflux.dataframes.selection.DataFrameColumnSelection;
import de.mindscan.brightflux.dataframes.selection.DataFrameColumnSelectionImpl;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;

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
 * 
 * Check if we need this:
 * - remove/delete data frame column
 * - rename data frame column
 * - replace data frame column
 *
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

    private int dataFrameGeneration;

    DataFrameImpl( String nameOfDataFrame ) {
        this.nameOfDataFrame = nameOfDataFrame;
        this.uuidOfDataFrame = UUID.randomUUID();
        this.dataFrameJournal = new DataFrameJournal();
        this.dataFrameGeneration = 0;
    }

    DataFrameImpl( String nameOfDataFrame, UUID uuid ) {
        this.nameOfDataFrame = nameOfDataFrame;
        this.uuidOfDataFrame = UUID.fromString( uuid.toString() );
        this.dataFrameJournal = new DataFrameJournal();
        this.dataFrameGeneration = 0;
    }

    /**
     * @return the uuid of the data frame
     */
    @Override
    public UUID getUuid() {
        return UUID.fromString( uuidOfDataFrame.toString() );
    }

    /**
     * @return the name Of DataFrame
     */
    public String getName() {
        return nameOfDataFrame;
    }

    @Override
    public String getTitle() {
        if (dataFrameGeneration == 0) {
            return nameOfDataFrame;
        }
        return nameOfDataFrame + "(" + Integer.toString( dataFrameGeneration ) + ")";
    }

    public void setName( String dfName ) {
        this.nameOfDataFrame = dfName;
    }

    @Override
    public Collection<String> getColumnNames() {
        return new ArrayList<>( columnsMap.keySet() );
    }

    @Override
    public DataFrameColumn<?> getColumn( String name ) {
        return columnsMap.get( name );
    }

    @Override
    public Iterator<DataFrameRow> rowIterator() {
        // TODO instead of iterating through size, we can use the "__idx" column values to iterate over....
        if (!columnsMap.containsKey( DataFrameSpecialColumns.INDEX_COLUMN_NAME )) {

            // iterate over the column index.... if idx-column is not present..
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
        else {
            // TODO: here we must iterate over the __idx-column instead
            // Maybe we use an iterator over the __idx - columnvalues
            // for sparse columns  this is the right approach...

            DataFrameColumn<?> index_column = columnsMap.get( DataFrameSpecialColumns.INDEX_COLUMN_NAME );

            if (index_column instanceof SparseColumn) {
                Object[] indexArray = index_column.toArray();
                Arrays.sort( indexArray );

                return new Iterator<DataFrameRow>() {
                    private int currentIndexPosition = 0;

                    @Override
                    public boolean hasNext() {
                        return currentIndexPosition < indexArray.length;
                    }

                    @Override
                    public DataFrameRow next() {
                        // check for validity of position
                        if (currentIndexPosition >= indexArray.length) {
                            throw new NoSuchElementException();
                        }

                        DataFrameRow currentRow = new DataFrameRowImpl( DataFrameImpl.this, (Integer) indexArray[currentIndexPosition] );
                        currentIndexPosition++;

                        return currentRow;
                    }
                };
            }

            // TODO: we need to reindex the __idx column each time.... (it actually is not that simple ....
            //       for some time it might be still okay to iterate over the original columnindex... but this is quite a stretch... and a hack.
            // TODO: iterate over the __idx column next... replace this current implementation.
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
    }

    /**
     * This will return all columns for this data frame
     * @return
     */
    @Override
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
        columns = values.toArray( new DataFrameColumn<?>[values.size()] );
    }

    // add multiple data frame columns
    @Override
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

    @Override
    public boolean isEmpty() {
        // TODO: this needs to be fixed in case we have sparse columns in the dataframe, and when we start from empty...
        // the set operation is nice for dataframes, but the dataframe assumes, that set only replaces instead of adds in case of a sparse column...
        // this thing somehow needs to be fixed...
        return this.getSize() == 0;
    }

    /**
     * @return
     */
    @Override
    public int getSize() {
        return size;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public boolean hasColumn( String columnName ) {
        return columnsMap.get( columnName ) != null;
    }

    @Override
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

    @Override
    public DataFrame head() {
        return head( DEFAULT_HEAD_ROW_COUNT );
    }

    @Override
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
    public DataFrameRow getRow( int rowIndex ) {
        return new DataFrameRowImpl( DataFrameImpl.this, rowIndex );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Collection<DataFrameRow> getRows() {
        // TODO we have to work around this... for sparse frames
        // another idea is to use the __idx column as the source of truth of the iterator - this will work in case 
        // that __idx is reindexed... on operations.
        return getRows( 0, size );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Collection<DataFrameRow> getRows( int fromIndex, int toIndex ) {
        // TODO maybe we have to work around this... for sparse frames
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

        Iterator<DataFrameRow> rowIterator = this.rowIterator();
        while (rowIterator.hasNext()) {
            DataFrameRow row = rowIterator.next();

            if (predicate.test( row )) {
                selectedResultRows.add( row );
            }
        }

        return selectedResultRows;
    }

    private void initRows( Collection<DataFrameRow> otherDFRows, DataFrameColumn<?>[] otherDFColumns ) {
        // TODO: make sure the dataframe is empty?

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
        return new DataFrameColumnSelectionImpl( this, "*", this.columns );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public DataFrame query( String query ) {
        try {
            DataFrameQueryLanguageEngine engine = new DataFrameQueryLanguageEngine();
            return engine.executeDFQuery( this, query );
        }
        catch (Exception ex) {
            ex.printStackTrace();
            // we got an invalid syntax...
            throw new NotYetImplemetedException();
        }
    }

    @Override
    public DataFrame queryCB( String query, Map<String, DataFrameRowQueryCallback> callbacks ) {
        try {
            DataFrameQueryLanguageEngine engine = new DataFrameQueryLanguageEngine();
            /* return */
            engine.executeDFCallbackQuery( this, query, callbacks );

            this.appendJournal( DataFrameJournalEntryType.ROWCALLBACK, query );
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new NotYetImplemetedException();
        }

        return this;
    }

    @Override
    public DataFrame queryTKN( String query, Function<DataFrameTokenizerJob, DataFrame> jobToDataFrameExecutor ) {
        try {
            DataFrameQueryLanguageEngine engine = new DataFrameQueryLanguageEngine();

            DataFrame newDataFrame = engine.executeDFTokenize( this, query, jobToDataFrameExecutor );
            newDataFrame.appendJournal( DataFrameJournalEntryType.TOKENIZE, query );
            return newDataFrame;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new NotYetImplemetedException();
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public DataFrameColumnSelection select( String... columnname ) {
        if (columnname.length == 1 && "*".equals( columnname[0] )) {
            return this.select();
        }

        List<DataFrameColumn<?>> convertedColumns = Arrays.stream( columnname )//
                        .filter( name -> columnsMap.containsKey( name ) ) //
                        .map( name -> columnsMap.get( name ) ).collect( Collectors.toList() );

        String collectedColumnNames = convertedColumns.stream().map( c -> "'" + c.getColumnName() + "'" ).collect( Collectors.joining( ", " ) );

        DataFrameColumn<?>[] selectedColumns = convertedColumns.toArray( new DataFrameColumn[convertedColumns.size()] );

        return new DataFrameColumnSelectionImpl( this, collectedColumnNames, selectedColumns );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public DataFrameColumnSelection select( DataFrameColumn<?> column ) {
        throw new NotYetImplemetedException();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public DataFrameColumnSelection select( DataFrameColumn<?>... columns ) {
        throw new NotYetImplemetedException();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void appendJournal( DataFrameJournalEntryType entryType, String message ) {
        dataFrameJournal.addJournalEntry( entryType, message );
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

    @Override
    public void appendJournal( DataFrameJournal otherJournal ) {
        dataFrameJournal.addAllJournalEntries( otherJournal );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public DataFrameJournal getJournal() {
        return dataFrameJournal;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public DataFrame inheritNewDataFrame() {
        DataFrameImpl inheritedDataFrame = new DataFrameImpl( getName() );
        inheritedDataFrame.dataFrameGeneration = this.dataFrameGeneration + 1;

        // add the current journal to the new DataFrame, because the new dataframe inherits from this dataframe
        inheritedDataFrame.appendJournal( this.getJournal() );

        return inheritedDataFrame;
    }

    /**
     * @return the dataFrameGeneration
     */
    @Override
    public int getDataFrameGeneration() {
        return dataFrameGeneration;
    }

    @Override
    public void setAt( int columnIndex, int rowIndex, Object value ) {
        columns[columnIndex].setRaw( rowIndex, value );
    }

    @Override
    public void setAt( String columnName, int rowIndex, Object value ) {
        columnsMap.get( columnName ).setRaw( rowIndex, value );
    }

    @Override
    public boolean isPresent( int columnIndex, int row ) {
        return columns[columnIndex].isPresent( row );
    }

    @Override
    public void setNA( int columnIndex, int rowIndex ) {
        columns[columnIndex].setNA( rowIndex );
    }

    @Override
    public boolean isPresent( String columnName, int row ) {
        return columnsMap.get( columnName ).isPresent( row );
    }

    @Override
    public void setNA( String columnName, int rowIndex ) {
        columnsMap.get( columnName ).setNA( rowIndex );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public int getOriginalRowIndex( int rowIndex ) {
        if (hasColumn( DataFrameSpecialColumns.ORIGINAL_INDEX_COLUMN_NAME )) {
            Object originalColumnIndexValue = getAt( DataFrameSpecialColumns.ORIGINAL_INDEX_COLUMN_NAME, rowIndex );
            if (originalColumnIndexValue != null) {
                return ((Integer) originalColumnIndexValue).intValue();
            }
        }

        return rowIndex;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public int predictRowIndex( String columnName, Object value ) {
        // Actually instead of predict row index, we look for the insert position without inserting something.
        // he idea is to use a particular column and a particular value for this column, and binary search until we found the element where the insert should go.
        // probably it is the best if such a "search" operation is part of the column. for a binary search on a column, the whole column must be sorted in ascending. order.

        return columnsMap.get( columnName ).findInsertRowIndexRaw( value );

        // throw new NotYetImplemetedException( "implement predict Row index feature" );

        // -1 if row can't be predicted. 
        // return -1;
    }

//    // how about calculated columns (eg. uncorrected time, synced time?)

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
