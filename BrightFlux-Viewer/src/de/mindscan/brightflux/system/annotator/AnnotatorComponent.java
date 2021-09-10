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
import de.mindscan.brightflux.system.events.dataframe.DataFrameAnnotateRowEvent;
import de.mindscan.brightflux.system.events.dataframe.DataFrameCreatedEvent;

/**
 * I want some kind of component which will take care of the Annotations... I don't want it to be a graphical component
 * but one which provides some business logic for log data analysis 
 */
public class AnnotatorComponent implements ProjectRegistryParticipant {

    DataFrame logAnalysisFrame = null;

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
        // TODO Auto-generated method stub

        // TODO: register for dataframe created events, look for dataframes with certain name
        //       keep reference to it.
        BFEventListener dfCreatedListener = new BFEventListenerAdapter() {
            @Override
            public void handleEvent( BFEvent event ) {
                if (event instanceof DataFrameCreatedEvent) {
                    DataFrame frameToAnnotate = ((DataFrameCreatedEvent) event).getDataFrame();
                    if ("logAnalysisFrame".equals( frameToAnnotate.getName() )) {
                        AnnotatorComponent.this.logAnalysisFrame = frameToAnnotate;
                        System.out.println( "Found the annotator dataframe" );
                    }
                }
            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( DataFrameCreatedEvent.class, dfCreatedListener );

        BFEventListener dfAnnotateListener = new BFEventListenerAdapter() {
            @Override
            public void handleEvent( BFEvent event ) {
                DataFrameAnnotateRowEvent x = ((DataFrameAnnotateRowEvent) event);

                // x.getDataFrame()

                System.out.println( "We want to annotate the dataframe, with the following text? Using the following String: '" + x.getAnnotation() + "'" );
            }
        };
        // TODO: register for logdata analysis annotate events, and do the annotation accordingly on the correct dataframe.
        projectRegistry.getEventDispatcher().registerEventListener( DataFrameAnnotateRowEvent.class, dfAnnotateListener );
    }

    /**
     * @return the logAnalysisFrame
     */
    public DataFrame getLogAnalysisFrame() {
        return logAnalysisFrame;
    }

}
