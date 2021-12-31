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
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameBuilder;
import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.plugin.search.commands.SearchCommandFactory;
import de.mindscan.brightflux.plugin.search.utils.SearchDFColumns;
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
    private ProjectRegistry projectRegistry;

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
        upperComposite.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        lowerComposite = new Composite( this, SWT.NONE );
        lowerComposite.setLayoutData( BorderLayout.CENTER );
        lowerComposite.setLayout( new TableColumnLayout() );

        tableViewer = new TableViewer( lowerComposite, SWT.BORDER | SWT.FULL_SELECTION );
        tableViewer.addDoubleClickListener( new IDoubleClickListener() {
            public void doubleClick( DoubleClickEvent event ) {
                // retrieve (and then open) the content of the file.
                IStructuredSelection structuredSelection = tableViewer.getStructuredSelection();
                DataFrameRow fristSelectedDataFrameRow = (DataFrameRow) structuredSelection.getFirstElement();

                if (fristSelectedDataFrameRow != null) {
                    String pathInformation = String.valueOf( fristSelectedDataFrameRow.get( SearchDFColumns.FILE_PATH ) );
                    // The ContentId is not yet supported/ not yet implemented
                    dispatchCommand( SearchCommandFactory.retrieveContent( pathInformation, "" ) );
                }
            }
        } );
        tableViewer.setUseHashlookup( true );
        table = tableViewer.getTable();
        table.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                // send event to show the details in lower part of the parent dialog
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

        MenuItem mntmEvidence = new MenuItem( menu, SWT.CASCADE );
        mntmEvidence.setText( "Evidence" );

        Menu menu_2 = new Menu( mntmEvidence );
        mntmEvidence.setMenu( menu_2 );

        MenuItem mntmAddToCase = new MenuItem( menu_2, SWT.NONE );
        mntmAddToCase.setText( "Add To Case Evidence" );

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

    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        this.projectRegistry = projectRegistry;
    }

    private void dispatchCommand( BFCommand command ) {
        if (projectRegistry != null) {
            projectRegistry.getCommandDispatcher().dispatchCommand( command );
        }
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

}
