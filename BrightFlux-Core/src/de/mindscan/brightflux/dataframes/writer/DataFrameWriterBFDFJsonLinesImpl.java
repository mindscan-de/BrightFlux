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

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.dataframes.writer.bfdfjson.JsonLinesDataFrameInfo;

/**
 * 
 */
public class DataFrameWriterBFDFJsonLinesImpl implements DataFrameWriter {

    private Type dataFrameInfoType = new TypeToken<JsonLinesDataFrameInfo>() {
    }.getType();

    private Type dataType = new TypeToken<Map<String, Object>>() {
    }.getType();

    /**
     * 
     */
    public DataFrameWriterBFDFJsonLinesImpl() {
        // intentionally left empty
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void writeToFile( DataFrame df, Path outputPath ) {

        // we put a header line into the .bfjsonl file...
        Gson gson = new GsonBuilder().create();

        // --------------------- DATAFRAMEINFO ------------------   

        JsonLinesDataFrameInfo dataFrameInfo = new JsonLinesDataFrameInfo();
        dataFrameInfo.initWithDataFrame( df );

        System.out.println( gson.toJson( dataFrameInfo, dataFrameInfoType ) );

        // -------------------------- DATA ----------------------
        Collection<String> columnNames = df.getColumnNames();

        // we will save that as a json-lines file.
        Iterator<DataFrameRow> rowIterator = df.rowIterator();

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
