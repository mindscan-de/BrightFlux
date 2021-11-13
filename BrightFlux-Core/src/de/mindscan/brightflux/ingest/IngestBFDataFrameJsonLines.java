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
package de.mindscan.brightflux.ingest;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameBuilder;
import de.mindscan.brightflux.dataframes.DataFrameColumn;
import de.mindscan.brightflux.dataframes.DataFrameSpecialColumns;
import de.mindscan.brightflux.dataframes.columns.DataFrameColumnFactory;
import de.mindscan.brightflux.dataframes.columns.SparseColumn;
import de.mindscan.brightflux.dataframes.columntypes.ColumnValueTypes;
import de.mindscan.brightflux.dataframes.writer.bfdfjson.BFDFColumnInfo;
import de.mindscan.brightflux.dataframes.writer.bfdfjson.JsonLinesDataFrameInfo;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;

/**
 * TODO: This class needs to be rewritten later using a proper solution for the Gson problem outlined below.
 * 
 * Long values are parsed as double, which might have a to small precision compared to long. (e.g. timestamps)
 * Also we have issues when the data is somehow not properly read/or written? (UTF-8)
 */
public class IngestBFDataFrameJsonLines {

    private Type dataFrameInfoType = new TypeToken<JsonLinesDataFrameInfo>() {
    }.getType();

    private Type dataFrameDataRowType = new TypeToken<LinkedHashMap<String, Object>>() {
    }.getType();

    public DataFrame loadAsDataFrame( Path path ) {
        Gson gson = new GsonBuilder().create();

        try (BufferedReader br = Files.newBufferedReader( path )) {
            // actually the known information is there...
            // we know the columns - their types etc.
            // so let's load the first line decode that as JSON
            // into Type JsonLinesDataFrameInfo
            JsonLinesDataFrameInfo dataFrameInfo = readJsonLinesDataFrameInfo( gson, br );
            DataFrame dataFrame = readJsonLinesDataFrameContent( gson, br, dataFrameInfo );

            return dataFrame;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        throw new NotYetImplemetedException();
    }

    public JsonLinesDataFrameInfo readJsonLinesDataFrameInfo( Gson gson, BufferedReader br ) throws IOException {
        String frameInfoLine = br.readLine();

        if (frameInfoLine == null) {
            throw new NotYetImplemetedException( "Can't read Data Frame Information Header." );
        }

        return gson.fromJson( frameInfoLine.trim(), dataFrameInfoType );
    }

    public DataFrame readJsonLinesDataFrameContent( Gson gson, BufferedReader br, JsonLinesDataFrameInfo dataFrameInfo ) throws IOException {
        List<BFDFColumnInfo> uncompiledColumns = dataFrameInfo.getColumns();

        // --- Compile columns ---

        List<DataFrameColumn<?>> compiledColumns = new LinkedList<>();
        List<String> columnNames = new LinkedList<>();
        Map<String, DataFrameColumn<?>> columnMap = new HashMap<>();

        for (BFDFColumnInfo bfdfColumnInfo : uncompiledColumns) {
            DataFrameColumn<?> compiledColumn = DataFrameColumnFactory.createColumnForType( bfdfColumnInfo.getName(), bfdfColumnInfo.getCType() );
            compiledColumns.add( compiledColumn );
            columnNames.add( bfdfColumnInfo.getName() );
            columnMap.put( bfdfColumnInfo.getName(), compiledColumn );
        }

        // --- Compile data ---

        // actually we want to be aware of the Types in the column, such that the columnvalues are not double by default, but have the proper type
        // new GsonBuilder().register...

        String dataRowLine;
        for (dataRowLine = br.readLine(); dataRowLine != null; dataRowLine = br.readLine()) {
            // process the line
            if (dataRowLine.trim().isBlank()) {
                break;
            }

            // The Map Type has a shortcoming ... Because we don't have a proper prototype in java for
            // a dataframe, GSON uses the javascript object notation specification as the truth - Numbers
            // are treated as of Double-Type

            // That means, that if we load data, what looks to us as an integer, it is handled as a Double value,
            // That also has some unexpected limitations, Long values lose precision, that makes things a bit
            // harder. As for now speaking, gson might be good enough, but probably must be replaced by a
            // strategy which knows the columnname and its type to parse the JSON data in the correct target 
            // type. We must either change the implementation which we use for reading JSON files, or we
            // register some special reader, which can deal with the right targettypes according to the columns.
            LinkedHashMap<String, Object> dataRow = gson.fromJson( dataRowLine.trim(), dataFrameDataRowType );

            // append this row, depending on the columntype....

            // after that we read each "row" and add data to it, according to the index. 
            // for the start we will support Sparse_Columns only.

            int idx = gsonHack_DoubeToInt( (Double) dataRow.get( DataFrameSpecialColumns.INDEX_COLUMN_NAME ) );
            // int org_idx = (Integer) dataRow.get( DataFrameSpecialColumns.ORIGINAL_INDEX_COLUMN_NAME );

            for (String name : columnNames) {
                DataFrameColumn<?> column = columnMap.get( name );
                if (column instanceof SparseColumn) {
                    if (dataRow.containsKey( name )) {
                        Object currentValue = dataRow.get( name );

                        // This is 
                        if (currentValue instanceof Double) {
                            Double doubleValue = (Double) currentValue;
                            String columnValueType = column.getColumnValueType();

                            switch (columnValueType) {
                                case ColumnValueTypes.COLUMN_TYPE_INT:
                                    column.setRaw( idx, gsonHack_DoubeToInt( doubleValue ) );
                                    break;
                                case ColumnValueTypes.COLUMN_TYPE_FLOAT:
                                    column.setRaw( idx, gsonHack_DoubeToFloat( doubleValue ) );
                                    break;
                                case ColumnValueTypes.COLUMN_TYPE_DOUBLE:
                                    column.setRaw( idx, doubleValue );
                                    break;
                                default:
                                    throw new NotYetImplemetedException(
                                                    "either lose precision or implement another solution for column valuetype='" + columnValueType + "'" );
                            }
                        }
                        else {
                            column.setRaw( idx, currentValue );
                        }
                    }
                    else {
                        column.setNA( idx );
                    }
                }
                else {
                    // throw new NotYetImplemetedException("we currently only support SparseColumns"); 
                }
            }

            // We do the hardcore-stuff at first here, and then we will refactor
            // this into the right places, after we got it working.

            // decide if we appendNA to it...
        }

        DataFrameBuilder dataFrameBuilder = new DataFrameBuilder( dataFrameInfo.getName() );
        dataFrameBuilder.addColumns( compiledColumns );

        return dataFrameBuilder.build();
    }

    private int gsonHack_DoubeToInt( Double double1 ) {
        return double1.intValue();
    }

    private float gsonHack_DoubeToFloat( Double double1 ) {
        return double1.floatValue();
    }

}
