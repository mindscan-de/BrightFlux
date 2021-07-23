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
import java.util.Collection;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRowFilterPredicate;
import de.mindscan.brightflux.dataframes.filterpredicate.DataFrameRowFilterPredicateFactory;
import de.mindscan.brightflux.ingest.IngestCsv;
import de.mindscan.brightflux.system.commands.BFCommand;
import de.mindscan.brightflux.system.commands.DataFrameCommandFactory;
import de.mindscan.brightflux.system.events.BFDataFrameEvent;
import de.mindscan.brightflux.system.events.BFEvent;
import de.mindscan.brightflux.system.events.BFEventListener;
import de.mindscan.brightflux.system.events.DataFrameLoadedEvent;
import de.mindscan.brightflux.system.registry.ProjectRegistry;
import de.mindscan.brightflux.system.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.viewer.parts.df.DataFrameColumnLabelProvider;
import de.mindscan.brightflux.viewer.parts.df.DataFrameContentProvider;

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

        Composite composite = new Composite( tabFolder, SWT.NONE );
        tbtmNewItem.setControl( composite );

        TableViewer tableViewer = new TableViewer( composite, SWT.BORDER | SWT.FULL_SELECTION );
        Table table = tableViewer.getTable();
        table.addMenuDetectListener( new MenuDetectListener() {
            public void menuDetected( MenuDetectEvent e ) {
                Point absolute = new Point( e.x, e.y );
                Point pt = table.toControl( absolute );
                int columnIndex = columnIndexAtposition( table, pt );
                if (columnIndex >= 0) {
                    if (pt.y < table.getHeaderHeight()) {
                        // header of columnindex (right) clicked....

                        // TODO open a menu here... and then use predicates 
                        DataFrameRowFilterPredicate predicate = DataFrameRowFilterPredicateFactory.and( // h2.sysctx == 6 && h2.b8==10 
                                        DataFrameRowFilterPredicateFactory.eq( "h2.sysctx", 6 ), DataFrameRowFilterPredicateFactory.eq( "h2.b8", 10 ) );
                        BFCommand command = DataFrameCommandFactory.filterDataFrame( ingestedDF, predicate );
                        MainProjectComposite.this.projectRegistry.getCommandDispatcher().dispatchCommand( command );
                    }
                    else {
                        // content of columnindex (right) clicked.

                        // TODO open a menu here...
                    }
                }
            }
        } );
        table.setFont( SWTResourceManager.getFont( "Tahoma", 10, SWT.NORMAL ) );
        // TODO: this should be per column?
        // table.setFont( SWTResourceManager.getFont( "Courier New", 10, SWT.NORMAL ) );
        table.setHeaderVisible( true );
        table.setLinesVisible( true );

        appenddDataFrameColumns( ingestedDF, tableViewer, composite );

        tableViewer.setContentProvider( new DataFrameContentProvider() );
        tableViewer.setInput( ingestedDF );

        // [/Desired TabItem]

        return tbtmNewItem;

    }

    private static int columnIndexAtposition( Table table, Point pt ) {
        int colIndex = -1;
        TableItem testRow = new TableItem( table, 0 );
        for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
            Rectangle bounds = testRow.getBounds( columnIndex );
            if ((pt.x >= bounds.x) && (pt.x < (bounds.x + bounds.width))) {
                colIndex = columnIndex;
            }
        }
        testRow.dispose();
        return colIndex;
    }

    // XXX: This is an awful quick hack to move towards the goal to present some data, 
    //      we then will refactor to patterns if it works good enough.

    private void appenddDataFrameColumns( DataFrame ingestedDF, TableViewer tableViewer, Composite composite ) {
        Collection<String> columnNames = ingestedDF.getColumnNames();

        TableColumnLayout tcl_composite = new TableColumnLayout();
        composite.setLayout( tcl_composite );

        for (final String columname : columnNames) {
            TableViewerColumn tableViewerColumn = new TableViewerColumn( tableViewer, SWT.NONE );
            TableColumn tableColumn = tableViewerColumn.getColumn();

            // TODO: take the width from the configuration? 
            // Settings or width of presentation?
            tcl_composite.setColumnData( tableColumn, new ColumnPixelData( 45, true, true ) );
            tableColumn.setText( columname );

            // TODO: the labelprovider should depend on the columnype
            // TODO: the labelprovider should depend also on a configuration for presentation,
            //       e.g. present long-value as timstamp.
            tableViewerColumn.setLabelProvider( new DataFrameColumnLabelProvider( columname ) );
        }

    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
