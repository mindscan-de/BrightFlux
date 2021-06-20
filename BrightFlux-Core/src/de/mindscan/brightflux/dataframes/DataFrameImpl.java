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
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * TODO: use some example code for a dataframe, e.g. write them as a unit test
 *       and then develop from there. Use pandas examples for reading some files
 *       and access some of the data - and then develop from here to a java specific 
 *       syntax.
 */
public class DataFrameImpl implements DataFrame {

    private String nameOfDataFrame;
    private UUID uuidOfDataFrame;

    // we will come from a line / stream dataset to a dataframe with columns. 
    // each column has its own type and own content. Problem is, that 
    // problem is reallocation in case of appending the columns with new values instead of adding columns
    private Map<String, DataFrameColumn<?>> columnsMap = new LinkedHashMap<>();
    // private DataFrameColumn<?>[] columns = null;

    private int size = 0;

    public DataFrameImpl( String nameOfDataFrame ) {
        this.nameOfDataFrame = nameOfDataFrame;
        this.uuidOfDataFrame = UUID.randomUUID();
    }

    public DataFrameImpl( String nameOfDataFrame, UUID uuid ) {
        this.nameOfDataFrame = nameOfDataFrame;
        this.uuidOfDataFrame = UUID.fromString( uuid.toString() );
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

                DataFrameRow currentRow = null;
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
        return null;
    }

//    public void toCsv( Path path ) {
//        DataFrameWriterFactory.create( "csv" ).writeToFile( this, path );
//    }
//
//    public void toH5( Path path ) {
//        DataFrameWriterFactory.create( "h5" ).writeToFile( this, path );
//    }

    // TODO? is that needed? remove/delete data frame column
    // TODO? is that needed? rename data frame column
    // TODO? is that needed? replace data frame column

//    public void head() {
//
//    }
//
//    public void tail() {
//
//    }
//
//    public void to_csv() {
//        // to_excel
//        // to_clipboard
//        // to dict
//        // ....
//        // to 
//    }
//    // column-names
//    // columns
//    // rows
//
//    // how about calculated columns (eg. uncorrected time, synced time?)
//    // each dataframe has an implicit or explicit id-column (which can serve as a primary key)
//    // they can be numbered, but they can also be sparse integers (e.g. position of a start of a line in a file)
}
