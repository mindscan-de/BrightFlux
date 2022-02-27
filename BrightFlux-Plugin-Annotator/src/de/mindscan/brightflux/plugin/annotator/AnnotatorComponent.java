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
package de.mindscan.brightflux.plugin.annotator;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameSpecialColumns;
import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.framework.events.BFEventListener;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.plugin.annotator.events.AnnotationDataFrameCreatedEvent;
import de.mindscan.brightflux.plugin.annotator.events.DataFrameAnnotateRowEvent;
import de.mindscan.brightflux.plugin.annotator.persistence.AnnotatorPersistenceModule;
import de.mindscan.brightflux.plugin.annotator.utils.AnnotatorReportBuilder;
import de.mindscan.brightflux.plugin.annotator.utils.AnnotatorUtils;
import de.mindscan.brightflux.plugin.dataframehierarchy.DataFrameHierarchyComponent;
import de.mindscan.brightflux.plugin.reports.ReportBuilder;
import de.mindscan.brightflux.plugin.reports.ReportGeneratorComponent;
import de.mindscan.brightflux.system.events.BFEventListenerAdapter;
import de.mindscan.brightflux.system.events.DataFrameEventListenerAdapter;

/**
 * I want some kind of component which will take care of the Annotations... I don't want it to be a graphical component
 * but one which provides some business logic for log data analysis 
 * 
 * This whole annotator component will probably refactored, when some mechanics will be understood and when we 
 * introduce the log analysis project file, then we probably have a different structure. but for now this thing
 * is just learning how a business component without a gui may work in that command and event dispatcher system
 * 
 * Maybe we will have generic commands in future, where each component registers itself and says which commands
 * it will be serving. Such that not only the event dispatcher has a listener, but also the commands have their 
 * own listeners as well. This is useful, because we already have one command which directly translates this 
 * command into an event, which then is routed here due to a listener, to process...
 * 
 * Having a specialized command and event might not be the best idea, but let's see.
 */
public class AnnotatorComponent implements ProjectRegistryParticipant {

    public static final String ANNOTATION_COLUMN_NAME = "annotation";
    public static final String ANNOTATION_DATAFRAME_NAME = "logAnalysisFrame";

    private AnnotatorPersistenceModule persistenceModule;
    private ReportGeneratorComponent reportGeneratorComponent;
    private DataFrameHierarchyComponent dataFrameHierarchyComponent;
    private Map<UUID, DataFrame> rootDfToAnnotationFrame;

    /**
     * 
     */
    public AnnotatorComponent() {
        this.rootDfToAnnotationFrame = new HashMap<>();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        registerAnnotationDFCreateEvent( projectRegistry );
        registerAnnotationSaveEvent( projectRegistry );
        registerAnnotateRowEvent( projectRegistry );
    }

    private void registerAnnotateRowEvent( ProjectRegistry projectRegistry ) {
        BFEventListener dfAnnotateListener = new BFEventListenerAdapter() {
            @Override
            public void handleEvent( BFEvent event ) {
                DataFrameAnnotateRowEvent x = ((DataFrameAnnotateRowEvent) event);

                DataFrame logAnalysisFrame = getLogAnalysisFrame( x.getDataFrame() );

                // TODO use another aditional key (e.g. a timstamp / original index in the dataframe)
                String newText = x.getAnnotation();

                if (newText.isBlank()) {
                    logAnalysisFrame.setNA( DataFrameSpecialColumns.INDEX_COLUMN_NAME, x.getRow() );
                    logAnalysisFrame.setNA( DataFrameSpecialColumns.ORIGINAL_INDEX_COLUMN_NAME, x.getRow() );
                    logAnalysisFrame.setNA( ANNOTATION_COLUMN_NAME, x.getRow() );
                    // TODO: clear a previous classification
                }
                else {
                    logAnalysisFrame.setAt( DataFrameSpecialColumns.INDEX_COLUMN_NAME, x.getRow(), x.getRow() );
                    logAnalysisFrame.setAt( DataFrameSpecialColumns.ORIGINAL_INDEX_COLUMN_NAME, x.getRow(), x.getRow() );
                    logAnalysisFrame.setAt( ANNOTATION_COLUMN_NAME, x.getRow(), newText );
                    // TODO: classify the annotation Text
                }

                // TODO: 
                // we might want to send something around, that we updated the dataframe - maybe the dataframe 
                // should be able to do this on its own... but this kind of listeners are just a way to loose track of listeners and memory.
            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( DataFrameAnnotateRowEvent.class, dfAnnotateListener );
    }

    // TODO: rewrite this... and split clear from load, which was both solved here.
    private void registerAnnotationDFCreateEvent( ProjectRegistry projectRegistry ) {
        // TODO: how to process this?
        DataFrameEventListenerAdapter dfCreatedListener = new DataFrameEventListenerAdapter() {
            @Override
            public void handleDataFrame( DataFrame dataFrame ) {
                if (ANNOTATION_DATAFRAME_NAME.equals( dataFrame.getName() )) {
                    // AnnotatorComponent.this.logAnalysisFrame = dataFrame;
                }
            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( AnnotationDataFrameCreatedEvent.class, dfCreatedListener );
    }

    private void registerAnnotationSaveEvent( ProjectRegistry projectRegistry ) {
        // intentionally left blank for now.
    }

    public DataFrame getLogAnalysisFrame( DataFrame selectedDataFrame ) {
        UUID rootUUID = this.dataFrameHierarchyComponent.getRootUUID( selectedDataFrame );
        return getAnnotatorDataframe( rootUUID );
    }

    public boolean isLogAnalysisFramePresent( DataFrame selectedDataFrame ) {
        UUID rootUUID = this.dataFrameHierarchyComponent.getRootUUID( selectedDataFrame );
        // TODO: actually this  will always return true in the new implementation...
        return getAnnotatorDataframe( rootUUID ) != null;
    }

    private DataFrame getAnnotatorDataframe( UUID rootUUID ) {
        return rootDfToAnnotationFrame.computeIfAbsent( rootUUID, this::createNewAnnotatorDataFrame );
    }

    private DataFrame createNewAnnotatorDataFrame( UUID ignoreMe ) {
        return AnnotatorUtils.createAnnotationDataFrame();
    }

    public String createFullReport( String reportName, int reportNameIndex, DataFrame forThisDataFrame ) {
        String[] templateNames = this.persistenceModule.getAvailableReportTemplateNames();
        ReportBuilder reportBuilder = reportGeneratorComponent.getReportBuilderFileTemplate( templateNames[reportNameIndex] );

        return AnnotatorReportBuilder.buildReport( forThisDataFrame, getLogAnalysisFrame( forThisDataFrame ), reportBuilder );
    }

    public AnnotatorPersistenceModule getPersistenceModule() {
        return persistenceModule;
    }

    public void setPersistenceModule( AnnotatorPersistenceModule persistenceModule ) {
        this.persistenceModule = persistenceModule;
    }

    public void setReportGeneratorComponent( ReportGeneratorComponent reportGeneratorComponent ) {
        this.reportGeneratorComponent = reportGeneratorComponent;
    }

    public void setDataframeHierarchyComponent( DataFrameHierarchyComponent dataFrameHierarchyComponent ) {
        this.dataFrameHierarchyComponent = dataFrameHierarchyComponent;
    }

}
