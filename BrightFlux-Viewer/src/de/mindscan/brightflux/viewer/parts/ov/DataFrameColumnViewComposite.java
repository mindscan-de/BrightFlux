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
package de.mindscan.brightflux.viewer.parts.ov;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wb.swt.SWTResourceManager;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.system.events.DataFrameEventListenerAdapter;
import de.mindscan.brightflux.viewer.parts.UIEvents;

/**
 * 
 */
public class DataFrameColumnViewComposite extends Composite implements ProjectRegistryParticipant {

    @SuppressWarnings( "unused" )
    private Shell parentShell;
    private ProjectRegistry projectRegistry;
    private TableViewer tableViewer;
    private Table table;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public DataFrameColumnViewComposite( Composite parent, int style ) {
        super( parent, style );

        parentShell = parent.getShell();

        buildLayout();
    }

    /**
     * 
     */
    private void buildLayout() {
        tableViewer = new TableViewer( this, SWT.BORDER | SWT.FULL_SELECTION );
        table = tableViewer.getTable();
        table.setHeaderVisible( true );

        table.setLinesVisible( true );
        table.setHeaderVisible( true );
        table.setFont( SWTResourceManager.getFont( "Tahoma", 10, SWT.NORMAL ) );
        table.setBounds( 0, 0, 446, 278 );

        TableColumnLayout tcl_composite = new TableColumnLayout();
        this.setLayout( tcl_composite );

        TableViewerColumn tableViewerColumn3 = new TableViewerColumn( tableViewer, SWT.NONE );
        TableColumn tableColumn3 = tableViewerColumn3.getColumn();
        tcl_composite.setColumnData( tableColumn3, new ColumnPixelData( 120, true, true ) );
        tableColumn3.setText( DataFrameColumnEntriesColumnLabelProvider.NAME_HEADER );
        tableViewerColumn3.setLabelProvider( new DataFrameColumnEntriesColumnLabelProvider( DataFrameColumnEntriesColumnLabelProvider.NAME_HEADER ) );

        TableViewerColumn tableViewerColumn2 = new TableViewerColumn( tableViewer, SWT.NONE );
        TableColumn tableColumn2 = tableViewerColumn2.getColumn();
        tcl_composite.setColumnData( tableColumn2, new ColumnPixelData( 120, true, true ) );
        tableColumn2.setText( DataFrameColumnEntriesColumnLabelProvider.TYPE_HEADER );
        tableViewerColumn2.setLabelProvider( new DataFrameColumnEntriesColumnLabelProvider( DataFrameColumnEntriesColumnLabelProvider.TYPE_HEADER ) );

        tableViewer.setContentProvider( DataFrameColumnEntriesContentProvider.getInstance() );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        this.projectRegistry = projectRegistry;

        if (this.projectRegistry != null) {
            DataFrameEventListenerAdapter listener = new DataFrameEventListenerAdapter() {
                @Override
                public void handleDataFrame( DataFrame dataFrame ) {
                    setCurrentDataFrame( dataFrame );
                }
            };
            this.projectRegistry.getEventDispatcher().registerEventListener( UIEvents.DataFrameSelectedEvent, listener );
        }
    }

    private void setCurrentDataFrame( DataFrame selectedDataFrame ) {
        tableViewer.setInput( selectedDataFrame );
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

}
