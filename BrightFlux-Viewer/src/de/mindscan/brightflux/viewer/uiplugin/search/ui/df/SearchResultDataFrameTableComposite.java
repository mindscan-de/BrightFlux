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
package de.mindscan.brightflux.viewer.uiplugin.search.ui.df;

import java.util.Collection;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameBuilder;
import de.mindscan.brightflux.viewer.parts.df.DataFrameColumnLabelProvider;
import de.mindscan.brightflux.viewer.parts.df.DataFrameContentProvider;
import swing2swt.layout.BorderLayout;

/**
 * 
 */
public class SearchResultDataFrameTableComposite extends Composite {
    private Table table;
    private TableViewer tableViewer;
    private Composite parentShell;

    private DataFrame searchResultDataFrame = new DataFrameBuilder( "(empty)" ).build();
    private Composite lowerComposite;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public SearchResultDataFrameTableComposite( Composite parent, int style ) {
        super( parent, style );

        this.parentShell = parent;

        buildLayout();
    }

    public void buildLayout() {
        setLayout( new BorderLayout( 0, 0 ) );

        Composite upperComposite = new Composite( this, SWT.NONE );
        upperComposite.setLayoutData( BorderLayout.NORTH );

        Button btnFoo = new Button( upperComposite, SWT.NONE );
        btnFoo.setBounds( 10, 10, 68, 23 );
        btnFoo.setText( "Foo" );

        lowerComposite = new Composite( this, SWT.NONE );
        lowerComposite.setLayoutData( BorderLayout.CENTER );
        lowerComposite.setLayout( new TableColumnLayout() );

        tableViewer = new TableViewer( lowerComposite, SWT.BORDER | SWT.FULL_SELECTION );
        tableViewer.setUseHashlookup( true );
        table = tableViewer.getTable();
        table.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
            }
        } );
        table.addMenuDetectListener( new MenuDetectListener() {
            public void menuDetected( MenuDetectEvent e ) {
                table.getMenu().setVisible( true );
            }
        } );
        table.setHeaderVisible( true );
        table.setLinesVisible( true );

        Menu menu = new Menu( table );
        table.setMenu( menu );

        MenuItem mntmFoobarbaz = new MenuItem( menu, SWT.CASCADE );
        mntmFoobarbaz.setText( "FooBarBaz" );

        Menu menu_1 = new Menu( mntmFoobarbaz );
        mntmFoobarbaz.setMenu( menu_1 );
    }

    public void closeSearchDataFrame() {
        this.setVisible( false );

        if (this.searchResultDataFrame != null) {
            setDataFrame( null );
        }
    }

    public void setDataFrame( DataFrame dataFrame ) {
        this.searchResultDataFrame = dataFrame;

        if (this.searchResultDataFrame != null) {
            appendDataFrameColumns( searchResultDataFrame, tableViewer, lowerComposite );

            tableViewer.setContentProvider( new DataFrameContentProvider() );
            tableViewer.setInput( searchResultDataFrame );
        }
        else {
            tableViewer.setContentProvider( new DataFrameContentProvider() );
            tableViewer.setInput( null );
        }
    }

    private void appendDataFrameColumns( DataFrame searchResultDataFrame, TableViewer tableViewer, Composite composite ) {
        Collection<String> columnNames = searchResultDataFrame.getColumnNames();

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

            DataFrameColumnLabelProvider labelProvider = new DataFrameColumnLabelProvider( columname );
            tableViewerColumn.setLabelProvider( labelProvider );
        }
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
