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
import java.util.List;
import java.util.OptionalInt;

/**
 * 
 */
public class DataFrameBuilder {

    private static final String EMPTY_DATAFRAME_NAME = "";

    private DataFrameImpl df = null;
    private ArrayList<DataFrameColumn<?>> columns = new ArrayList<>();

    /**
     * 
     */
    public DataFrameBuilder() {
        this( EMPTY_DATAFRAME_NAME );
    }

    public DataFrameBuilder( String dataFrameName ) {
        df = new DataFrameImpl( dataFrameName );
    }

    public DataFrame build() {

        int maxColumnLength = calculateMaxColumnLength( columns );

        prepareOriginalIndexColumn( maxColumnLength, columns );
        prepareIndexColumn( maxColumnLength, columns );

        // this will finalize the building of the dataframe
        df.addColumns( columns );
        // return the final dataframe
        return df;
    }

    private void prepareOriginalIndexColumn( int maxColumnLength, ArrayList<DataFrameColumn<?>> columns ) {
        // TODO:
        // Okay two things:
        // we just make sure that the columns will conatin an __original_index
        if (isColumnPresent( columns, DataFrameSpecialColumns.ORIGINAL_INDEX_COLUMN_NAME )) {
            // if we have an original index column we keep it.
            // and we don't touch it at all.
        }
        else {
            // if we have no original column we look for an index column
            if (isColumnPresent( columns, DataFrameSpecialColumns.INDEX_COLUMN_NAME )) {
                // TODO: we use the previous index column now as the original index
                columns.add( 0, DataFrameSpecialColumns.createOriginalIndexColumn( maxColumnLength ) );
            }
            else {
                // if we have no index column, we can use for the original index, we create one (first)
                columns.add( 0, DataFrameSpecialColumns.createOriginalIndexColumn( maxColumnLength ) );
            }
        }
    }

    private void prepareIndexColumn( int maxColumnLength, ArrayList<DataFrameColumn<?>> columns ) {
        // We just make sure that this dataframe gets an __index, for this current frame
        // if one is present, we remove it
        // then we create a new column
        if (isColumnPresent( columns, DataFrameSpecialColumns.INDEX_COLUMN_NAME )) {
            // TODO: Don't do anything until we also implemented the original column index as well...

            // we might want to remove the index column from the columns

            // and then just create a new one of the right size...
            // columns.add( 0, DataFrameSpecialColumns.createIndexColumn( maxColumnLength ) );
        }
        else {
            // TODO: This is how we will start this feature, that the correct columns are annotated.
            // but this code will be reconsidered once we introduced the original index column as well...

            // just create one.
            columns.add( 0, DataFrameSpecialColumns.createIndexColumn( maxColumnLength ) );
        }
    }

    private int calculateMaxColumnLength( ArrayList<DataFrameColumn<?>> columns2 ) {
        OptionalInt max = columns2.stream().mapToInt( c -> c.getSize() ).max();

        if (max.isPresent()) {
            return max.getAsInt();
        }

        return 0;
    }

    private boolean isColumnPresent( List<DataFrameColumn<?>> columns2, String indexColumnName ) {
        for (DataFrameColumn<?> dataFrameColumn : columns2) {
            if (indexColumnName.equals( dataFrameColumn.getColumnName() )) {
                return true;
            }
        }
        return false;
    }

    public DataFrameBuilder addName( String dfName ) {
        df.setName( dfName );
        return this;
    }

    public DataFrameBuilder addColumn( String columnName, String columnType ) {
        columns.add( DataFrameColumnFactory.createColumnForType( columnName, columnType ) );
        return this;
    }

    public DataFrameBuilder addColumn( DataFrameColumn<?> newColumn ) {
        this.columns.add( newColumn );
        return this;
    }

    public DataFrameBuilder addColumns( Collection<DataFrameColumn<?>> newColumns ) {
        for (DataFrameColumn<?> newColumn : newColumns) {
            this.columns.add( newColumn );
        }
        return this;
    }

    public DataFrameBuilder addColumns( String[] columnNames, String[] columnTypes ) {
        if (columnNames == null || columnNames.length == 0) {
            return this;
        }

        if (columnTypes == null || columnTypes.length == 0) {
            throw new IllegalArgumentException( "formats must not be null or empty" );
        }

        if (columnNames.length != columnTypes.length) {
            throw new IllegalArgumentException(
                            "columnNames(length: " + columnNames.length + ") and columnTypes(length: " + columnTypes.length + ") have different length" );
        }

        for (int i = 0; i < columnNames.length; i++) {
            this.addColumn( columnNames[i], columnTypes[i] );
        }

        return this;
    }

}
