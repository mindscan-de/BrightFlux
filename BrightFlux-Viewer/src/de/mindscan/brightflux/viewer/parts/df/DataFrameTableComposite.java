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

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wb.swt.SWTResourceManager;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameImpl;
import de.mindscan.brightflux.dataframes.DataFrameRowFilterPredicate;
import de.mindscan.brightflux.dataframes.filterpredicate.DataFrameRowFilterPredicateFactory;
import de.mindscan.brightflux.dataframes.journal.DataFrameJournalEntry;
import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.system.commands.DataFrameCommandFactory;
import de.mindscan.brightflux.system.filedescription.FileDescriptions;
import de.mindscan.brightflux.viewer.parts.ui.BrightFluxFileDialogs;

/**
 * 
 */
public class DataFrameTableComposite extends Composite implements ProjectRegistryParticipant {

    private Table table;

    private ProjectRegistry projectRegistry;

    private DataFrame ingestedDF = new DataFrameImpl( "(empty)" );

    private TableViewer tableViewer;

    private Shell parentShell;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public DataFrameTableComposite( Composite parent, int style ) {
        super( parent, style );

        parentShell = parent.getShell();

        buildLayout();
    }

    private void buildLayout() {
        tableViewer = new TableViewer( this, SWT.BORDER | SWT.FULL_SELECTION );
        table = tableViewer.getTable();

        table.addMenuDetectListener( new MenuDetectListener() {
            public void menuDetected( MenuDetectEvent e ) {
                table.getMenu().setVisible( true );
            }
        } );

        table.setLinesVisible( true );
        table.setHeaderVisible( true );
        table.setFont( SWTResourceManager.getFont( "Tahoma", 10, SWT.NORMAL ) );
        table.setBounds( 0, 0, 446, 278 );

        Menu menu = new Menu( table );
        table.setMenu( menu );

        MenuItem mntmDataframe = new MenuItem( menu, SWT.CASCADE );
        mntmDataframe.setText( "DataFrame" );

        Menu menu_DataFrame = new Menu( mntmDataframe );
        mntmDataframe.setMenu( menu_DataFrame );

        MenuItem mntmJournal = new MenuItem( menu_DataFrame, SWT.CASCADE );
        mntmJournal.setText( "Journal" );

        Menu menu_4 = new Menu( mntmJournal );
        mntmJournal.setMenu( menu_4 );

        MenuItem mntmShowPrint = new MenuItem( menu_4, SWT.NONE );
        mntmShowPrint.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                printOrShowDataFrameJournal( ingestedDF );
            }
        } );
        mntmShowPrint.setText( "Show / Print" );

        MenuItem mntmSaveAsReceipt = new MenuItem( menu_4, SWT.NONE );
        mntmSaveAsReceipt.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {

                BrightFluxFileDialogs.saveRegularFileAndConsumePath( parentShell, "Save Recipe", //
                                FileDescriptions.BFRECIPE, //
                                path -> {
                                    saveReceipt( ingestedDF, path );
                                } );
            }
        } );
        mntmSaveAsReceipt.setText( "Save As Receipt ..." );

        MenuItem mntmSaveToFile = new MenuItem( menu_DataFrame, SWT.NONE );
        mntmSaveToFile.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                BrightFluxFileDialogs.saveRegularFileAndConsumePath( parentShell, "Save Data Frame", //
                                FileDescriptions.CSV, //
                                path -> {
                                    saveAsCSV( ingestedDF, path );
                                } );
            }
        } );
        mntmSaveToFile.setText( "Save As CSV ..." );

        MenuItem mntmFilters = new MenuItem( menu, SWT.CASCADE );
        mntmFilters.setText( "Filters" );

        Menu menu_2 = new Menu( mntmFilters );
        mntmFilters.setMenu( menu_2 );

        MenuItem mntmevents = new MenuItem( menu_2, SWT.NONE );
        mntmevents.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {

//                Point absolute = new Point( e.x, e.y );
//                Point pt = table.toControl( absolute );
//                int columnIndex = columnIndexAtposition( table, pt );
//                if (columnIndex >= 0) {
//                    if (pt.y < table.getHeaderHeight()) {
//                        // header of columnindex (right) clicked....
//                    }
//                    else {
//                        // content of columnindex (right) clicked.
//                        // TODO open a menu here...
//                    }
//                }

                apply610filter( ingestedDF );
            }
        } );
        mntmevents.setText( "filter 6.10_events" );

        MenuItem mntmFilterx = new MenuItem( menu_2, SWT.NONE );
        mntmFilterx.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                apply666filter( ingestedDF );
            }

        } );
        mntmFilterx.setText( "filter 666" );

        MenuItem mntmSelect = new MenuItem( menu, SWT.CASCADE );
        mntmSelect.setText( "Select" );

        Menu menu_1 = new Menu( mntmSelect );
        mntmSelect.setMenu( menu_1 );

        MenuItem mntmHtsAndHmsg = new MenuItem( menu_1, SWT.NONE );
        mntmHtsAndHmsg.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                applyh1tsh2msg( ingestedDF );
            }
        } );
        mntmHtsAndHmsg.setText( "h1.ts and h2.msg" );

        MenuItem mntmReceipts = new MenuItem( menu, SWT.CASCADE );
        mntmReceipts.setText( "Receipts" );

        Menu menu_3 = new Menu( mntmReceipts );
        mntmReceipts.setMenu( menu_3 );

        MenuItem mntmApplyReceipt = new MenuItem( menu_3, SWT.NONE );
        mntmApplyReceipt.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                BrightFluxFileDialogs.openRegularFileAndConsumePath( parentShell, "Select Recipe to Execute", //
                                FileDescriptions.BFRECIPE, //
                                path -> {
                                    applyReceipt( ingestedDF, path );
                                } );
            }
        } );
        mntmApplyReceipt.setText( "Apply Receipt ..." );
    }

    /**
     * @param ingestedDF the ingestedDF to set
     */
    public void setDataFrame( DataFrame dataFrame ) {
        this.ingestedDF = dataFrame;

        if (this.ingestedDF != null) {
            appenddDataFrameColumns( ingestedDF, tableViewer, this );

            tableViewer.setContentProvider( new DataFrameContentProvider() );
            tableViewer.setInput( ingestedDF );
        }
        else {
            tableViewer.setContentProvider( new DataFrameContentProvider() );
            tableViewer.setInput( null );
        }
    }

    public DataFrame getDataFrame() {
        return ingestedDF;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {

        this.projectRegistry = projectRegistry;
    }

//    private static int columnIndexAtposition( Table table, Point pt ) {
//        int colIndex = -1;
//        TableItem testRow = new TableItem( table, 0 );
//        for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
//            Rectangle bounds = testRow.getBounds( columnIndex );
//            if ((pt.x >= bounds.x) && (pt.x < (bounds.x + bounds.width))) {
//                colIndex = columnIndex;
//            }
//        }
//        testRow.dispose();
//        return colIndex;
//    }

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

    private void apply610filter( DataFrame dataFrame ) {
        DataFrameRowFilterPredicate predicate = DataFrameRowFilterPredicateFactory.and( // h2.sysctx == 6 && h2.b8==10 
                        DataFrameRowFilterPredicateFactory.eq( "h2.sysctx", 6 ), DataFrameRowFilterPredicateFactory.eq( "h2.b8", 10 ) );
        dispatchCommand( DataFrameCommandFactory.filterDataFrame( dataFrame, predicate ) );
    }

    private void apply666filter( DataFrame dataFrame ) {
        DataFrameRowFilterPredicate predicate = DataFrameRowFilterPredicateFactory.containsStr( "h2.msg", "0x666" );
        dispatchCommand( DataFrameCommandFactory.filterDataFrame( dataFrame, predicate ) );
    }

    private void applyh1tsh2msg( DataFrame dataFrame ) {
        DataFrameRowFilterPredicate predicate = DataFrameRowFilterPredicateFactory.any();
        dispatchCommand( DataFrameCommandFactory.selectAndFilterDataFrame( dataFrame, new String[] { "h1.ts", "h2.msg" }, predicate ) );
    }

    private void printOrShowDataFrameJournal( DataFrame dataFrame ) {
        System.out.println( "DataFrameLog:" );
        List<DataFrameJournalEntry> journalEntries = dataFrame.getJournal().getJournalEntries();
        for (DataFrameJournalEntry entry : journalEntries) {
            System.out.println( ": " + entry.getLogMessage() );
        }
    }

    private void applyReceipt( DataFrame dataFrame, Path receipt ) {
        dispatchCommand( DataFrameCommandFactory.applyRecipe( dataFrame, receipt ) );
    }

    private void saveReceipt( DataFrame dataFrame, Path targetPath ) {
        dispatchCommand( DataFrameCommandFactory.saveRecipe( dataFrame, targetPath ) );
    }

    private void saveAsCSV( DataFrame dataFrame, Path targetPath ) {
        dispatchCommand( DataFrameCommandFactory.saveCSV( dataFrame, targetPath ) );
    }

    private void dispatchCommand( BFCommand command ) {
        if (projectRegistry != null) {
            projectRegistry.getCommandDispatcher().dispatchCommand( command );
        }
    }
}
