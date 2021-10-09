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
package de.mindscan.brightflux.ingest.compiler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.mindscan.brightflux.dataframes.DataFrameColumn;
import de.mindscan.brightflux.dataframes.columns.DataFrameColumnFactory;
import de.mindscan.brightflux.dataframes.columns.DoubleColumn;
import de.mindscan.brightflux.dataframes.columns.FloatColumn;
import de.mindscan.brightflux.dataframes.columns.IntegerColumn;
import de.mindscan.brightflux.dataframes.columns.LongColumn;
import de.mindscan.brightflux.dataframes.columns.StringColumn;
import de.mindscan.brightflux.dataframes.columntypes.ColumnTypes;
import de.mindscan.brightflux.ingest.DataToken;
import de.mindscan.brightflux.ingest.token.ColumnHeaderToken;
import de.mindscan.brightflux.ingest.token.IdentifierToken;
import de.mindscan.brightflux.ingest.token.NumberToken;
import de.mindscan.brightflux.ingest.token.TextToken;

/**
 * 
 * The purpose of the data frame compiler is to compile a collection of abstract DataFrameColumns into 
 * DataFrameColumns of the more specific type. An Abstract DataFrameColumn is a Column, which contains
 * DataTokens and only has the type hints provided by the Tokenizer. The parser just read the sequence
 * of tokens (DataToken) and rearranged them into a group of columns and also filled the blanks.
 * 
 * Therefore the data frame compiler has to do actually two steps, determine the correct "column primitive"
 * and then transfer and convert the raw DataTokens into the "column primitive" representation. 
 *
 */
public class DataFrameCompilerImpl implements DataFrameCompiler {

    /** 
     * {@inheritDoc}
     */
    @Override
    public List<DataFrameColumn<?>> compileDataFrameColumns( Collection<DataFrameColumn<DataToken>> dataframeColumns ) {
        List<DataFrameColumn<?>> compiledDataFrameColumns = new ArrayList<>();

        boolean hasColumnTitles = calcHasColumnTitles( dataframeColumns );

        for (DataFrameColumn<DataToken> column : dataframeColumns) {
            DataFrameColumn<?> compiledColumn = compileDataFrameColumn( hasColumnTitles, column );
            compiledDataFrameColumns.add( compiledColumn );
        }

        // this will build a data frame with typed and named columns, each column is compiled of its own 
        // from left to right? Bad data values are logged and these rows removed?
        return compiledDataFrameColumns;
    }

    private boolean calcHasColumnTitles( Collection<DataFrameColumn<DataToken>> dataframeColumns ) {
        Set<Class<? extends DataToken>> firstLineClasses = dataframeColumns.stream().map( col -> col.get( 0 ).getClass() ).collect( Collectors.toSet() );

        // the ColumnHeaderToken is a strong indicator, that we have columnHeaders.
        if (firstLineClasses.contains( ColumnHeaderToken.class )) {
            return true;
        }

        HashSet<Class<? extends DataToken>> temp = new HashSet<>( firstLineClasses );
        temp.remove( IdentifierToken.class );
        if (temp.size() == 0) {
            // first line only contained identifier tokens. -> Headings of one word (string indicator)
            return true;
        }

        temp.remove( TextToken.class );
        if (temp.size() == 0) {
            // first line only contained either identifiers or Text -> Headings contain at least one text (somehow weak indicator)
            return true;
        }

        // TODO: implement other useful constraints
        // e.g by comparing dataLineClasses to FirstLineClasses
        Set<Class<? extends DataToken>> dataLineClasses = dataframeColumns.stream().map( col -> col.get( 1 ).getClass() ).collect( Collectors.toSet() );
        Set<Class<? extends DataToken>> thirdLineClasses = dataframeColumns.stream().map( col -> col.get( 2 ).getClass() ).collect( Collectors.toSet() );
        Set<Class<? extends DataToken>> fourthLineClasses = dataframeColumns.stream().map( col -> col.get( 3 ).getClass() ).collect( Collectors.toSet() );

        dataLineClasses.addAll( thirdLineClasses );
        dataLineClasses.addAll( fourthLineClasses );

        // TODO: 
        // * there is also one expensive way decide by column, 
        // * but if one column deviate that means, that at least one column has a header.

        return false;
    }

