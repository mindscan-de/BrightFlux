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
package de.mindscan.brightflux.system.annotator.writer;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameColumn;
import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.dataframes.writer.bfdfjson.BFDFColumnInfo;

/**
 * 
 */
public class AnnotatorJsonLWriterImpl {

    private Type dataType = new TypeToken<Map<String, Object>>() {
    }.getType();

    /**
     * 
     */
    public AnnotatorJsonLWriterImpl() {
    }

    public void writeFile( DataFrame annotatorDataFrame, Path targetFilePath ) {
        // we put a header line into the .bfjsonl file...
        Gson gson = new GsonBuilder().create();

        // we will save some of the column / dataframe information as for the dataframe speaking.

        // --------------------- DATAFRAMEINFO ------------------        

        // TODO: this info should be used to rebuild the dataframe layout.
        // TODO: refactor this to the core components
        Map<String, Object> dataframeInfo = new LinkedHashMap<>();

        Collection<DataFrameColumn<?>> columns = annotatorDataFrame.getColumns();

        List<BFDFColumnInfo> columnInfoList = new ArrayList<>();

        // DO we need this?
        for (DataFrameColumn<?> dataFrameColumn : columns) {
            columnInfoList.add( new BFDFColumnInfo( dataFrameColumn.getColumnName(), dataFrameColumn.getColumnType() ) );
        }

        // TODO: version info of the file
        // TODO: version info of the data frame 

        dataframeInfo.put( "name", annotatorDataFrame.getName() );
        dataframeInfo.put( "title", annotatorDataFrame.getTitle() );
        dataframeInfo.put( "gen", annotatorDataFrame.getDataFrameGeneration() );
        dataframeInfo.put( "uuid", annotatorDataFrame.getUuid().toString() );
        dataframeInfo.put( "columns", columnInfoList );

        // TODO: log info for the frame.

        System.out.println( gson.toJson( dataframeInfo, dataType ) );

        // -------------------------- DATA ----------------------
        Collection<String> columnNames = annotatorDataFrame.getColumnNames();

        // we will save that data as a json-lines file.
        Iterator<DataFrameRow> rowIterator = annotatorDataFrame.rowIterator();

        while (rowIterator.hasNext()) {
            DataFrameRow dataFrameRow = (DataFrameRow) rowIterator.next();

            Map<String, Object> rowMap = new LinkedHashMap<>();
            for (String columnName : columnNames) {
                rowMap.put( columnName, dataFrameRow.get( columnName ) );
            }

            System.out.println( gson.toJson( rowMap, dataType ) );
        }

    }
}
