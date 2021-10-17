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
import java.util.LinkedHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.writer.bfdfjson.JsonLinesDataFrameInfo;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;

/**
 * 
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
            String firstLine;
            firstLine = br.readLine();
            if (firstLine == null) {
                throw new NotYetImplemetedException( "Actually we should return an empty Dataframe?" );
            }

            JsonLinesDataFrameInfo dataFrameInfo = gson.fromJson( firstLine.trim(), dataFrameInfoType );

            // Build column names
            // build columns from it

            String dataRowLine;
            while ((dataRowLine = br.readLine()) != null) {
                // process the line
                if (dataRowLine.trim().isBlank()) {
                    break;
                }

                LinkedHashMap<String, Object> dataRow = gson.fromJson( dataRowLine.trim(), dataFrameDataRowType );

                // append this row, depending on the columntype....

                // after that we read each "row" and add data to it, according to the index. 
                // for the start we will support Sparse_Columns only.

                // We do the hardcore-stuff at first here, and then we will refactor
                // this into the right places, after we got it working.

                // decide if we appendNA to it...
            }

            // TODO: Build the dataframe from the read columns.
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        throw new NotYetImplemetedException();
    }
}
