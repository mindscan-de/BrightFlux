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
import java.util.List;

import de.mindscan.brightflux.dataframes.DataFrameColumn;
import de.mindscan.brightflux.dataframes.columns.DoubleColumn;
import de.mindscan.brightflux.dataframes.columns.IntegerColumn;
import de.mindscan.brightflux.ingest.DataToken;

/**
 * 
 */
public class DataFrameCompiler {

    public List<DataFrameColumn<?>> compileDataFrame( Collection<DataFrameColumn<DataToken>> dataframeColumns ) {
        List<DataFrameColumn<?>> compiledDataFrameColumns = new ArrayList<>();

        // TODO: check whether the first line contains only identifiers or Texts, and check if the second column has elements different to the first colum types.
        boolean hasColumnTitles = calcHasColumnTitles( dataframeColumns );

        for (DataFrameColumn<DataToken> column : dataframeColumns) {
            DataFrameColumn<?> compiledColumn = compileDataFrameColumn( hasColumnTitles, column );
            compiledDataFrameColumns.add( compiledColumn );
        }

        // this will build a dataframe with typed and named columns, each column is compiled of its own 
        // from left to right? Bad datavalues are logged and these rows removed?
        return compiledDataFrameColumns;
    }

    private boolean calcHasColumnTitles( Collection<DataFrameColumn<DataToken>> dataframeColumns ) {
        // TODO: check whether the first line contains only identifiers or Texts, and check if the second column has elements different to the first colum types.
        return true;
    }

    private DataFrameColumn<?> compileDataFrameColumn( boolean hasColumnTitles, DataFrameColumn<DataToken> column ) {

        // TODO: infer the type and use the proper Compiler 
        DataToken token = column.get( 0 );
        String value = token.getValue();

        DataFrameColumn<?> newColumn;
        if (value.equals( "oldpeak" )) {
            newColumn = new DoubleColumn();
        }
        else {
            newColumn = new IntegerColumn();
        }

        // 
        if (hasColumnTitles) {
            newColumn.setColumnName( value );
        }

        return newColumn;
    }
}
