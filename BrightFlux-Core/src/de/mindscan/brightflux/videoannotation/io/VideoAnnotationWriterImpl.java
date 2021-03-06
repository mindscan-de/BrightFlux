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
package de.mindscan.brightflux.videoannotation.io;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.writer.DataFrameOutputStreamWriter;
import de.mindscan.brightflux.dataframes.writer.DataFrameWriterBFDFJsonLinesImpl;
import de.mindscan.brightflux.videoannotation.VideoAnnotationWriter;
import de.mindscan.brightflux.videoannotation.VideoAnnotatorVideoObject;

/**
 * 
 */
public class VideoAnnotationWriterImpl implements VideoAnnotationWriter {

    private DataFrameOutputStreamWriter dataframeWriterDelegate = new DataFrameWriterBFDFJsonLinesImpl();

    public VideoAnnotationWriterImpl() {
        // intentionally left blank.
    }

    @Override
    public void writeToFile( VideoAnnotatorVideoObject videoObject, Path outputPath ) {
        try {
            if (!Files.exists( outputPath )) {
                Files.createFile( outputPath );
            }

            Gson gson = new GsonBuilder().registerTypeHierarchyAdapter( Path.class, new BFGsonPathSerializer() ).create();

            try (OutputStream outputFile = Files.newOutputStream( outputPath, StandardOpenOption.TRUNCATE_EXISTING );) {

                // write the videoObject information as header
                String metaDataString = gson.toJson( videoObject.getMetaData() ) + "\n";
                outputFile.write( metaDataString.getBytes() );

                // write reference time stamp data 
                String referenceTimestampData = gson.toJson( videoObject.getRefTimestampData() ) + "\n";
                outputFile.write( referenceTimestampData.getBytes() );

                // write the internal data frame to the output stream
                DataFrame df = videoObject.getVideoAnnotationDataFrame();
                dataframeWriterDelegate.writeToOutputStream( df, outputFile );
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
