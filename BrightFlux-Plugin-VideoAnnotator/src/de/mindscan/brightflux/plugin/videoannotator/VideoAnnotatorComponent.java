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
package de.mindscan.brightflux.plugin.videoannotator;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.plugin.videoannotator.events.VideoAnnotationVideoObjectClosedEvent;
import de.mindscan.brightflux.plugin.videoannotator.events.VideoAnnotationVideoObjectCreatedEvent;
import de.mindscan.brightflux.plugin.videoannotator.persistence.VideoAnnotatorPersistenceModule;
import de.mindscan.brightflux.plugin.videoannotator.utils.VideoAnnotatorUtils;
import de.mindscan.brightflux.videoannotation.VideoAnnotationColumns;
import de.mindscan.brightflux.videoannotation.VideoAnnotatorVideoObject;

/**
 * 
 */
public class VideoAnnotatorComponent implements ProjectRegistryParticipant {

    private List<VideoAnnotatorVideoObject> videoAnnotationVideoObjects;

    private ProjectRegistry projectRegistry;

    private Path ffprobePath;

    private VideoAnnotatorPersistenceModule persistenceModule;

    // TODO how do we want to address the different videoAnnotationDataframes 

    /**
     * 
     */
    public VideoAnnotatorComponent() {
        this.videoAnnotationVideoObjects = new LinkedList<>();
    }

    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        this.projectRegistry = projectRegistry;

        registerVideoObjectCreatedHandler();
        registerVideoObjectClosedHandler();
    }

    private void registerVideoObjectCreatedHandler() {
        VideoObjectEventListenerAdapter createdVideoObjectListener = new VideoObjectEventListenerAdapter() {
            @Override
            public void handleVdideoObject( VideoAnnotatorVideoObject videoObject ) {
                registerVideoObject( videoObject );

            }
        };
        this.projectRegistry.getEventDispatcher().registerEventListener( VideoAnnotationVideoObjectCreatedEvent.class, createdVideoObjectListener );
    }

    private void registerVideoObjectClosedHandler() {
        VideoObjectEventListenerAdapter closedVideoObjectListener = new VideoObjectEventListenerAdapter() {
            @Override
            public void handleVdideoObject( VideoAnnotatorVideoObject videoObject ) {
                unregisterVideoObject( videoObject );
            }
        };
        this.projectRegistry.getEventDispatcher().registerEventListener( VideoAnnotationVideoObjectClosedEvent.class, closedVideoObjectListener );

    }

    public List<VideoAnnotatorVideoObject> getVideoAnnotationVideoObjects() {
        return videoAnnotationVideoObjects;
    }

    private void registerVideoObject( VideoAnnotatorVideoObject videoObject ) {
        videoAnnotationVideoObjects.add( videoObject );
    }

    private void unregisterVideoObject( VideoAnnotatorVideoObject videoObject ) {
        videoAnnotationVideoObjects.remove( videoObject );
    }

    public String createFullReport() {
        return buildFullVideoAnnoationReport( getVideoAnnotationVideoObjects() );
    }

    private String buildFullVideoAnnoationReport( List<VideoAnnotatorVideoObject> videoAnnotationVideoObjects ) {
        if (videoAnnotationVideoObjects == null || videoAnnotationVideoObjects.isEmpty()) {
            return "";
        }

        // TODO: use the generator and a template.
        // ReportGeneratorImpl generator = new ReportGeneratorImpl();

        StringBuilder sb = new StringBuilder();

        // build another report from smaller reports.
        for (VideoAnnotatorVideoObject videoAnnotatorVideoObject : videoAnnotationVideoObjects) {
            String partialReport = buildSingleVideoAnnotationReport( videoAnnotatorVideoObject );

            // Use a different template for this...
            // TODO: use the generator
            if (!partialReport.isBlank()) {
                sb.append( "h5. Analysis of [^" ).append( videoAnnotatorVideoObject.getSimpleName() ).append( "]\n\n" );
                sb.append( "The video length is:\n" );
                sb.append( partialReport );
                sb.append( "\n" );
            }
        }

        return sb.toString();
    }

    private String buildSingleVideoAnnotationReport( VideoAnnotatorVideoObject videoObject ) {
        // TODO: use the generator and a template / template block to build the real report...
        // ReportGeneratorImpl generator = new ReportGeneratorImpl();
        DataFrame currentSelectedDF = videoObject.getVideoAnnotationDataFrame();

        StringBuilder sb = new StringBuilder();

        Iterator<DataFrameRow> currentDFRowsIterator = currentSelectedDF.rowIterator();
        while (currentDFRowsIterator.hasNext()) {
            DataFrameRow dataFrameRow = (DataFrameRow) currentDFRowsIterator.next();

            int timestampInSeconds = (Integer) dataFrameRow.get( VideoAnnotationColumns.TIMESTAMP_COLUMN_NAME );
            String timstamp = VideoAnnotatorUtils.convertSecondsToTimeString( timestampInSeconds );
            String annotationContent = String.valueOf( dataFrameRow.get( VideoAnnotationColumns.ANNOTATION_COLUMN_NAME ) );
            // TODO: we need also the referenced data-rows, so we can align the timestamps to a dataframe, containing different timstamps 

            // TODO: use a named template here instead, also use the report generator
            sb.append( "* " ).append( timstamp ).append( " (video time): " ).append( annotationContent ).append( "\n" );
        }

        return sb.toString();
    }

    public void setFFProbePath( Path ffprobePath ) {
        this.ffprobePath = ffprobePath;
    }

    public Path getFFProbePath() {
        return ffprobePath;
    }

    public void setPersistenceModule( VideoAnnotatorPersistenceModule persistenceModule ) {
        this.persistenceModule = persistenceModule;
    }

    public VideoAnnotatorPersistenceModule getPersistenceModule() {
        return persistenceModule;
    }
}
