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

import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.ingest.IngestCsv;
import de.mindscan.brightflux.system.events.BFDataFrameEvent;
import de.mindscan.brightflux.system.events.BFEvent;
import de.mindscan.brightflux.system.events.BFEventListener;
import de.mindscan.brightflux.system.events.DataFrameLoadedEvent;
import de.mindscan.brightflux.system.registry.ProjectRegistry;
import de.mindscan.brightflux.system.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.viewer.parts.df.DataFrameTableComposite;

/**
 * 
 */
public class MainProjectComposite extends Composite implements ProjectRegistryParticipant {
    private CTabFolder mainTabFolder;

    // XXX: Awful hack right now.
    private final static Path path = Paths
                    .get( "D:\\Projects\\SinglePageApplication\\Angular\\BrightFlux\\BrightFlux-Core\\test\\de\\mindscan\\brightflux\\ingest\\heart.csv" );
    IngestCsv ingest = new IngestCsv();

    private ProjectRegistry projectRegistry;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public MainProjectComposite( Composite parent, int style ) {
        super( parent, style );

        // TODO: * register for project configuration updates/changes, and provide a listener, so that we can add and remove the content for data frames to show in the tab folder.
        //       * we should also be able to update the project configuration by closing a dataframe and such.

        // build layout - basically the tabFolder.
        buildLayout();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {

        this.projectRegistry = projectRegistry;
        // TODO: implement an anti corruption layer, for the specific events, instead of referencing specific classes in
        //       other packages, which may change over time. So that not every refactoring and move operation will lead 
        //       to a change in hundreds of files.
        projectRegistry.getEventDispatcher().registerEventListener( DataFrameLoadedEvent.class, new BFEventListener() {
            @Override
            public void handleEvent( BFEvent event ) {
                BFDataFrameEvent loaded = (BFDataFrameEvent) event;
                addDataFrameTab( loaded.getDataFrame() );
            }
        } );
    }

    private void buildLayout() {
        setLayout( new FillLayout( SWT.HORIZONTAL ) );

        mainTabFolder = new CTabFolder( this, SWT.NONE );

        DataFrame ingestedDF = ingest.loadAsDataFrame( path );
        addTabItem( mainTabFolder, ingestedDF );
    }

    private void addDataFrameTab( DataFrame newDataFrame ) {
        CTabItem item = addTabItem( mainTabFolder, newDataFrame );
        mainTabFolder.setSelection( item );
    }

    private CTabItem addTabItem( CTabFolder tabFolder, final DataFrame ingestedDF ) {
        String ingestedDFName = ingestedDF.getName();

        // [Desired TabItem] - but leave it like this until we added some other interesting stuff to it 
        CTabItem tbtmNewItem = new CTabItem( tabFolder, SWT.NONE );
        tbtmNewItem.setShowClose( true );
        tbtmNewItem.setText( ingestedDFName );

        DataFrameTableComposite composite = new DataFrameTableComposite( tabFolder, SWT.NONE );
        composite.setProjectRegistry( projectRegistry );
        composite.setDataFrame( ingestedDF );
        tbtmNewItem.setControl( composite );

        // [/Desired TabItem]

        return tbtmNewItem;

    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
