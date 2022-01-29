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

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.dataframes.DataFrameSpecialColumns;
import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.framework.events.BFEventListener;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.plugin.annotator.events.AnnotationDataFrameCreatedEvent;
import de.mindscan.brightflux.plugin.annotator.events.DataFrameAnnotateRowEvent;
import de.mindscan.brightflux.plugin.annotator.persistence.AnnotatorPersistenceModule;
import de.mindscan.brightflux.plugin.annotator.utils.AnnotatorUtils;
import de.mindscan.brightflux.plugin.annotator.writer.AnnotatorJsonLWriterImpl;
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

    // Special report markers, to change mode
    private static final String EXTEND_LOG_DATA_LINE = ".";
    private static final String EXTEND_LOG_DATA_LINES_WITH_SKIP = "..";

    public static final String ANNOTATION_COLUMN_NAME = "annotation";
    public static final String ANNOTATION_DATAFRAME_NAME = "logAnalysisFrame";

    // Block names for the Annotator Template
    private static final String BLOCKNAME_ANNOTATION_DETAILS = "X";
    private static final String BLOCKNAME_ANNOTATION_DETAILS_LOG = "Y";

    private DataFrame logAnalysisFrame = null;
    private AnnotatorPersistenceModule persistenceModule;
    private ReportGeneratorComponent reportGeneratorComponent;

    public static final String TODO_TEMPLATE = "{{block:begin:X}}{{data:evidence_description}}\r\n" + //
                    "\r\n" + //
                    "{code}\r\n" + //
                    "{{block:begin:Y}}{{data:extracontent}}{{data:row.h1.ts}}:{{data:row.h2.msg}}\r\n" + //
                    "{{block:end:Y}}{code}\r\n" + //
                    "\r\n" + //
                    "{{block:end:X}}\r\n" + // 
                    "\r\n" + // 
                    "";

    /**
     * 
     */
    public AnnotatorComponent() {
        this.logAnalysisFrame = AnnotatorUtils.createAnnotationDataFrame();
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

                // TODO: the dataframe should be used to figure out which logAnalysisFrame should be used...

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

    private void registerAnnotationDFCreateEvent( ProjectRegistry projectRegistry ) {
        DataFrameEventListenerAdapter dfCreatedListener = new DataFrameEventListenerAdapter() {
            @Override
            public void handleDataFrame( DataFrame dataFrame ) {
                if (ANNOTATION_DATAFRAME_NAME.equals( dataFrame.getName() )) {
                    AnnotatorComponent.this.logAnalysisFrame = dataFrame;
                }
            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( AnnotationDataFrameCreatedEvent.class, dfCreatedListener );
    }

    private void registerAnnotationSaveEvent( ProjectRegistry projectRegistry ) {
        // TODO Auto-generated method stub

    }

    /**
     * @return the logAnalysisFrame
     */
    public DataFrame getLogAnalysisFrame() {
        return logAnalysisFrame;
    }

    public boolean isLogAnalysisFramePresent() {
        return logAnalysisFrame != null;
    }

    public void writeAnnotationFile( Path annotationFilePath ) {
        if (logAnalysisFrame != null) {
            AnnotatorJsonLWriterImpl annotatorJsonLWriterImpl = new AnnotatorJsonLWriterImpl();

            annotatorJsonLWriterImpl.writeFile( logAnalysisFrame, annotationFilePath );
        }
    }

    public String createFullReport( String reportName, DataFrame forThisDataFrame ) {
        ReportBuilder reportBuilder = reportGeneratorComponent.getReportBuilder( TODO_TEMPLATE );

        Iterator<DataFrameRow> currentDFRowsIterator2 = forThisDataFrame.rowIterator();
        while (currentDFRowsIterator2.hasNext()) {
            DataFrameRow dataFrameRow = (DataFrameRow) currentDFRowsIterator2.next();
            int originalRowIndex = dataFrameRow.getOriginalRowIndex();
            if (getLogAnalysisFrame().isPresent( ANNOTATION_COLUMN_NAME, originalRowIndex )) {
                // we found annotation details for a line in the dataframe we create the report for

                String evidence_description = ((String) getLogAnalysisFrame().getAt( ANNOTATION_COLUMN_NAME, originalRowIndex )).trim();
                // TODO this kind of data row transfer, should be something a datarow provides, 
                // TODO e.g. extract row data as a map with some selectable prefix...
                // TODO because we want to have more flexible templates, and the template should select what they render
                Map<String, String> templateData = new HashMap<>();
                templateData.put( "row.h1.ts", String.valueOf( dataFrameRow.get( "h1.ts" ) ) );
                templateData.put( "row.h2.msg", String.valueOf( dataFrameRow.get( "h2.msg" ) ) );

                if (evidence_description.isBlank()) {
                    // intentionally left blank.
                }
                else if (EXTEND_LOG_DATA_LINE.equals( evidence_description )) {
                    templateData.put( "extracontent", "" );
                    reportBuilder.block( BLOCKNAME_ANNOTATION_DETAILS_LOG, templateData );
                }
                else if (EXTEND_LOG_DATA_LINES_WITH_SKIP.equals( evidence_description )) {
                    templateData.put( "extracontent", "[..]\r\n" );
                    reportBuilder.block( BLOCKNAME_ANNOTATION_DETAILS_LOG, templateData );
                }
                else {
                    templateData.put( "extracontent", "" );
                    templateData.put( "evidence_description", evidence_description );
                    reportBuilder.block( BLOCKNAME_ANNOTATION_DETAILS, templateData );
                    reportBuilder.block( BLOCKNAME_ANNOTATION_DETAILS_LOG, templateData );
                }
            }
        }
        return reportBuilder.render( new HashMap<>() );
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

}
