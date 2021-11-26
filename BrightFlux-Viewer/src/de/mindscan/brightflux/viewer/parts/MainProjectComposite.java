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
package de.mindscan.brightflux.viewer.parts;

import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.system.events.DataFrameEventListenerAdapter;
import de.mindscan.brightflux.system.highlighter.HighlighterComponent;
import de.mindscan.brightflux.viewer.parts.df.DataFrameTableComposite;
import de.mindscan.brightflux.viewer.uievents.UIEventFactory;
import de.mindscan.brightflux.viewer.uievents.UUIDRequestEvent;
import de.mindscan.brightflux.viewer.uievents.UUIDRequestEventListenerAdapter;

/**
 * 
 */
public class MainProjectComposite extends Composite implements ProjectRegistryParticipant {
    private CTabFolder mainTabFolder;

    private ProjectRegistry projectRegistry;
    private HighlighterComponent highlighterComponent;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public MainProjectComposite( Composite parent, int style ) {
        super( parent, style );

        buildComponent();
        buildLayout();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        this.projectRegistry = projectRegistry;
        registerEvents( projectRegistry );
        registerHighlighter( projectRegistry );
    }

    private void registerEvents( ProjectRegistry projectRegistry ) {
        projectRegistry.getEventDispatcher().registerEventListener( SystemEvents.DataFrameLoaded, new DataFrameEventListenerAdapter() {
            @Override
            public void handleDataFrame( DataFrame dataFrame ) {
                addDataFrameTab( dataFrame );
            }
        } );

        projectRegistry.getEventDispatcher().registerEventListener( SystemEvents.DataFrameCreated, new DataFrameEventListenerAdapter() {
            @Override
            public void handleDataFrame( DataFrame dataFrame ) {
                addDataFrameTab( dataFrame );
            }
        } );

        projectRegistry.getEventDispatcher().registerEventListener( UIEvents.DataFrameRequestSelectEvent, new UUIDRequestEventListenerAdapter() {
            @Override
            public void handleUUIDRequestEvent( UUIDRequestEvent event ) {
                requestDataFrameSelection( event.getRequestedUUID() );
            }
        } );
    }

    private void registerHighlighter( ProjectRegistry projectRegistry ) {
        if (highlighterComponent != null) {
            highlighterComponent.setProjectRegistry( projectRegistry );
        }
    }

    private void buildLayout() {
        setLayout( new FillLayout( SWT.HORIZONTAL ) );

        mainTabFolder = new CTabFolder( this, SWT.BORDER );
        mainTabFolder.setSimple( false );
        mainTabFolder.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent event ) {

                if (event.item instanceof CTabItem) {
                    Control control = ((CTabItem) event.item).getControl();
                    if (control instanceof DataFrameTableComposite) {
                        sendDataFrameSelectionEvent( ((DataFrameTableComposite) control).getDataFrame() );
                    }
                }
            }
        } );
        mainTabFolder.addCTabFolder2Listener( new CTabFolder2Adapter() {
            /** 
             * {@inheritDoc}
             */
            @Override
            public void close( CTabFolderEvent event ) {
                if (event.item instanceof CTabItem) {
                    Control control = ((CTabItem) event.item).getControl();
                    if (control instanceof DataFrameTableComposite) {
                        // we want to ignore the visual update... before closing, because it takes a lot of time....
                        // so we hide it first before voiding the referenced data
                        ((DataFrameTableComposite) control).closeDataframe();
                    }
                }
                super.close( event );
            }
        } );
    }

    private void buildComponent() {
        this.highlighterComponent = new HighlighterComponent();
    }

    private void addDataFrameTab( DataFrame newDataFrame ) {
        CTabItem item = addTabItem( mainTabFolder, newDataFrame );
        mainTabFolder.setSelection( item );
    }

    private CTabItem addTabItem( CTabFolder tabFolder, final DataFrame ingestedDF ) {
        String ingestedDFName = ingestedDF.getTitle();

        CTabItem tbtmNewItem = new CTabItem( tabFolder, SWT.NONE );
        tbtmNewItem.setShowClose( true );
        tbtmNewItem.setText( ingestedDFName );

        DataFrameTableComposite composite = new DataFrameTableComposite( tabFolder, SWT.NONE );
        composite.setHighlighterComponent( highlighterComponent );
        // TODO: autowire?
        composite.setProjectRegistry( projectRegistry );
        composite.setDataFrame( ingestedDF );
        tbtmNewItem.setControl( composite );

        sendDataFrameSelectionEvent( ingestedDF );

        return tbtmNewItem;

    }

    private void requestDataFrameSelection( UUID requestedUUID ) {
        if (requestedUUID == null) {
            return;
        }

        CTabItem[] items = mainTabFolder.getItems();
        if (items == null) {
            return;
        }

        for (int i = 0; i < items.length; i++) {
            if (selectIfRequestedUuidMatches( requestedUUID, items[i] )) {
                return;
            }
        }
    }

    private boolean selectIfRequestedUuidMatches( UUID requestedUUID, CTabItem item ) {
        Control currentControl = item.getControl();

        if (!(currentControl instanceof DataFrameTableComposite)) {
            return false;
        }

        DataFrameTableComposite dataFrameTableComposite = (DataFrameTableComposite) currentControl;
        DataFrame dataFrame = dataFrameTableComposite.getDataFrame();
        if (requestedUUID.equals( dataFrame.getUuid() )) {
            // make sure the highlights and annotations and markers are refreshed/updated.
            dataFrameTableComposite.refresh();

            mainTabFolder.setSelection( item );
            sendDataFrameSelectionEvent( dataFrame );
            return true;
        }

        return false;
    }

    private void sendDataFrameSelectionEvent( final DataFrame dataFrame ) {
        if (projectRegistry != null) {
            projectRegistry.getEventDispatcher().dispatchEvent( UIEventFactory.dataFrameSelected( dataFrame ) );
        }
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
