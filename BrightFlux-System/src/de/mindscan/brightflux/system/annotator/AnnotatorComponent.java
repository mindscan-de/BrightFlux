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
package de.mindscan.brightflux.system.annotator;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.framework.events.BFEventListener;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.system.events.BFEventListenerAdapter;
import de.mindscan.brightflux.system.events.dataframe.AnnotationDataFrameCreatedEvent;
import de.mindscan.brightflux.system.events.dataframe.BFAbstractDataFrameEvent;
import de.mindscan.brightflux.system.events.dataframe.DataFrameAnnotateRowEvent;

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
    private DataFrame logAnalysisFrame = null;

    /**
     * 
     */
    public AnnotatorComponent() {
        // intentionally left blank
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
                // logAnalysisFrame.setRawValue( "h1.ts", 1, x.getTimestamp() );
                String newText = x.getAnnotation();

                if (newText.isBlank()) {
                    logAnalysisFrame.setNA( ANNOTATION_COLUMN_NAME, x.getRow() );
                }
                else {
                    logAnalysisFrame.setAt( ANNOTATION_COLUMN_NAME, x.getRow(), newText );
                }

                // TODO: 
                // we might want to send something around, that we updated the dataframe - maybe the dataframe 
                // should be able to do this on its own... but this kind of listeners are just a way to loose track of listeners and memory.
            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( DataFrameAnnotateRowEvent.class, dfAnnotateListener );
    }

    private void registerAnnotationDFCreateEvent( ProjectRegistry projectRegistry ) {
        BFEventListener dfCreatedListener = new BFEventListenerAdapter() {
            @Override
            public void handleEvent( BFEvent event ) {
                if (event instanceof BFAbstractDataFrameEvent) {
                    DataFrame frameToAnnotate = ((BFAbstractDataFrameEvent) event).getDataFrame();
                    if ("logAnalysisFrame".equals( frameToAnnotate.getName() )) {
                        AnnotatorComponent.this.logAnalysisFrame = frameToAnnotate;
                        System.out.println( "Found the annotator dataframe" );
                    }
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

}
