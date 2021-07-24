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
package de.mindscan.brightflux.viewer.parts.df;

import java.util.Collection;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameImpl;
import de.mindscan.brightflux.dataframes.DataFrameRowFilterPredicate;
import de.mindscan.brightflux.dataframes.filterpredicate.DataFrameRowFilterPredicateFactory;
import de.mindscan.brightflux.system.commands.BFCommand;
import de.mindscan.brightflux.system.commands.DataFrameCommandFactory;
import de.mindscan.brightflux.system.registry.ProjectRegistry;
import de.mindscan.brightflux.system.registry.ProjectRegistryParticipant;

/**
 * 
 */
public class DataFrameTableComposite extends Composite implements ProjectRegistryParticipant {
    private Table table;

    private ProjectRegistry projectRegistry;

    private DataFrame ingestedDF = new DataFrameImpl( "(empty)" );

    private TableViewer tableViewer;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public DataFrameTableComposite( Composite parent, int style ) {
        super( parent, style );

        buildLayout();
    }

    private void buildLayout() {
        tableViewer = new TableViewer( this, SWT.BORDER | SWT.FULL_SELECTION );
        table = tableViewer.getTable();

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
                        DataFrameTableComposite.this.projectRegistry.getCommandDispatcher().dispatchCommand( command );
                    }
                    else {
                        // content of columnindex (right) clicked.

                        // TODO open a menu here...
                    }
                }
            }
        } );

        table.setLinesVisible( true );
        table.setHeaderVisible( true );
        table.setFont( SWTResourceManager.getFont( "Tahoma", 10, SWT.NORMAL ) );
        table.setBounds( 0, 0, 446, 278 );
    }

    /**
     * @param ingestedDF the ingestedDF to set
     */
    public void setDataFrame( DataFrame dataFrame ) {
        this.ingestedDF = dataFrame;

        appenddDataFrameColumns( ingestedDF, tableViewer, this );

        tableViewer.setContentProvider( new DataFrameContentProvider() );
        tableViewer.setInput( ingestedDF );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {

        this.projectRegistry = projectRegistry;
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
