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
import java.util.LinkedList;
import java.util.List;

import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.plugin.reports.ReportBuilder;
import de.mindscan.brightflux.plugin.reports.ReportGeneratorComponent;
import de.mindscan.brightflux.plugin.videoannotator.events.VideoAnnotationVideoObjectClosedEvent;
import de.mindscan.brightflux.plugin.videoannotator.events.VideoAnnotationVideoObjectCreatedEvent;
import de.mindscan.brightflux.plugin.videoannotator.persistence.VideoAnnotatorPersistenceModule;
import de.mindscan.brightflux.plugin.videoannotator.utils.VideoAnnotatorReportBuilder;
import de.mindscan.brightflux.videoannotation.VideoAnnotatorVideoObject;

/**
 * 
 */
public class VideoAnnotatorComponent implements ProjectRegistryParticipant {

    private List<VideoAnnotatorVideoObject> videoAnnotationVideoObjects;

    private ProjectRegistry projectRegistry;

    private VideoAnnotatorPersistenceModule persistenceModule;

    private ReportGeneratorComponent reportGeneratorComponent;

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

    public String createFullReport( String reportname, int reportNameIndex ) {
        String[] templateNames = persistenceModule.getAvailableReportTemplateNames();
        ReportBuilder reportBuilder = reportGeneratorComponent.getReportBuilderFileTemplate( templateNames[reportNameIndex] );

        return VideoAnnotatorReportBuilder.buildFullVideoAnnoationReport( reportname, reportNameIndex, getVideoAnnotationVideoObjects(), reportBuilder );
    }

    public Path getFFProbePath() {
        return this.persistenceModule.getFFProbePath();
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
