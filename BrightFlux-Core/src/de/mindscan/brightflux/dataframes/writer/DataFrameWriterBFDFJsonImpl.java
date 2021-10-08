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
package de.mindscan.brightflux.dataframes.writer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameColumn;
import de.mindscan.brightflux.dataframes.writer.bfdfjson.BFDFColumnInfo;
import de.mindscan.brightflux.dataframes.writer.bfdfjson.BFDFJsonContent;

/**
 * Brightflux 
 */
public class DataFrameWriterBFDFJsonImpl implements DataFrameWriter {

    /** 
     * {@inheritDoc}
     */
    @Override
    public void writeToFile( DataFrame df, Path outputPath ) {
        BFDFJsonContent jsonDF = new BFDFJsonContent();

        // we want to process the columns (headers)
        jsonDF.setColumns( processColumnInformation( df.getColumns() ) );

        // we want to process the data

        // TODO write the json dataframe using GSON
    }

    /**
     * @param columns
     * @return 
     */
    private BFDFColumnInfo[] processColumnInformation( Collection<DataFrameColumn<?>> columns ) {
        List<BFDFColumnInfo> columnInfoList = new ArrayList<>();

        // DO we need this?
        for (DataFrameColumn<?> dataFrameColumn : columns) {
            columnInfoList.add( new BFDFColumnInfo( dataFrameColumn.getColumnName(), dataFrameColumn.getColumnType() ) );
        }

        return columnInfoList.toArray( new BFDFColumnInfo[columnInfoList.size()] );
    }

}
