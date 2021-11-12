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
package de.mindscan.brightflux.system.videoannotator.io;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.writer.DataFrameOutputStreamWriter;
import de.mindscan.brightflux.dataframes.writer.DataFrameWriterBFDFJsonLinesImpl;
import de.mindscan.brightflux.system.videoannotator.VideoAnnotationWriter;
import de.mindscan.brightflux.system.videoannotator.VideoAnnotatorVideoObject;
import de.mindscan.brightflux.system.videoannotator.impl.VideoAnnotatorVideoObjectMetaData;

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

            // we put a header line into the .bf_jsonl file...
            Gson gson = new GsonBuilder().registerTypeHierarchyAdapter( Path.class, new BFGsonPathConverter() ).create();

            try (OutputStream outputFile = Files.newOutputStream( outputPath, StandardOpenOption.TRUNCATE_EXISTING );) {

                // write the videoObject information as header + "\n"
                VideoAnnotatorVideoObjectMetaData metaData = videoObject.getMetaData();

                String metaDataString = gson.toJson( metaData ) + "\n";
                outputFile.write( metaDataString.getBytes() );

                // write the internal dataframe to the output stream
                DataFrame df = videoObject.getVideoAnnotationDataFrame();
                dataframeWriterDelegate.writeToOutputStream( df, outputFile );
            }
        }
        catch (Exception e) {
        }

    }

}
