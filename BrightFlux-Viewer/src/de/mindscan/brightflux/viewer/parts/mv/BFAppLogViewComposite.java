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
package de.mindscan.brightflux.viewer.parts.mv;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.system.events.BFEventListenerAdapter;
import de.mindscan.brightflux.system.registry.ProjectRegistry;
import de.mindscan.brightflux.system.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.viewer.parts.SystemEvents;

/**
 * 
 */
public class BFAppLogViewComposite extends Composite implements ProjectRegistryParticipant {
    private Table table_1;
    private ProjectRegistry projectRegistry;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public BFAppLogViewComposite( Composite parent, int style ) {
        super( parent, style );
        setLayout( new FillLayout( SWT.HORIZONTAL ) );

        TableViewer tableViewer = new TableViewer( this, SWT.BORDER | SWT.FULL_SELECTION );
        table_1 = tableViewer.getTable();
        table_1.setHeaderVisible( true );
        table_1.setLinesVisible( true );

    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        this.projectRegistry = projectRegistry;
        // TODO: register for BFEvent and get all the BFEvents - but currently we can't do that

        // Small work around: we register for all SystemEvents.

        BFEventListenerAdapter listener = new BFEventListenerAdapter() {
            @Override
            public void handleEvent( BFEvent event ) {
                String eventMessage = event.getBFEventMessage();
                // TODO: create a timestamp  
                // TODO: create a logentry this event
                // TODO: add this logentry
                System.out.println( "eventMessage: " + eventMessage );
            }
        };

        this.projectRegistry.getEventDispatcher().registerEventListener( SystemEvents.CommandExecutionStarted, listener );
        this.projectRegistry.getEventDispatcher().registerEventListener( SystemEvents.CommandExecutionFinished, listener );
        this.projectRegistry.getEventDispatcher().registerEventListener( SystemEvents.CommandExecutionException, listener );
        this.projectRegistry.getEventDispatcher().registerEventListener( SystemEvents.DataFrameLoaded, listener );
        this.projectRegistry.getEventDispatcher().registerEventListener( SystemEvents.DataFrameCreated, listener );
        this.projectRegistry.getEventDispatcher().registerEventListener( SystemEvents.RecipeSaveResult, listener );
    }
}
