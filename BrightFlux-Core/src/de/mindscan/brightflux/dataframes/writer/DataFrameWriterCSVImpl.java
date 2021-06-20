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

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRow;

/**
 * 
 */
public class DataFrameWriterCSVImpl implements DataFrameWriter {

    /** 
     * {@inheritDoc}
     */
    @Override
    public void writeToFile( DataFrame df, Path outputPath ) {
        try (BufferedWriter writer = Files.newBufferedWriter( outputPath, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE )) {
            // write header
            writer.write( calculateColumnNameLine( df ) );

            if (!df.isEmpty()) {
                Iterator<DataFrameRow> iter = df.rowIterator();

                // Iterate all rows / has next row
                while (iter.hasNext()) {
                    // write newline for each new row.
                    writer.write( "\n" );

                    // read row data
                    DataFrameRow rowcontent = iter.next();

                    writer.write( "test" );
                    // TODO: write data....
                    // TODO Iterate each row, but return String or Object?
                    // TODO each column should have a serializer, 
                    // or should the serializer be part of the writer (which makes more sense)
                }
            }
            writer.write( "\n" );
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String calculateColumnNameLine( DataFrame df ) {
        return String.join( ",", df.getColumnNames() );
    }

}