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
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRow;

/**
 * This class implements a serializer for dataframes which will be expressed as a csv file. We might be 
 * inclined to provide a reasonable configuration for the serialized file. This configuration should be 
 * part of the project configuration.
 * 
 * For the current implementation there is no possibility to configure the output. But this possibility 
 * to configure the output, must be implemented over time. But for now, we ain't need it right now. E.g. 
 * we want to configure the presentation of boolean values either as true, false or 1,0 or x / empty...
 */
public class DataFrameWriterCSVImpl implements DataFrameWriter {

    /** 
     * {@inheritDoc}
     */
    @Override
    public void writeToFile( DataFrame df, Path outputPath ) {
        try (BufferedWriter writer = Files.newBufferedWriter( outputPath, StandardOpenOption.CREATE_NEW, StandardOpenOption.TRUNCATE_EXISTING )) {
            // write header
            writer.write( calculateColumnNameLine( df ) );

            if (!df.isEmpty()) {
                Iterator<DataFrameRow> iter = df.rowIterator();

                // Iterate all rows / has next row
                while (iter.hasNext()) {
                    // write newline for each new row.
                    writer.write( "\n" );

                    // read row data
                    DataFrameRow rowContent = iter.next();

                    // TODO each column should have a serializer?
                    // or should the serializer be part of the writer (which makes more sense)

                    // Actually there are two dimensions, to this, the type of the data, and how we want to
                    // present the same data, we might want to have different presentation for different columns

                    // write data....
                    writer.write( calculateDataLine( rowContent.getAll() ) );
                }
            }
            writer.write( "\n" );
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String calculateDataLine( Object[] rowValues ) {
        // TODO: if a rowvalue contains the Separator char, this should be quoted in double quotes before writing the content.
        // TODO: also if it contains whitespaces, this whole thing should be quoted in double quotes before writing the content.
        return String.join( ",", convertAllElementsToString( rowValues ) );
    }

    private String calculateColumnNameLine( DataFrame df ) {
        return String.join( ",", df.getColumnNames() );
    }

    private Collection<String> convertAllElementsToString( Object[] rowValues ) {
        return Arrays.stream( rowValues ).map( r -> convertElement( r ) ).collect( Collectors.toList() );
    }

    private String convertElement( Object o ) {
        if (o == null) {
            return "";
        }

        // depending on the type the correct serializer should be chosen.
        // depending on the column presentation the correct serializer should be chosen.

        return o.toString();
    }

}
