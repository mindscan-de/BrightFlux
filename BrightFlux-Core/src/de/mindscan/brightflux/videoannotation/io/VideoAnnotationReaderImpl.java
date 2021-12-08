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

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.writer.bfdfjson.JsonLinesDataFrameInfo;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;
import de.mindscan.brightflux.ingest.IngestBFDataFrameJsonLines;
import de.mindscan.brightflux.videoannotation.VideoAnnotatorVideoObject;
import de.mindscan.brightflux.videoannotation.impl.VideoAnnotatorVideoObjectImpl;
import de.mindscan.brightflux.videoannotation.impl.VideoAnnotatorVideoObjectMetaData;
import de.mindscan.brightflux.videoannotation.impl.VideoAnnotatorVideoObjectRefTimestampData;

/**
 * 
 */
public class VideoAnnotationReaderImpl {

    private IngestBFDataFrameJsonLines ingestDataFrameDelegate = new IngestBFDataFrameJsonLines();

    public VideoAnnotatorVideoObject loadAnnotationAsVideoObject( Path path ) {
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter( Path.class, new BFGsonPathDeserializer() ).create();

        try (BufferedReader br = Files.newBufferedReader( path )) {
            // todo read videoAnnotation meta information
            VideoAnnotatorVideoObjectMetaData metaData = readJsonLinesVideoAnnotationMetadata( gson, br );

            // todo: change format and read 
            VideoAnnotatorVideoObjectRefTimestampData refTimestampData = new VideoAnnotatorVideoObjectRefTimestampData();

            // read dataframe description
            JsonLinesDataFrameInfo dataFrameInfo = ingestDataFrameDelegate.readJsonLinesDataFrameInfo( gson, br );
            // read dataframe data
            DataFrame dataFrame = ingestDataFrameDelegate.readJsonLinesDataFrameContent( gson, br, dataFrameInfo );

            // Refactor to some kind of factory; this is a too tightly coupled to implementation
            return new VideoAnnotatorVideoObjectImpl( dataFrame, metaData, refTimestampData );
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        throw new NotYetImplemetedException();

    }

    private VideoAnnotatorVideoObjectMetaData readJsonLinesVideoAnnotationMetadata( Gson gson, BufferedReader br ) throws IOException {
        String videoAnnotationMetaDataLine = br.readLine();

        if (videoAnnotationMetaDataLine == null) {
            throw new NotYetImplemetedException( "VideoAnnotation metadata seems to be missing." );
        }

        return gson.fromJson( videoAnnotationMetaDataLine.trim(), VideoAnnotatorVideoObjectMetaData.class );
    }
}
