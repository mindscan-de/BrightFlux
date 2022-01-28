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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.plugin.reports.ReportBuilder;
import de.mindscan.brightflux.plugin.reports.ReportGeneratorComponent;
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

    private ReportGeneratorComponent reportGeneratorComponent;

    public static final String TODO_TEMPLATE = "{{block:begin:VideoFile}}h5. Analysis of [^{{data:videofilename}}]\r\n" + // 
                    "\r\n" + //
                    "The length of the video is {{data:videolength}}:\r\n" + // 
                    "{{block:begin:VideoFilePosition}}* {{data:videotimestamp}} (video time): {{data:evidence_description}}\r\n" + // 
                    "{{block:end:VideoFilePosition}}" + // 
                    "{{block:end:VideoFile}}\r\n" + //
                    "";

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

    public String createFullReport( String reportname ) {
        return buildFullVideoAnnoationReport( reportname, getVideoAnnotationVideoObjects() );
    }

    private String buildFullVideoAnnoationReport( String reportname, List<VideoAnnotatorVideoObject> videoAnnotationVideoObjects ) {
        if (videoAnnotationVideoObjects == null || videoAnnotationVideoObjects.isEmpty()) {
            return "buildFullVideoAnnoationReport was somehow empty....";
        }

        ReportBuilder reportBuilder = reportGeneratorComponent.getReportBuilder( TODO_TEMPLATE );

        // build another report from smaller reports.
        for (VideoAnnotatorVideoObject videoAnnotatorVideoObject : videoAnnotationVideoObjects) {
            HashMap<String, String> videoData = new HashMap<String, String>();
            videoData.put( "videofilename", videoAnnotatorVideoObject.getSimpleName() );
            videoData.put( "videolength", VideoAnnotatorUtils.convertSecondsToTimeString( videoAnnotatorVideoObject.getVideoDurationInSeconds() ) );

            reportBuilder.block( "VideoFile", videoData );

            buildSingleVideoAnnotationReport( reportBuilder, videoAnnotatorVideoObject );
        }

        return reportBuilder.render( new HashMap<String, String>() );
    }

    private void buildSingleVideoAnnotationReport( ReportBuilder reportBuilder, VideoAnnotatorVideoObject videoObject ) {
        DataFrame currentSelectedDF = videoObject.getVideoAnnotationDataFrame();

        Iterator<DataFrameRow> currentDFRowsIterator = currentSelectedDF.rowIterator();
        while (currentDFRowsIterator.hasNext()) {
            Map<String, String> templateData = new HashMap<>();

            DataFrameRow dataFrameRow = (DataFrameRow) currentDFRowsIterator.next();
            int timestampInSeconds = (Integer) dataFrameRow.get( VideoAnnotationColumns.TIMESTAMP_COLUMN_NAME );

            templateData.put( "videotimestamp", VideoAnnotatorUtils.convertSecondsToTimeString( timestampInSeconds ) );
            templateData.put( "evidence_description", String.valueOf( dataFrameRow.get( VideoAnnotationColumns.ANNOTATION_COLUMN_NAME ) ) );

            reportBuilder.block( "VideoFilePosition", templateData );
        }
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

    public void setReportGeneratorComponent( ReportGeneratorComponent reportGeneratorComponent ) {
        this.reportGeneratorComponent = reportGeneratorComponent;
    }
}