    private DataFrameColumn<?> compileDataFrameColumn( boolean hasColumnTitles, DataFrameColumn<DataToken> sourceColumn ) {

        DataFrameColumn<?> destinationColumn;

        DataToken firstRowToken = sourceColumn.get( 0 );

        if (firstRowToken instanceof ColumnHeaderToken) {
            ColumnHeaderToken columnHeader = (ColumnHeaderToken) firstRowToken;
            if (columnHeader.hasTypeHint()) {
                // In case we also have a type, this part is easy and we can generate a column of the correct type and name
                destinationColumn = DataFrameColumnFactory.createColumnForType( columnHeader.getTypeHint() );
            }
            else {
                // TODO: we repeat this 4 times
                String interferenceType = inferType( getRandomDataTokenSample( 7, sourceColumn ) );

                destinationColumn = DataFrameColumnFactory.createColumnForType( interferenceType );
            }

            destinationColumn.setColumnName( columnHeader.getColumnHeaderName() );
        }
        else {

            String columnHeaderName = hasColumnTitles ? firstRowToken.getValue() : sourceColumn.toString();

            // TODO: we repeat this 4 times
            String interferenceType = inferType( getRandomDataTokenSample( 7, sourceColumn ) );

            destinationColumn = DataFrameColumnFactory.createColumnForType( interferenceType );
            destinationColumn.setColumnName( columnHeaderName );
        }

        transferAndTransformColumnData( hasColumnTitles, sourceColumn, destinationColumn );

        return destinationColumn;
    }

    /**
     * @param x
     * @return
     */
    private String inferType( List<DataToken> x ) {
        // TODO very basic implementation of the type interference 
        // we should compare two converted values and then do inference on their type on a pair by pair basis

        String returntype = ColumnTypes.COLUMN_TYPE_INT;
        for (DataToken dataToken : x) {
            if (dataToken instanceof IdentifierToken) {
                return ColumnTypes.COLUMN_TYPE_STRING;
            }
            if (dataToken instanceof TextToken) {
                return ColumnTypes.COLUMN_TYPE_STRING;
            }
            if (dataToken instanceof NumberToken) {
                // not so easy, because it can be different locale etc.
                if (dataToken.getValue().contains( "." )) {
                    return ColumnTypes.COLUMN_TYPE_DOUBLE;
                }

                if (dataToken.getValue().length() > 9) {
                    return ColumnTypes.COLUMN_TYPE_LONG;
                }
            }
        }

        return returntype;
    }

    private List<DataToken> getRandomDataTokenSample( int max, DataFrameColumn<DataToken> sourceColumn ) {
        Random rnd = new Random();
        int size = sourceColumn.getSize();
        return Stream.generate( () -> generateRandomIndex( rnd, size ) ).limit( max )//
                        .map( index -> sourceColumn.get( index ) ).collect( Collectors.toList() );
    }

    private int generateRandomIndex( Random rnd, int size ) {
        if (size <= 2) {
            return 0;
        }

        return Math.max( 0, 1 + rnd.nextInt( size - 2 ) );
    }

    private void transferAndTransformColumnData( boolean hasColumnTitles, DataFrameColumn<DataToken> sourceColumn, DataFrameColumn<?> destinationColumn ) {

        for (int row = calculateStartRow( hasColumnTitles ); row < sourceColumn.getSize(); row++) {
            if (!sourceColumn.isPresent( row )) {
                destinationColumn.appendNA();
                continue;
            }

            DataToken dataToken = sourceColumn.get( row );
            String rowValue = dataToken.getValue();

            // TODO: do this more generic....

            // TODO: depending on what type we have, we have to convert the source string based "value" into the target type of the column.
            // TODO: change this approach, but at the moment i don't like that...
            // also don't do that for every row...

            if (destinationColumn instanceof IntegerColumn) {
                IntegerColumn intColumn = (IntegerColumn) destinationColumn;
                intColumn.append( Integer.parseInt( rowValue ) );
            }
            else if (destinationColumn instanceof LongColumn) {
                LongColumn longColumn = (LongColumn) destinationColumn;
                longColumn.append( Long.parseLong( rowValue ) );
            }
            else if (destinationColumn instanceof DoubleColumn) {
                DoubleColumn intColumn = (DoubleColumn) destinationColumn;
                intColumn.append( Double.parseDouble( rowValue ) );
            }
            else if (destinationColumn instanceof FloatColumn) {
                FloatColumn floatColumn = (FloatColumn) destinationColumn;
                floatColumn.append( Float.parseFloat( rowValue ) );
            }
            else if (destinationColumn instanceof StringColumn) {
                StringColumn stringColumn = (StringColumn) destinationColumn;
                stringColumn.append( rowValue );
            }

            // TODO: we have BooleanColumns, 

            // TODO: for sparse Columns, we actually don't know how to handle these, because the append metapther doesn't really work here at this moment.
            // SparseIntegerColumns, SparseStringColumns
            // 
        }
    }

    private int calculateStartRow( boolean hasColumnTitles ) {
        return hasColumnTitles ? 1 : 0;
    }
}
